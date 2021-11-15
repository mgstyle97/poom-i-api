INSERT INTO authority
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_ANONYMOUS');

INSERT INTO upload_file(id, file_name, file_access_uri, file_download_uri, content_type)
VALUES (
1, 'board3.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/board3.jpg',
'/api/download?image=board3.jpg', 'image/jpg'),
(
2, 'board1.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/board1.jpg',
'/api/download?image=board1.jpg', 'image/jpg'),
(
3, 'board2.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/board2.jpg',
'/api/download?image=board2.jpg', 'image/jpg'),
(
4, 'form1.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/form1.jpg',
'/api/download?image=form1.jpg', 'image/jpg'),
(
5, 'form2.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/form2.jpg',
'/api/download?image=form2.jpg', 'image/jpg'),
(
6, 'form3.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/form3.jpg',
'/api/download?image=form3.jpg', 'image/jpg'),
(
7, 'form4.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/form4.jpg',
'/api/download?image=form4.jpg', 'image/jpg'),
(
8, 'group1.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/group1.jpg',
'/api/download?image=group1.jpg', 'image/jpg'),
(
9, 'group2.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/group2.jpg',
'/api/download?image=group2.jpg', 'image/jpg'),
(
10, 'group3.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/group3.jpg',
'/api/download?image=group3.jpg', 'image/jpg'),
(
11, 'group4.jpg',
'https://poomi-storage-service.s3.ap-northeast-2.amazonaws.com/group4.jpg',
'/api/download?image=group4.jpg', 'image/jpg'),
(
12, '4a5fa5f4-80ff-40b7-ac2d-f25c0f1ba410.enc',
'http://www.poomi.space:8080/api/image/encrypt?image=4a5fa5f4-80ff-40b7-ac2d-f25c0f1ba410.enc',
'http://www.poomi.space:8080/api/download/encrypt?image=4a5fa5f4-80ff-40b7-ac2d-f25c0f1ba410.enc', 'image/png');

INSERT INTO address_tag(id, extra_address)
VALUES (1, '(월평동, 전원아파트)');

INSERT INTO address(id, post_code, address, detail_address, address_tag_id)
VALUES (1, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 102동 602호', 1),
       (2, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 102동 403호', 1),
       (3, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 102동 705호', 1),
       (4, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 101동 101호', 1),
       (5, '35217', '대전 서구 월평중로 50', '월평동 전원아파트 103동 101호', 1);

INSERT INTO residence_certification(id, approval_status, residence_file_id, expired_validation_token)
VALUES (1, 'APPROVED', 12, 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs'),
       (2, 'APPROVED', 12, 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs'),
       (3, 'APPROVED', 12, 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs'),
       (4, 'APPROVED', 12, 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs');

INSERT INTO member(id, name, phone_number, email, password, nick, gender, age, address_id, approval_status, residence_id)
VALUES (1, 'admin', '01012123636', 'admin@test.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'admin', 'MALE', 25, 1, 'APPROVED', 1),
       (2, '손지연', '01064288185', 'aldusehd@gmail.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', '지연동', 'FEMALE', 23, 2, 'APPROVED', 2),
       (3, '박이정', '01011111111', 'leejung@gmail.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', '아이정', 'FEMALE', 27, 3, 'APPROVED', 3),
       (4, '최창인', '01075976959', 'chain@gmail.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', '최인창', 'MALE', 26, 4, 'APPROVED', 4);

INSERT INTO member_evaluation(id, score, member_id)
VALUES (1, 5, 1),
       (2, 5, 2),
       (3, 5, 3),
       (4, 5, 4);

INSERT INTO member_authority(member_id, authority_id)
VALUES (1, 2),
       (2, 1),
       (3, 1),
       (4, 1);

INSERT INTO child(id, name, birthday, school, special_note, gender, member_id)
VALUES (1, '이철수', '2013-10-20', '월평초등학교', null, 'MALE', 3),
       (2, '이지수', '2014-04-03', '월평초등학교', null, 'FEMALE', 3),
       (3, '최민정', '2014-09-23', '갈마초등학교', null, 'FEMALE', 4),
       (4, '최민호', '2016-07-15', '갈마유치원', null, 'MALE', 4),
       (5, '김라온', '2016-05-10', '갈마유치원', null, 'MALE', 2),
       (6, '김라이', '2016-05-10', '갈마유치원', null, 'FEMALE', 2),
       (7, '김라율', '2018-05-10', '갈마유치원', null, 'FEMALE', 2);

INSERT INTO child_care_group(id, name, regular_meeting_day, main_activity, description, created_at, modified_at, recruitment_status, writer_id, address_tag_id, profile_image_id)
VALUES (1, '숲이 조아', '매월 둘째, 넷째 주 토요일 오후 2~5시', '숲 속 체험', '안녕하세요~ 숲이 조아의 대표 지연동입니다\n저희는 품앗이 이름처럼 자연과 함께 할 수 있는 활동으로 진행하고 있습니다.\n 자연과 함께 뛰놀 아이와 부모님 함께 해요~',
now(), now(), 'RECRUITING', 2, 1, 11);

INSERT INTO group_participating_member(id, member_id, child_id, group_id, participation_type)
VALUES (1, 2, null, 1, 'MANAGE'),
       (2, 3, 1, 1, 'PARTICIPATION'),
       (3, 3, 2, 1, 'PARTICIPATION');

INSERT INTO child_care_group(id, name, regular_meeting_day, main_activity, description, created_at, modified_at, recruitment_status, writer_id, address_tag_id, profile_image_id)
VALUES (2, '우리동네 품앗이', '매월 첫째, 셋째 주 토요일 오후 2~5시', '그림, 놀이', '안녕하세요~ 우리동네 품앗이의 대표 아이정입니다\n저희는 아이들이 다같이 그림도 그리고 놀 수 있는 활동을 진행하고 있습니다.\n 그림과 놀이에 관심있는 아이와 부모님 함께 해요~',
now(), now(), 'CLOSED', 3, 1, 10);

INSERT INTO group_participating_member(id, member_id, child_id, group_id, participation_type)
VALUES (4, 3, null, 2, 'MANAGE'),
       (5, 2, 5, 2, 'PARTICIPATION'),
       (6, 4, 4, 2, 'PARTICIPATION');

INSERT INTO child_care_group(id, name, regular_meeting_day, main_activity, description, created_at, modified_at, recruitment_status, writer_id, address_tag_id, profile_image_id)
VALUES (3, '블럭과 함께', '매월 둘째, 넷째 주 토요일 오후 2~5시', '블럭 체험', '안녕하세요~ 블럭과 함께의 대표 지연동입니다\n저희는 아이들의 창의성을 기를 수 있는 블럭 놀이를 진행하고 있습니다.\n 블럭을 좋아하고 관심있는 아이와 부모님 함께 해요~',
now(), now(), 'RECRUITING', 2, 1, 9);

INSERT INTO group_participating_member(id, member_id, child_id, group_id, participation_type)
VALUES (7, 2, null, 3, 'MANAGE'),
       (8, 3, 2, 3, 'PARTICIPATION'),
       (9, 4, 3, 3, 'PARTICIPATION');

INSERT INTO child_care_group(id, name, regular_meeting_day, main_activity, description, created_at, modified_at, recruitment_status, writer_id, address_tag_id, profile_image_id)
VALUES (4, '문학이 조아요', '매월 첫째, 셋째 주 일요일 오후 1~3시', '독서', '안녕하세요~ 문학이 조아요의 대표 아이정입니다\n저희는 아이들이 독서에 흥미를 가지고 다양한 책을 읽을 수 있는 활동을 진행하고 있습니다.\n 아이들의 글읽기에 관심있는 아이와 부모님 함께 해요~',
now(), now(), 'RECRUITING', 3, 1, 8);

INSERT INTO group_participating_member(id, member_id, group_id, participation_type)
VALUES (10, 3, 1, 'MANAGE');

INSERT INTO group_board(id, contents, created_at, modified_at, group_id, writer_id)
VALUES (1, '오랜만에 야외로 나가보았습니다~ 오랜만에 야외에서의 활동이라 아이들이 너무 좋아해 기분 좋았습니다! 기존에 만들던 천막을 활용해 놀이를 하니 아이들도 뿌듯함을 느낀 것 같더라구요~', now(), now(), 2, 3);

INSERT INTO board_image(board_id, image_id)
VALUES (1, 1);

INSERT INTO group_board(id, contents, created_at, modified_at, group_id, writer_id)
VALUES (2, '이번주 활동은 아이들과 야외에서 뛰어놀며 다양한 활동을 해보았는데요. 오랜만에 바람도 쐬고 아이들이 좋아하는 모습을 보니 기분이 좋더라구요!', now(), now(), 1, 2);

INSERT INTO board_image(board_id, image_id)
VALUES (2, 3);

INSERT INTO group_board(id, contents, created_at, modified_at, group_id, writer_id)
VALUES (3, '한동안 모두가 바빠서 활동을 못했었는데 이렇게 넓은 들판에 놀러갔다오니 기분이 상쾌했습니다!, 다음에는 다른 분들도 꼭 함께했으면 좋겠네요~', now(), now(), 2, 3);

INSERT INTO board_image(board_id, image_id)
VALUES (3, 2);

INSERT INTO playground_vote(id, purpose_using, approval_status, expired_status, address_id, registrant_id, created_at, modified_at, expired_validation_token)
VALUES (1, '아이들 놀이 및 무대 공간', 'APPROVED', 'VOTING', 1, 2, now(), now(), 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs');

INSERT INTO playground_voter(id, dong, ho, vote_type, vote_id)
VALUES (1, '101동', '101호', 'NOT_YET', 1),
       (2, '101동', '102호', 'AGREE', 1),
       (3, '101동', '103호', 'NOT_YET', 1),
       (4, '102동', '201호', 'AGREE', 1),
       (5, '102동', '202호', 'NOT_YET', 1),
       (6, '102동', '203호', 'DISAGREE', 1),
       (7, '103동', '301호', 'AGREE', 1),
       (8, '103동', '302호', 'DISAGREE', 1),
       (9, '103동', '303호', 'NOT_YET', 1);

INSERT INTO playground_vote_image(vote_id, image_id)
VALUES (1, 6);

INSERT INTO playground_vote(id, purpose_using, approval_status, expired_status, address_id, registrant_id, created_at, modified_at, expired_validation_token)
VALUES (2, '아이들과 부모님 쉼터', 'APPROVED', 'VOTING', 1, 4, now(), now(), 'eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTIwMDYxNDgsInJlc2lkZW5jZSI6InJlc2lkZW5jZSB0b2tlbiJ9.YpAU_PhDGf8fP1iIaA_oAdrmYtTmQijeWCUNwrUVINs');

INSERT INTO playground_voter(id, dong, ho, vote_type, vote_id)
VALUES (10, '101동', '101호', 'AGREE', 2),
       (11, '101동', '102호', 'AGREE', 2),
       (12, '101동', '103호', 'AGREE', 2),
       (13, '102동', '201호', 'NOT_YET', 2),
       (14, '102동', '202호', 'NOT_YET', 2),
       (15, '102동', '203호', 'DISAGREE', 2),
       (16, '103동', '301호', 'NOT_YET', 2),
       (17, '103동', '302호', 'AGREE', 2),
       (18, '103동', '303호', 'AGREE', 2);

INSERT INTO playground_vote_image(vote_id, image_id)
VALUES (2, 7);

INSERT INTO child_care_playground(id, name, operating_hours, holiday, call_number, features, address_id, registrant_id)
VALUES (
    1, 'WS APT 101동 101호', '09:00~21:00', '월요일 휴무', '042-000-0000', '무료 이용, 팀당 보호자 1인 필수',
    4, 1
);

INSERT INTO playground_image(playground_id, image_id)
VALUES (1, 4);

INSERT INTO playground_search(playground_id, address_tag_id)
VALUES (1, 1);

INSERT INTO child_care_playground(id, name, operating_hours, holiday, call_number, features, address_id, registrant_id)
VALUES (
    2, 'WS APT 103동 101호', '10:00~18:00', '화요일, 수요일 휴무', '042-111-1111', '무료 이용, 점심 간식 제공',
    5, 1
);

INSERT INTO playground_image(playground_id, image_id)
VALUES (2, 5);

INSERT INTO playground_search(playground_id, address_tag_id)
VALUES (2, 1);

INSERT INTO child_care_expert(id, contents, recruit_type, created_at, modified_at, recruitment_status, start_time, end_time, writer_id, writer_child_id, address_tag_id)
VALUES (1, '갑자기 야근이 생겼어요.. 한 9시쯤에야 집에 들어갈 수 있을 것 같은데 혹시 우리 애 봐주실 수 있는 분 계신가요?\n학원에서 6시에 끝나 돌아옵니다..!', 'RECRUIT', now(), now(), 'RECRUITING', '2021-11-15 17:00:00', '2021-11-15 19:00:00', 2, 5, 1),
       (2, '비가 오네요,, 딸아이 마중 나가려 하는데 ㅁㅁ초등학교 자녀 분 계심 우산 같이 씌워줄게요!\n도움 필요하신 분들 신청하세요:)', 'VOLUNTEER', now(), now(), 'RECRUITING', '2021-11-15 14:00:00', '2021-11-15 16:00:00', 4, 3, 1),
       (3, '오늘 일찍 퇴근하게 되어서 우리 아이 태권도 끝나는 시간(3시 ~ 4시)에 맞춰서 마중 나가려 합니다.\n 한마음태권도 다니는 자녀분 있으시면 신청해주세요,,! ', 'VOLUNTEER', now(), now(), 'RECRUITING', '2021-11-15 15:00:00', '2021-11-15 16:30:00', 3, 1, 1);

-- INSERT INTO address_tag
-- VALUES (1, '한밭아파트'),
--        (2, '오투 그란데'),
--        (3, '월평 아파트');

