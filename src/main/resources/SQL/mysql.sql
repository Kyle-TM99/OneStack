## DATABASE 생성 및 선택
DROP DATABASE IF EXISTS onestack;
CREATE DATABASE IF NOT EXISTS onestack;
use onestack;

## Member - 회원
CREATE TABLE Member (member_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                     name VARCHAR(5) NOT NULL,
                     member_id VARCHAR(50) UNIQUE NOT NULL,
                     pass VARCHAR(100) NOT NULL,
                     nickname VARCHAR(20) UNIQUE NOT NULL,
                     birth DATE NOT NULL,
                     gender VARCHAR(10) NOT NULL,
                     zipcode   VARCHAR(5) NOT NULL,
                     address   VARCHAR(50) NOT NULL,
                     address2 VARCHAR(50) NOT NULL,
                     email VARCHAR(30) UNIQUE NOT NULL,
                     email_get TINYINT DEFAULT 1 NOT NULL, -- 1(수신) 0(비수신) --
                     phone VARCHAR(13) UNIQUE NOT NULL,
                     member_reg_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                     member_type   TINYINT DEFAULT 0 NOT NULL, -- 0(초보자) 1(전문가) 2(탈퇴) 3(정지) --
                     member_image VARCHAR(100) NULL,
                     stack INTEGER DEFAULT 0 NOT NULL,
                     stack_name VARCHAR(20) DEFAULT '기본 회원' NOT NULL,
                     withdrawal_end_date   TIMESTAMP NULL,
                     ban_end_date   TIMESTAMP NULL,
                     reported_count INTEGER DEFAULT 0 NOT NULL,
                     is_social TINYINT(1) DEFAULT 0 NOT NULL, -- 0: 일반 계정, 1: 소셜 계정
                     social_type ENUM('none', 'kakao', 'google') DEFAULT 'none' NOT NULL -- 소셜 로그인 유형
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Professional - 전문가
CREATE TABLE Professional (pro_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                           member_no INTEGER NOT NULL,
                           category_no   INTEGER   NOT NULL,
                           self_introduction VARCHAR(100) NOT NULL,
                           career VARCHAR(500) NOT NULL,
                           award_career VARCHAR(500) NULL,
                           student_count INTEGER DEFAULT 0 NOT NULL,
                           rate INTEGER DEFAULT 0 NOT NULL,
                           professor_status INTEGER DEFAULT 1 NOT NULL, -- 1(심사 전) 2(승인) 3(거부) --
                           screening_msg VARCHAR(100) NULL,
                           pro_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           contactable_time VARCHAR(15) NOT NULL,
                           average_price INTEGER DEFAULT 0 NOT NULL,
                           review_count INTEGER DEFAULT 0 NOT NULL,
                           CONSTRAINT member_no_pro_fk FOREIGN KEY (member_no) REFERENCES member(member_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Category - 카테고리
CREATE TABLE Category (item_no   INTEGER   NOT NULL PRIMARY KEY, -- 11(기획) 12(웹) 13(소프트웨어) 14(안드로이드) 15(iOS) 16(게임) 17(AI) 18(QA 및 테스트) / 21(가공 및 라벨링) 22 (데이터 복구) 23(크롤링) 24(DB 구축) 25(통계분석) --
                       item_title   VARCHAR(20) NOT   NULL,
                       category_no   INTEGER   NOT NULL -- 1(개발) 2(데이터) --
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## ProfessionalAdvancedInformation - 전문가 고급정보
CREATE TABLE ProfessionalAdvancedInformation (pro_advanced_no   INTEGER   AUTO_INCREMENT PRIMARY KEY,
                                              pro_no INTEGER   NOT NULL,
                                              item_no INTEGER   NOT NULL,
                                              pro_answer1 VARCHAR(200) NOT NULL,
                                              pro_answer2   VARCHAR(200) NULL,
                                              pro_answer3   VARCHAR(200) NULL,
                                              pro_answer4   VARCHAR(200) NULL,
                                              pro_answer5   VARCHAR(200) NULL,
                                              CONSTRAINT pro_no_proAd_fk FOREIGN KEY (pro_no) REFERENCES Professional(pro_no) ON DELETE CASCADE,
                                              CONSTRAINT item_no_proAd_fk FOREIGN KEY (item_no) REFERENCES Category(item_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Portfolio - 포트폴리오
CREATE TABLE Portfolio (portfolio_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                        pro_no INTEGER NOT NULL,
                        pro_advanced_no   INTEGER   NOT NULL,
                        portfolio_title   VARCHAR(20)   NOT NULL,
                        portfolio_content VARCHAR(500) NOT NULL,
                        visibility   TINYINT DEFAULT 1 NOT NULL, -- 1(공개) 0(비공개) --
                        thumbnail_image VARCHAR(100) NOT NULL,
                        portfolio_file1   VARCHAR(100) NOT NULL,
                        portfolio_file2   VARCHAR(100) NULL,
                        portfolio_file3   VARCHAR(100) NULL,
                        portfolio_file4   VARCHAR(100) NULL,
                        portfolio_file5   VARCHAR(100) NULL,
                        portfolio_file6   VARCHAR(100) NULL,
                        portfolio_file7   VARCHAR(100) NULL,
                        portfolio_file8   VARCHAR(100) NULL,
                        portfolio_file9   VARCHAR(100) NULL,
                        portfolio_file10 VARCHAR(100) NULL,
                        CONSTRAINT pro_no_portfolio_fk FOREIGN KEY (pro_no) REFERENCES Professional(pro_no) ON DELETE CASCADE,
                        CONSTRAINT pro_advanced_no_portfolio_fk FOREIGN KEY (pro_advanced_no) REFERENCES ProfessionalAdvancedInformation(pro_advanced_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


## Survey - 설문조사
CREATE TABLE Survey (survey_no INTEGER PRIMARY KEY,
                     item_no   INTEGER   NOT NULL,
                     survey_question   VARCHAR(100) NULL,
                     survey_option VARCHAR(500) NULL,
                     CONSTRAINT item_no_survey_fk FOREIGN KEY (item_no) REFERENCES Category(item_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Filter - 필터링
CREATE TABLE Filter (filter_no INTEGER AUTO_INCREMENT KEY,
                     item_no   INTEGER   NOT NULL,
                     filter_question_no   INTEGER   NOT NULL,
                     filter_question   VARCHAR(100) NULL,
                     filter_option VARCHAR(500) NULL,
                     CONSTRAINT item_no_filter_fk FOREIGN KEY (item_no) REFERENCES Category(item_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## ProfessionalBoard - 전문가 찾기 게시판
CREATE TABLE ProfessionalBoard (item_no   INTEGER   NOT NULL,
                                pro_no INTEGER NOT NULL,
                                survey_no INTEGER NOT NULL,
                                CONSTRAINT item_no_professionalBoard_fk FOREIGN KEY (item_no) REFERENCES Category(item_no) ON DELETE CASCADE,
                                CONSTRAINT pro_no_professionalBoard_fk FOREIGN KEY (pro_no) REFERENCES Professional(pro_no) ON DELETE CASCADE,
                                CONSTRAINT survey_no_professionalBoard_fk FOREIGN KEY (survey_no) REFERENCES Survey(survey_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Estimation - 견적
CREATE TABLE Estimation (estimation_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                         member_no INTEGER NOT NULL,
                         pro_no INTEGER NOT NULL,
                         item_no   INTEGER   NOT NULL,
                         estimation_content VARCHAR(300)   NOT NULL,
                         estimation_price DECIMAL(10,2) NOT NULL,
                         estimation_msg VARCHAR(300)   NULL,
                         estimation_isread TINYINT DEFAULT 0 NOT NULL, -- 0(안읽음) 1(읽음) --
                         CONSTRAINT member_no_estimation_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE,
                         CONSTRAINT pro_no_estimation_fk FOREIGN KEY (pro_no) REFERENCES Professional(pro_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Matching - 매칭
CREATE TABLE Matching (matching_no   INTEGER AUTO_INCREMENT PRIMARY KEY,
                       estimation_no INTEGER NOT NULL,
                       member_no INTEGER NOT NULL,
                       pro_no INTEGER NOT NULL,
                       CONSTRAINT estimation_no_matching_fk FOREIGN KEY (estimation_no) REFERENCES Estimation(estimation_no) ON DELETE CASCADE,
                       CONSTRAINT member_no_matching_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE,
                       CONSTRAINT pro_no_mathcing_fk FOREIGN KEY (pro_no) REFERENCES Professional(pro_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Quotation - 견적서
CREATE TABLE Quotation (quotation_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                        matching_no INTEGER   NOT NULL,
                        member_no INTEGER NOT NULL,
                        pro_no INTEGER NOT NULL,
                        quotation_content VARCHAR(500) NOT NULL,
                        quotation_price   DECIMAL(10,2) NOT NULL,
                        quotation_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        CONSTRAINT matching_no_quotation_fk FOREIGN KEY (matching_no) REFERENCES Matching(matching_no) ON DELETE CASCADE,
                        CONSTRAINT member_no_quotation_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE,
                        CONSTRAINT pro_no_quotation_fk FOREIGN KEY (pro_no) REFERENCES Professional(pro_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Review - 리뷰
CREATE TABLE Review (review_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                     pro_no INTEGER NOT NULL,
                     member_no INTEGER NOT NULL,
                     review_content VARCHAR(200)   NOT NULL,
                     review_rate   INTEGER   DEFAULT 0 NOT NULL,
                     review_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                     review_activation TINYINT DEFAULT 1 NOT NULL, -- 1(활성화) 0(비활성화) --
                     CONSTRAINT pro_no_review_fk FOREIGN KEY (pro_no) REFERENCES Professional(pro_no) ON DELETE CASCADE,
                     CONSTRAINT member_no_review_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Community - 커뮤니티
CREATE TABLE Community (community_board_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                        member_no INTEGER NOT NULL,
                        community_board_title VARCHAR(30) NOT NULL,
                        community_board_content   VARCHAR(1000) NOT NULL,
                        community_board_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        community_view   INTEGER DEFAULT 0 NOT NULL,
                        community_file   VARCHAR(100) NULL,
                        community_board_like INTEGER DEFAULT 0 NOT NULL,
                        community_board_dislike   INTEGER   DEFAULT 0 NOT NULL,
                        community_board_activation TINYINT DEFAULT 1 NOT NULL, -- 1(활성화) 0(비활성화) --
                        CONSTRAINT member_no_community_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## CommunityReply - 커뮤니티 댓글
CREATE TABLE CommunityReply (community_reply_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                             community_board_no INTEGER NOT NULL,
                             member_no INTEGER NOT NULL,
                             community_reply_content   VARCHAR(500) NOT NULL,
                             community_reply_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             community_reply_like INTEGER DEFAULT 0 NOT NULL,
                             community_reply_dislike   INTEGER DEFAULT 0 NOT NULL,
                             community_reply_activation TINYINT DEFAULT 1 NOT NULL, -- 1(활성화) 0(비활성화) --
                             CONSTRAINT member_no_communityReply_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE,
                             CONSTRAINT community_board_no_communityReply_fk FOREIGN KEY (community_board_no) REFERENCES Community(community_board_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## QnA - 질문 게시판
CREATE TABLE QnA (qna_board_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                  member_no INTEGER NOT NULL,
                  qna_board_title   VARCHAR(30)   NOT NULL,
                  qna_board_content VARCHAR(1000)   NOT NULL,
                  qna_board_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                  qna_view INTEGER DEFAULT 0 NOT NULL,
                  qna_file VARCHAR(100) NULL,
                  qna_board_like INTEGER DEFAULT 0 NOT NULL,
                  qna_adoption_status   TINYINT DEFAULT 0 NOT NULL, -- 1(채택O) 0(채택X) --
                  qna_board_activation TINYINT DEFAULT 1 NOT NULL, -- 1(활성화) 0(비활성화) --
                  CONSTRAINT member_no_qna_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## QnAReply - 질문 게시판 댓글
CREATE TABLE QnAReply (qna_reply_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                       qna_board_no INTEGER NOT NULL,
                       member_no INTEGER NOT NULL,
                       qna_reply_content VARCHAR(500) NOT NULL,
                       qna_reply_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       qna_adoption TINYINT DEFAULT 0 NOT NULL, -- 1(채택O) 0(채택X) --
                       qna_reply_like INTEGER DEFAULT 0 NOT NULL,
                       qna_reply_activation TINYINT DEFAULT 1 NOT NULL, -- 1(활성화) 0(비활성화) --
                       CONSTRAINT member_no_qnaReply_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE,
                       CONSTRAINT qna_board_no_qnaReply_fk FOREIGN KEY (qna_board_no) REFERENCES QnA(qna_board_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Manager - 관리자
CREATE TABLE Manager (manager_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                      manager_name VARCHAR(20) NOT NULL,
                      manager_pass VARCHAR(100) NOT NULL,
                      member_type   INTEGER DEFAULT 4 NOT NULL, -- 4(관리자) --
                      manager_image VARCHAR(100) NOT NULL,
                      manager_type INTEGER NULL -- 추후 관리자 유형별 나눌 예정 --
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Notice - 공지사항
CREATE TABLE Notice (notice_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                     manager_no INTEGER NOT NULL,
                     notice_title VARCHAR(30) NOT NULL,
                     notice_content VARCHAR(1000) NOT NULL,
                     notice_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                     notice_view   INTEGER DEFAULT 0 NOT NULL,
                     notice_file   VARCHAR(100) NULL,
                     CONSTRAINT manager_no_notice_fk FOREIGN KEY (manager_no) REFERENCES Manager(manager_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Inquiry - 고객문의
CREATE TABLE Inquiry (inquiry_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                      member_no INTEGER NOT NULL,
                      inquiry_title VARCHAR(30) NOT NULL,
                      inquiry_content   VARCHAR(1000) NOT NULL,
                      inquiry_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                      inquiry_file VARCHAR(100) NULL,
                      inquiry_status ENUM('답변 대기', '답변 중', '답변 완료') DEFAULT '답변 대기' NOT NULL,
                      inquiry_satisfaction TINYINT DEFAULT 0 NOT NULL, -- 1(만족) 0(불만족) --
                      CONSTRAINT member_no_inquiry_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## InquiryAnswer - 고객문의 답변
CREATE TABLE InquiryAnswer (inquiry_answer_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                            inquiry_no INTEGER NOT NULL,
                            manager_no INTEGER NOT NULL,
                            inquiry_answer_content VARCHAR(500)   NOT NULL,
                            inquiry_answer_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            inquiry_answer_file   VARCHAR(100) NULL,
                            CONSTRAINT inquiry_no_inquiryAnswer_fk FOREIGN KEY (inquiry_no) REFERENCES Inquiry(inquiry_no) ON DELETE CASCADE,
                            CONSTRAINT manager_no_inquiryAnswer_fk FOREIGN KEY (manager_no) REFERENCES Manager(manager_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## ManagerLog - 관리자 로그
CREATE TABLE ManagerLog (log_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                         manager_no INTEGER NOT NULL,
                         member_no INTEGER NOT NULL,
                         log_type INTEGER NOT NULL,
                         log_content   VARCHAR(50)   NOT NULL,
                         log_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         CONSTRAINT member_no_managerLog_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE,
                         CONSTRAINT manager_no_managerLog_fk FOREIGN KEY (manager_no) REFERENCES Manager(manager_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## Reports - 신고
CREATE TABLE Reports (reports_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                      member_no INTEGER NOT NULL,
                      reported_member_no INTEGER NOT NULL,
                      manager_no INTEGER NULL,
                      reports_type ENUM('community', 'qna', 'reply', 'review') NOT NULL,
                      reports_target VARCHAR(100)   NOT NULL,
                      reports_reason VARCHAR(100) NOT NULL,
                      reports_status TINYINT DEFAULT 0 NOT NULL, -- 0(처리 전) 1(처리 완료)
                      reports_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                      CONSTRAINT member_no_reports_fk FOREIGN KEY (member_no) REFERENCES Member(member_no) ON DELETE CASCADE,
                      CONSTRAINT reported_member_no_reports_fk FOREIGN KEY (reported_member_no) REFERENCES Member(member_no) ON DELETE CASCADE,
                      CONSTRAINT manager_no_reports_fk FOREIGN KEY (manager_no) REFERENCES Manager(manager_no) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## FAQ - 자주 묻는 질문
CREATE TABLE FAQ (faq_no INTEGER AUTO_INCREMENT PRIMARY KEY,
                  faq_type TINYINT NOT NULL, -- 1(전문가) 0(회원) --
                  faq_question VARCHAR(100) NOT NULL,
                  faq_response VARCHAR(500) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

## 비밀번호 찾기 테이블
CREATE TABLE PasswordResetToken (id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 member_id VARCHAR(50) NOT NULL,
                                 token VARCHAR(100) NOT NULL,
                                 expiry_date DATETIME NOT NULL,
                                 FOREIGN KEY (member_id) REFERENCES Member(member_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;