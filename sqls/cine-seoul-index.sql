-- USERS 인덱스 생성
CREATE INDEX IDX_USERS_01 ON USERS(ID);
CREATE INDEX IDX_USERS_02 ON USERS(PHONE_NUM);

-- MOVIE 인덱스 생성
CREATE INDEX IDX_MOVIE_01 ON MOVIE(TITLE);
CREATE INDEX IDX_MOVIE_02 ON MOVIE(RELEASE_DATE);

-- SCHEDULE 인덱스 생성
CREATE INDEX IDX_SCHEDULE_01 ON SCHEDULE(MOVIE_NUM);
CREATE INDEX IDX_SCHEDULE_02 ON SCHEDULE(SCHED_TIME);

-- TICKET 인덱스 생성
CREATE INDEX IDX_TICKET_01 ON TICKET(USER_NUM);
CREATE INDEX IDX_TICKET_02 ON TICKET(SCHED_NUM);

-- PAYMENT 인덱스 생성
CREATE INDEX IDX_PAYMENT_01 ON PAYMENT(USER_NUM);
CREATE INDEX IDX_PAYMENT_02 ON PAYMENT(TICKET_NUM);

-- ACTOR 인덱스 생성
CREATE INDEX IDX_ACTOR_01 ON ACTOR(NAME);

-- DIRECTOR 인덱스 생성
CREATE INDEX IDX_DIRECTOR_01 ON DIRECTOR(NAME);

-- DISTRIBUTOR 인덱스 생성
CREATE INDEX IDX_DISTRIBUTOR_01 ON DISTRIBUTOR(NAME);

-- REVIEW 인덱스 생성
CREATE INDEX IDX_REVIEW_01 ON REVIEW(MOVIE_NUM);

-- AUDIENCE 인덱스 생성
CREATE UNIQUE INDEX IDX_AUDIENCE_PK ON AUDIENCE(TICKET_NUM, AUDIENCE_TYPE);

-- MOVIE_GENRE 인덱스 생성
CREATE UNIQUE INDEX IDX_MOVIE_GENRE_PK ON MOVIE_GENRE(MOVIE_NUM, GENRE_CODE);
CREATE INDEX IDX_MOVIE_GENRE_01 ON MOVIE_GENRE(GENRE_CODE);

-- MOVIE_ACTOR 인덱스 생성
CREATE UNIQUE INDEX IDX_MOVIE_ACTOR_PK ON MOVIE_ACTOR(MOVIE_NUM, ACT_NUM);

-- MOVIE_DIRECTOR 인덱스 생성
CREATE UNIQUE INDEX IDX_MOVIE_DIRECTOR_PK ON MOVIE_DIRECTOR(MOVIE_NUM, DIR_NUM);

-- MOVIE_COUNTRY 인덱스 생성
CREATE UNIQUE INDEX IDX_MOVIE_COUNTRY_PK ON MOVIE_COUNTRY(MOVIE_NUM, COUNTRY_CODE);

-- TICKET_SEAT 인덱스 생성
CREATE UNIQUE INDEX IDX_TICKET_SEAT_PK ON TICKET_SEAT(TICKET_NUM, SEAT_NUM);

-- SCHEDULE_SEAT 인덱스 생성
CREATE UNIQUE INDEX IDX_SCHEDULE_SEAT_PK ON SCHEDULE_SEAT(SCHED_NUM, SEAT_NUM);
