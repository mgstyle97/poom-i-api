INSERT INTO authority
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_ANONYMOUS');

INSERT INTO upload_file(id, file_name, file_access_uri, file_download_uri, content_type)
VALUES (
1, '1dfe5783-67cf-45d8-990d-53732442e0ee.png',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/1dfe5783-67cf-45d8-990d-53732442e0ee.png',
'/api/download?image=1dfe5783-67cf-45d8-990d-53732442e0ee.png', 'image/png');

INSERT INTO address_tag(id, extra_address)
VALUES (1, '(월평동, 전원아파트)');

INSERT INTO address(id, post_code, address, detail_address, address_tag_id)
VALUES (1, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 102동 602호', 1),
       (2, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 102동 403호', 1),
       (3, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 102동 705호', 1),
       (4, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 101동 101호', 1),
       (5, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 103동 101호', 1);

INSERT INTO residence_certification(id, approval_status, residence_file_id, expired_validation_token)
VALUES (1, 'APPROVED', 1, 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs'),
       (2, 'APPROVED', 1, 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs'),
       (3, 'APPROVED', 1, 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs');

INSERT INTO member(id, name, phone_number, email, password, nick, gender, age, address_id, approval_status, residence_id)
VALUES (1, 'admin', '01075976959', 'admin@test.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'admin', 'MALE', 25, 1, 'APPROVED', 1),
       (2, '미연', '01064288185', 'aldusehd@gmail.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'aldusehd', 'FEMALE', 23, 2, 'APPROVED', 2),
       (3, '테스트', '01011111111', 'test@gmail.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'test', 'FEMALE', 27, 3, 'APPROVED', 3);

INSERT INTO member_evaluation(id, score, member_id)
VALUES (1, 5, 1),
       (2, 5, 2),
       (3, 5, 3);

INSERT INTO member_authority(member_id, authority_id)
VALUES (1, 2),
       (2, 1),
       (3, 1);

INSERT INTO child(id, name, birthday, school, special_note, gender, member_id)
VALUES (1, '철수', '2000-10-20', '월평초등학교', null, 'MALE', 3),
       (2, '지수', '2002-09-03', '월평초등학교', null, 'FEMALE', 3),
       (3, '민정', '2005-05-10', '갈마초등학교', null, 'FEMALE', 1);

INSERT INTO child_care_group(id, name, regular_meeting_day, main_activity, description, created_at, modified_at, recruitment_status, writer_id, address_tag_id, profile_image_id)
VALUES (1, '숲이 조아', '매월 둘째, 넷째 주 토요일 오후 2~5시', '숲 속 체험', '안녕하세요~ 자연과 품앗이의 대표 준이맘입니다\n저희는 품앗이 이름처럼 자연과 함께 할 수 있는 활동으로 진행하고 있습니다.\n 자연과 함께 뛰놀 아이와 부모님 함께 해요~',
now(), now(), 'RECRUITING', 1, 1, 1);

INSERT INTO group_participating_member(id, member_id, group_id, participation_type)
VALUES (1, 1, 1, 'MANAGE');

INSERT INTO child_care_group(id, name, regular_meeting_day, main_activity, description, created_at, modified_at, recruitment_status, writer_id, address_tag_id, profile_image_id)
VALUES (2, '우리동네 품앗이', '매월 첫째, 셋째 주 토요일 오후 2~5시', '숲 속 체험', '안녕하세요~ 자연과 품앗이의 대표 준이맘입니다\n저희는 품앗이 이름처럼 자연과 함께 할 수 있는 활동으로 진행하고 있습니다.\n 자연과 함께 뛰놀 아이와 부모님 함께 해요~',
now(), now(), 'CLOSED', 1, 1, 1);

INSERT INTO group_participating_member(id, member_id, group_id, participation_type)
VALUES (2, 1, 2, 'MANAGE');

INSERT INTO playground_vote(id, purpose_using, approval_status, expired_status, address_id, registrant_id, created_at, modified_at, expired_validation_token)
VALUES (1, '아이들 놀이터', 'APPROVED', 'VOTING', 1, 1, now(), now(), 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs');

INSERT INTO playground_voter(id, dong, ho, vote_type, vote_id)
VALUES (1, '101동', '101호', 'NOT_YET', 1),
       (2, '101동', '102호', 'NOT_YET', 1),
       (3, '101동', '103호', 'NOT_YET', 1),
       (4, '102동', '201호', 'NOT_YET', 1),
       (5, '102동', '202호', 'NOT_YET', 1),
       (6, '102동', '203호', 'NOT_YET', 1),
       (7, '103동', '301호', 'NOT_YET', 1),
       (8, '103동', '302호', 'NOT_YET', 1),
       (9, '103동', '303호', 'NOT_YET', 1);


INSERT INTO child_care_playground(id, name, operating_hours, holiday, call_number, features, address_id, registrant_id)
VALUES (
    1, 'WS APT 101동 101호', '09:00~21:00', '월요일 휴무', '042-000-0000', '무료 이용, 팀당 보호자 1인 필수',
    4, 1
);

INSERT INTO playground_image(playground_id, image_id)
VALUES (1, 1);

INSERT INTO playground_search(playground_id, address_tag_id)
VALUES (1, 1);

INSERT INTO child_care_playground(id, name, operating_hours, holiday, call_number, features, address_id, registrant_id)
VALUES (
    2, 'WS APT 103동 101호', '10:00~18:00', '화요일, 수요일 휴무', '042-111-1111', '무료 이용, 점심 간식 제공',
    5, 1
);

INSERT INTO playground_image(playground_id, image_id)
VALUES (2, 1);

INSERT INTO playground_search(playground_id, address_tag_id)
VALUES (2, 1);

-- INSERT INTO address_tag
-- VALUES (1, '한밭아파트'),
--        (2, '오투 그란데'),
--        (3, '월평 아파트');

