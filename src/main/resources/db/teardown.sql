-- 더미 데이터 작성 파일
SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE users;
TRUNCATE TABLE trees;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO users (username, password, nickname, role, created_at)
VALUES ('admin', 'admin', 'admin', 'ADMIN', '2021-01-01 00:00:00'),
       ('user', 'user', 'user', 'USER', '2021-01-01 00:00:00');

INSERT INTO trees (user_id, name, level, experience, image_url, price, access_level, start_date, end_date, created_at)
VALUES (1, '나무1', 1, 1, 'https://via.placeholder.com/150', 0, 'FREE', '2021-01-01 00:00:00', '2021-12-31 23:59:59', '2021-01-01 00:00:00'),
       (1, '나무2', 2, 5, 'https://via.placeholder.com/150', 0, 'FREE', '2021-01-01 00:00:00', '2021-12-31 23:59:59', '2021-01-01 00:00:00'),
       (1, '나무3', 3, 35, 'https://via.placeholder.com/150', 100, 'PAY', '2021-01-01 00:00:00', '2021-12-31 23:59:59', '2021-01-01 00:00:00'),
       (1, '나무4', 1, 1, 'https://via.placeholder.com/150', 0, 'FREE', '2021-01-01 00:00:00', '2021-12-31 23:59:59', '2021-01-01 00:00:00'),
       (2, '나무5', 2, 5, 'https://via.placeholder.com/150', 0, 'FREE', '2021-01-01 00:00:00', '2021-12-31 23:59:59', '2021-01-01 00:00:00'),
       (2, '나무6', 3, 35, 'https://via.placeholder.com/150', 100, 'PAY', '2021-01-01 00:00:00', '2021-12-31 23:59:59', '2021-01-01 00:00:00'),
       (2, '나무7', 1, 1, 'https://via.placeholder.com/150', 0, 'FREE', '2021-01-01 00:00:00', '2021-12-31 23:59:59', '2021-01-01 00:00:00'),
       (2, '나무8', 2, 5, 'https://via.placeholder.com/150', 0, 'FREE', '2021-01-01 00:00:00', '2021-12-31 23:59:59', '2021-01-01 00:00:00'),
       (2, '나무9', 3, 35, 'https://via.placeholder.com/150', 100, 'PAY', '2021-01-01 00:00:00', '2021-12-31 23:59:59', '2021-01-01 00:00:00');


