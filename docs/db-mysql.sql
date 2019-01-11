
-- 创建weather_day表
CREATE TABLE
    weather_day
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        areaName VARCHAR(40),
        areaCode VARCHAR(40),
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

-- 创建weather_month表
CREATE TABLE
    weather_month
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        areaName VARCHAR(40),
        areaCode VARCHAR(40),
		yearMonth VARCHAR(8),
		urlName VARCHAR(60),
		url VARCHAR(255),
		crawlFlag VARCHAR(2),
		crawlCount int,
		validStatus VARCHAR(2),
        insertTime DATETIME,
        updateTime DATETIME,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
-- 创建weather_city表
CREATE TABLE
    weather_city
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        areaName VARCHAR(40),
		areaCode VARCHAR(40),
		indexLetter VARCHAR(10),
		url VARCHAR(255),
		crawlFlag VARCHAR(2),
		crawlCount int,
		remark VARCHAR(255),
		validStatus VARCHAR(2),
        insertTime DATETIME,
        updateTime DATETIME,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

--    
alter table weather_day add areaCode VARCHAR(40) DEFAULT '' after areaName;
