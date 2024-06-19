drop table if exists upwork.postings;
create sequence upwork.postings_id_seq;
alter sequence upwork.postings_id_seq owner to aaron;

create table upwork.postings
(
    id                    bigint primary key not null default nextval('postings_id_seq'::regclass),
    url                   text,
    budget                text,
    category              text,
    skills                text,
    country               text,
    location_requirements text,
    hourly_range          text,
    description           text,
    datetime              timestamp without time zone
);

create index postings_hourly_range_index on postings using btree (hourly_range);
create index postings_url_index on postings using btree (url);

alter sequence upwork.postings_id_seq owned by postings.id;
