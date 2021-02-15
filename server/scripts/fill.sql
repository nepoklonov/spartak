CREATE TABLE teams (
    name    text,
    link    text,
    "isOur" boolean,
    year    text
);
CREATE TABLE trainers (
    "teamLink"    text,
    photo    text,
    "name" text,
    info    text
);
CREATE TABLE teammembers (
    "teamLink"    text,
    "number"    text,
    photo text,
    "firstName"    text,
    "lastName"     text,
    "role"      text,
    birthday    text,
    city        text,
    "teamRole"  text
);
CREATE TABLE news (
    "url"    text,
    "date"      bigint
);
CREATE TABLE games (
    "date"    text,
    "time"  text,
    "year"  text,
    teamAId     int,
    teamBId     int,
    stadium    text,
    "result"    text
);
CREATE TABLE admins (
    "login"    text,
    "password"  text
);
CREATE TABLE gallerysections (
    "name"    text,
    "link"  text
);
CREATE TABLE photos (
    "url"    text,
    "gallerySection"  text
);
CREATE TABLE recruitment (
    "dates"    text,
    "name"  text,
    birthday    text,
    role    text,
    stickGrip   text,
    params  text,
    previousSchool  text,
    city    text,
    phone   text,
    email   text
);
CREATE TABLE gamessections (
    name    text,
    link    text
);
CREATE TABLE workouts (
    startTime    text,
    endTime    text,
    dayOfWeek   int,
    sectionLink     text,
    text    text,
    actualFromDate  real,
    actualToDate    real
);
CREATE TABLE workoutssections (
    name    text,
    link    text
);
INSERT INTO teams (name, link, "isOur", year) VALUES ('Команда МХК «Спартак» 2003 г.р.', '2003', true, '2003');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Команда МХК «Спартак» 2005 г.р.', '2005', true, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Команда МХК «Спартак» 2006 г.р.', '2006', true, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('МКМ', '', false, '2003');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Невский', '', false, '2003');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СКА-ХКД', '', false, '2003');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Северсталь', '', false, '2003');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Динамо-Форвард', '', false, '2003');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Динамо Юниор', '', false, '2003');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СШОР №1', '', false, '2003');
INSERT INTO teams (name, link, "isOur", year) VALUES ('ХКД', '', false, '2003');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Динамо-Светлогорец', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Северсталь', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Невский', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Олимпийские Надежды', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СКА-Варяги', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СКА-ХКД', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Динамо СПб', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СШОР №1', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Динамо Юниор', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('МКМ', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СКА', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Красная Звезда-2', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Йети', '', false, '2005');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СШОР №1', '', false, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('МКМ', '', false, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Динамо Юниор', '', false, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СКА-ХКД', '', false, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СКА-Стрельна', '', false, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СКА', '', false, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Северсталь', '', false, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СКА-Серебряные Львы', '', false, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('Динамо СПб', '', false, '2006');
INSERT INTO teams (name, link, "isOur", year) VALUES ('СКА-Варяги', '', false, '2006');


INSERT INTO trainers ("teamLink", photo, "name", info) VALUES ('2003', 'trainer2003.jpg', 'Гридин Андрей Викторович', 'Тренер высшей категории. Тренер команды МХК «Спартак» 2003 г.р. Неоднократный победитель первенства и кубка Санкт-Петербурга по 2003 г.р. Тренер сборной команды Санкт-Петербурга 2003 г.р. в сезонах 2016/17, 2017/18 и 2018/19.');
INSERT INTO trainers ("teamLink", photo, "name", info) VALUES ('2005', 'trainer2005.jpg', 'Корайкин Артем Викторович', 'Тренер команды МХК «Спартак» 2005 г.р. Чемпион России среди юношей 1998 г.р. Бронзовый призер Первенства России среди молодежных команд');
INSERT INTO trainers ("teamLink", photo, "name", info) VALUES ('2006', 'trainer2006.jpg', 'Чеснов Владислав Евгеньевич', 'Тренер первой категории. Тренер команды МХК «Спартак» 2006 г.р. Бронзовый призем ОПМ по 2003 г.р. Тренер сборной команды Ярославской области 2003 г.р. в сезоне 2017/18.');

INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '1', '2003_1.jpg', 'Александр', 'Жирнов', 'goalkeepers', '21.01.2003', 'г. Москва','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '20', '2003_20.jpg', 'Илья', 'Чубенко', 'goalkeepers', '14.04.2003', 'г. Хабаровск','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '5', '2003_5.jpg', 'Алексей', 'Чернявский', 'defenders', '15.03.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '7', '2003_7.jpg', 'Глеб', 'Васильев', 'defenders', '23.08.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '12', '2003_12.jpg', 'Климентий', 'Рачкаускас', 'defenders', '02.03.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '57', '2003_57.jpg', 'Максим', 'Будник', 'defenders', '22.04.2003', 'г. Хабаровск','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '59', '2003_59.jpg', 'Николай', 'Думченко', 'defenders', '10.10.2003', 'г. Хабаровск','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '72', '2003_72.jpg', 'Никита', 'Андрианов', 'defenders', '01.11.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '78', '2003_78.jpg', 'Владислав', 'Киселев', 'defenders', '06.02.2003', 'г. Хабаровск','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '88', '2003_88.jpg', 'Артем', 'Кулаков', 'defenders', '14.02.2003', 'г. Большой Камень','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '8', '2003_8.jpg', 'Никита', 'Подковыров', 'strikers', '20.09.2003', 'г. Хабаровск','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '9', '2003_9.jpg', 'Артем', 'Ахуткин', 'strikers', '09.04.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '10', '2003_10.jpg', 'Георгий', 'Славороссов', 'strikers', '01.06.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '17', '2003_17.jpg', 'Иван', 'Тимофеев', 'strikers', '20.09.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '18', '2003_18.jpg', 'Артем', 'Пушков', 'strikers', '17.02.2003', 'г. Череповец','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '22', '2003_22.jpg', 'Владимир', 'Андреев', 'strikers', '16.11.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '23', '2003_23.jpg', 'Валерий', 'Асратян', 'strikers', '23.09.2003', 'г. Нижневартовск','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '27', '2003_27.jpg', 'Максим', 'Белов', 'strikers', '30.09.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '28', '2003_28.jpg', 'Всеволод', 'Гайдамак', 'strikers', '09.04.2003', 'г. Хабаровск','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '43', '2003_43.jpg', 'Сергей', 'Пурдышов', 'strikers', '20.10.2003', 'г. Хабаровск','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '71', '2003_71.jpg', 'Александр', 'Клюшниченко', 'strikers', '12.05.2003', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '73', '2003_73.jpg', 'Григорий', 'Зоринов', 'strikers', '07.02.2003', 'г. Череповец','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '77', '2003_77.jpg', 'Тимофей', 'Турзанов', 'strikers', '23.04.2004', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '83', '', 'Андрей', 'Петров', 'strikers', '05.01.2003', 'Нарьян-Мар','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2003', '87', '', 'Александр', 'Комаров', 'strikers', '07.01.2003', 'г. Санкт-Петербург','');

INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '1', '2005_1.jpg', 'Глеб', 'Гусев', 'goalkeepers', '30.06.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '30', '2005_30.jpg', 'Даниил', 'Ваганов', 'goalkeepers', '14.10.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '2', '2005_2.jpg', 'Аким', 'Николаев', 'defenders', '07.02.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '5', '2005_5.jpg', 'Михаил', 'Мастрюков', 'defenders', '03.01.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '7', '2005_7.jpg', 'Дмитрий', 'Щукин', 'defenders', '07.05.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '23', '2005_23.jpg', 'Роман', 'Чернов', 'defenders', '01.12.2005', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '27', '2005_27.jpg', 'Георгий', 'Елиеев', 'defenders', '03.08.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '55', '2005_55.jpg', 'Игорь', 'Репка', 'defenders', '15.03.2005', 'г. Якутск','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '8', '2005_8.jpg', 'Роман', 'Логунов', 'strikers', '21.02.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '11', '2005_11.jpg', 'Тимур', 'Насибуллин', 'strikers', '11.03.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '12', '2005_12.jpg', 'Егор', 'Обухов', 'strikers', '2005_12.jpg', 'г. Владивосток','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '16', '2005_16.jpg', 'Алексей', 'Лазовский', 'strikers', '31.01.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '17', '2005_17.jpg', 'Максим', 'Медведев', 'strikers', '12.08.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '19', '2005_19.jpg', 'Артем', 'Абрамов', 'strikers', '19.06.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '20', '2005_20.jpg', 'Ярослав', 'Ситов', 'strikers', '20.07.2005', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '26', '2005_26.jpg', 'Павел', 'Полрус', 'strikers', '12.11.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '29', '2005_29.jpg', 'Максим', 'Филиппов', 'strikers', '31.12.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '35', '2005_35.jpg', 'Никита', 'Старосельцев', 'strikers', '03.05.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '42', '2005_42.jpg', 'Кирилл', 'Маслов', 'strikers', '22.10.2005', 'г. Владивосток','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '45', '2005_45.jpg', 'Александр', 'Шапкин', 'strikers', '18.06.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '63', '2005_63.jpg', 'Михаил', 'Серов', 'strikers', '26.09.2005', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '74', '2005_74.jpg', 'Йонг', 'Парк', 'strikers', '17.06.2005', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '87', '2005_87.jpg', 'Савелий', 'Лифшиц', 'strikers', '04.03.2005', 'г. Санкт-Петербург','','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '96', '2005_96.jpg', 'Игорь', 'Иванов', 'strikers', '22.01.2005', 'г. Санкт-Петербург','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2005', '97', '2005_97.jpg', 'Иван', 'Королев', 'strikers', '09.07.2005', 'г. Санкт-Петербург','');

INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '1', '', 'Даниил', 'Климин', 'goalkeepers', '03.04.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '41', '', 'Егор', 'Шпак', 'goalkeepers', '24.07.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '87', '', 'Артем', 'Покладок', 'goalkeepers', '17.05.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '4', '', 'Никита', 'Веселков', 'defenders', '30.09.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '13', '', 'Иван', 'Капылович', 'defenders', '13.01.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '18', '', 'Григорий', 'Шпак', 'defenders', '24.07.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '19', '', 'Егор', 'Чипкинян', 'defenders', '19.06.2007', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '25', '', 'Иван', 'Коновалов', 'defenders', '01.10.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '70', '', 'Федор', 'Бушляков', 'defenders', '07.08.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '81', '', 'Марк', 'Коннов', 'defenders', '26.02.2007', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '8', '', 'Андрей', 'Гассанов', 'strikers', '30.03.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '14', '', 'Варвара', 'Пазухина', 'strikers', '03.12.2005', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '23', '', 'Никита', 'Киселев', 'strikers', '03.08.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '27', '', 'Нил', 'Альбицкий', 'strikers', '09.07.2007', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '44', '', 'Святослав', 'Телегин', 'strikers', '23.12.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '66', '', 'Максим', 'Милаков', 'strikers', '10.08.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '77', '', 'Юлиан', 'Афанасьев', 'strikers', '31.07.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '88', '', 'Иван', 'Дерягин', 'strikers', '18.05.2006', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '98', '', 'Симеон', 'Дмитриев', 'strikers', '08.02.2007', '','');
INSERT INTO teammembers ("teamLink", "number", photo, "firstName", "lastName", "role", birthday, city, "teamRole") VALUES ('2006', '99', '', 'Кристоф', 'Муиссу Жоэ', 'strikers', '22.08.2006', '','');



INSERT INTO news (url, "date") VALUES ('1.html', 1566501170000);
INSERT INTO news (url, "date") VALUES ('2.html', 1566587570000);
INSERT INTO news (url, "date") VALUES ('3.html', 1566673970000);
INSERT INTO news (url, "date") VALUES ('4.html', 1566760370000);
INSERT INTO news (url, "date") VALUES ('5.html', 1567192370000);
INSERT INTO news (url, "date") VALUES ('6.html', 1567278770000);
INSERT INTO news (url, "date") VALUES ('7.html', 1567365170000);
INSERT INTO news (url, "date") VALUES ('8.html', 1568056370000);
INSERT INTO news (url, "date") VALUES ('9.html', 1568920370000);
INSERT INTO news (url, "date") VALUES ('10.html', 1572549170000);
INSERT INTO news (url, "date") VALUES ('11.html', 1572635570000);
INSERT INTO news (url, "date") VALUES ('12.html', 1573758770000);
INSERT INTO news (url, "date") VALUES ('13.html', 1575659570000);
INSERT INTO news (url, "date") VALUES ('14.html', 1576869170000);
INSERT INTO news (url, "date") VALUES ('15.html', 1579633970000);
INSERT INTO news (url, "date") VALUES ('16.html', 1583435570000);
INSERT INTO news (url, "date") VALUES ('17.html', 1584645170000);
INSERT INTO news (url, "date") VALUES ('19.html', 1593717170000);
INSERT INTO news (url, "date") VALUES ('20.html', 1595531570000);
INSERT INTO news (url, "date") VALUES ('21.html', 1597086770000);
INSERT INTO news (url, "date") VALUES ('21.html', 1605035570000);

INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('03.10.20', '19:15', 'championship2003', 4, 1, 'ЛОК', '3:6');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('10.10.20', '19:45', 'championship2003', 1, 5, 'ГКА', '9:1');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('11.10.20', '19:00', 'championship2003', 6, 1, 'ХКД', '4:11');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('17.10.20', '19:45', 'championship2003', 1, 7, 'ГКА', '8:3');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('24.10.20', '09:00', 'championship2003', 1, 8, 'ГКА', '5:4');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('25.10.20', '15:15', 'championship2003', 9, 1, 'БУТ', '1:6');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('31.10.20', '19:45', 'championship2003', 1, 10, 'ГКА', '8:2');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('07.11.20', '09:00', 'championship2003', 1, 4, 'ГКА', '6:3');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('14.11.20', '18:15', 'championship2003', 5, 1, 'АНТ', '4:5ПБ');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('15.11.20', '10:00', 'championship2003', 1, 7, 'ГКА', '4:5ОТ');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('22.11.20', '10:00', 'championship2003', 1, 11, 'ГКА', '6:1');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('05.12.20', '18:45', 'championship2003', 8, 1, 'МАЛ', '3:4');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('13.12.20', '17:00', 'championship2003', 10, 1, 'КСМ', '2:3');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('19.12.20', '09:00', 'championship2003', 1, 7, 'ГКА', '0:6');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('20.12.20', '10:00', 'championship2003', 1, 9, 'ГКА', '3:4');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('26.12.20', '', 'championship2003', 4, 1, 'ЛОК', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('09.01.21', '', 'championship2003', 1, 5, 'ГКА', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('16.01.21', '', 'championship2003', 6, 1, 'ХКД', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('23.01.21', '', 'championship2003', 1, 8, 'ГКА', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('30.01.21', '', 'championship2003', 9, 1, 'БУТ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('06.02.21', '', 'championship2003', 1, 10, 'ГКА', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('13.02.21', '', 'championship2003', 1, 4, 'ГКА', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('20.02.21', '', 'championship2003', 5, 1, 'АНТ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('28.02.21', '', 'championship2003', 1, 6, 'ГКА', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('13.03.21', '', 'championship2003', 1, 7, 'ГКА', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('14.03.21', '', 'championship2003', 8, 1, 'МАЛ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('20.03.21', '', 'championship2003', 1, 9, 'ГКА', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('21.03.21', '', 'championship2003', 10, 1, 'КСМ', '');


INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('10.10.20', '09:00', 'championship2005', 2, 12, 'ГКА', '5:4');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('17.10.20', '09:00', 'championship2005', 2, 13, 'ГКА', '1:8');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('18.10.20', '18:45', 'championship2005', 2, 14, 'ГКА', '3:4ПБ');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('24.10.20', '19:30', 'championship2005', 2, 15, 'ГКА', '1:5');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('31.10.20', '09:00', 'championship2005', 16, 2, 'ГКА', '9:0');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('01.11.20', '18:45', 'championship2005', 2, 17, 'ГКА', '4:3');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('04.11.20', '11:00', 'championship2005', 18, 2, 'КАХ', '10:1');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('07.11.20', '18:45', 'championship2005', 19, 2, 'КСМ', '23:0');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('14.11.20', '19:30', 'championship2005', 2, 20, 'ГКА', '2:7');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('22.11.20', '16:30', 'championship2005', 21, 2, 'ЛОК', '13:2');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('05.12.20', '12:00', 'championship2005', 22, 2, 'ЖДН', '11:0');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('12.12.20', '09:00', 'championship2005', 2, 23, 'ГКА', '0:2');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('13.12.20', '10:00', 'championship2005', 2, 24, 'ГКА', '2:1');

INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('11.10.20', '15:15', 'championship2006', 25, 3, 'КСМ', '5:0');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('17.10.20', '13:00', 'championship2006', 26, 3, 'ЛОК', '4:11');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('18.10.20', '10:00', 'championship2006', 3, 27, 'ГКА', '4:3ОТ');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('01.11.20', '17:45', 'championship2006', 28, 3, 'ХКД', '4:5ПБ');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('04.11.20', '12:00', 'championship2006', 3, 29, 'ГКА', '1:11');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('08.11.20', '10:00', 'championship2006', 3, 30, 'ГКА', '2:3');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('05.12.20', '16:45', 'championship2006', 29, 3, 'АСК', '17:4');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('06.12.20', '10:00', 'championship2006', 3, 30, 'ГКА', '1:6');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('12.11.20', '09:30', 'championship2006', 3, 31, 'ГКА', '2:4');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('13.12.20', '16:30', 'championship2006', 32, 3, 'ХГГ', '7:1');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('20.12.20', '13:45', 'championship2006', 33, 3, 'КАХ', '7:2');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('26.12.20', '', 'championship2006', 3, 34, 'ГКА', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('16.01.21', '10:15', 'championship2006', 3, 32, 'СОЧ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('18.01.21', '16:00', 'championship2006', 34, 3, 'СОЧ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('21.01.21', '10:15', 'championship2006', 3, 31, 'СОЧ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('23.01.21', '16:00', 'championship2006', 3, 33, 'СОЧ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('27.01.21', '10:15', 'championship2006', 30, 3, 'СОЧ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('30.01.21', '10:15', 'championship2006', 3, 29, 'СОЧ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('27.01.21', '10:15', 'championship2006', 30, 3, 'СОЧ', '');
INSERT INTO games ("date", "time", "year", teamAId, teamBId, stadium, "result") VALUES ('07.02.21', '16:00', 'championship2006', 3, 30, 'КСМ', '');

INSERT INTO admins ("login", "password") VALUES ('admin', 'admin');

INSERT INTO gallerysections ("name", "link") VALUES ('Пекин', 'Beijing');
INSERT INTO gallerysections ("name", "link") VALUES ('Тренировочный процесс', 'trainingProcess');
INSERT INTO gallerysections ("name", "link") VALUES ('Турнир 2011 г.р.', 'championship2011');
INSERT INTO gallerysections ("name", "link") VALUES ('Кубок Ладоги 2019', 'LadogaCup2019');

INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_0.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_1.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_2.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_3.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_4.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_5.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_6.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_7.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_8.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_9.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_10.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_11.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_12.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_13.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_14.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_15.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_16.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_17.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_18.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_19.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_20.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_21.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_22.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_23.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_24.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_25.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_26.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_27.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_28.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_29.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_30.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_31.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_32.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_33.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_34.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_35.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_36.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_37.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_38.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_39.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_40.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_41.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_42.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_43.jpg', 'Beijing');
INSERT INTO photos ("url", "gallerySection") VALUES ('Beijing_44.jpg', 'Beijing');

INSERT INTO photos ("url", "gallerySection") VALUES ('Ladoga_Cup_2019_0.jpg', 'LadogaCup2019');
INSERT INTO photos ("url", "gallerySection") VALUES ('Ladoga_Cup_2019_1.jpg', 'LadogaCup2019');
INSERT INTO photos ("url", "gallerySection") VALUES ('Ladoga_Cup_2019_2.jpg', 'LadogaCup2019');
INSERT INTO photos ("url", "gallerySection") VALUES ('Ladoga_Cup_2019_3.jpg', 'LadogaCup2019');
INSERT INTO photos ("url", "gallerySection") VALUES ('Ladoga_Cup_2019_4.jpg', 'LadogaCup2019');
INSERT INTO photos ("url", "gallerySection") VALUES ('Ladoga_Cup_2019_5.jpg', 'LadogaCup2019');
INSERT INTO photos ("url", "gallerySection") VALUES ('Ladoga_Cup_2019_6.jpg', 'LadogaCup2019');
INSERT INTO photos ("url", "gallerySection") VALUES ('Ladoga_Cup_2019_7.jpg', 'LadogaCup2019');

INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_0.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_1.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_2.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_3.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_4.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_5.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_6.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_7.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_8.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_9.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_10.jpg', 'championship2011');
INSERT INTO photos ("url", "gallerySection") VALUES ('Tournament_of_2011_11.jpg', 'championship2011');

INSERT INTO photos ("url", "gallerySection") VALUES ('Training_process_0.jpg', 'trainingProcess');
INSERT INTO photos ("url", "gallerySection") VALUES ('Training_process_1.jpg', 'trainingProcess');
INSERT INTO photos ("url", "gallerySection") VALUES ('Training_process_2.jpg', 'trainingProcess');
INSERT INTO photos ("url", "gallerySection") VALUES ('Training_process_3.jpg', 'trainingProcess');
INSERT INTO photos ("url", "gallerySection") VALUES ('Training_process_4.jpg', 'trainingProcess');
INSERT INTO photos ("url", "gallerySection") VALUES ('Training_process_5.jpg', 'trainingProcess');

INSERT INTO gallerysections ("name", "link") VALUES ('Тренировочный процесс', 'trainingProcess');
INSERT INTO gallerysections ("name", "link") VALUES ('Кубок Ладоги 2019', 'LadogaCup2019');
INSERT INTO gallerysections ("name", "link") VALUES ('Пекин', 'Beijing');
INSERT INTO gallerysections ("name", "link") VALUES ('Турнир 2011 г.р.', 'championship2011');

INSERT INTO workoutssections ("name", "link") VALUES ('ШХМ', 'shhm');
INSERT INTO workoutssections ("name", "link") VALUES ('Спартак 2013', 'spartak_2013');
INSERT INTO workoutssections ("name", "link") VALUES ('Спартак 2003-2004', 'spartak_2003_2004');
INSERT INTO workoutssections ("name", "link") VALUES ('Спартак 2005', 'spartak_2005');
INSERT INTO workoutssections ("name", "link") VALUES ('Вратарские тренировки', 'goalkeepers');
INSERT INTO workoutssections ("name", "link") VALUES ('Нруппа набора', 'recruitment');
INSERT INTO workoutssections ("name", "link") VALUES ('Спартак 2006', 'spartak_2006');
INSERT INTO workoutssections ("name", "link") VALUES ('Спартак красная ракета', 'spartak_red_rocket');
INSERT INTO workoutssections ("name", "link") VALUES ('Спартак 2008', 'spartak_2006');