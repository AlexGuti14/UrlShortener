USE url;
SET sql_mode = '';

DROP TABLE IF EXISTS CLICK;
DROP TABLE IF EXISTS SHORTURL;


-- ShortURL

CREATE TABLE IF NOT EXISTS SHORTURL
(
    HASH    VARCHAR(30) PRIMARY KEY, -- Key
    TARGET  VARCHAR(1024),           -- Original URL
    SPONSOR VARCHAR(1024),           -- Sponsor URL
    CREATED TIMESTAMP,               -- Creation date
    OWNER   VARCHAR(255),            -- User id
    MODE    INTEGER,                 -- Redirect mode
    SAFE    BOOLEAN,                 -- Safe target
    IP      VARCHAR(20),             -- IP
    COUNTRY VARCHAR(50),             -- Country
    VALIDATION TIMESTAMP             -- Timestamp to periodic validation
);

-- Click

CREATE TABLE IF NOT EXISTS CLICK
(
    ID       BIGINT AUTO_INCREMENT PRIMARY KEY,                                             -- KEY
    HASH     VARCHAR(30),                                                 -- Foreing key
    CREATED  TIMESTAMP,                                                   -- Creation date
    REFERRER VARCHAR(1024),                                               -- Traffic origin
    BROWSER  VARCHAR(50),                                                 -- Browser
    PLATFORM VARCHAR(50),                                                 -- Platform
    IP       VARCHAR(20),                                                 -- IP
    COUNTRY  VARCHAR(50),                                                 -- Country
    CONSTRAINT fk_hash FOREIGN KEY (HASH) REFERENCES SHORTURL(HASH)
)
