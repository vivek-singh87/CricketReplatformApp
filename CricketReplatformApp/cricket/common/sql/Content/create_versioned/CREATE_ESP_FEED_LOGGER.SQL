-- Table for logging the ESP feeds. It is available in the Publishing schema as it is used for storing the ESP feeds sent to ATG BCC. 
-- It is not meant to publish any data to CAT and CATB schema and for the same reason it is non-versioned.
CREATE TABLE CKT_FEED_LOGGER
   (FEED_ID VARCHAR2(40 BYTE), 
	FEED CLOB,
  STATUS NUMBER(1),
  SOURCE_SYSTEM VARCHAR2(40 BYTE),
  DESCRIPTION VARCHAR2(500 BYTE),
  CREATION_DATE VARCHAR2(100 BYTE),
  UPDATION_DATE VARCHAR2(100 BYTE));
CREATE UNIQUE INDEX CKT_FEED_LOGGER_P ON CKT_FEED_LOGGER (FEED_ID);
ALTER TABLE CKT_FEED_LOGGER ADD CONSTRAINT CKT_FEED_LOGGER_P PRIMARY KEY (FEED_ID);
COMMIT;