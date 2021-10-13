DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS authority CASCADE;
DROP TABLE IF EXISTS address_tag CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS member_authority CASCADE;
DROP TABLE IF EXISTS child CASCADE;
DROP TABLE IF EXISTS sms_certification CASCADE;
DROP TABLE IF EXISTS email_certification CASCADE;
DROP TABLE IF EXISTS refresh_token CASCADE;
DROP TABLE IF EXISTS child_care_group CASCADE;
DROP TABLE IF EXISTS group_board CASCADE;
DROP TABLE IF EXISTS board_likes CASCADE;
DROP TABLE IF EXISTS group_apply CASCADE;
DROP TABLE IF EXISTS group_participating_member CASCADE;
DROP TABLE IF EXISTS group_participating_child CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS image CASCADE;
DROP TABLE IF EXISTS child_care_expert CASCADE;
DROP TABLE IF EXISTS expert_apply CASCADE;
DROP TABLE IF EXISTS expert_likes CASCADE;
DROP TABLE IF EXISTS profile_image CASCADE;


CREATE TABLE sms_certification(
    id integer primary key,
    phone_number varchar not null,
    certification_number varchar not null
);

CREATE TABLE email_certification(
    id integer primary key,
    email varchar not null,
    certification_number varchar not null
);

CREATE TABLE profile_image(
    id integer primary key,
    email varchar not null,
    profile_image_url varchar not null
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
    address varchar not null,
    detail_address varchar not null,
    ad_tag_id integer,
    foreign key(ad_tag_id) references address_tag(id)
);

CREATE TABLE authority (
    id integer primary key,
    authority varchar unique
);

CREATE TABLE member(
    id integer primary key,
    name varchar not null,
    phone_number varchar not null,
    email varchar unique,
    password varchar not null,
    nick varchar unique,
    gender varchar not null,
    age integer not null,
    profile_image_path varchar,
    score integer not null default 0,
    score_provider_count integer not null default 0,
    address_id integer,
    foreign key (address_id) references address(id)
);

CREATE TABLE member_authority(
    member_id integer not null ,
    authority_id integer not null,
    foreign key (member_id) references member(id),
    foreign key (authority_id) references authority(id)
);

CREATE TABLE child_care_group(
    id integer primary key,
    title varchar unique,
    regular_meeting_day varchar not null,
    main_activity varchar not null,
    description varchar not null,
    created_at date,
    modified_at date,
    recruitment_status varchar,
    writer_id integer not null,
    address_tag_id integer not null,
    foreign key (writer_id) references member(id),
    foreign key (address_tag_id) references address_tag(id)
);

CREATE TABLE group_board(
    id integer primary key,
    contents CLOB not null,
    created_at date,
    modified_at date,
    group_id integer not null,
    writer_id integer not null,
    foreign key (group_id) references child_care_group(id),
    foreign key (writer_id) references member(id)
);

CREATE TABLE board_likes(
    board_id integer not null,
    member_id integer not null,
    foreign key(board_id) references group_board(id),
    foreign key(member_id) references member(id)
);

CREATE TABLE comment(
    id integer primary key,
    contents CLOB not null,
    created_at date,
    modified_at date,
    board_id integer not null,
    writer_id integer not null,
    foreign key(board_id) references group_board(id),
    foreign key(writer_id) references member(id)
);

CREATE TABLE image(
    id integer primary key,
    image_name varchar not null,
    image_original_name varchar not null,
    image_path varchar not null,
    image_uri varchar not null,
    board_id integer,
    foreign key(board_id) references group_board(id)
);


CREATE TABLE child(
    id integer primary key,
    name varchar not null,
    birthday date not null,
    school varchar not null,
    special_note varchar,
    member_id integer not null,
    foreign key(member_id) references member(id)
);

CREATE TABLE child_care_expert(
    id integer primary key,
    contents CLOB not null,
    recruit_type varchar not null,
    created_at date,
    modified_at date,
    recruitment_status varchar,
    start_time timestamp not null,
    end_time timestamp not null,
    writer_id integer not null,
    child_id integer,
    manager_id integer,
    address_tag_id integer not null,
    foreign key(writer_id) references member(id),
    foreign key(child_id) references child(id),
    foreign key(manager_id) references member(id),
    foreign key(address_tag_id) references address_tag(id)
);

CREATE TABLE expert_apply(
    id integer primary key,
    contents varchar not null,
    writer_id integer not null,
    child_id integer,
    expert_id integer not null,
    foreign key(writer_id) references member(id),
    foreign key(child_id) references child(id),
    foreign key(expert_id) references child_care_expert(id)
);

CREATE TABLE expert_likes(
    member_id integer not null,
    expert_id integer not null,
    foreign key(member_id) references member(id),
    foreign key(expert_id) references child_care_expert(id)
);

CREATE TABLE group_apply(
    id integer primary key,
    contents varchar not null,
    child_id integer,
    writer_id integer not null,
    group_id integer not null,
    foreign key(child_id) references child(id),
    foreign key(writer_id) references member(id),
    foreign key(group_id) references child_care_group(id)
);

CREATE TABLE group_participating_member(
    id integer primary key,
    participating_type varchar not null,
    member_id integer not null,
    group_id integer not null,
    foreign key (member_id) references member(id),
    foreign key (group_id) references child_care_group(id)
);

CREATE TABLE group_participating_child(
    id integer primary key,
    child_id integer not null,
    group_id integer not null,
    foreign key (child_id) references child(id),
    foreign key (group_id) references child_care_group(id)
);