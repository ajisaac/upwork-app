alter table upwork.postings
    add title text default '' not null;

alter table upwork.postings
    add guid text default '' not null;

alter table upwork.postings
    add pub_date timestamp;
