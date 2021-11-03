create table if not exists poke_result
(
    id           varchar(128),
    service_name varchar(128),
    performed_at timestamp,
    result       varchar(64)
);
