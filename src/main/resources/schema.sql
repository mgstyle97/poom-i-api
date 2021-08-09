DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS authority CASCADE;
DROP TABLE IF EXISTS childminder_info CASCADE;
DROP TABLE IF EXISTS address_tag CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS member_authority CASCADE;
DROP TABLE IF EXISTS child CASCADE;
DROP TABLE IF EXISTS sms_certification CASCADE;
DROP TABLE IF EXISTS refresh_token CASCADE;

CREATE TABLE sms_certification(
    id integer primary key,
    phone_number varchar not null,
    certification_number varchar not null
);

CREATE TABLE refresh_token (
    id integer primary key,
    member_email varchar not null,
    refresh_token varchar not null
);

CREATE TABLE address_tag (
    id integer primary key,
    extra_address varchar unique
);

CREATE TABLE address (
    id integer primary key,
    post_code varchar not null,
    detail_address varchar not null,
    ad_tag_id integer,
    foreign key(ad_tag_id) references address_tag(id)
);

CREATE TABLE authority (
    id integer primary key,
    authority varchar unique
);

CREATE TABLE child(
    id integer primary key,
    name varchar not null,
    birthday date not null,
    school varchar not null,
    special_note varchar
);

CREATE TABLE childminder_info(
    id integer primary key,
    date date,
    experience varchar,
    greeting varchar,
    score integer
);

CREATE TABLE member(
    id integer primary key,
    name varchar not null,
    phone_number varchar ,
    email varchar unique,
    password varchar ,
    nick varchar,
    cm_info_id integer,
    address_id integer,
    child_id integer,
    foreign key (cm_info_id) references childminder_info(id),
    foreign key (address_id) references address(id),
    foreign key (child_id) references  child(id)
);

CREATE TABLE member_authority(
    member_id integer ,
    authority_id integer ,
    foreign key (member_id) references member(id),
    foreign key (authority_id) references authority(id)
);