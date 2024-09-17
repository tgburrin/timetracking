-- drop schema flyway_metadata cascade; drop schema timekeeping cascade;
create schema if not exists timekeeping;
create extension if not exists btree_gist;
create extension if not exists "uuid-ossp";

CREATE OR REPLACE FUNCTION timekeeping.set_timestatmps_trg()
RETURNS TRIGGER
LANGUAGE plpgsql
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
$$;

create table if not exists timekeeping.permissions (
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    permission_id bigserial not null primary key,
    code text not null,
    description text not null,
    constraint uq_perm_code unique(code)
);

drop trigger if exists AA_permissions_dup on timekeeping.permissions;
create trigger AA_permissions_dup
before update
on timekeeping.permissions
for each row
execute procedure suppress_redundant_updates_trigger();

DROP TRIGGER IF EXISTS FF_permission_timestamps on timekeeping.permissions;
CREATE TRIGGER FF_permission_timestamps
BEFORE INSERT OR UPDATE
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

drop trigger if exists AA_usergroups_dup on timekeeping.usergroups;
create trigger AA_usergroups_dup
before update
on timekeeping.usergroups
for each row
execute procedure suppress_redundant_updates_trigger();

DROP TRIGGER IF EXISTS FF_usergroups_timestamps on timekeeping.usergroups;
CREATE TRIGGER FF_usergroups_timestamps
BEFORE INSERT OR UPDATE
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

drop trigger if exists AA_tasks_dup on timekeeping.tasks;
create trigger AA_tasks_dup
before update
on timekeeping.tasks
for each row
execute procedure suppress_redundant_updates_trigger();

DROP TRIGGER IF EXISTS FF_tasks_timestamps on timekeeping.tasks;
CREATE TRIGGER FF_tasks_timestamps
BEFORE INSERT OR UPDATE
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

drop trigger if exists AA_task_assignments_dup on timekeeping.task_assignments;
create trigger AA_task_assignments_dup
before update
on timekeeping.task_assignments
for each row
execute procedure suppress_redundant_updates_trigger();

DROP TRIGGER IF EXISTS FF_task_assignments_timestamps on timekeeping.task_assignments;
CREATE TRIGGER FF_task_assignments_timestamps
BEFORE INSERT OR UPDATE
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

drop trigger if exists AA_task_time_instances_dup on timekeeping.task_time_instances;
create trigger AA_task_time_instances_dup
before update
on timekeeping.task_time_instances
for each row
execute procedure suppress_redundant_updates_trigger();

DROP TRIGGER IF EXISTS FF_task_time_instances_timestamps on timekeeping.task_time_instances;
CREATE TRIGGER FF_task_time_instances_timestamps
BEFORE INSERT OR UPDATE
ON timekeeping.task_time_instances
FOR EACH ROW
EXECUTE PROCEDURE timekeeping.set_timestatmps_trg();

CREATE OR REPLACE FUNCTION timekeeping.set_task_times(ttid uuid, sd timestamptz, ed timestamptz)
returns table (
    created timestamptz,
    updated timestamptz,
    id uuid,
    task_id bigint,
    user_id bigint,
    start_dt timestamptz,
    end_dt timestamptz
)
LANGUAGE plpgsql
as $$
BEGIN
    RETURN QUERY
    update timekeeping.task_time_instances set
        task_time = tstzrange(sd, ed, '[)')
    where
        id = ttid
    returning
        created,
        updated,
        id,
        task_id,
        user_id,
        lower(task_time),
        nullif(upper(task_time), 'Infinity')
    ;
END;
$$
;

CREATE OR REPLACE FUNCTION timekeeping.start_task_times(uid bigint, tids bigint[])
returns table (
    created timestamptz,
    updated timestamptz,
    id uuid,
    task_id bigint,
    user_id bigint,
    start_dt timestamptz,
    end_dt timestamptz
)
LANGUAGE plpgsql
as $$
BEGIN
    RETURN QUERY
    insert into timekeeping.task_time_instances (id, user_id, task_id, task_time)
    select
        public.uuid_generate_v4(),
        uid,
        t,
        tstzrange(now(), 'Infinity', '[)')
    from unnest(tids) as t
    returning
        created,
        updated,
        id,
        task_id,
        user_id,
        lower(task_time),
        nullif(upper(task_time), 'Infinity')
    ;
END;
$$
;

CREATE OR REPLACE FUNCTION timekeeping.stop_task_times(uid bigint, tids bigint[])
returns table (
    created timestamptz,
    updated timestamptz,
    id uuid,
    task_id bigint,
    user_id bigint,
    start_dt timestamptz,
    end_dt timestamptz
)
LANGUAGE plpgsql
as $$
BEGIN
    RETURN QUERY
    update timekeeping.task_time_instances set
        task_time = tstzrange(lower(task_time), now(), '[)')
    where
        user_id = uid and
        task_id = ANY(tids)
    returning
        created,
        updated,
        id,
        task_id,
        user_id,
        lower(task_time),
        nullif(upper(task_time), 'Infinity')
    ;
END;
$$
;

CREATE OR REPLACE FUNCTION timekeeping.time_card_report(tr tstzrange)
RETURNS TABLE (
    user_id bigint,
    start_dt timestamptz,
    end_dt timestamptz,
    duration_min int,
    task_ids bigint[],
    task_times_mins integer[]
)
LANGUAGE plpgsql
as $$
BEGIN
    RETURN QUERY
    with l as (
        select
            t1.user_id,
            tt as new_lower,
            array_agg(t1.task_id) as task_ids
        from
            timekeeping.task_time_instances t1
            join lateral unnest(ARRAY[
                    greatest(lower(t1.task_time), lower(tr)),
                    least(upper(t1.task_time), upper(tr))
                ]) as tt on true
        where
            t1.task_time && tr
        group by 1, 2
        order by 1, 2
    ), u as (
        select
            *,
            lead(l.new_lower) over (partition by l.user_id order by l.new_lower) as new_upper
        from l
    ), tr as (
        select
            u.user_id,
            tstzrange(
                    u.new_lower,
                    u.new_upper,
                    '[)'
            ) as task_time
        from u
    ), agg_min as (
        select
            t1.user_id,
            tr.task_time,
            (extract(epoch from date_trunc('min', upper(tr.task_time)) - date_trunc('min', lower(tr.task_time))) / 60)::int as diff_min,
            array_agg(t1.task_id order by t1.task_id) as task_ids
        from
            tr
            join timekeeping.task_time_instances t1 on
                        tr.user_id = t1.user_id
                    and tr.task_time && t1.task_time
        group by 1, 2, 3 order by 1, 2, 3
    ), td as (
        select
            am.user_id,
            am.task_time,
            t.tid as task_id,
            t.pos,
            am.diff_min,
            am.diff_min / array_length(am.task_ids, 1) +
            case
                when t.pos <= am.diff_min - ((am.diff_min / array_length(am.task_ids, 1)) * array_length(am.task_ids, 1))
                    then
                    1
                else 0
                end
                  as amt,
            am.diff_min / array_length(am.task_ids, 1) base_amt,
            am.diff_min - ((am.diff_min / array_length(am.task_ids, 1)) * array_length(am.task_ids, 1)) as round_err
        from
            agg_min am
            join lateral unnest(am.task_ids) with ordinality as t(tid, pos) on true
    ), re_combine as (
        select
            td.user_id,
            td.task_time,
            array_agg(td.amt order by td.pos) as task_times_mins
        from
            td
        group by 1, 2
    )
    select
        am.user_id,
        lower(am.task_time) as start_dt,
        upper(am.task_time) as end_dt,
        am.diff_min,
        am.task_ids,
        rc.task_times_mins
    from
        agg_min am
        join re_combine rc on am.user_id = rc.user_id and am.task_time = rc.task_time
    ;
END;
$$
;

insert into timekeeping.usergroups (name,type,group_id)
values ('tgburrin','U',1), ('baburrin', 'U', 1);

insert into timekeeping.tasks (external_id, external_status)
values ('',''), ('',''), ('',''), ('','');
