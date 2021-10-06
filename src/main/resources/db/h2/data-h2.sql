INSERT INTO authority
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_ANONYMOUS');

INSERT INTO address_tag(id, extra_address)
VALUES (1, '월평동');

INSERT INTO address(id, post_code, address, detail_address, ad_tag_id)
VALUES (1, '35521', '대전 서구 한밭대로570번길', '청산예술관 3층 301호', 1);

INSERT INTO member(id, name, phone_number, email, password, nick, gender, age, address_id, score)
VALUES (1, 'admin', '01075976959', 'admin@test.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'admin', 'MALE', 25, 1, 5);

INSERT INTO member(id, name, phone_number, email, password, nick, gender, age, address_id, score)
VALUES (2, '미연', '01064288185', 'aldusehd@naver.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'aldusehd', 'FEMALE', 23, 1, 5);

INSERT INTO member(id, name, phone_number, email, password, nick, gender, age, address_id, score)
VALUES (3, '테스트', '01011111111', 'test@gmail.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'test', 'FEMALE', 27, 1, 5);

INSERT INTO member_authority(member_id, authority_id)
VALUES (1, 1);

INSERT INTO member_authority(member_id, authority_id)
VALUES (2, 1);

INSERT INTO member_authority(member_id, authority_id)
VALUES (3, 1);

INSERT INTO child(id, name, birthday, school, special_note, member_id)
VALUES (1, '철수', '2000-10-20', '월평초등학교', null, 3);

INSERT INTO child(id, name, birthday, school, special_note, member_id)
VALUES (2, '지수', '2002-09-03', '월평초등학교', null, 3);

INSERT INTO child(id, name, birthday, school, special_note, member_id)
VALUES (3, '민정', '2005-05-10', '갈마초등학교', null, 1);

-- INSERT INTO address_tag
-- VALUES (1, '한밭아파트'),
--        (2, '오투 그란데'),
--        (3, '월평 아파트');

