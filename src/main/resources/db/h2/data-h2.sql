INSERT INTO authority
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

INSERT INTO address_tag(id, extra_address)
VALUES (1, '월평동');

INSERT INTO address(id, post_code, address, detail_address, ad_tag_id)
VALUES (1, '35521', '대전 서구 한밭대로570번길', '청산예술관 3층 301호', 1);

INSERT INTO member(id, name, phone_number, email, password, nick, gender, address_id)
VALUES (1, 'admin', '01075976959', 'admin@test.com', '{bcrypt}$2a$10$uUooeQNjjnw3JqLaKqqKoO9sv/ZqkoRAVOcayqWH1tJe7FF4LN5em', 'admin', 'MALE', 1);

INSERT INTO member_authority(member_id, authority_id)
VALUES (1, 1);

-- INSERT INTO address_tag
-- VALUES (1, '한밭아파트'),
--        (2, '오투 그란데'),
--        (3, '월평 아파트');

