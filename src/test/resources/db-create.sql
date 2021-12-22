-- Quotes
drop table if exists quotes cascade;
create table quotes
(
    id    int auto_increment,
    quote varchar(255)
);


-- User
drop table if exists users cascade;
create table users
(
    identifier varchar not null,
    firstname  varchar not null,
    lastname   varchar not null,
    latitude    float not null,
    longitude   float not null,
    status      varchar not null,
    constraint USERS_PK
        primary key (identifier)
);

create unique index USERS_IDENTIFIER_UINDEX
    on users (identifier);


-- Subscriptions
drop table if exists subscriptions cascade;
create table subscriptions
(
    name        varchar not null,
    description varchar not null,
    price       double not null,
    constraint SUBSCRIPTIONS_PK
        primary key (name)
);

create unique index SUBSCRIPTIONS_IDENTIFIER_UINDEX
    on subscriptions (name);


-- User Subscription
drop table if exists user_subscription;
create table user_subscription
(
    user_identifier   varchar   not null,
    subscription_name varchar   not null,
    price             double    not null,
    start_date        date      not null default CURRENT_DATE,
    end_date          date      default null,
    reimbursed        boolean   default false,
    constraint user_subscription_subscription_name_fk
        foreign key (subscription_name) references subscriptions
            on update cascade on delete cascade,
    constraint user_subscription_user_identifier_fk
        foreign key (user_identifier) references users
            on update cascade on delete cascade
);


-- Vehicles
drop table if exists vehicles;
create table vehicles
(
    identifier varchar not null,
    occupied boolean not null default false,
    latitude float,
    longitude float
);

-- Domes
drop table if exists domes;
create table domes
(
    identifier varchar not null,
    size int not null,
    latitude float,
    longitude float
);

-- Dangerzones
drop table if exists DANGERZONES cascade;
create table DANGERZONES
(
    identifier varchar not null,
    latitude float not null,
    longitude  float not null,
    radius   float not null
);