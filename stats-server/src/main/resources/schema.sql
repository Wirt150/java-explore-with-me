DROP TABLE IF EXISTS uris CASCADE;
DROP TABLE IF EXISTS stats CASCADE;

CREATE TABLE IF NOT EXISTS uris
(
    name     VARCHAR(100)                            NOT NULL,
    app_name VARCHAR(100)                            NOT NULL,
    CONSTRAINT pk_uris PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS stats
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    uri       VARCHAR(100)                                  NOT NULL,
    ip        VARCHAR(20)                             NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_stats PRIMARY KEY (id),
    CONSTRAINT fk_uri_name_stats FOREIGN KEY (uri) REFERENCES uris (name)
);




