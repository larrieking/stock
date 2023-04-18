CREATE TABLE `STOCK` (
                         ID INT NOT NULL AUTO_INCREMENT,
                         NAME VARCHAR(100) NOT NULL,
                         CURRENT_PRICE DECIMAL(10,2) NOT NULL DEFAULT '0.00',
                         CREATED_DATE       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         LAST_MODIFIED_DATE TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`ID`)
);