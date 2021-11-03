create table if not exists service
(
    id           varchar(256) DEFAULT (uuid()) NOT NULL PRIMARY KEY,
    service_name varchar(256)                             NOT NULL,
    uri          varchar(256)                             NOT NULL,
    created_at   timestamp  DEFAULT CURRENT_TIMESTAMP
);

create table if not exists poke_result
(
    id           varchar(256) DEFAULT (uuid()) NOT NULL PRIMARY KEY,
    service_id   varchar(256) NOT NULL,
    performed_at timestamp DEFAULT CURRENT_TIMESTAMP,
    result       varchar(64),

    INDEX (service_id),

    FOREIGN KEY (service_id)
        REFERENCES service (id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

insert into service(service_name, uri) VALUES ('super-nice-health-service', 'https://www.kry.se/en/');
insert into service(service_name, uri) VALUES ('great-service-poller', 'https://www.google.com/');
insert into service(service_name, uri) VALUES ('amazing-clinic-service', 'https://www.clinic.com/');
insert into service(service_name, uri) VALUES ('amazing-clinic-service', 'https://www.clinic.com/se');
insert into service(service_name, uri) VALUES ('amazing-clinic-service', 'https://www.clinic2.net/en');