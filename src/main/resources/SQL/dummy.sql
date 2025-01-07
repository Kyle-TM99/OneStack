-- 초보자
INSERT INTO Member (name, member_id, pass, nickname, birth, gender, zipcode, address, address2, email, email_get, phone, member_reg_date, member_type) 
VALUES 
('홍길동', 'user1', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname1', '1990-05-15', 'male', '12345', '서울특별시 강남구', '테헤란로 123', 'user1@example.com', 1, '010-1111-2222', SYSDATE(), 0),
('이순신', 'user2', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname2', '1985-03-22', 'female', '54321', '서울특별시 종로구', '세종대로 456', 'user2@example.com', 1, '010-3333-4444', SYSDATE(), 0),
('김구', 'user3', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname3', '1992-08-30', 'male', '23456', '서울특별시 서초구', '반포대로 789', 'user3@example.com', 0, '010-5555-6666', SYSDATE(), 0),
('유관순', 'user4', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname4', '1995-07-12', 'female', '34567', '경기도 성남시', '분당로 123', 'user4@example.com', 1, '010-7777-8888', SYSDATE(), 0),
('안중근', 'user5', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname5', '1988-01-15', 'male', '45678', '경기도 수원시', '영통로 456', 'user5@example.com', 1, '010-9999-0000', SYSDATE(), 0),
('윤동주', 'user6', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname6', '1991-10-25', 'female', '56789', '부산광역시 해운대구', '광안로 789', 'user6@example.com', 0, '010-1234-5678', SYSDATE(), 0),
('장영실', 'user7', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'nickname7', '1989-04-01', 'male', '67890', '대전광역시 유성구', '과학로 123', 'user7@example.com', 1, '010-2468-1357', SYSDATE(), 0),
-- 전문가
('이사부', 'pro1', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'proNickname1', '1980-01-01', 'male', '12345', '서울특별시 강남구', '테헤란로 321', 'pro1@example.com', 1, '010-9876-5432', SYSDATE(), 1),
('김유신', 'pro2', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'proNickname2', '1982-02-02', 'male', '54321', '서울특별시 종로구', '세종대로 654', 'pro2@example.com', 1, '010-8765-4321', SYSDATE(), 1),
('강감찬', 'pro3', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'proNickname3', '1985-03-03', 'female', '23456', '서울특별시 서초구', '반포대로 987', 'pro3@example.com', 0, '010-7654-3210', SYSDATE(), 1),
('강태공', 'pro4', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'proNickname4', '1990-04-04', 'male', '34567', '경기도 성남시', '분당로 321', 'pro4@example.com', 1, '010-6543-2109', SYSDATE(), 1),
('정몽주', 'pro5', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'proNickname5', '1988-05-05', 'female', '45678', '경기도 수원시', '영통로 654', 'pro5@example.com', 1, '010-5432-1098', SYSDATE(), 1),
('한석봉', 'pro6', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 'proNickname6', '1987-06-06', 'male', '56789', '부산광역시 해운대구', '광안로 987', 'pro6@example.com', 0, '010-4321-0987', SYSDATE(), 1);

-- 전문가 등록
INSERT INTO Professional (member_no, item_no, self_introduction, carrer, award_carrer, student_count, rate, professor_status, screening_msg)
VALUES
(8, 11, '기획 전문가입니다.', '10년 경력의 기획자', NULL, 100, 5, 2, NULL),
(9, 12, '웹 개발 전문가입니다.', '5년 경력의 프론트엔드 개발자', '웹 개발 대회 1위', 50, 4, 2, NULL),
(10, 13, '소프트웨어 개발 전문가입니다.', '8년 경력의 소프트웨어 엔지니어', NULL, 80, 5, 2, NULL),
(11, 14, '안드로이드 개발자입니다.', '6년 경력의 모바일 앱 개발자', NULL, 60, 4, 2, NULL),
(12, 15, 'iOS 개발자입니다.', '5년 경력의 iOS 앱 개발자', NULL, 70, 5, 2, NULL),
(13, 16, '게임 개발 전문가입니다.', '7년 경력의 게임 개발자', '게임 개발 대회 2위', 90, 5, 2, NULL);

-- 카테고리
INSERT INTO Category (item_no, item_title, category_no) 
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

INSERT INTO ProfessionalAdvancedInformation (pro_no, item_no, pro_answer1, pro_answer2, pro_answer3, pro_answer4, pro_answer5)
VALUES
(1, 11, '요구사항 명세서', '기술/IT', NULL, NULL, NULL),
(2, 12, '시니어(6년 이상)', '온라인 진행을 원해요', 'NULL', NULL, NULL),
(3, 13, 'Windows', '일반 프로그램', '시니어(6년 이상)', NULL, NULL),
(4, 14, '하이브리드', '시니어(6년 이상)', '온라인 진행을 원해요', NULL, NULL),
(5, 15, '네이티브', '미드(4~9년)', NULL, NULL, NULL),
(6, 16, 'PC', '미드(4~9년)', '어떤 방식이든 상관없어요', NULL, NULL);

-- 포트폴리오 
INSERT INTO Portfolio (pro_no, pro_advanced_no, portfolio_title, portfolio_content) 
VALUES
(1, 1, '기획 프로젝트 A', '대규모 프로젝트 기획 및 실행 사례'),
(2, 2, '웹 프로젝트 B', '고객 웹사이트 제작 및 유지 보수 경험'),
(3, 3, '소프트웨어 프로젝트 C', '다양한 소프트웨어 개발 프로젝트 사례'),
(4, 4, '모바일 앱 프로젝트 D', 'Android 앱 개발 및 배포 사례'),
(5, 5, 'iOS 앱 프로젝트 E', 'iOS 앱 개발 및 사용자 경험 개선'),
(6, 6, '게임 프로젝트 F', '다수의 인기 게임 개발 사례');

-- 관리자 
INSERT INTO Manager (manager_name, manager_pass, member_type, manager_image) 
VALUES
('admin1', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 4, 'https://via.placeholder.com/200x100'),
('admin2', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 4, 'https://via.placeholder.com/200x100'),
('admin3', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 4, 'https://via.placeholder.com/200x100'),
('admin4', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 4, 'https://via.placeholder.com/200x100'),
('admin5', '$2a$10$.g6l.wyIFO1.j4u4gvVtKOnG9ACBUT1GRlDwlMZcjBxZPrCAURLaG', 4, 'https://via.placeholder.com/200x100');


-- 설문조사 더미 데이터 --
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(1, 11, '의뢰 받을 기획서 종류를 선택해주세요.', '요구사항 정의서, 기능 명세서, 스토리보드, 기타');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(2, 11, '의뢰 받을 사업 분야를 알려주세요.', '기술/IT, 제조, 도소매, 요식, 교육, 보건/복지, 비영리, 기타');

INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(3, 12, '의뢰받으려는 프로젝트의 제작 상태를 선택해주세요.', '신규 제작, 기존 웹 리뉴얼');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(4, 12, '개발자 경력을 선택해주세요.', '주니어 (3년 이하), 미드 (4~9년), 시니어 (10년 이상)');

INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(5, 13, '제작 가능한 플랫폼을 선택해주세요.', 'Windows, Mac, Linux, iOS, Android, 기타');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(6, 13, '제작 가능한 개발 종류를 선택해주세요.', '일반 프로그램, 게임, 임베디드, 기타');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(7, 13, '개발자 경력을 선택해주세요.', '주니어 (3년 이하), 미드 (4~9년), 시니어 (10년 이상)');

INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(8, 14, '개발 가능한 앱 형태를 선택해주세요.', '네이티브, 하이브리드, 기타');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(9, 14, '개발자 경력을 선택해주세요.', '주니어 (3년 이하), 미드 (4~9년), 시니어 (10년 이상)');

INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(10, 15, '개발 가능한 형태의 앱을 선택해주세요.', '네이티브, 하이브리드, 기타');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(11, 15, '개발자 경력을 선택해주세요.', '주니어 (3년 이하), 미드 (4~9년), 시니어 (10년 이상)');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(12, 15, '서버 개발이 가능하신지 선택해주세요.', '네., 아니요.');

INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(13, 16, '개발 가능한 플랫폼을 선택해주세요.', '안드로이드, PC, iOS, Mac, Linux, 기타');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(14, 16, '개발자 경력을 선택해주세요.', '주니어 (3년 이하), 미드 (4~9년), 시니어 (10년 이상)');

INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(15, 17, '개발 가능한 인공지능 기반을 선택해주세요.', '텍스트, 음성, 이미지, 기타');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(16, 17, '개발 가능한 작업을 선택해주세요.', '분류, 예측, 기타');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(17, 17, '개발자 경력을 선택해주세요.', '주니어 (3년 이하), 미드 (4~9년), 시니어 (10년 이상)');

INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(18, 18, '테스트 가능한 분야를 선택해주세요.', '기능 테스트, 비기능 테스트, 성능 테스트, 테스트 자동화, 기타');
INSERT INTO Survey(survey_no, item_no, survey_question, survey_option)
VALUES(19, 18, '원하는 테스트 기간을 선택해주세요.', '1회 계약, 정기 계약, 무관');
