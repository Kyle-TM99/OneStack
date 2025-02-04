INSERT IGNORE INTO Member (member_no, name, member_id, pass, nickname, birth, gender, zipcode, address, address2, email, email_get, phone, member_reg_date, member_type, member_status,member_image)
VALUES
    (1, '홍길동', 'user1', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname1', '1990-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user1@example.com', 1, '010-1111-2222', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (2, '이순신', 'user2', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname2', '1985-03-22', 'female', '54321', '서울특별시 종로구', '세종대로 456', 'user2@example.com', 1, '010-3333-4444', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (3, '김구', 'user3', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname3', '1992-08-30', 'male', '23456', '서울특별시 서초구', '반포대로 789', 'user3@example.com', 0, '010-5555-6666', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (4, '유관순', 'user4', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname4', '1995-07-12', 'female', '34567', '경기도 성남시', '분당로 123', 'user4@example.com', 1, '010-7777-8888', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (5, '안중근', 'user5', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname5', '1988-01-15', 'male', '45678', '경기도 수원시', '영통로 456', 'user5@example.com', 1, '010-9999-0000', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (6, '윤동주', 'user6', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname6', '1991-10-25', 'female', '56789', '부산광역시 해운대구', '광안로 789', 'user6@example.com', 0, '010-1234-5678', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (7, '장영실', 'user7', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname7', '1989-04-01', 'male', '67890', '대전광역시 유성구', '과학로 123', 'user7@example.com', 1, '010-2468-1357', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (8, '김민태', 'rlaxoals77', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'Kyle1', '1999-06-12', 'male', '67890', '대전광역시 유성구', '과학로 123', 'rlaxoals77@gmail.com', 1, '010-5478-5037', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (9, '김민수', 'minsu01', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'minsu', '1990-05-12', 'Male', '06234', '서울특별시 강남구 테헤란로', '101호', 'minsu01@example.com', 1, '010-1244-5678', '2025-01-01', 1, 0,'/images/default-profile.png'),
    (10, '이유진', 'yujin02', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'yujin', '1995-08-20', 'Female', '07345', '서울특별시 영등포구 여의대로', '102호', 'yujin02@example.com', 1, '010-2345-6789', '2025-01-02', 1, 0,'/images/default-profile.png'),
    (11, '박준영', 'junyoung03', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'junyoung', '1988-03-15', 'Male', '04567', '서울특별시 중구 세종대로', '103호', 'junyoung03@example.com', 0, '010-3456-7890', '2025-01-03', 1, 0,'/images/default-profile.png'),
    (12, '최지은', 'jieun04', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'jieun', '1992-12-10', 'Female', '12345', '경기도 성남시 분당구 정자일로', '104호', 'jieun04@example.com', 1, '010-4567-8901', '2025-01-04', 1, 0,'/images/default-profile.png'),
    (13, '정민호', 'minho05', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'minho', '1985-09-25', 'Male', '54321', '부산광역시 해운대구 해운대로', '105호', 'minho05@example.com', 0, '010-5678-9012', '2025-01-05', 1, 0,'/images/default-profile.png'),
    (14, '한수연', 'sooyeon06', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'sooyeon', '1993-11-01', 'Female', '98765', '대구광역시 중구 동성로', '106호', 'sooyeon06@example.com', 1, '010-6789-0123', '2025-01-06', 1, 0,'/images/default-profile.png'),
    (15, '오지훈', 'jihoon07', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'jihoon', '1987-02-14', 'Male', '67890', '인천광역시 남동구 인하로', '107호', 'jihoon07@example.com', 0, '010-7890-1234', '2025-01-07', 1, 0,'/images/default-profile.png'),
    (16, '윤하나', 'hana08', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'hana', '1991-04-08', 'Female', '11223', '경상남도 창원시 의창구 중앙대로', '108호', 'hana08@example.com', 1, '010-8901-2345', '2025-01-08', 1, 0,'/images/default-profile.png'),
    (17, '강태현', 'taehyun09', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'taehyun', '1994-06-25', 'Male', '22334', '대전광역시 유성구 대덕대로', '109호', 'taehyun09@example.com', 0, '010-9012-3456', '2025-01-09', 1, 0,'/images/default-profile.png'),
    (18, '이지혜', 'jihye10', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'jihye', '1989-01-14', 'Female', '33445', '광주광역시 서구 금호로', '110호', 'jihye10@example.com', 1, '010-0123-4567', '2025-01-10', 1, 0,'/images/default-profile.png'),
    (19, '최현우', 'hyunwoo11', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'hyunwoo', '1980-03-12', 'Male', '44556', '울산광역시 남구 삼산로', '111호', 'hyunwoo11@example.com', 0, '010-1515-2222', '2025-01-11', 1, 0,'/images/default-profile.png'),
    (20, '문지수', 'jisoo12', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'jisoo', '1993-07-04', 'Female', '55667', '경기도 고양시 일산동구 장항로', '112호', 'jisoo12@example.com', 1, '010-4242-4444', '2025-01-12', 1, 0,'/images/default-profile.png'),
    (21, '조성민', 'seongmin13', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'seongmin', '1990-10-01', 'Male', '66778', '충청북도 청주시 상당구 상당로', '113호', 'seongmin13@example.com', 0, '010-2425-6666', '2025-01-13', 1, 0,'/images/default-profile.png'),
    (22, '배수현', 'soohyun14', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'soohyun', '1985-12-25', 'Female', '77889', '전라북도 전주시 완산구 팔달로', '114호', 'soohyun14@example.com', 1, '010-3677-8888', '2025-01-14', 1, 0,'/images/default-profile.png'),
    (23, '홍영훈', 'younghoon15', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'younghoon', '1992-05-16', 'Male', '88990', '제주특별자치도 제주시 중앙로', '115호', 'younghoon15@example.com', 0, '010-2434-0000', '2025-01-15', 1, 0,'/images/default-profile.png'),
    (24, '김철수', 'user24', '$2a$10$knv7T7kp4jYsFGYrFXD1xeQU7U8YhM2rM9KNOyKKrG8KoLFy7Wc9O', 'nickname24', '1985-09-20', 'male', '23456', '서울특별시 송파구', '잠실로 456', 'user24@example.com', 1, '010-2512-4444', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (25, '박지영', 'user25', '$2a$10$zEOw5GnZ8N7F79ZFTmwPQ2cAZdITLZJEXteF1uUq2hT5WuoE7HJ7C', 'nickname25', '1992-11-10', 'female', '34567', '서울특별시 마포구', '홍익로 789', 'user25@example.com', 1, '010-5636-6666', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (26, '이민호', 'user26', '$2a$10$yGQ.d2QqlOCTu9QzF.L.gJrDRa4vmF9J4XnqshYX8t5hUEI6nLZgS', 'nickname26', '1994-03-25', 'male', '45678', '경기도 성남시', '분당구 황새울로 101', 'user26@example.com', 0, '010-3424-8888', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (27, '최수정', 'user27', '$2a$10$Lg8zxzyQFvAB1y9QYXq7eFihtlhP5zNS7okfQxeRf8oN35DN7VoGq', 'nickname27', '1988-07-30', 'female', '56789', '부산광역시 해운대구', '해운대해변로 123', 'user27@example.com', 0, '010-2144-0000', SYSDATE(), 1, 0,'/images/default-profile.png'),
    (28, '전문가', 'user100', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname100', '1990-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user100@example.com', 1, '010-1217-2222', SYSDATE(), 2, 0,'/images/default-profile.png'),
    (29, '전몬가', 'user200', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname200', '1992-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user200@example.com', 1, '010-1217-3333', SYSDATE(), 2, 0,'/images/default-profile.png'),
    (30, '전먼가', 'user300', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname300', '1994-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user300@example.com', 1, '010-1217-5555', SYSDATE(), 2, 0,'/images/default-profile.png');

INSERT IGNORE INTO Member (member_no, name, member_id, pass, nickname, birth, gender, zipcode, address, address2, email, email_get, phone, member_reg_date, member_type, member_status, is_admin, member_image)
   VALUES(31, '관리자', 'admin1', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'admin', '1994-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'admin@example.com', 1, '010-1217-5546', SYSDATE(), 1, 0, 1, '/images/admin.png');

INSERT IGNORE INTO Professional (pro_no, member_no, category_no, self_introduction, career, award_career, student_count, rate, professor_status, screening_msg, contactable_time, average_price, review_count)
VALUES
    (1, 1, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 11000, 20),
    (2, 2, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 3, 1, NULL, '오전 8시 - 오후 9시', 15000, 10),
    (3, 3, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 12000, 40),
    (4, 4, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 1, 1, NULL, '오전 8시 - 오후 9시', 110000, 50),
    (5, 5, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 10000, 200),
    (6, 6, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 12000, 222),
    (7, 7, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 15500, 15),
    (8, 8, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 9900, 24),
    (9, 9, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 3, 1, NULL, '오전 8시 - 오후 9시', 12000, 13),
    (10, 10, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 14000, 20),
    (11, 11, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 22000, 22),
    (12, 12, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 7000, 66),
    (13, 13, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 1, 1, NULL, '오전 8시 - 오후 9시', 79000, 75),
    (14, 14, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 135000, 20),
    (15, 15, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 3, 1, NULL, '오전 8시 - 오후 9시', 12400, 2),
    (16, 16, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 12600, 21),
    (17, 17, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 17300, 12),
    (18, 18, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 12300, 15),
    (19, 19, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 53400, 17),
    (20, 20, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 2, 1, NULL, '오전 8시 - 오후 9시', 15200, 212),
    (21, 21, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 1, 1, NULL, '오전 8시 - 오후 9시', 63400, 13),
    (22, 22, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 1, 1, NULL, '오전 8시 - 오후 9시', 121200, 20),
    (23, 23, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 3, 1, NULL, '오전 8시 - 오후 9시', 66300, 6),
    (24, 24, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 11000, 3),
    (25, 25, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 9시', 15500, 2),
    (26, 26, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 88700, 22),
    (27, 27, 1, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 1, NULL, '오전 8시 - 오후 9시', 53500, 18),
    (28, 28, 2, '웹 개발자 전문가입니다.', '15년 경력의 기획자', NULL, 100, 4, 1, NULL, '오전 8시 - 오후 10시', 11100, 25);

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

INSERT IGNORE INTO ProfessionalAdvancedInformation (pro_advanced_no, pro_no, item_no, pro_answer1, pro_answer2, pro_answer3, pro_answer4, pro_answer5)
VALUES (1, 1, 11, '요구사항 정의서', '기술/IT', NULL, NULL, NULL),
       (2, 2, 11, '요구사항 정의서', '기술/IT', NULL, NULL, NULL),
       (3, 3, 11, '요구사항 정의서', '기술/IT', NULL, NULL, NULL),
       (4, 4, 11, '요구사항 정의서', '기술/IT', NULL, NULL, NULL),
       (5, 5, 11, '요구사항 정의서', '요식', NULL, NULL, NULL),
       (6, 6, 11, '요구사항 정의서', '요식', NULL, NULL, NULL),
       (7, 7, 11, '요구사항 정의서', '요식', NULL, NULL, NULL),
       (8, 8, 11, '요구사항 정의서', '요식', NULL, NULL, NULL),
       (9, 9, 11, '기능 명세서', '기술/IT', NULL, NULL, NULL),
       (10, 10, 11, '기능 명세서', '기술/IT', NULL, NULL, NULL),
       (11, 11, 11, '기능 명세서', '기술/IT', NULL, NULL, NULL),
       (12, 12, 11, '기능 명세서', '도소매', NULL, NULL, NULL),
       (13, 13, 11, '기능 명세서', '도소매', NULL, NULL, NULL),
       (14, 14, 11, '기능 명세서', '도소매', NULL, NULL, NULL),
       (15, 15, 11, '기능 명세서', '도소매', NULL, NULL, NULL),
       (16, 16, 11, '기능 명세서', '보건/복지', NULL, NULL, NULL),
       (17, 17, 11, '기능 명세서', '보건/복지', NULL, NULL, NULL),
       (18, 18, 11, '기능 명세서', '보건/복지', NULL, NULL, NULL),
       (19, 19, 11, '스토리보드', '비영리', NULL, NULL, NULL),
       (20, 20, 11, '스토리보드', '비영리', NULL, NULL, NULL),
       (21, 21, 11, '스토리보드', '비영리', NULL, NULL, NULL),
       (22, 22, 11, '스토리보드', '기술/IT', NULL, NULL, NULL),
       (23, 23, 11, '스토리보드', '기술/IT', NULL, NULL, NULL),
       (24, 24, 11, '기타', '비영리', NULL, NULL, NULL),
       (25, 25, 11, '기타', '비영리', NULL, NULL, NULL),
       (26, 26, 11, '기타', '기타', NULL, NULL, NULL),
       (27, 27, 11, '기타', '기타', NULL, NULL, NULL),
       (28, 28, 12, '신규 제작', '미드(4~9년)', NULL, NULL, NULL);

-- 포트폴리오
INSERT IGNORE INTO Portfolio (portfolio_no, pro_no, pro_advanced_no, portfolio_title, portfolio_content,thumbnail_image,portfolio_file1)
VALUES
    (1, 1, 1, '기획 프로젝트 A', '대규모 프로젝트 기획 및 실행 사례', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (2, 2, 2, '웹 프로젝트 B', '고객 웹사이트 제작 및 유지 보수 경험', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (3, 3, 3, '소프트웨어 프로젝트 C', '다양한 소프트웨어 개발 프로젝트 사례', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (4, 4, 4, '모바일 앱 프로젝트 D', 'Android 앱 개발 및 배포 사례', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (5, 5, 5, 'iOS 앱 프로젝트 E', 'iOS 앱 개발 및 사용자 경험 개선', 'https://via.placeholder.com/200x100','portfolio_file1 link'),
    (6, 6, 6, '게임 프로젝트 F', '다수의 인기 게임 개발 사례', 'https://via.placeholder.com/200x100','portfolio_file1 link');

-- 설문조사 더미 데이터 --
INSERT IGNORE INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(1, 11, '의뢰 받을 기획서 종류를 선택해주세요.', '요구사항 정의서, 기능 명세서, 스토리보드, 기타'),
       (2, 11, '의뢰 받을 사업 분야를 알려주세요.', '기술/IT, 제조, 도소매, 요식, 교육, 보건/복지, 비영리, 기타'),
       (3, 12, '의뢰받으려는 프로젝트의 제작 상태를 선택해주세요.', '신규 제작, 기존 웹 리뉴얼'),
       (4, 12, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
       (5, 13, '제작 가능한 플랫폼을 선택해주세요.', 'Windows, Mac, Linux, iOS, Android, 기타'),
       (6, 13, '제작 가능한 개발 종류를 선택해주세요.', '일반 프로그램, 게임, 임베디드, 기타'),
       (7, 13, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
       (8, 14, '개발 가능한 앱 형태를 선택해주세요.', '네이티브, 하이브리드, 기타'),
       (9, 14, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
       (10, 15, '개발 가능한 형태의 앱을 선택해주세요.', '네이티브, 하이브리드, 기타'),
       (11, 15, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
       (12, 15, '서버 개발이 가능하신지 선택해주세요.', '네., 아니요.'),
       (13, 16, '개발 가능한 플랫폼을 선택해주세요.', '안드로이드, PC, iOS, Mac, Linux, 기타'),
       (14, 16, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
       (15, 17, '개발 가능한 인공지능 기반을 선택해주세요.', '텍스트, 음성, 이미지, 기타'),
       (16, 17, '개발 가능한 작업을 선택해주세요.', '분류, 예측, 기타'),
       (17, 17, '개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
       (18, 18, '테스트 가능한 분야를 선택해주세요.', '기능 테스트, 비기능 테스트, 성능 테스트, 테스트 자동화, 기타'),
       (19, 18, '원하는 테스트 기간을 선택해주세요.', '1회 계약, 정기 계약, 무관'),
       (20, 21, '데이터 가공 및 라벨링이 가능한 형태를 선택해주세요.', '문자, 이미지, 음성, 기타'),
       (21, 22, '데이터 복구 가능한 것을 선택해주세요.', '스마트기기(휴대폰/태블릿 등), 프로그램(카톡 등), 하드디스크(외장하드 등), 메모리카드(USB/SD/SSD 등), 기타'),
       (22, 23, '크롤링 가능한 서비스를 선택해주세요.', '웹 크롤링, 앱 크롤링, 기타'),
       (23, 23, '크롤링 가능한 웹사이트/애플리케이션을 선택해주세요.', '검색 포털(네이버/구글 등), 동영상 채널(유튜브 등), 오픈마켓(쿠팡/옥션 등), 쇼핑몰, 기타'),
       (24, 23, '크롤링 프로그램 제작이 가능한지 선택해주세요.', '네., 아니요.'),
       (25, 24, '데이터베이스 설계가 가능한 분야를 선택해주세요.', '식품, 도소매, 서비스업, IT, 기타'),
       (26, 25, '통계분석 가능한 서비스를 선택해주세요.', '데이터 분석, 통계 모델링, 기타'),
       (27, 25, '사용 가능한 분석 프로그램을 선택해주세요.', 'SPSS, R, Python, 엑셀, 무관');

-- 필터링 --
INSERT IGNORE INTO Filter(filter_no, item_no, filter_question_no, filter_question, filter_option)
VALUES(1, 11, 1, '의뢰할 기획서 종류를 선택해주세요.', '요구사항 정의서, 기능 명세서, 스토리보드, 기타'),
      (2, 11, 2, '의뢰할 사업 분야를 알려주세요.', '기술/IT, 제조, 도소매, 요식, 교육, 보건/복지, 비영리, 기타'),
      (3, 12, 1, '의뢰하려는 프로젝트의 제작 상태를 선택해주세요.', '신규 제작, 기존 웹 리뉴얼'),
      (4, 12, 2, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
      (5, 13, 1, '제작을 원하는 플랫폼을 선택해주세요.', 'Windows, Mac, Linux, iOS, Android, 기타'),
      (6, 13, 2, '제작을 원하는 개발 종류를 선택해주세요.', '일반 프로그램, 게임, 임베디드, 기타'),
      (7, 13, 3, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
      (8, 14, 1, '개발을 원하는 앱 형태를 선택해주세요.', '네이티브, 하이브리드, 기타'),
      (9, 14, 2, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
      (10, 15, 1, '개발을 원하는 형태의 앱을 선택해주세요.', '네이티브, 하이브리드, 기타'),
      (11, 15, 2, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
      (12, 15, 3, '서버 개발이 필요한지 선택해주세요.', '네., 아니요.'),
      (13, 16, 1, '개발을 원하는 플랫폼을 선택해주세요.', '안드로이드, PC, iOS, Mac, Linux, 기타'),
      (14, 16, 2, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
      (15, 17, 1, '개발을 원하는 인공지능 기반을 선택해주세요.', '텍스트, 음성, 이미지, 기타'),
      (16, 17, 2, '개발을 원하는 작업을 선택해주세요.', '분류, 예측, 기타'),
      (17, 17, 3, '원하는 개발자 경력을 선택해주세요.', '주니어(3년 이하), 미드(4~9년), 시니어(10년 이상)'),
      (18, 18, 1, '테스트 하고 싶은 분야를 선택해주세요.', '기능 테스트, 비기능 테스트, 성능 테스트, 테스트 자동화, 기타'),
      (19, 18, 2, '원하는 테스트 기간을 선택해주세요.', '1회 계약, 정기 계약, 무관'),
      (20, 21, 1, '데이터 가공 및 라벨링을 원하는 형태를 선택해주세요.', '문자, 이미지, 음성, 기타'),
      (21, 22, 1, '데이터 복구를 원하는 것을 선택해주세요.', '스마트기기(휴대폰/태블릿 등), 프로그램(카톡 등), 하드디스크(외장하드 등), 메모리카드(USB/SD/SSD 등), 기타'),
      (22, 23, 1, '크롤링을 원하는 서비스를 선택해주세요.', '웹 크롤링, 앱 크롤링, 기타'),
      (23, 23, 2, '크롤링을 원하는 웹사이트/애플리케이션을 선택해주세요.', '검색 포털(네이버/구글 등), 동영상 채널(유튜브 등), 오픈마켓(쿠팡/옥션 등), 쇼핑몰, 기타'),
      (24, 23, 3, '크롤링 프로그램 제작이 필요한지 선택해주세요.', '네., 아니요.'),
      (25, 24, 1, '데이터베이스 설계를 원하는 분야를 선택해주세요.', '식품, 도소매, 서비스업, IT, 기타'),
      (26, 25, 1, '통계분석을 원하는 서비스를 선택해주세요.', '데이터 분석, 통계 모델링, 기타'),
      (27, 25, 2, '원하는 분석 프로그램을 선택해주세요.', 'SPSS, R, Python, 엑셀, 무관');

-- 회원 문의글 더미 데이터 --
INSERT IGNORE INTO Inquiry (inquiry_no, member_no, inquiry_title, inquiry_content, inquiry_reg_date, inquiry_file, inquiry_status, inquiry_satisfaction)
VALUES (1, 1, '로그인이 안돼요', '비밀번호를 입력했는데 로그인이 되지 않습니다.', NOW(), NULL, '답변 대기', 0),
(2, 2, '회원 정보 수정', '전화번호 변경을 요청합니다.', NOW(), NULL, '답변 중', 0),
(3, 3, '결제 오류', '결제가 완료되지 않고 오류가 발생했습니다.', NOW(), NULL, '답변 완료', 1),
(4, 4, '상품 문의', '해당 상품의 배송 일정이 궁금합니다.', NOW(), NULL, '답변 대기', 0),
(5, 5, '쿠폰 사용법', '프로모션 쿠폰은 어디에서 사용할 수 있나요?', NOW(), NULL, '답변 대기', 0),
(6, 6, '계정 해킹 의심', '다른 사람이 제 계정을 사용한 흔적이 있습니다.', NOW(), NULL, '답변 중', 0),
(7, 7, '환불 요청', '구매한 상품이 마음에 들지 않습니다. 환불 요청합니다.', NOW(), NULL, '답변 중', 0),
(8, 8, '배송 상태 확인', '제 주문이 아직 도착하지 않았습니다. 확인 부탁드립니다.', NOW(), NULL, '답변 완료', 1);


-- 리뷰 더미 데이터 --
INSERT IGNORE INTO Review (review_no, pro_no, member_no, review_content, review_rate, review_date, review_activation)
VALUES
    (1, 1, 4, '기획 외주 너무 잘 만들어주셨어요.', 4, '2025-02-03 12:34:56', 1),
    (2, 1, 5, '기획 외주의 신이야...', 5, '2024-12-13 10:00:00', 1),
    (3, 1, 6, '기획 프로젝트 별론데요?', 1, '2025-01-02 14:30:00', 1),
    (4, 1, 7, '다음에 또 부탁드릴게요', 5, '2024-02-01 09:00:00', 1),
    (5, 1, 8, '마음에 안들어', 2, '2025-02-03 12:34:56', 1),
    (6, 1, 9, '좋아요~', 4, '2025-01-18 16:45:00', 1);

UPDATE Review
SET review_date = '2025-02-03 12:34:56'
WHERE review_no = 1;

UPDATE Review
SET review_date = '2024-12-13 10:00:00'
WHERE review_no = 2;

UPDATE Review
SET review_date = '2025-01-02 14:30:00'
WHERE review_no = 3;

UPDATE Review
SET review_date = '2024-02-01 09:00:00'
WHERE review_no = 4;

UPDATE Review
SET review_date = '2025-02-03 12:34:56'
WHERE review_no = 5;

UPDATE Review
SET review_date = '2025-01-18 16:45:00'
WHERE review_no = 6;

-- Community 테이블 더미 데이터
insert into Community (community_board_no, member_no, community_board_title, community_board_content, community_board_reg_date)
values
    (1, 1, '이 외주, 진짜 대박이었어!', '이 방법대로 외주 진행했더니 진짜 효과 있더라.', sysdate()),
    (2, 2, '외주 프로젝트, 이렇게 하면 성공!', '여기서 이런 방식으로 진행했더니 성공했어.', sysdate()),
    (3, 3, '이 외주 플랫폼 어때?', '이 플랫폼은 유용해서 자주 사용하고 있어.', sysdate()),
    (4, 4, '외주 프로젝트 맡길 때 주의사항', '이럴 땐 꼭 주의해야 한다는 팁이 있어.', sysdate()),
    (5, 5, '외주 경험 공유', '내가 해본 외주 프로젝트는 이렇게 진행됐어.', sysdate()),
    (6, 6, '외주 맡기기 전 팁', '외주 맡길 때 이렇게 하면 효율적이야.', sysdate()),
    (7, 7, '외주 작업자 찾기, 이런 방법이!', '이렇게 찾으면 좋은 작업자를 만날 수 있어.', sysdate()),
    (8, 8, '외주 프로젝트 완료 후기', '이 프로젝트는 정말 순조롭게 끝났어.', sysdate()),
    (9, 9, '외주 비용 책정, 어떻게 할까?', '외주 비용은 이렇게 계산하는 게 좋더라.', sysdate()),
    (10, 10, '외주 계약서 작성, 중요하다!', '계약서에서 놓치면 안 될 부분들 알려줄게.', sysdate()),
    (11, 11, '이 외주 플랫폼은 정말 괜찮다', '몇 개의 플랫폼을 써봤는데 이게 제일 나았어.', sysdate()),
    (12, 12, '외주 진행 시 체크리스트', '이 체크리스트를 꼭 지켜야 성공할 수 있어.', sysdate()),
    (13, 1, '외주 프로젝트 처음 시작할 때', '첫 외주 프로젝트라 좀 긴장했는데 잘 됐어.', sysdate()),
    (14, 1, '외주 맡길 때 피해야 할 실수', '이런 실수만 안 하면 외주는 성공할 거야.', sysdate()),
    (15, 5, '좋은 외주 작업자 찾는 법', '외주 작업자를 잘 찾는 팁 공유할게.', sysdate()),
    (16, 6, '외주 퀄리티 유지하는 법', '퀄리티 높은 외주 결과물을 얻는 방법!', sysdate()),
    (17, 7, '외주 업무가 밀리면 어떻게 할까?', '일정 지키기 힘들다면 이렇게 대처하면 좋아.', sysdate()),
    (18, 8, '외주에 필요한 소프트웨어', '이 소프트웨어가 있으면 외주 진행이 훨씬 수월해.', sysdate()),
    (19, 9, '외주를 통해 배운 점', '외주 프로젝트를 통해 얻은 노하우를 공유할게.', sysdate()),
    (20, 2, '외주가 잘 안 될 때 해결책', '외주 진행에 어려움이 있다면 이렇게 해결할 수 있어.', sysdate()),
    (21, 1, '외주 작업의 품질 검사 방법', '작업물 품질 검사를 이렇게 하면 정확해.', sysdate()),
    (22, 2, '외주 가격 협상 팁', '가격 협상 시 유리하게 대처하는 법을 알려줄게.', sysdate()),
    (23, 3, '외주 플랫폼 후기', '이 외주 플랫폼은 이렇게 좋았다.', sysdate()),
    (24, 4, '성공적인 외주 계약서 작성법', '계약서 작성 시 놓치지 말아야 할 중요한 포인트!', sysdate()),
    (25, 2, '외주 프로젝트 일정 관리', '일정을 어떻게 관리하는 게 효율적인지 공유할게.', sysdate()),
    (26, 6, '외주 작업자에게 피드백 주는 법', '이렇게 피드백을 주면 작업자가 잘 반영해.', sysdate()),
    (27, 2, '외주에서 중요한 것은 신뢰', '신뢰를 바탕으로 외주를 성공적으로 진행한 경험!', sysdate()),
    (28, 2, '외주 프로젝트 리스크 관리', '리스크를 관리하는 방법에 대해 알려줄게.', sysdate()),
    (29, 9, '외주 종료 후 후속 관리', '외주가 끝난 후에도 이렇게 관리하는 게 중요해.', sysdate()),
    (30, 3, '외주 계약 시 필수 조건', '계약 시 반드시 명시해야 할 조건들!', sysdate()),
    (31, 3, '외주 품질 관리', '외주 작업의 품질을 어떻게 관리하는지 팁을 줄게.', sysdate()),
    (32, 2, '외주 작업자와의 소통', '소통을 잘하면 외주가 훨씬 원활하게 진행돼.', sysdate()),
    (33, 3, '외주 프로젝트 일정 지키는 법', '일정을 지키려면 이렇게 준비해야 해.', sysdate()),
    (34, 4, '외주 작업을 잘 마무리하는 법', '외주를 성공적으로 마무리하려면 이렇게 해야 해.', sysdate()),
    (35, 5, '외주 프로젝트 진행 속도', '외주 프로젝트를 빠르게 진행하는 방법!', sysdate()),
    (36, 3, '외주 계약서에서 중요 항목', '계약서에서 놓치지 말아야 할 중요한 부분을 소개할게.', sysdate()),
    (37, 3, '외주 품질 보증', '품질을 보증하는 방법에 대해 알려줄게.', sysdate()),
    (38, 3, '외주 프로젝트 후기', '내가 맡았던 외주 프로젝트 후기를 공유할게.', sysdate()),
    (39, 9, '외주를 통해 성장한 경험', '외주를 하면서 내가 성장한 경험을 나눠줄게.', sysdate()),
    (40, 8, '외주 플랫폼에서 찾은 보석 같은 작업자', '정말 좋은 작업자를 발견한 경험!', sysdate()),
    (41, 1, '외주 업체 고르는 팁', '이렇게 고르면 좋은 외주 업체를 찾을 수 있어.', sysdate()),
    (42, 4, '외주 계약 전 주의할 점', '계약 전 꼭 체크해야 할 사항들을 알려줄게.', sysdate()),
    (43, 4, '외주 프로젝트 일정 지연 해결 방법', '일정 지연 시 이렇게 대처하면 좋아.', sysdate()),
    (44, 4, '외주 품질을 높이는 방법', '이렇게 하면 품질을 높일 수 있어.', sysdate()),
    (45, 5, '외주에서 중요한 것은 커뮤니케이션', '좋은 커뮤니케이션으로 외주가 원활하게 진행됐어.', sysdate()),
    (46, 6, '외주 프로젝트로 얻은 경험', '이 경험을 통해 많은 걸 배웠어.', sysdate()),
    (47, 4, '외주 비용 절감 방법', '비용을 절감하면서도 품질은 유지하는 방법을 알려줄게.', sysdate()),
    (48, 4, '외주 프로젝트 협업', '협업을 잘 하면 외주도 성공할 수 있어.', sysdate()),
    (49, 4, '외주 계약 시 중요한 협상 포인트', '계약 시 협상할 때 중요한 포인트를 알려줄게.', sysdate()),
    (50, 5, '외주를 맡길 때 체크리스트', '이 체크리스트를 참고하면 외주를 잘 맡길 수 있어.', sysdate()),
    (51, 1, '외주 프로젝트 후기', '내가 맡았던 외주 프로젝트는 이렇게 끝났어.', sysdate()),
    (52, 5, '성공적인 외주 진행을 위한 팁', '성공적인 외주 진행을 위한 중요한 팁을 알려줄게.', sysdate()),
    (53, 5, '외주와 관련된 트렌드', '최근 외주 트렌드에 대해서 알려줄게.', sysdate()),
    (54, 4, '외주 플랫폼에서의 경험담', '내가 이용했던 외주 플랫폼은 이런 점이 좋았어.', sysdate()),
    (55, 5, '외주 프로젝트에서 중요한 점', '중요한 점들을 체크하면서 진행한 경험을 공유할게.', sysdate()),
    (56, 5, '외주 프로젝트 완료 후 관리', '프로젝트 완료 후에는 이렇게 관리하는 것이 중요해.', sysdate()),
    (57, 7, '외주를 잘 진행하는 방법', '외주를 잘 진행하기 위한 여러 가지 팁을 공유할게.', sysdate()),
    (58, 5, '외주 품질 검사', '작업의 품질 검사를 이렇게 하면 완벽하다.', sysdate()),
    (59, 5, '외주 계약서 필수 항목', '계약서에서 반드시 포함되어야 할 항목들을 알려줄게.', sysdate()),
    (60, 6, '외주 경험 공유', '이렇게 외주를 진행했더니 좋은 결과가 나왔어.', sysdate()),
    (61, 6, '외주 프로젝트 일정을 잘 맞추는 법', '이렇게 하면 외주 일정이 잘 맞춰져요.', sysdate()),
    (62, 2, '외주 비용 책정 방법', '비용 책정 방법에 대해 알아보자.', sysdate()),
    (63, 6, '외주 품질 관리 팁', '품질을 높이는 팁을 소개할게.', sysdate());


-- CommunityReply 테이블 더미 데이터
insert into CommunityReply (community_reply_no, community_board_no, member_no, community_reply_content, community_reply_reg_date)
values
    (1, 1, 1, '대박이다.. 말이 돼?', sysdate()),
    (2, 1, 2, '여기서 저렇게 했더니 요래 됐더라', sysdate()),
    (3, 2, 3, '이거 정말 신기하네', sysdate()),
    (4, 2, 4, '효과가 있었나봐', sysdate()),
    (5, 3, 5, '정말 그런지 해봐야겠다', sysdate()),
    (6, 3, 6, '이건 꽤 괜찮은 방법인 것 같아', sysdate()),
    (7, 4, 7, '완전 대박이다', sysdate()),
    (8, 4, 8, '이 방법을 시도해봐야겠다', sysdate()),
    (9, 5, 9, '이렇게 해본 사람들 많더라', sysdate()),
    (10, 5, 10, '이거 해보면 괜찮을 것 같아', sysdate()),
    (11, 6, 11, '다들 이 방법 쓰고 있더라', sysdate()),
    (12, 6, 12, '해봐야겠다', sysdate()),
    (13, 7, 13, '이거 대박이다', sysdate()),
    (14, 7, 14, '이 방법대로 해봤어', sysdate()),
    (15, 8, 15, '다들 이걸로 성공했다고 하던데', sysdate()),
    (16, 8, 16, '정말 효과 있을까?', sysdate()),
    (17, 9, 17, '이 방법은 진짜 신기하다', sysdate()),
    (18, 9, 18, '내가 해봐도 되겠지?', sysdate()),
    (19, 10, 19, '이 방법대로 하니까 효과가 있네', sysdate()),
    (20, 10, 20, '이런 결과는 처음이라 놀랐어', sysdate()),
    (21, 11, 21, '이 방법 진짜 대박', sysdate()),
    (22, 11, 22, '다시 시도해봐야겠다', sysdate()),
    (23, 12, 23, '처음 해봤는데 성공했다', sysdate()),
    (24, 12, 24, '해보니까 정말 좋더라', sysdate()),
    (25, 13, 25, '이렇게 되는 거구나', sysdate()),
    (26, 13, 26, '와 이걸로 정말 달라졌어', sysdate()),
    (27, 14, 27, '진짜 이 방법 대박이다', sysdate()),
    (28, 14, 28, '나도 해보니까 됐다', sysdate()),
    (29, 15, 29, '이건 놀라운 경험이었다', sysdate()),
    (30, 15, 30, '정말 효과 있더라', sysdate());
