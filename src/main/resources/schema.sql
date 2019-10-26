DROP TABLE IF EXISTS customer_profile;
DROP TABLE IF EXISTS accountDTO;

CREATE TABLE customer_profile (
  id         INT         NOT NULL AUTO_INCREMENT,
  name       VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE accountDTO (
  id            INT   NOT NULL AUTO_INCREMENT,
  balance       FLOAT NOT NULL,
  customer_id   INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (customer_id) REFERENCES customer_profile(id)
);

