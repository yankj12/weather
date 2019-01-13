
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

--增加areacode字段
alter table weather_day add areaCode VARCHAR(40) DEFAULT '' after areaName;

--对没有indexletter的数据补充indexLetter
update weather_city set indexLetter=upper(left(areaCode, 1));




-- 创建索引
CREATE INDEX idx_weather_city_areacode ON weather_city (areacode);
CREATE INDEX idx_weather_city_areaname ON weather_city (areaname);

CREATE INDEX idx_weather_month_areacode ON weather_month (areacode);
CREATE INDEX idx_weather_month_yearmonth ON weather_month (yearmonth);

CREATE INDEX idx_weather_day_areacode ON weather_day (areacode);
CREATE INDEX idx_weather_day_date ON weather_day (date);

