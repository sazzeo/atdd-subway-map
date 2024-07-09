drop table if exists line CASCADE;

drop table if exists station CASCADE;

create table line (
                      id bigint generated by default as identity,
                      color varchar(255),
                      distance bigint,
                      name varchar(255),
                      down_station_id bigint,
                      up_station_id bigint,
                      primary key (id)
);


create table station (
                         station_id bigint generated by default as identity,
                         name varchar(20) not null,
                         primary key (station_id)
);
