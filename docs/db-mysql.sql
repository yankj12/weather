
-- 创建weather_day表
CREATE TABLE
    weather_day
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        areaName VARCHAR(40),
		date VARCHAR(12),
		year VARCHAR(6),
		month VARCHAR(2),
		day VARCHAR(2),
		temperatureMax DOUBLE,
		temperatureMin DOUBLE,
		summary VARCHAR(12),
		windDirection VARCHAR(24),
		windScale VARCHAR(12),
		remark VARCHAR(255),
		validStatus VARCHAR(2),
        insertTime DATETIME,
        updateTime DATETIME,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

