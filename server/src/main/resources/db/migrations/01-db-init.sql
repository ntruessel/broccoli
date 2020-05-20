create table events (
    id identity primary key,
    type varchar not null,
    data json not null,
    transaction uuid not null
);

create table version (
    id uuid primary key,
    version integer not null
);
