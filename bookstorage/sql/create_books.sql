CREATE TABLE BOOK(
	BOOK_ID           BIGINT NOT NULL,
	AUTHOR_ID            BIGINT NOT NULL,
	GENRE_ID           BIGINT NOT NULL,
	NAME          VARCHAR(255) NOT NULL,
	PUBLISH_YEAR          DATE NOT NULL,
	CITY             VARCHAR(80) NOT NULL,
	PUBLISHER            VARCHAR(80) NOT NULL,
	FOREIGN KEY(AUTHOR_ID) REFERENCES AUTHOR(AUTHOR_ID),
	FOREIGN KEY(GENRE_ID) REFERENCES GENRE(GENRE_ID)
);
