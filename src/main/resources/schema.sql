DROP TABLE IF EXISTS customer_profile;
DROP TABLE IF EXISTS account;

CREATE TABLE customer_profile (
  id         INT         NOT NULL AUTO_INCREMENT,
  name       VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE account (
  id            INT   NOT NULL AUTO_INCREMENT,
  balance       FLOAT NOT NULL,
  customer_id   INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (customer_id) REFERENCES customer_profile(id)
);

