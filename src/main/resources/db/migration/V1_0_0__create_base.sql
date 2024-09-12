create schema if not exists timekeeping;
create extension if not exists btree_gist;

CREATE OR REPLACE FUNCTION timekeeping.set_timestatmps_trg()
    RETURNS TRIGGER
AS $$
BEGIN
    IF TG_WHEN = 'BEFORE' THEN
        IF TG_OP = 'INSERT' THEN
            NEW.created := clock_timestamp();
            NEW.updated := clock_timestamp();
        END IF;

        IF TG_OP = 'UPDATE' THEN
            NEW.updated := clock_timestamp();
        END IF;

        RETURN NEW;
    ELSIF TG_WHEN = 'AFTER' THEN
        RETURN NULL;
    ELSE
        RAISE EXCEPTION 'Unhandled trigger lifecycle % in %.% on %.%', TG_OP, TG_WHEN, TG_NAME, TG_TABLE_SCHEMA, TG_TABLE_NAME;
    END IF;
END;
$$ LANGUAGE plpgsql;

create table if not exists timekeeping.permissions (
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    permission_id bigserial not null primary key,
    code text not null,
    description text not null,
    constraint uq_perm_code unique(code)
);

DROP TRIGGER IF EXISTS FF_permission_timestamps on timekeeping.permissions;
CREATE TRIGGER FF_permission_timestamps
    BEFORE INSERT OR UPDATE OR DELETE
    ON timekeeping.permissions
    FOR EACH ROW
EXECUTE PROCEDURE timekeeping.set_timestatmps_trg();

insert into timekeeping.permissions(code, description)
values
('ADMIN', 'ability to do anything in the system'),
('EDIT_TIME', 'the ability to edit task times of your own'),
('MANAGE_TIME', 'the ability to edit task times of others')
on conflict do nothing;

create table if not exists timekeeping.usergroups (
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    ug_id bigserial not null primary key,
    name text not null,
    type char(1) not null default 'U',
    status char(1) not null default 'A',
    group_id bigint not null
);

create unique index if not exists ug_user_name on timekeeping.usergroups (name, type);

DROP TRIGGER IF EXISTS FF_usergroups_timestamps on timekeeping.usergroups;
CREATE TRIGGER FF_usergroups_timestamps
BEFORE INSERT OR UPDATE OR DELETE
ON timekeeping.usergroups
FOR EACH ROW
EXECUTE PROCEDURE timekeeping.set_timestatmps_trg();

insert into timekeeping.usergroups (name, type, group_id)
values
    ('admins', 'G', 0)
    on conflict do nothing
;

create table if not exists timekeeping.permission_grant (
    created timestamptz not null default clock_timestamp(),
    group_id bigint not null,
    permission_id bigint not null,
    primary key (group_id, permission_id),
    constraint fk_group foreign key (group_id)
        references timekeeping.usergroups(ug_id),
    constraint fk_permission foreign key (permission_id)
        references timekeeping.usergroups(ug_id)
);

insert into timekeeping.permission_grant (group_id, permission_id)
select
    ug.ug_id,
    p.permission_id
from
    timekeeping.usergroups ug,
    timekeeping.permissions p
where
    ug.name = 'admins' and
    ug.type = 'G' and
    p.code = 'ADMIN'
on conflict  do nothing
;

create table if not exists timekeeping.tasks (
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    task_id bigserial not null primary key,
    external_id text not null,
    external_status text not null,
    status char(1) not null default 'O' -- O = Open
);

create unique index uq_external_task on timekeeping.tasks (task_id);

DROP TRIGGER IF EXISTS FF_tasks_timestamps on timekeeping.tasks;
CREATE TRIGGER FF_tasks_timestamps
    BEFORE INSERT OR UPDATE OR DELETE
    ON timekeeping.tasks
    FOR EACH ROW
EXECUTE PROCEDURE timekeeping.set_timestatmps_trg();

create table if not exists timekeeping.task_assignments (
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    task_id bigint not null,
    user_id bigint not null,
    primary key (user_id, task_id),
    constraint fk_user
        foreign key (user_id)
            references timekeeping.usergroups(ug_id),
    constraint fk_task
        foreign key (task_id)
            references timekeeping.tasks(task_id)
);

DROP TRIGGER IF EXISTS FF_task_assignments_timestamps on timekeeping.task_assignments;
CREATE TRIGGER FF_task_assignments_timestamps
    BEFORE INSERT OR UPDATE OR DELETE
    ON timekeeping.task_assignments
    FOR EACH ROW
EXECUTE PROCEDURE timekeeping.set_timestatmps_trg();

create table if not exists timekeeping.task_time_instances (
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    id uuid not null primary key,
    task_id bigint not null,
    user_id bigint not null,
    task_time tstzrange not null,
    constraint fk_user
        foreign key (user_id)
            references timekeeping.usergroups(ug_id),
    constraint fk_task
        foreign key (task_id)
            references timekeeping.tasks(task_id),
    constraint uq_task_user_time
        exclude using gist ( user_id with = , task_id with =, task_time with && )
);

DROP TRIGGER IF EXISTS FF_task_time_instances_timestamps on timekeeping.task_time_instances;
CREATE TRIGGER FF_task_time_instances_timestamps
    BEFORE INSERT OR UPDATE OR DELETE
    ON timekeeping.task_time_instances
    FOR EACH ROW
EXECUTE PROCEDURE timekeeping.set_timestatmps_trg();

