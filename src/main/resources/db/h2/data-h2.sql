INSERT INTO authority
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_ANONYMOUS');

INSERT INTO address_tag(id, extra_address)
VALUES (1, '(월평동, 전원아파트)');

INSERT INTO address(id, post_code, address, detail_address, address_tag_id)
VALUES (1, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 102동 602호', 1),
       (2, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 102동 403호', 1),
       (3, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 102동 705호', 1);

INSERT INTO member(id, name, phone_number, email, password, nick, gender, age, address_id, score, approval_status)
VALUES (1, 'admin', '01075976959', 'admin@test.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'admin', 'MALE', 25, 1, 5, 'APPROVED'),
       (2, '미연', '01064288185', 'aldusehd@gmail.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'aldusehd', 'FEMALE', 23, 2, 5, 'APPROVED'),
       (3, '테스트', '01011111111', 'test@gmail.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'test', 'FEMALE', 27, 3, 5, 'APPROVED');

INSERT INTO member_authority(member_id, authority_id)
VALUES (1, 2),
       (2, 1),
       (3, 1);

INSERT INTO child(id, name, birthday, school, special_note, gender, member_id)
VALUES (1, '철수', '2000-10-20', '월평초등학교', null, 'MALE', 3),
       (2, '지수', '2002-09-03', '월평초등학교', null, 'FEMALE', 3),
       (3, '민정', '2005-05-10', '갈마초등학교', null, 'FEMALE', 1);

INSERT INTO child_care_group(id, name, regular_meeting_day, main_activity, description, created_at, modified_at, recruitment_status, writer_id, address_tag_id)
VALUES (1, '숲이 조아', '매월 둘째, 넷째 주 토요일 오후 2~5시', '숲 속 체험', '안녕하세요~ 자연과 품앗이의 대표 준이맘입니다\n저희는 품앗이 이름처럼 자연과 함께 할 수 있는 활동으로 진행하고 있습니다.\n 자연과 함께 뛰놀 아이와 부모님 함께 해요~',
now(), now(), 'RECRUITING', 1, 1);

INSERT INTO group_participating_member(member_id, group_id)
VALUES (1, 1);

INSERT INTO child_care_group(id, name, regular_meeting_day, main_activity, description, created_at, modified_at, recruitment_status, writer_id, address_tag_id)
VALUES (2, '우리동네 품앗이', '매월 첫째, 셋째 주 토요일 오후 2~5시', '숲 속 체험', '안녕하세요~ 자연과 품앗이의 대표 준이맘입니다\n저희는 품앗이 이름처럼 자연과 함께 할 수 있는 활동으로 진행하고 있습니다.\n 자연과 함께 뛰놀 아이와 부모님 함께 해요~',
now(), now(), 'CLOSED', 1, 1);

INSERT INTO group_participating_member(member_id, group_id)
VALUES (1, 2);

INSERT INTO playground_vote(id, purpose_using, approval_status, expired_status, address_id, registrant_id, created_at, modified_at)
VALUES (1, '아이들 놀이터', 'APPROVED', 'CLOSED', 1, 1, now(), now());

-- INSERT INTO address_tag
-- VALUES (1, '한밭아파트'),
--        (2, '오투 그란데'),
--        (3, '월평 아파트');

