alter table upwork.postings
    add title text default '' not null;

alter table upwork.postings
    add guid text default '' not null;

alter table upwork.postings
    add pub_date timestamp;

alter table upwork.postings
    add status text default 'new' not null;

alter table upwork.postings
    add html_description text;
