DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS authority CASCADE;
DROP TABLE IF EXISTS address_tag CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS member_authority CASCADE;
DROP TABLE IF EXISTS residence_certification CASCADE;
DROP TABLE IF EXISTS child CASCADE;
DROP TABLE IF EXISTS sms_certification CASCADE;
DROP TABLE IF EXISTS email_certification CASCADE;
DROP TABLE IF EXISTS child_care_group CASCADE;
DROP TABLE IF EXISTS board_image CASCADE;
DROP TABLE IF EXISTS group_board CASCADE;
DROP TABLE IF EXISTS board_likes CASCADE;
DROP TABLE IF EXISTS group_apply CASCADE;
DROP TABLE IF EXISTS group_participating_member CASCADE;
DROP TABLE IF EXISTS group_participating_child CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS upload_file CASCADE;
DROP TABLE IF EXISTS child_care_expert CASCADE;
DROP TABLE IF EXISTS expert_apply CASCADE;
DROP TABLE IF EXISTS expert_likes CASCADE;
DROP TABLE IF EXISTS child_care_playground CASCADE;
DROP TABLE IF EXISTS playground_image CASCADE;
DROP TABLE IF EXISTS playground_search CASCADE;
DROP TABLE IF EXISTS playground_vote CASCADE;
DROP TABLE IF EXISTS playground_vote_image CASCADE;
DROP TABLE IF EXISTS playground_voter CASCADE;
DROP TABLE IF EXISTS chat_room CASCADE;
DROP TABLE IF EXISTS chat_message CASCADE;
DROP TABLE IF EXISTS room_participating_member CASCADE;
DROP TABLE IF EXISTS notice CASCADE;

CREATE TABLE upload_file(
    id integer primary key,
    file_name varchar not null,
    file_access_uri varchar not null,
    file_download_uri varchar not null,
    content_type varchar not null
);

CREATE TABLE sms_certification(
    id integer primary key,
    phone_number varchar not null,
    certification_number varchar not null,
    expired_validation_token varchar not null
);

CREATE TABLE email_certification(
    id integer primary key,
    email varchar not null,
    certification_number varchar not null,
    expired_validation_token varchar not null
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
    address_tag_id integer,
    foreign key(address_tag_id) references address_tag(id)
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
    profile_image_id integer,
    score integer not null default 0,
    score_provider_count integer not null default 0,
    approval_status varchar not null default 'UN_APPROVED',
    address_id integer,
    foreign key (address_id) references address(id),
    foreign key (profile_image_id) references upload_file(id),
    CONSTRAINT approval_value CHECK approval_status in ('APPROVED', 'UN_APPROVED')
);

CREATE TABLE member_authority(
    member_id integer not null ,
    authority_id integer not null,
    foreign key (member_id) references member(id),
    foreign key (authority_id) references authority(id),
    primary key (member_id, authority_id)
);

CREATE TABLE residence_certification(
    id integer primary key,
    certification_status varchar not null default 'UN_APPROVED',
    member_id integer not null,
    residence_file_id integer not null,
    expired_validation_token varchar,
    foreign key (member_id) references member(id),
    foreign key (residence_file_id) references upload_file(id)
);

CREATE TABLE child_care_group(
    id integer primary key,
    name varchar unique,
    regular_meeting_day varchar not null,
    main_activity varchar not null,
    description varchar not null,
    created_at date,
    modified_at date,
    recruitment_status varchar,
    writer_id integer not null,
    address_tag_id integer not null,
    profile_image_id integer,
    foreign key (writer_id) references member(id),
    foreign key (address_tag_id) references address_tag(id),
    foreign key (profile_image_id) references upload_file(id)
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

CREATE TABLE board_image(
    board_id integer not null,
    image_id integer not null,
    foreign key (board_id) references group_board(id),
    foreign key (image_id) references upload_file(id),
    primary key (board_id, image_id)
);

CREATE TABLE board_likes(
    board_id integer not null,
    member_id integer not null,
    foreign key(board_id) references group_board(id),
    foreign key(member_id) references member(id),
    primary key (board_id, member_id)
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


CREATE TABLE child(
    id integer primary key,
    name varchar not null,
    birthday date not null,
    school varchar not null,
    special_note varchar,
    gender varchar not null,
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
    foreign key(expert_id) references child_care_expert(id),
    primary key (member_id, expert_id)
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
    member_id integer not null,
    child_id integer,
    group_id integer not null,
    participation_type varchar not null,
    foreign key (member_id) references member(id),
    foreign key (child_id) references child(id),
    foreign key (group_id) references child_care_group(id)
);

-- CREATE TABLE group_participating_child(
--     child_id integer not null,
--     group_id integer not null,
--     foreign key (child_id) references child(id),
--     foreign key (group_id) references child_care_group(id),
--     primary key (child_id, group_id)
-- );

CREATE TABLE child_care_playground(
    id integer primary key,
    name varchar not null,
    operating_hours varchar not null,
    holiday varchar not null,
    call_number varchar not null,
    features CLOB,
    address_id integer not null,
    registrant_id integer not null,
    foreign key (address_id) references address(id),
    foreign key (registrant_id) references member(id)
);

CREATE TABLE playground_image(
    playground_id integer not null,
    image_id integer not null,
    foreign key (playground_id) references child_care_playground(id),
    foreign key (image_id) references upload_file(id),
    primary key (playground_id, image_id)
);

CREATE TABLE playground_search(
    playground_id integer not null,
    address_tag_id integer not null,
    foreign key (playground_id) references child_care_playground(id),
    foreign key (address_tag_id) references address_tag(id),
    primary key (playground_id, address_tag_id)
);

CREATE TABLE playground_vote(
    id integer primary key,
    purpose_using varchar not null,
    approval_status varchar not null default 'UN_APPROVED',
    expired_status varchar not null,
    address_id integer not null,
    registrant_id integer not null,
    expired_validation_token varchar,
    created_at date,
    modified_at date,
    foreign key (address_id) references address(id),
    foreign key (registrant_id) references member(id)
);

CREATE TABLE playground_vote_image(
    vote_id integer not null,
    image_id integer not null,
    foreign key (vote_id) references playground_vote(id),
    foreign key (image_id) references upload_file(id),
    primary key (vote_id, image_id)
);

CREATE TABLE playground_voter(
    id integer primary key,
    dong varchar,
    ho varchar not null,
    vote_type varchar not null default 'NOT_YET',
    vote_id integer not null,
    foreign key (vote_id) references playground_vote(id)
);

CREATE TABLE chat_room(
    id integer primary key
);

CREATE TABLE chat_message(
    id integer primary key,
    message CLOB not null,
    reading_status varchar not null default 'NOT_READ',
    sender_id integer not null,
    room_id integer not null,
    foreign key (sender_id) references member(id),
    foreign key (room_id) references chat_room(id)
);

CREATE TABLE room_participating_member(
    id integer primary key,
    member_id integer not null,
    room_id integer not null,
    foreign key (member_id) references member(id),
    foreign key (room_id) references chat_room(id)
);

CREATE TABLE notice(
    id integer primary key,
    contents CLOB not null,
    reading_status varchar not null default 'NOT_READ',
    receiver_id integer not null,
    foreign key (receiver_id) references member(id)
);