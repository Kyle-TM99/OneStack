-- Member - 회원
CREATE TABLE IF NOT EXISTS Member
(
    member_no           INTEGER AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(5)                                                 NOT NULL,
    member_id           VARCHAR(50) UNIQUE                                         NOT NULL,
    pass                VARCHAR(100)                                               NOT NULL,
    nickname            VARCHAR(20) UNIQUE                                         NOT NULL,
    birth               DATE                                                       NOT NULL,
    gender              VARCHAR(10)                                                NOT NULL,
    zipcode             VARCHAR(5)                                                 NOT NULL,
    address             VARCHAR(50)                                                NOT NULL,
    address2            VARCHAR(50)                                                NOT NULL,
    email               VARCHAR(30) UNIQUE                                         NOT NULL,
    email_get           TINYINT                          DEFAULT 1                 NOT NULL, -- 1(수신) 0(비수신) --
    phone               VARCHAR(13) UNIQUE                                         NOT NULL,
    member_reg_date     TIMESTAMP                        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    member_type         INTEGER                          DEFAULT 0                 NOT NULL, -- 0(초보자) 1(전문가) 2(심사중)--
    member_status       INTEGER                          DEFAULT 0                 NOT NULL, -- 0(활성화) 1(비활성화) 2(정지) 3(탈퇴) --
    member_image        VARCHAR(500)                                               NULL,
    member_stop         VARCHAR(5000)                                              NULL,     -- 정지 사유 (member_status가 2(정지)일때만)
    withdrawal_end_date TIMESTAMP                                                  NULL,
    ban_end_date        TIMESTAMP                                                  NULL,
    reported_count      INTEGER                          DEFAULT 0                 NOT NULL,
    is_admin            TINYINT(1)                       DEFAULT 0                 NOT NULL, -- 0: 회원, 1: 관리자
    is_social           TINYINT(1)                       DEFAULT 0                 NOT NULL, -- 0: 일반 계정, 1: 소셜 계정
    social_type         ENUM ('none', 'kakao', 'google') DEFAULT 'none'            NOT NULL  -- 소셜 로그인 유형
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- Professional - 전문가
CREATE TABLE IF NOT EXISTS Professional
(
    pro_no            INTEGER AUTO_INCREMENT PRIMARY KEY,
    member_no         INTEGER                             NOT NULL,
    category_no       INTEGER                             NOT NULL,
    self_introduction VARCHAR(100)                        NOT NULL,
    career            VARCHAR(500)                        NOT NULL,
    award_career      VARCHAR(500)                        NULL,
    student_count     INTEGER   DEFAULT 0                 NOT NULL,
    rate              INTEGER   DEFAULT 0                 NOT NULL,
    professor_status  TINYINT(1)                          NULL, -- 1(승인) 0(거부) --
    screening_msg     VARCHAR(100)                        NULL,
    pro_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    contactable_time  VARCHAR(15)                         NOT NULL,
    average_price     INTEGER   DEFAULT 0                 NOT NULL,
    review_count      INTEGER   DEFAULT 0                 NOT NULL,
    CONSTRAINT member_no_pro_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Category - 카테고리
CREATE TABLE IF NOT EXISTS Category
(
    item_no     INTEGER     NOT NULL PRIMARY KEY, -- 11(기획) 12(웹) 13(소프트웨어) 14(안드로이드) 15(iOS) 16(게임) 17(AI) 18(QA 및 테스트) / 21(가공 및 라벨링) 22 (데이터 복구) 23(크롤링) 24(DB 구축) 25(통계분석) --
    item_title  VARCHAR(20) NOT NULL,
    category_no INTEGER     NOT NULL              -- 1(개발) 2(데이터) --
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ProfessionalAdvancedInformation - 전문가 고급정보
CREATE TABLE IF NOT EXISTS ProfessionalAdvancedInformation
(
    pro_advanced_no INTEGER AUTO_INCREMENT PRIMARY KEY,
    pro_no          INTEGER      NOT NULL,
    item_no         INTEGER      NOT NULL,
    pro_answer1     VARCHAR(200) NOT NULL,
    pro_answer2     VARCHAR(200) NULL,
    pro_answer3     VARCHAR(200) NULL,
    pro_answer4     VARCHAR(200) NULL,
    pro_answer5     VARCHAR(200) NULL,
    CONSTRAINT pro_no_proAd_fk FOREIGN KEY (pro_no) REFERENCES Professional (pro_no) ON DELETE CASCADE,
    CONSTRAINT item_no_proAd_fk FOREIGN KEY (item_no) REFERENCES Category (item_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Portfolio - 포트폴리오
CREATE TABLE IF NOT EXISTS Portfolio
(
    portfolio_no      INTEGER AUTO_INCREMENT PRIMARY KEY,
    pro_no            INTEGER           NOT NULL,
    pro_advanced_no   INTEGER           NOT NULL,
    portfolio_title   VARCHAR(50)       NOT NULL,
    portfolio_content VARCHAR(500)      NOT NULL,
    visibility        TINYINT DEFAULT 1 NOT NULL, -- 1(공개) 0(비공개) --
    thumbnail_image   VARCHAR(100)      NOT NULL,
    portfolio_file1   VARCHAR(100)      NOT NULL,
    portfolio_file2   VARCHAR(100)      NULL,
    portfolio_file3   VARCHAR(100)      NULL,
    portfolio_file4   VARCHAR(100)      NULL,
    portfolio_file5   VARCHAR(100)      NULL,
    portfolio_file6   VARCHAR(100)      NULL,
    portfolio_file7   VARCHAR(100)      NULL,
    portfolio_file8   VARCHAR(100)      NULL,
    portfolio_file9   VARCHAR(100)      NULL,
    portfolio_file10  VARCHAR(100)      NULL,
    CONSTRAINT pro_no_portfolio_fk FOREIGN KEY (pro_no) REFERENCES Professional (pro_no) ON DELETE CASCADE,
    CONSTRAINT pro_advanced_no_portfolio_fk FOREIGN KEY (pro_advanced_no) REFERENCES ProfessionalAdvancedInformation (pro_advanced_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- Survey - 설문조사
CREATE TABLE IF NOT EXISTS Survey
(
    survey_no       INTEGER PRIMARY KEY,
    item_no         INTEGER      NOT NULL,
    survey_question VARCHAR(100) NULL,
    survey_option   VARCHAR(500) NULL,
    CONSTRAINT item_no_survey_fk FOREIGN KEY (item_no) REFERENCES Category (item_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Filter - 필터링
CREATE TABLE IF NOT EXISTS Filter
(
    filter_no          INTEGER AUTO_INCREMENT KEY,
    item_no            INTEGER      NOT NULL,
    filter_question_no INTEGER      NOT NULL,
    filter_question    VARCHAR(100) NULL,
    filter_option      VARCHAR(500) NULL,
    CONSTRAINT item_no_filter_fk FOREIGN KEY (item_no) REFERENCES Category (item_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Estimation - 견적
CREATE TABLE IF NOT EXISTS Estimation
(
    estimation_no      INTEGER AUTO_INCREMENT PRIMARY KEY,
    member_no          INTEGER                             NOT NULL,
    pro_no             INTEGER                             NOT NULL,
    item_no            INTEGER                             NOT NULL,
    estimation_content VARCHAR(300)                        NOT NULL,
    estimation_price   INTEGER                             NOT NULL,
    estimation_msg     VARCHAR(300)                        NULL,
    progress           INTEGER   DEFAULT 0                 NOT NULL, -- 0(요청) 1(채팅) 2(결제) 3(완료) 4(거절)
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 견적 요청 --
    updated_at         TIMESTAMP                           NULL,     -- 결제 시 --
    estimation_check   TINYINT   DEFAULT 0                 NOT NULL, -- 0(견적 최종 확인 전) 1(견적 최종 확인 후) --
    CONSTRAINT member_no_estimation_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE,
    CONSTRAINT pro_no_estimation_fk FOREIGN KEY (pro_no) REFERENCES Professional (pro_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Matching - 매칭
CREATE TABLE IF NOT EXISTS Matching
(
    matching_no   INTEGER AUTO_INCREMENT PRIMARY KEY,
    estimation_no INTEGER NOT NULL,
    member_no     INTEGER NOT NULL,
    pro_no        INTEGER NOT NULL,
    CONSTRAINT estimation_no_matching_fk FOREIGN KEY (estimation_no) REFERENCES Estimation (estimation_no) ON DELETE CASCADE,
    CONSTRAINT member_no_matching_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE,
    CONSTRAINT pro_no_mathcing_fk FOREIGN KEY (pro_no) REFERENCES Professional (pro_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- Review - 리뷰
CREATE TABLE IF NOT EXISTS Review
(
    review_no         INTEGER AUTO_INCREMENT PRIMARY KEY,
    pro_no            INTEGER                             NOT NULL,
    member_no         INTEGER                             NOT NULL,
    review_content    VARCHAR(200)                        NOT NULL,
    review_rate       INTEGER   DEFAULT 0                 NOT NULL,
    review_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    review_activation TINYINT   DEFAULT 1                 NOT NULL, -- 1(활성화) 0(비활성화) --
    CONSTRAINT pro_no_review_fk FOREIGN KEY (pro_no) REFERENCES Professional (pro_no) ON DELETE CASCADE,
    CONSTRAINT member_no_review_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Community - 커뮤니티
CREATE TABLE IF NOT EXISTS Community
(
    community_board_no         INTEGER AUTO_INCREMENT PRIMARY KEY,
    member_no                  INTEGER                             NOT NULL,
    community_board_title      VARCHAR(30)                         NOT NULL,
    community_board_content    LONGTEXT                            NOT NULL,
    community_board_reg_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    community_view             INTEGER   DEFAULT 0                 NOT NULL,
    community_file             VARCHAR(100)                        NULL,
    community_board_like       INTEGER   DEFAULT 0                 NOT NULL,
    community_board_dislike    INTEGER   DEFAULT 0                 NOT NULL,
    community_board_activation TINYINT   DEFAULT 1                 NOT NULL, -- 1(활성화) 0(비활성화) --
    CONSTRAINT member_no_community_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- CommunityReply - 커뮤니티 댓글
CREATE TABLE IF NOT EXISTS CommunityReply
(
    community_reply_no         INTEGER AUTO_INCREMENT PRIMARY KEY,
    community_board_no         INTEGER                             NOT NULL,
    member_no                  INTEGER                             NOT NULL,
    community_reply_content    VARCHAR(500)                        NOT NULL,
    community_reply_reg_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    community_reply_activation TINYINT   DEFAULT 1                 NOT NULL, -- 1(활성화) 0(비활성화) --
    CONSTRAINT member_no_communityReply_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE,
    CONSTRAINT community_board_no_communityReply_fk FOREIGN KEY (community_board_no) REFERENCES Community (community_board_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- CommunityRecommend - 커뮤니티 추천
CREATE TABLE IF NOT EXISTS CommunityRecommend
(
    community_board_no INTEGER                             NOT NULL,
    member_no          INTEGER                             NOT NULL,
    recommend_type     VARCHAR(10)                         NOT NULL, -- 'LIKE' 또는 'DISLIKE'
    recommend_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (community_board_no, member_no),
    CONSTRAINT member_no_communityRecommend_fk
        FOREIGN KEY (member_no)
            REFERENCES Member (member_no)
            ON DELETE CASCADE,
    CONSTRAINT community_board_no_communityRecommend_fk
        FOREIGN KEY (community_board_no)
            REFERENCES Community (community_board_no)
            ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- QnA - 질문 게시판
CREATE TABLE IF NOT EXISTS QnA
(
    qna_board_no         INTEGER AUTO_INCREMENT PRIMARY KEY,
    member_no            INTEGER                             NOT NULL,
    qna_board_title      VARCHAR(30)                         NOT NULL,
    qna_board_content    VARCHAR(1000)                       NOT NULL,
    qna_board_reg_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    qna_view             INTEGER   DEFAULT 0                 NOT NULL,
    qna_file             VARCHAR(100)                        NULL,
    qna_board_like       INTEGER   DEFAULT 0                 NOT NULL,
    qna_adoption_status  TINYINT   DEFAULT 0                 NOT NULL, -- 1(채택O) 0(채택X) --
    qna_board_activation TINYINT   DEFAULT 1                 NOT NULL, -- 1(활성화) 0(비활성화) --
    CONSTRAINT member_no_qna_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- QnAReply - 질문 게시판 댓글
CREATE TABLE IF NOT EXISTS QnAReply
(
    qna_reply_no         INTEGER AUTO_INCREMENT PRIMARY KEY,
    qna_board_no         INTEGER                             NOT NULL,
    member_no            INTEGER                             NOT NULL,
    qna_reply_content    VARCHAR(500)                        NOT NULL,
    qna_reply_reg_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    qna_adoption         TINYINT   DEFAULT 0                 NOT NULL, -- 1(채택O) 0(채택X) --
    qna_reply_like       INTEGER   DEFAULT 0                 NOT NULL,
    qna_reply_activation TINYINT   DEFAULT 1                 NOT NULL, -- 1(활성화) 0(비활성화) --
    CONSTRAINT member_no_qnaReply_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE,
    CONSTRAINT qna_board_no_qnaReply_fk FOREIGN KEY (qna_board_no) REFERENCES QnA (qna_board_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Notice - 공지사항
CREATE TABLE IF NOT EXISTS Notice
(
    notice_no       INTEGER AUTO_INCREMENT PRIMARY KEY,
    member_no       INTEGER                             NOT NULL,
    notice_title    VARCHAR(30)                         NOT NULL,
    notice_content  VARCHAR(1000)                       NOT NULL,
    notice_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    notice_view     INTEGER   DEFAULT 0                 NOT NULL,
    notice_file     VARCHAR(100)                        NULL,
    CONSTRAINT member_no_notice_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Inquiry - 고객문의
CREATE TABLE IF NOT EXISTS Inquiry
(
    inquiry_no           INTEGER AUTO_INCREMENT PRIMARY KEY,
    member_no            INTEGER                                                   NOT NULL,
    inquiry_title        VARCHAR(100)                                              NOT NULL,
    inquiry_content      LONGTEXT                                                  NOT NULL,
    inquiry_reg_date     TIMESTAMP                       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    inquiry_file         VARCHAR(100)                                              NULL,
    inquiry_status       ENUM ('답변 대기', '답변 중', '답변 완료') DEFAULT '답변 대기'           NOT NULL,
    inquiry_satisfaction TINYINT                         DEFAULT NULL, -- 초기값 NULL, 1(만족), 0(불만족)
    CONSTRAINT member_no_inquiry_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- InquiryAnswer - 고객문의 답변
CREATE TABLE IF NOT EXISTS InquiryAnswer
(
    inquiry_answer_no       INTEGER AUTO_INCREMENT PRIMARY KEY,
    inquiry_no              INTEGER                             NOT NULL,
    member_no               INTEGER                             NOT NULL,
    inquiry_answer_content  VARCHAR(500)                        NOT NULL,
    inquiry_answer_reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    inquiry_answer_file     VARCHAR(100)                        NULL,
    CONSTRAINT inquiry_no_inquiryAnswer_fk FOREIGN KEY (inquiry_no) REFERENCES Inquiry (inquiry_no) ON DELETE CASCADE,
    CONSTRAINT member_no_inquiryAnswer_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Reports - 신고
CREATE TABLE IF NOT EXISTS Reports
(
    reports_no          INTEGER AUTO_INCREMENT PRIMARY KEY,
    member_no           INTEGER                                         NOT NULL,
    reported_member_no  INTEGER                                         NOT NULL,
    reports_type        ENUM ('community', 'member', 'reply', 'review') NOT NULL,
    reports_target      VARCHAR(100)                                    NOT NULL,
    reports_target_link VARCHAR(500)                                    NOT NULL,
    reports_reason      VARCHAR(100)                                    NOT NULL,
    reports_status      TINYINT   DEFAULT 0                             NOT NULL, -- 0(처리 전) 1(처리 완료)
    reports_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP             NOT NULL,
    CONSTRAINT member_no_reports_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE,
    CONSTRAINT reported_member_no_reports_fk FOREIGN KEY (reported_member_no) REFERENCES Member (member_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- FAQ - 자주 묻는 질문
CREATE TABLE IF NOT EXISTS FAQ
(
    faq_no       INTEGER AUTO_INCREMENT PRIMARY KEY,
    faq_type     TINYINT      NOT NULL, -- 1(전문가) 0(회원) --
    faq_question VARCHAR(100) NOT NULL,
    faq_response VARCHAR(500) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 비밀번호 찾기 테이블
CREATE TABLE IF NOT EXISTS PasswordResetToken
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   VARCHAR(50)  NOT NULL,
    token       VARCHAR(100) NOT NULL,
    expiry_date DATETIME     NOT NULL,
    FOREIGN KEY (member_id) REFERENCES Member (member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- Pay - 결제
CREATE TABLE IF NOT EXISTS Pay
(
    pay_no        INTEGER AUTO_INCREMENT PRIMARY KEY,
    estimation_no INTEGER                             NOT NULL,
    member_no     INTEGER                             NOT NULL,
    pay_type      VARCHAR(50)                         NOT NULL,
    pay_content   VARCHAR(500)                        NOT NULL,
    pay_price     INTEGER                             NOT NULL,
    pay_status    TINYINT(1)                          NOT NULL, -- 1(결제 완료) 0(미결제) --
    pay_date      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT member_no_pay_fk FOREIGN KEY (member_no) REFERENCES Member (member_no) ON DELETE CASCADE,
    CONSTRAINT estimation_no_pay_fk FOREIGN KEY (estimation_no) REFERENCES Estimation (estimation_no) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 채팅방
CREATE TABLE IF NOT EXISTS chat_room
(
    room_id       VARCHAR(50) PRIMARY KEY,
    room_name     VARCHAR(100) NOT NULL,
    member_no     INTEGER      NOT NULL,
    pro_no        INTEGER      NOT NULL,
    estimation_no INTEGER      NOT NULL,
    max_users     INT          NOT NULL DEFAULT 2,
    created_at    TIMESTAMP    NOT NULL,
    room_admin    INT          NOT NULL, -- 방장 ID (전문가 ID) 컬럼 추가
    CONSTRAINT fk_room_member FOREIGN KEY (member_no) REFERENCES Member (member_no),
    CONSTRAINT fk_room_pro FOREIGN KEY (pro_no) REFERENCES Member (member_no),
    CONSTRAINT fk_room_estimation FOREIGN KEY (estimation_no) REFERENCES Estimation (estimation_no),
    CONSTRAINT fk_room_admin FOREIGN KEY (room_admin) REFERENCES Member (member_no)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 채팅 메세지
CREATE TABLE IF NOT EXISTS chat_message
(
    message_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id      VARCHAR(50) NOT NULL,
    sender       VARCHAR(50) NOT NULL,
    nickname     VARCHAR(20) NOT NULL,
    message      TEXT        NOT NULL,
    message_type VARCHAR(10) NOT NULL,
    sent_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_chat_room FOREIGN KEY (room_id) REFERENCES chat_room (room_id) ON DELETE CASCADE,
    CONSTRAINT fk_chat_sender FOREIGN KEY (sender) REFERENCES Member (member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 채팅방 참여자 테이블 추가
CREATE TABLE IF NOT EXISTS chat_room_participant
(
    room_id   VARCHAR(50),
    member_id VARCHAR(50),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (room_id, member_id),
    CONSTRAINT fk_participant_room FOREIGN KEY (room_id) REFERENCES chat_room (room_id),
    CONSTRAINT fk_participant_member FOREIGN KEY (member_id) REFERENCES Member (member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 채팅방 게시판 테이블 추가
CREATE TABLE IF NOT EXISTS chat_board_event
(
    board_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id       VARCHAR(50),
    member_id     VARCHAR(50),
    board_title   VARCHAR(50)    NOT NULL,
    board_content VARCHAR(10000) NOT NULL,
    joined_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_chat_board_room FOREIGN KEY (room_id) REFERENCES chat_room (room_id) ON DELETE CASCADE,
    CONSTRAINT fk_chat_board_member FOREIGN KEY (member_id) REFERENCES Member (member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 채팅방 일정 테이블 추가
CREATE TABLE IF NOT EXISTS chat_calendar_event
(
    event_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id     VARCHAR(50)  NOT NULL,
    created_by  VARCHAR(50)  NOT NULL,
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    start_date  DATETIME     NOT NULL,
    end_date    DATETIME     NOT NULL,
    all_day     BOOLEAN   DEFAULT false,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_calendar_room FOREIGN KEY (room_id) REFERENCES chat_room (room_id) ON DELETE CASCADE,
    CONSTRAINT fk_calendar_creator FOREIGN KEY (created_by) REFERENCES Member (member_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
