-- 더미 데이터 작성 파일
SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE users;
TRUNCATE TABLE trees;
TRUNCATE TABLE tree_posts;
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


INSERT INTO tree_posts (tree_id, user_id, content, image_url, created_at)
VALUES (1, 1, '나무1 게시글1', 'https://via.placeholder.com/150', now()),
       (1, 1, '나무1 게시글2', 'https://via.placeholder.com/150', now()),
       (1, 1, '나무1 게시글3', 'https://via.placeholder.com/150', now()),
       (1, 1, '나무1 게시글4', 'https://via.placeholder.com/150', '2021-01-01 00:00:00'),
       (1, 1, '나무1 게시글5', 'https://via.placeholder.com/150', '2021-01-02 00:00:00'),
       (1, 1, '나무1 게시글6', 'https://via.placeholder.com/150', '2021-01-03 00:00:00'),
       (1, 1, '나무1 게시글7', 'https://via.placeholder.com/150', '2021-01-04 00:00:00'),
       (1, 1, '나무1 게시글8', 'https://via.placeholder.com/150', '2021-01-05 00:00:00'),
       (1, 1, '나무1 게시글9', 'https://via.placeholder.com/150', '2021-01-06 00:00:00'),
       (2, 1, '나무2 게시글1', 'https://via.placeholder.com/150', '2021-01-07 00:00:00'),
       (2, 1, '나무2 게시글2', 'https://via.placeholder.com/150', '2021-01-08 00:00:00'),
       (2, 1, '나무2 게시글3', 'https://via.placeholder.com/150', '2021-01-01 00:00:00'),
       (3, 1, '나무3 게시글1', 'https://via.placeholder.com/150', '2021-01-01 00:00:00'),
       (3, 1, '나무3 게시글2', 'https://via.placeholder.com/150', '2021-01-01 00:00:00'),
       (3, 1, '나무3 게시글3', 'https://via.placeholder.com/150', '2021-01-01 00:00:00'),
       (4, 1, '나무4 게시글1', 'https://via.placeholder.com/150', '2021-01-01 00:00:00'),
       (4, 1, '나무4 게시글2', 'https://via.placeholder.com/150', '2021-01-01 00:00:00'),
       (4, 1, '나무4 게시글3', 'https://via.placeholder.com/150', '2021-01-01 00:00:00'),
       (5, 2, '나무5 게시글1', 'https://via.placeholder.com/150', '2021-01-01 00:00:00');