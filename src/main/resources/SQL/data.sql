INSERT IGNORE INTO Member (name, member_id, pass, nickname, birth, gender, zipcode, address, address2, email, email_get, phone, member_reg_date, member_type, member_status)
VALUES
    ('홍길동', 'user1', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname1', '1990-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user1@example.com', 1, '010-1111-2222', SYSDATE(), 1, 0),
    ('이순신', 'user2', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname2', '1985-03-22', 'female', '54321', '서울특별시 종로구', '세종대로 456', 'user2@example.com', 1, '010-3333-4444', SYSDATE(), 1, 0),
    ('김구', 'user3', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname3', '1992-08-30', 'male', '23456', '서울특별시 서초구', '반포대로 789', 'user3@example.com', 0, '010-5555-6666', SYSDATE(), 1, 0),
    ('유관순', 'user4', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname4', '1995-07-12', 'female', '34567', '경기도 성남시', '분당로 123', 'user4@example.com', 1, '010-7777-8888', SYSDATE(), 1, 0),
    ('안중근', 'user5', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname5', '1988-01-15', 'male', '45678', '경기도 수원시', '영통로 456', 'user5@example.com', 1, '010-9999-0000', SYSDATE(), 1, 0),
    ('윤동주', 'user6', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname6', '1991-10-25', 'female', '56789', '부산광역시 해운대구', '광안로 789', 'user6@example.com', 0, '010-1234-5678', SYSDATE(), 1, 0),
    ('장영실', 'user7', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname7', '1989-04-01', 'male', '67890', '대전광역시 유성구', '과학로 123', 'user7@example.com', 1, '010-2468-1357', SYSDATE(), 1, 0),
    ('김민태', 'rlaxoals77', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'Kyle1', '1999-06-12', 'male', '67890', '대전광역시 유성구', '과학로 123', 'rlaxoals77@gmail.com', 1, '010-5478-5037', SYSDATE(), 1, 0),
    ('김민수', 'minsu01', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'minsu', '1990-05-12', 'Male', '06234', '서울특별시 강남구 테헤란로', '101호', 'minsu01@example.com', 1, '010-1244-5678', '2025-01-01', 1, 0),
    ('이유진', 'yujin02', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'yujin', '1995-08-20', 'Female', '07345', '서울특별시 영등포구 여의대로', '102호', 'yujin02@example.com', 1, '010-2345-6789', '2025-01-02', 1, 0),
    ('박준영', 'junyoung03', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'junyoung', '1988-03-15', 'Male', '04567', '서울특별시 중구 세종대로', '103호', 'junyoung03@example.com', 0, '010-3456-7890', '2025-01-03', 1, 0),
    ('최지은', 'jieun04', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'jieun', '1992-12-10', 'Female', '12345', '경기도 성남시 분당구 정자일로', '104호', 'jieun04@example.com', 1, '010-4567-8901', '2025-01-04', 1, 0),
    ('정민호', 'minho05', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'minho', '1985-09-25', 'Male', '54321', '부산광역시 해운대구 해운대로', '105호', 'minho05@example.com', 0, '010-5678-9012', '2025-01-05', 1, 0),
    ('한수연', 'sooyeon06', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'sooyeon', '1993-11-01', 'Female', '98765', '대구광역시 중구 동성로', '106호', 'sooyeon06@example.com', 1, '010-6789-0123', '2025-01-06', 1, 0),
    ('오지훈', 'jihoon07', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'jihoon', '1987-02-14', 'Male', '67890', '인천광역시 남동구 인하로', '107호', 'jihoon07@example.com', 0, '010-7890-1234', '2025-01-07', 1, 0),
    ('윤하나', 'hana08', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'hana', '1991-04-08', 'Female', '11223', '경상남도 창원시 의창구 중앙대로', '108호', 'hana08@example.com', 1, '010-8901-2345', '2025-01-08', 1, 0),
    ('강태현', 'taehyun09', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'taehyun', '1994-06-25', 'Male', '22334', '대전광역시 유성구 대덕대로', '109호', 'taehyun09@example.com', 0, '010-9012-3456', '2025-01-09', 1, 0),
    ('이지혜', 'jihye10', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'jihye', '1989-01-14', 'Female', '33445', '광주광역시 서구 금호로', '110호', 'jihye10@example.com', 1, '010-0123-4567', '2025-01-10', 1, 0),
    ('최현우', 'hyunwoo11', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'hyunwoo', '1980-03-12', 'Male', '44556', '울산광역시 남구 삼산로', '111호', 'hyunwoo11@example.com', 0, '010-1515-2222', '2025-01-11', 1, 0),
    ('문지수', 'jisoo12', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'jisoo', '1993-07-04', 'Female', '55667', '경기도 고양시 일산동구 장항로', '112호', 'jisoo12@example.com', 1, '010-4242-4444', '2025-01-12', 1, 0),
    ('조성민', 'seongmin13', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'seongmin', '1990-10-01', 'Male', '66778', '충청북도 청주시 상당구 상당로', '113호', 'seongmin13@example.com', 0, '010-2425-6666', '2025-01-13', 1, 0),
    ('배수현', 'soohyun14', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'soohyun', '1985-12-25', 'Female', '77889', '전라북도 전주시 완산구 팔달로', '114호', 'soohyun14@example.com', 1, '010-3677-8888', '2025-01-14', 1, 0),
    ('홍영훈', 'younghoon15', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'younghoon', '1992-05-16', 'Male', '88990', '제주특별자치도 제주시 중앙로', '115호', 'younghoon15@example.com', 0, '010-2434-0000', '2025-01-15', 1, 0),
    ('김철수', 'user24', '$2a$10$knv7T7kp4jYsFGYrFXD1xeQU7U8YhM2rM9KNOyKKrG8KoLFy7Wc9O', 'nickname24', '1985-09-20', 'male', '23456', '서울특별시 송파구', '잠실로 456', 'user24@example.com', 1, '010-2512-4444', SYSDATE(), 1, 0),
    ('박지영', 'user25', '$2a$10$zEOw5GnZ8N7F79ZFTmwPQ2cAZdITLZJEXteF1uUq2hT5WuoE7HJ7C', 'nickname25', '1992-11-10', 'female', '34567', '서울특별시 마포구', '홍익로 789', 'user25@example.com', 1, '010-5636-6666', SYSDATE(), 1, 0),
    ('이민호', 'user26', '$2a$10$yGQ.d2QqlOCTu9QzF.L.gJrDRa4vmF9J4XnqshYX8t5hUEI6nLZgS', 'nickname26', '1994-03-25', 'male', '45678', '경기도 성남시', '분당구 황새울로 101', 'user26@example.com', 0, '010-3424-8888', SYSDATE(), 1, 0),
    ('최수정', 'user27', '$2a$10$Lg8zxzyQFvAB1y9QYXq7eFihtlhP5zNS7okfQxeRf8oN35DN7VoGq', 'nickname27', '1988-07-30', 'female', '56789', '부산광역시 해운대구', '해운대해변로 123', 'user27@example.com', 0, '010-2144-0000', SYSDATE(), 1, 0),
    ('전문가', 'user100', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname100', '1990-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user100@example.com', 1, '010-1217-2222', SYSDATE(), 2, 0),
    ('전몬가', 'user200', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname200', '1992-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user200@example.com', 1, '010-1217-3333', SYSDATE(), 2, 0),
    ('전먼가', 'user300', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname300', '1994-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user300@example.com', 1, '010-1217-5555', SYSDATE(), 2, 0);
INSERT IGNORE INTO Professional (member_no, category_no, self_introduction, career, award_career, student_count, rate, professor_status, screening_msg, contactable_time, average_price, review_count)
    ('전문가', 'user100', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname100', '1990-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user100@example.com', 1, '010-1217-2222', SYSDATE(), 1, 0),
    ('관리자', 'admin1', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'admin', '1994-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'admin@example.com', 1, '010-1217-5546', SYSDATE(), 1, 0, 1);

INSERT INTO Professional (member_no, category_no, self_introduction, career, award_career, student_count, rate, professor_status, screening_msg, contactable_time, average_price, review_count)
VALUES
    (1, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 11000, 20),
    (2, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 3, 1, NULL, '오전 8시 - 오후 9시', 15000, 10),
    (3, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 12000, 40),
    (4, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 1, 1, NULL, '오전 8시 - 오후 9시', 110000, 50),
    (5, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 10000, 200),
    (6, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 12000, 222),
    (7, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 15500, 15),
    (8, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 9900, 24),
    (9, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 3, 1, NULL, '오전 8시 - 오후 9시', 12000, 13),
    (10, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 14000, 20),
    (11, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 22000, 22),
    (12, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 7000, 66),
    (13, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 1, 1, NULL, '오전 8시 - 오후 9시', 79000, 75),
    (14, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 135000, 20),
    (15, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 3, 1, NULL, '오전 8시 - 오후 9시', 12400, 2),
    (16, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 12600, 21),
    (17, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 17300, 12),
    (18, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 12300, 15),
    (19, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 53400, 17),
    (20, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 15200, 212),
    (21, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 1, 1, NULL, '오전 8시 - 오후 9시', 63400, 13),
    (22, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 1, 1, NULL, '오전 8시 - 오후 9시', 121200, 20),
    (23, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 3, 1, NULL, '오전 8시 - 오후 9시', 66300, 6),
    (24, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 11000, 3),
    (25, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 15500, 2),
    (26, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 88700, 22),
    (27, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 53500, 18),
    (28, 2, '웹 개발자 전문가입니다.', '15년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 10시', 11100, 25);
-- 카테고리
INSERT IGNORE INTO Category (item_no, item_title, category_no)
VALUES
    (11, '기획', 1),
    (12, '웹', 1),
    (13, '소프트웨어', 1),
    (14, '안드로이드', 1),
    (15, 'iOS', 1),
    (16, '게임', 1),
    (17, 'AI', 1),
    (18, 'QA 및 테스트', 1),
    (21, '가공 및 라벨링', 2),
    (22, '데이터 복구', 2),
    (23, '크롤링', 2),
    (24, 'DB 구축', 2),
    (25, '통계분석', 2);

INSERT IGNORE INTO ProfessionalAdvancedInformation (pro_no, item_no, pro_answer1, pro_answer2, pro_answer3, pro_answer4, pro_answer5)
VALUES (1, 11, '요구사항 정의서', '기술/IT', NULL, NULL, NULL),
       (2, 11, '요구사항 정의서', '기술/IT', NULL, NULL, NULL),
       (3, 11, '요구사항 정의서', '기술/IT', NULL, NULL, NULL),
       (4, 11, '요구사항 정의서', '기술/IT', NULL, NULL, NULL),
       (5, 11, '요구사항 정의서', '요식', NULL, NULL, NULL),
       (6, 11, '요구사항 정의서', '요식', NULL, NULL, NULL),
       (7, 11, '요구사항 정의서', '요식', NULL, NULL, NULL),
       (8, 11, '요구사항 정의서', '요식', NULL, NULL, NULL),
       (9, 11, '기능 명세서', '기술/IT', NULL, NULL, NULL),
       (10, 11, '기능 명세서', '기술/IT', NULL, NULL, NULL),
       (11, 11, '기능 명세서', '기술/IT', NULL, NULL, NULL),
       (12, 11, '기능 명세서', '도소매', NULL, NULL, NULL),
       (13, 11, '기능 명세서', '도소매', NULL, NULL, NULL),
       (14, 11, '기능 명세서', '도소매', NULL, NULL, NULL),
       (15, 11, '기능 명세서', '도소매', NULL, NULL, NULL),
       (16, 11, '기능 명세서', '보건/복지', NULL, NULL, NULL),
       (17, 11, '기능 명세서', '보건/복지', NULL, NULL, NULL),
       (18, 11, '기능 명세서', '보건/복지', NULL, NULL, NULL),
       (19, 11, '스토리보드', '비영리', NULL, NULL, NULL),
       (20, 11, '스토리보드', '비영리', NULL, NULL, NULL),
       (21, 11, '스토리보드', '비영리', NULL, NULL, NULL),
       (22, 11, '스토리보드', '기술/IT', NULL, NULL, NULL),
       (23, 11, '스토리보드', '기술/IT', NULL, NULL, NULL),
       (24, 11, '기타', '비영리', NULL, NULL, NULL),
       (25, 11, '기타', '비영리', NULL, NULL, NULL),
       (26, 11, '기타', '기타', NULL, NULL, NULL),
       (27, 11, '기타', '기타', NULL, NULL, NULL),
       (28, 12, '신규 제작', '미드(4~9년)', NULL, NULL, NULL);
-- 포트폴리오
INSERT IGNORE INTO Portfolio(pro_no, pro_advanced_no, portfolio_title , portfolio_content , visibility,
                      thumbnail_image, portfolio_file1, portfolio_file2, portfolio_file3, portfolio_file4, portfolio_file5,
                      portfolio_file6, portfolio_file7, portfolio_file8, portfolio_file9, portfolio_file10)
VALUES (1, 1, '포트폴리오 제목', '포트폴리오 내용', 1, 'thumbnail image', 'image1', 'image2', 'image3', 'image4', 'image5', null, null, null, null, null);


INSERT IGNORE INTO Portfolio(pro_no, pro_advanced_no, portfolio_title , portfolio_content , visibility,
                      thumbnail_image, portfolio_file1, portfolio_file2, portfolio_file3, portfolio_file4, portfolio_file5,
                      portfolio_file6, portfolio_file7, portfolio_file8, portfolio_file9, portfolio_file10)
VALUES (1, 1, '포트폴리오 제목', '포트폴리오 내용', 1, 'thumbnail image', 'image1', 'image2', 'image3', 'image4', 'image5', null, null, null, null, null);

INSERT IGNORE INTO Portfolio (pro_no, pro_advanced_no, portfolio_title, portfolio_content,thumbnail_image,portfolio_file1)
VALUES
    (1, 1, '기획 프로젝트 A', '대규모 프로젝트 기획 및 실행 사례', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (2, 2, '웹 프로젝트 B', '고객 웹사이트 제작 및 유지 보수 경험', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (3, 3, '소프트웨어 프로젝트 C', '다양한 소프트웨어 개발 프로젝트 사례', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (4, 4, '모바일 앱 프로젝트 D', 'Android 앱 개발 및 배포 사례', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (5, 5, 'iOS 앱 프로젝트 E', 'iOS 앱 개발 및 사용자 경험 개선', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (6, 6, '게임 프로젝트 F', '다수의 인기 게임 개발 사례', 'https://via.placeholder.com/200x100','portfolio_file1 link');

-- 설문조사 더미 데이터 --
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(1, 11, '의뢰 받을 기획서 종류를 선택해주세요.', '요구사항 정의서, 기능 명세서, 스토리보드, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(2, 11, '의뢰 받을 사업 분야를 알려주세요.', '기술/IT, 제조, 도소매, 요식, 교육, 보건/복지, 비영리, 기타');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(3, 12, '의뢰받으려는 프로젝트의 제작 상태를 선택해주세요.', '신규 제작, 기존 웹 리뉴얼');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(4, 12, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(5, 13, '제작 가능한 플랫폼을 선택해주세요.', 'Windows, Mac, Linux, iOS, Android, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(6, 13, '제작 가능한 개발 종류를 선택해주세요.', '일반 프로그램, 게임, 임베디드, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(7, 13, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(8, 14, '개발 가능한 앱 형태를 선택해주세요.', '네이티브, 하이브리드, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(9, 14, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(10, 15, '개발 가능한 형태의 앱을 선택해주세요.', '네이티브, 하이브리드, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(11, 15, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(12, 15, '서버 개발이 가능하신지 선택해주세요.', '네., 아니요.');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(13, 16, '개발 가능한 플랫폼을 선택해주세요.', '안드로이드, PC, iOS, Mac, Linux, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(14, 16, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(15, 17, '개발 가능한 인공지능 기반을 선택해주세요.', '텍스트, 음성, 이미지, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(16, 17, '개발 가능한 작업을 선택해주세요.', '분류, 예측, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(17, 17, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(18, 18, '테스트 가능한 분야를 선택해주세요.', '기능 테스트, 비기능 테스트, 성능 테스트, 테스트 자동화, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(19, 18, '원하는 테스트 기간을 선택해주세요.', '1회 계약, 정기 계약, 무관');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(20, 21, '데이터 가공 및 라벨링이 가능한 형태를 선택해주세요.', '문자, 이미지, 음성, 기타');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(21, 22, '데이터 복구 가능한 것을 선택해주세요.', '스마트기기(휴대폰/태블릿 등), 프로그램(카톡 등), 하드디스크(외장하드 등), 메모리카드(USB/SD/SSD 등), 기타');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(22, 23, '크롤링 가능한 서비스를 선택해주세요.', '웹 크롤링, 앱 크롤링, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(23, 23, '크롤링 가능한 웹사이트/애플리케이션을 선택해주세요.', '검색 포털(네이버/구글 등), 동영상 채널(유튜브 등), 오픈마켓(쿠팡/옥션 등), 쇼핑몰, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(24, 23, '크롤링 프로그램 제작이 가능한지 선택해주세요.', '네., 아니요.');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(25, 24, '데이터베이스 설계가 가능한 분야를 선택해주세요.', '식품, 도소매, 서비스업, IT, 기타');

INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(26, 25, '통계분석 가능한 서비스를 선택해주세요.', '데이터 분석, 통계 모델링, 기타');
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(27, 25, '사용 가능한 분석 프로그램을 선택해주세요.', 'SPSS, R, Python, 엑셀, 무관');

-- 필터링 --
INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(11, 1, '의뢰할 기획서 종류를 선택해주세요.', '요구사항 정의서, 기능 명세서, 스토리보드, 기타'),
      (11, 2, '의뢰할 사업 분야를 알려주세요.', '기술/IT, 제조, 도소매, 요식, 교육, 보건/복지, 비영리, 기타');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(12, 1, '의뢰하려는 프로젝트의 제작 상태를 선택해주세요.', '신규 제작, 기존 웹 리뉴얼'),
      (12, 2, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(13, 1, '제작을 원하는 플랫폼을 선택해주세요.', 'Windows, Mac, Linux, iOS, Android, 기타'),
      (13, 2, '제작을 원하는 개발 종류를 선택해주세요.', '일반 프로그램, 게임, 임베디드, 기타'),
      (13, 3, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(14, 1, '개발을 원하는 앱 형태를 선택해주세요.', '네이티브, 하이브리드, 기타'),
      (14, 2, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(15, 1, '개발을 원하는 형태의 앱을 선택해주세요.', '네이티브, 하이브리드, 기타'),
      (15, 2, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
      (15, 3, '서버 개발이 필요한지 선택해주세요.', '네., 아니요.');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(16, 1, '개발을 원하는 플랫폼을 선택해주세요.', '안드로이드, PC, iOS, Mac, Linux, 기타'),
      (16, 2, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(17, 1, '개발을 원하는 인공지능 기반을 선택해주세요.', '텍스트, 음성, 이미지, 기타'),
      (17, 2, '개발을 원하는 작업을 선택해주세요.', '분류, 예측, 기타'),
      (17, 3, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(18, 1, '테스트 하고 싶은 분야를 선택해주세요.', '기능 테스트, 비기능 테스트, 성능 테스트, 테스트 자동화, 기타'),
      (18, 2, '원하는 테스트 기간을 선택해주세요.', '1회 계약, 정기 계약, 무관');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(21, 1, '데이터 가공 및 라벨링을 원하는 형태를 선택해주세요.', '문자, 이미지, 음성, 기타');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(22, 1, '데이터 복구를 원하는 것을 선택해주세요.', '스마트기기(휴대폰/태블릿 등), 프로그램(카톡 등), 하드디스크(외장하드 등), 메모리카드(USB/SD/SSD 등), 기타');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(23, 1, '크롤링을 원하는 서비스를 선택해주세요.', '웹 크롤링, 앱 크롤링, 기타'),
      (23, 2, '크롤링을 원하는 웹사이트/애플리케이션을 선택해주세요.', '검색 포털(네이버/구글 등), 동영상 채널(유튜브 등), 오픈마켓(쿠팡/옥션 등), 쇼핑몰, 기타'),
      (23, 3, '크롤링 프로그램 제작이 필요한지 선택해주세요.', '네., 아니요.');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(24, 1, '데이터베이스 설계를 원하는 분야를 선택해주세요.', '식품, 도소매, 서비스업, IT, 기타');

INSERT IGNORE INTO Filter(item_no, filter_question_no, filter_question, filter_option)
VALUES(25, 1, '통계분석을 원하는 서비스를 선택해주세요.', '데이터 분석, 통계 모델링, 기타'),
      (25, 2, '원하는 분석 프로그램을 선택해주세요.', 'SPSS, R, Python, 엑셀, 무관');

select * FROM MEMBER;
select * from inquiry;
-- 회원 문의글 더미 데이터 --
INSERT INTO Inquiry (member_no, inquiry_title, inquiry_content, inquiry_reg_date, inquiry_status, inquiry_satisfaction)
VALUES (1, '로그인이 안돼요', '비밀번호를 입력해도 로그인이 되지 않습니다.', NOW(),'답변 대기', false),
(1, '결제 문의드립니다', '결제 후 포인트가 적립되지 않았어요', DATE_SUB(NOW(), INTERVAL 1 DAY), '답변 중', true),
(2, '회원 정보 변경 문의', '프로필 사진을 변경하고 싶은데 방법을 모르겠어요', DATE_SUB(NOW(), INTERVAL 2 DAY), '답변 완료', true),
(2, '게시글 작성 오류', '게시글 작성 시 에러가 발생합니다', DATE_SUB(NOW(), INTERVAL 3 DAY), '답변 대기', false),
(3, '환불 요청드립니다', '잘못 결제했는데 환불 가능할까요?', DATE_SUB(NOW(), INTERVAL 4 DAY), '답변 중', false),
(3, '사이트 이용 문의', '사이트 이용 방법을 알고 싶습니다', DATE_SUB(NOW(), INTERVAL 5 DAY), '답변 완료', false),
(1, '계정 보안 문의', '계정 보안 설정은 어떻게 하나요?', DATE_SUB(NOW(), INTERVAL 6 DAY), '답변 대기', true),
(2, '포인트 사용 문의', '포인트는 어떻게 사용하나요?', DATE_SUB(NOW(), INTERVAL 7 DAY), '답변 중', true),
(3, '회원 탈퇴 문의', '회원 탈퇴는 어떻게 하나요?', DATE_SUB(NOW(), INTERVAL 8 DAY), '답변 완료', false),
(1, '오류 신고드립니다', '페이지 로딩이 안되는 오류가 있습니다', DATE_SUB(NOW(), INTERVAL 9 DAY), '답변 대기', false);

INSERT INTO Inquiry (member_no, inquiry_title, inquiry_content, inquiry_reg_date, inquiry_file, inquiry_status, inquiry_satisfaction)
VALUES (1, '로그인이 안돼요', '비밀번호를 입력했는데 로그인이 되지 않습니다.', NOW(), NULL, '답변 대기', 0),
(2, '회원 정보 수정', '전화번호 변경을 요청합니다.', NOW(), NULL, '답변 중', 0),
(3, '결제 오류', '결제가 완료되지 않고 오류가 발생했습니다.', NOW(), NULL, '답변 완료', 1),
(4, '상품 문의', '해당 상품의 배송 일정이 궁금합니다.', NOW(), NULL, '답변 대기', 0),
(5, '쿠폰 사용법', '프로모션 쿠폰은 어디에서 사용할 수 있나요?', NOW(), NULL, '답변 대기', 0),
(6, '계정 해킹 의심', '다른 사람이 제 계정을 사용한 흔적이 있습니다.', NOW(), NULL, '답변 중', 0),
(7, '환불 요청', '구매한 상품이 마음에 들지 않습니다. 환불 요청합니다.', NOW(), NULL, '답변 중', 0),
(8, '배송 상태 확인', '제 주문이 아직 도착하지 않았습니다. 확인 부탁드립니다.', NOW(), NULL, '답변 완료', 1);

