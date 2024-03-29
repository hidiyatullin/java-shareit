drop table if exists users cascade;
drop table if exists requests cascade;
drop table if exists items cascade;
drop table if exists items_requests cascade;
drop table if exists statuses cascade;
drop table if exists bookings cascade;
drop table if exists comments cascade;


create table if not exists users
(
    id    BIGINT generated by default as identity primary key,
    name  VARCHAR(20) not null,
    email VARCHAR(20) not null unique
);

create table if not exists requests
(
    id           BIGINT generated by default as identity primary key,
    description  VARCHAR(100),
    requester_id BIGINT references users on delete cascade,
    created      TIMESTAMP WITHOUT TIME ZONE
);

create table if not exists items
(
    id           BIGINT generated by default as identity primary key,
    name         VARCHAR(20)      not null,
    description  VARCHAR(100) not null,
    owner_id     BIGINT references users on delete cascade,
    is_available BOOLEAN,
    request_id   BIGINT references requests
);

create table if not exists items_requests
(
    item_id    BIGINT references items,
    request_id BIGINT references requests
);

create table if not exists statuses
(
    id   INTEGER primary key,
    name VARCHAR(20)
);

create table if not exists bookings
(
    id         BIGINT generated by default as identity primary key,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT references items on delete cascade,
    booker_id  BIGINT references users on delete cascade,
    status     VARCHAR(50)
);

create table if not exists comments
(
    id        BIGINT generated by default as identity primary key,
    text      VARCHAR(200),
    item_id   BIGINT references ITEMS on delete cascade,
    author_id BIGINT references USERS on delete cascade,
    created   TIMESTAMP WITHOUT TIME ZONE

);