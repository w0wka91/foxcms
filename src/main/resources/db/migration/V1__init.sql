create table "user"
(
    id              serial primary key,
    username        varchar    not null,
    password        varchar    not null,
    created_at      timestamp default now(),
    updated_at      timestamp default now()
);

create table project
(
    id              serial primary key,
    name            varchar(100)    not null,
    generated_name  varchar(100)    not null,
    created_at      timestamp default now(),
    updated_at      timestamp default now()
);

create table branch
(
    id              serial primary key,
    project_id      integer         not null references project (id),
    name            varchar(100)    not null,
    created_at      timestamp default now(),
    updated_at      timestamp default now()
);

create table content_model
(
    id                serial primary key,
    branch_id         integer         not null references branch (id),
    name              varchar(100)    not null,
    api_name          varchar(100)    not null,
    description       varchar(100)    not null,
    preview_field_id  integer,
    created_at        timestamp default now(),
    updated_at        timestamp default now()
);

create type concern as enum ('OPTIONAL', 'REQUIRED');

create type "constraint" as enum ('NONE', 'UNIQUE');

create type display_type as enum ('SINGLE_LINE_TEXT', 'MULTI_LINE_TEXT', 'INTEGER', 'FLOAT', 'DATE', 'JSON_EDITOR', 'CHECKBOX');

create type relation_type as enum ('ONE_TO_ONE_DIRECTIVE', 'ONE_TO_ONE', 'ONE_TO_MANY', 'MANY_TO_ONE', 'MANY_TO_MANY');

create table field
(
    type_id          integer          not null,
    id               serial primary key,
    content_model_id integer          not null references content_model (id),
    type             display_type,
    position         integer,
    name             varchar(100)     not null,
    api_name         varchar(100)     not null,
    concern          concern,
    "constraint"     "constraint",
    relation_type    relation_type,
    relates_to       integer          references content_model (id),
    enum_id          integer          references content_model (id),
    created_at       timestamp default now(),
    updated_at       timestamp default now()
);

alter table content_model
add constraint preview_field_fk
FOREIGN KEY (preview_field_id)
REFERENCES field(id);

create table "enum"
(
    id               serial primary key,
    branch_id        integer         not null references branch (id),
    name             varchar(100)    not null,
    api_name         varchar(100)     not null,
    values           text[],
    created_at       timestamp default now(),
    updated_at       timestamp default now()
);
/*
create type licensing_model as enum ('TWO_DAYS', 'LIFE_LONG');

create table movies
(
    id              serial primary key,
    name            varchar         not null,
    licensing_model licensing_model not null,
    created_at      timestamp default now(),
    updated_at      timestamp default now()
);

insert into movies (name, licensing_model)
VALUES ('A Star is Born', 'TWO_DAYS');

insert into movies (name, licensing_model)
VALUES ('Iron Man 2', 'TWO_DAYS');

insert into movies (name, licensing_model)
VALUES ('It!', 'LIFE_LONG');

insert into movies (name, licensing_model)
VALUES ('Inside out', 'LIFE_LONG');

create table purchased_movies
(
    id              serial primary key,
    movie_id        integer                  not null references movies (id),
    customer_id     integer                  not null references customers (id),
    price           decimal                  not null,
    purchase_date   timestamp with time zone not null default now(),
    expiration_date timestamp with time zone,
    created_at      timestamp                         default now(),
    updated_at      timestamp                         default now()
);*/
