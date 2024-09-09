create schema if not exists timekeeping;

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
create extension if not exists btree_gist;

create table if not exists timekeeping.usergroups (
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    id bigserial not null primary key,
    name text not null,
    type char(1) not null default 'U',
    status char(1) not null default 'A',
    group_id bigint not null
);

create index if not exists ug_user_name on timekeeping.usergroups (name, type);

DROP TRIGGER IF EXISTS FF_usergroups_timestamps on timekeeping.usergroups;
CREATE TRIGGER FF_usergroups_timestamps
BEFORE INSERT OR UPDATE OR DELETE
ON timekeeping.usergroups
FOR EACH ROW
EXECUTE PROCEDURE timekeeping.set_timestatmps_trg();

insert into timekeeping.usergroups (id, name, type, group_id)
values
    (1, 'admins', 'G', 0)
    on conflict do nothing
;

create table if not exists timekeeping.tasks (
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    task_id bigserial not null primary key,
    external_id text not null,
    external_status text not null,
    status char(1) not null default 'O' -- O = Open
);

DROP TRIGGER IF EXISTS FF_tasks_timestamps on timekeeping.tasks;
CREATE TRIGGER FF_tasks_timestamps
    BEFORE INSERT OR UPDATE OR DELETE
    ON timekeeping.tasks
    FOR EACH ROW
EXECUTE PROCEDURE timekeeping.set_timestatmps_trg();

create table if not exists timekeeping.task_assignments (
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    task_id bigserial not null primary key,
    external_id text not null,
    external_status text not null,
    status char(1) not null default 'O' -- O = Open
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
    task_id bigint not null,
    user_id bigint not null,
    task_time tstzrange not null,
    constraint fk_user
        foreign key (user_id)
            references timekeeping.usergroups(id),
    constraint fk_task
        foreign key (task_id)
            references timekeeping.tasks(task_id),
    exclude using gist ( task_id with =, task_time with && )
);

DROP TRIGGER IF EXISTS FF_task_time_instances_timestamps on timekeeping.task_time_instances;
CREATE TRIGGER FF_task_time_instances_timestamps
    BEFORE INSERT OR UPDATE OR DELETE
    ON timekeeping.task_time_instances
    FOR EACH ROW
EXECUTE PROCEDURE timekeeping.set_timestatmps_trg();
