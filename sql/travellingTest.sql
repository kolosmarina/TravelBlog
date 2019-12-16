CREATE DATABASE travelling_repository_test encoding ='UTF8';

/*перейти в БД
\c travelling_repository_test;*/

CREATE SCHEMA travelling_storage;

SET SEARCH_PATH = travelling_storage;

CREATE TABLE traveller
(
    id                BIGSERIAL PRIMARY KEY,
    role              CHARACTER VARYING(32)         NOT NULL,
    email             CHARACTER VARYING(128) UNIQUE NOT NULL,
    password          CHARACTER VARYING(128)        NOT NULL,
    nickname          CHARACTER VARYING(56) UNIQUE  NOT NULL,
    registration_date BIGINT,
    is_active         BOOLEAN
);

/*DROP TABLE traveller;*/

/*SELECT *
FROM traveller;*/

INSERT INTO traveller (role, email, password, nickname, registration_date, is_active)
VALUES ('ADMIN', 'newadmin@gmail.com', '4d54497a4e44553d', 'admin', 1535831575000, true),
       ('USER', 'somename@somesubdomain.travel', '4d54497a4e44553d', 'sky_man', 1544464800000, true),
       ('USER', 'info.info@mail.ru', '4d54497a4e44553d', 'sunny_hamster', 1550655000000, true),
       ('USER', 'supertraveler@gmail.com', '4d54497a4e445532', 'super_traveler', 1557757800000, true),
       ('USER', 'seawave@gmail.com', '4d54497a4e445532', 'sea_wave', 1559563200000, true);

CREATE TABLE post
(
    id           BIGSERIAL PRIMARY KEY,
    name         CHARACTER VARYING(256)           NOT NULL,
    publish_date BIGINT,
    traveller_id BIGINT REFERENCES traveller (id) NOT NULL,
    world_part   CHARACTER VARYING(32)            NOT NULL
);

/*DROP TABLE post;*/

/*SELECT *
FROM post;*/

INSERT INTO post (name, publish_date, traveller_id, world_part)
VALUES ('Литва зимой? Почему бы и нет... ', 1544443200000, 2, 'EUROPE'),
       ('Барселона и ее причудливая архитектура', 1546088400000, 2, 'EUROPE'),
       ('Прогулка по Дрездену', 1550667600000, 3, 'EUROPE'),
       ('Гастрономическая Италия', 1557752400000, 4, 'EUROPE'),
       ('Мое первое путешествие зарубеж - Прага', 1565010000000, 5, 'EUROPE'),
       ('Рим. Имперская столица', 1553511600000, 3, 'EUROPE'),
       ('Путешествие чайника в Лондон', 1554894000000, 3, 'EUROPE'),
       ('Ирландия. Дожди. Пабы', 1549362540000, 2, 'EUROPE'),
       ('Миры старого города. Таллин', 1559388540000, 5, 'EUROPE'),
       ('Португалии за 3 недели. Путешествие моей мечты', 1551261600000, 2, 'EUROPE'),
       ('Поездка в Краков: королевский город и тени Второй мировой', 1554634800000, 3, 'EUROPE'),
       ('Погулка по мосткам Кемерского болота. Латвия', 1561975200000, 5, 'EUROPE'),
       ('Сверхэкономное путешествие в Питер', 1559206800000, 4, 'EUROPE'),
       ('Рига и Гауя: о кошках на крыше и вине из одуванчиков', 1552204800000, 2, 'EUROPE'),
       ('Париж моими глазами...', 1559721600000, 5, 'EUROPE'),
       ('На москвиче-2141 по замкам Беларуси', 1552377600000, 2, 'EUROPE'),
       ('Спонтанная поездка в Барселону', 1559563200000, 4, 'EUROPE'),
       ('Барселона - город в который невозможно не влюбиться!', 1562155200000, 5, 'EUROPE'),
       ('Пуп земли или наше путешествие в Тайланд', 1552640400000, 2, 'ASIA'),
       ('Противоречивая Шри-Ланка', 1561021200000, 4, 'ASIA');

CREATE TABLE photo
(
    id            BIGSERIAL PRIMARY KEY,
    description   TEXT,
    post_id       BIGINT REFERENCES post (id) not null,
    is_main_photo BOOLEAN,
    url           CHARACTER VARYING(256)      not null
);

/*DROP TABLE photo;*/

/*SELECT *
FROM photo;*/

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Вильнюс не может соперничать с большими городами в плане достопримечательностей. Это небольшой и уютный город, но все же столица. Предлагаю вам немного прогуляться.',
        1, false, 'd:\JAVA\Study\travelling-data\Lithuania-topic-1\1.jpg'),
       ('Ратушная площадь.', 1, true, 'd:\JAVA\Study\travelling-data\Lithuania-topic-1\2.jpg'),
       ('Башня Гядеминаса(Гедемина),вдали Три креста.', 1, false,
        'd:\JAVA\Study\travelling-data\Lithuania-topic-1\3.jpg'),
       ('Главная новогодняя елка. Говорят ,что ее признали самой красивой в Европе в этом году. Мне больше нравилась в прошлом году.Она была такая же только зеленые огни были.',
        1, false, 'd:\JAVA\Study\travelling-data\Lithuania-topic-1\4.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Барселона – столица автономной провинции Каталония. Свой флаг, свой язык. Все стараются делать в пику испанскому правительству. Даже корриду запретили как недостойное испанское развлечение.',
        2, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-2\1.jpg'),
       ('Барселона - город Гауди. Этот мужчина всего 100 лет назад построил здесь около десятка сооружений, и сегодня это самые красивые дома Барселоны, на посещении которых стригут просто огромные деньги.',
        2, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-2\2.jpg'),
       ('Гауди ничего бы не смог без своего партнера и главного заказчика - сказочно богатого промышленника Гуэля (что-то вроде каталонского Елисеева). Именно он финансировал безумные и дорогие проекты зодчего. На снимке ворота бывшей усадьбы олигарха Гуэля.',
        2, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-2\3.jpg'),
       ('В честь Гуэля назван парк, спроектированный Гауди. Центральное место в парке занимает смотровая площадка, которая покоится на колоннах.',
        2, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-2\4.jpg'),
       ('Средиземное море в апреле примерно как Ладога в июне. Местные практически не залезают в воду, пляжи пусты. А северным людям, вроде меня, искупаться в кайф.',
        2, true, 'd:\JAVA\Study\travelling-data\Barcelona-topic-2\5.jpg'),
       ('Для обеда в Испании желательно найти какой-нибудь семейный ресторанчик, куда ходят аборигены. Обращает внимание, что почти все официанты в заведениях - мужчины. Причем запросто можно встретить официанта, которому уже перевалило за 40, а то и за 60, что у нас практически немыслимо.',
        2, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-2\6.jpg'),
       ('Мегакот в имигрнатско-студенческом районе Раваль. Рядом с котом анклав турецких кебабных с денером, качество которого я бы поставил на один уровень с берлинским.',
        2, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-2\7.jpg'),
       ('Довольно известная скульптура Миро "женщина и птица". Обычно ее снимают с другого ракурса, и становится непонятно, причем тут женщина.',
        2, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-2\8.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Дрезден нельзя назвать типичным немецким городом. Дрезден – это имперская роскошь...Погуляем немножко...', 3,
        true, 'd:\JAVA\Study\travelling-data\Dresden-topic-3\1.jpg'),
       ('Знаменитая Дрезденская картинная галерея (Галерея Старых мастеров – Alte Meister)', 3, false,
        'd:\JAVA\Study\travelling-data\Dresden-topic-3\2.jpg'),
       ('Semperoper (архитектор G.Semper), Саксонская Государственная Опера, – тоже достопримечательность Дрездена. Дважды была разрушена: в 1869 г. от пожара и в 1945 году в результате неоправданной бомбардировки английскими и американскими ВВС. После войны, во время сноса полуразрушенных зданий, руины театра снесены не были, реставрационные работы начались только в 1977 году и длились до 1985 года',
        3, false, 'd:\JAVA\Study\travelling-data\Dresden-topic-3\3.jpg'),
       ('Дворец-резиденция саксонских курфюрстов (князей) династии Веттинов, которые правили в Саксонии на протяжении 800 лет. Кстати, английская королева Елизавета II является потомком этой немецкой династии (Виндзоры являются потомками Веттинов).',
        3, false, 'd:\JAVA\Study\travelling-data\Dresden-topic-3\4.jpg'),
       ('Cправа - снова Дворец-резиденция (только с другой стороны), слева – придворная церковь Hofkirche:', 3, false,
        'd:\JAVA\Study\travelling-data\Dresden-topic-3\5.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Вот я и вернулся из путешествия по Италии и теперь поведаю Вам, как нужно организовывать своё знакомство с этой страной, чтобы всё шло «как по маслу».',
        4, true, 'd:\JAVA\Study\travelling-data\Italy-topic-4\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Мысль о поездке в Прагу пришла неожиданно, т.к. собираясь в отпуск одна, металась от пляжного отдыха к экскурсионке. К пляжному отдыху с наступлением прохладной осени появилась какая-то аппатия, а Италия при подсчетах оказалась мне не по карману.',
        5, true, 'd:\JAVA\Study\travelling-data\Prague-topic-5\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Моё путешествие в Рим началось с Неаполя, рейсом Киев-Неаполь. Честно говоря, хотела захватить несколько дней в Неаполе, съездить на Везувий и в Помпеи, но была неприятно поражена грязью Неаполя, поэтому сразу поехала на вокзал за билетом в Рим.',
        6, true, 'd:\JAVA\Study\travelling-data\Rome-topic-6\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Лондон притягивает туристов как магнит. Каждый хочет своими глазами увидеть красный даблдекер, красную же телефонную будку, гвардейца Её Величества в медвежьей шапке, восковые куклы Мадам Тюссо, Тауэр и Биг-Бен — то, о чем говорили на школьных уроках английского под общим заголовком “London is the capital of Great Britain”.',
        7, true, 'd:\JAVA\Study\travelling-data\London-topic-7\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Ирландия — страна зеленых лугов, белых овечек, скалистых и песчаных побережий, разноцветных домиков. Красоту, уют и гостеприимство этой страны невозможно передать словами, это нужно прочуствовать.',
        8, true, 'd:\JAVA\Study\travelling-data\Ireland-topic-8\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('В Таллине очень много парков и практически лесов. Посреди города раскинулся зеленый парк Кадриорг с одноименным дворцом посредине (аналог нашего Петергофа). Гуляй неторопливо по красивым дорожкам, дыши морским воздухом.',
        9, true, 'd:\JAVA\Study\travelling-data\Tallinn-topic-9\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Второй мой рассказ хочу посвятить прекрасной солнечной стране — Португалии. Ездила туда с мужем в августе 2014 года. Страна очень красивая и колоритная!',
        10, true, 'd:\JAVA\Study\travelling-data\Portugal-topic-10\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('О том, какой Краков просто так не напишешь, ведь он прекрасен во всех отношениях. Но я все же попробую рассказать вам о своем знакомстве с этим потрясающим городом.',
        11, true, 'd:\JAVA\Study\travelling-data\Cracow-topic-11\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Как-то Так случайно получилось, что на Большое Кемерское болото мы выбрались в значимый для него (болота)  день - 9 августа.',
        12, true, 'd:\JAVA\Study\travelling-data\Latvia-topic-12\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('И вот мы приехали в Санкт-Петербург. В первый день мама предложила пойти погулять по Невскому проспекту. Это очень знаменитый проспект, главная улица в Санкт-Петербурге.',
        13, true, 'd:\JAVA\Study\travelling-data\Peter-topic-13\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Сама Рига оказалась удивительно фотогеничным городом.', 14, true,
        'd:\JAVA\Study\travelling-data\Riga-topic-14\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('На башню решено не забираться из-за жуткого холода, дождя и ветра. На этот раз ограничимся каноническим видом, а в следующий раз, оказавшись в Париже при более благоприятных погодных условиях, обязательно отобедаем наверху.',
        15, true, 'd:\JAVA\Study\travelling-data\Paris-topic-15\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Знак на въезде гласит, что въезд на территорию Мирскго замка разрешён только для гостей отеля в этом замке, но по факту никто вас проверять не будет - даже если не собираетесь заселяться в отель, всё равно можно машину у стен замка оставить. :)',
        16, true, 'd:\JAVA\Study\travelling-data\Belarus-topic-16\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('На крыше дома Гауди Casa Batllo, каждый вечер играла живая музыка ) так что еще и музыкальное сопровождение присутствовало).',
        17, true, 'd:\JAVA\Study\travelling-data\Barcelona-topic-17\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Планируя отдых в Испанию, я хотела как говорят «убить двух зайцев», а именно совместить пляжный отдых на Средиземном море с активным туризмом по стране.',
        18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\1.jpg'),
       ('Сады дель Палау де Педральбес – это историческая резиденция королевской семьи в Барселоне. В парке имеется множество прекрасных скульптур и статуй.',
        18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\2.jpg'),
       ('Внутренние дворики', 18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\3.jpg'),
       ('Порт и набережная Барселоны — район, который обязательно следует посетить туристу, осматривая местные достопримечательности.',
        18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\4.jpg'),
       ('Архитектура не перестает удивлять...', 18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\5.jpg'),
       ('Саграда Фамилия', 18, true, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\7.jpg'),
       ('Статуя гигантского кота весом 2 тонны, длиной 7 метров и высотой два с половиной метра появилась в столице Каталонии в 1987 году. Равальский кот',
        18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\8.jpg'),
       ('Лабиринт Орта (парк)', 18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\9.jpg'),
       ('Вкусности...мммм)))', 18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\10.jpg'),
       ('Надо сразу сказать, что голодным в Барселоне остаться сложно, к вашим услугам множество заведений, начиная от фастфуда и заканчивая элитными ресторанами.',
        18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\12.jpg'),
       ('Волшебные фонтаны Монтжуика (Font Màgica)', 18, false,
        'd:\JAVA\Study\travelling-data\Barcelona-topic-18\12.jpg'),
       ('Парк Гуэль', 18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\13.jpg'),
       ('Предпочтительный транспорт...очень распространен в Барселоне', 18, false,
        'd:\JAVA\Study\travelling-data\Barcelona-topic-18\14.jpg'),
       ('Метро', 18, false, 'd:\JAVA\Study\travelling-data\Barcelona-topic-18\15.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Мой рассказ будет интересен любителям самостоятельного отдыха или тех, кто хотел бы попробовать путешествовать именно так.',
        19, true, 'd:\JAVA\Study\travelling-data\Thailand-topic-19\1.jpg');

INSERT INTO photo (description, post_id, is_main_photo, url)
VALUES ('Захотелось посмотреть Шри Ланку.', 20, true, 'd:\JAVA\Study\travelling-data\Sri Lanka-topic-20\1.jpg');


CREATE TABLE comment
(
    id           BIGSERIAL PRIMARY KEY,
    publish_date BIGINT,
    value        TEXT                             NOT NULL,
    photo_id     BIGINT REFERENCES photo (id)     NOT NULL,
    traveller_id BIGINT REFERENCES traveller (id) NOT NULL
);

/*DROP TABLE comment;*/

/*SELECT *
FROM comment;*/

INSERT INTO comment (publish_date, value, photo_id, traveller_id)
VALUES (1550739600000, 'Вот бы Рождественские каникулы длились дольше...', 1, 2),
       (1557914400000, 'Вильнюс очень красив в Рождественские праздники', 1, 3),
       (1564999200000, 'Подтверждаю', 1, 4),
       (1552226400000, 'Красивая елка', 4, 5),
       (1552230000000, 'Совсем маленький котик', 38, 3),
       (1565103600000, 'Хочу на море', 9, 5),
       (1565364600000, 'Я тоже..', 9, 3),
       (1554823800000, 'Дрезден очень строгий', 13, 2),
       (1554910200000, 'Мне этот город совсем не понравился', 13, 4),
       (1562248800000, 'Какого веселого цвета самолет))', 32, 2),
       (1562508000000, 'Wizz Air', 32, 3),
       (1563638400000, 'Пальмы', 33, 4),
       (1566057600000, 'Какая красота', 33, 5),
       (1567353600000, 'Интересные дворики', 34, 2),
       (1563202800000, 'Как много лодок...', 35, 4),
       (1563382800000, 'Море рядом...почему бы и не прикупить лодку...', 35, 3),
       (1562778000000, 'Величественное здание', 37, 2),
       (1562346000000, 'Ой..какой малыш', 38, 2),
       (1562612400000, 'Малыш...не то слово...2 тонны', 38, 4),
       (1562684400000, 'кот, который гуляет сам по себе', 38, 3),
       (1562770800000,
        'в одной из первых сложившихся о нем легенд, говорится о том, что коту нужно погладить его достоинство и тогда ты не только вернешься в Барселону, но и преуспеешь в жизни',
        38, 5),
       (1566579600000, 'Долго мы не могли выйти из этого лабиринта((', 39, 3),
       (1566759600000, 'А мы быстро нашли выход', 39, 4),
       (1567022400000, 'Прикольная задумка...', 39, 5);

CREATE TABLE tag
(
    id    BIGSERIAL PRIMARY KEY,
    value CHARACTER VARYING(64) UNIQUE NOT NULL
);

/*DROP TABLE tag;*/

/*SELECT *
FROM tag;*/

INSERT INTO tag (value)
VALUES ('вильнюс'),
       ('литва'),
       ('европа'),
       ('барселона'),
       ('испания'),
       ('германия'),
       ('италия'),
       ('рим'),
       ('прага'),
       ('лондон'),
       ('англия'),
       ('ираландия'),
       ('эстония'),
       ('минск'),
       ('море'),
       ('зима'),
       ('лето'),
       ('азия'),
       ('америка'),
       ('лучшее_путешествие');

CREATE TABLE tag_photo
(
    tag_id   BIGINT REFERENCES tag (id)   NOT NULL,
    photo_id BIGINT REFERENCES photo (id) NOT NULL,
    UNIQUE (tag_id, photo_id)
);

/*select * FROM tag_photo;*/

/*DROP TABLE tag_photo;*/

INSERT INTO tag_photo (tag_id, photo_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 2),
       (3, 3),
       (3, 4),
       (3, 5),
       (3, 6),
       (3, 7),
       (3, 8),
       (3, 9),
       (3, 10),
       (3, 11),
       (3, 12),
       (3, 15),
       (3, 17),
       (3, 18),
       (3, 19),
       (3, 20),
       (3, 23),
       (3, 29),
       (3, 32),
       (3, 33),
       (3, 35),
       (3, 37),
       (3, 38),
       (3, 39),
       (3, 41),
       (3, 43),
       (3, 44),
       (4, 5),
       (4, 6),
       (4, 7),
       (4, 8),
       (4, 9),
       (4, 10),
       (4, 11),
       (4, 32),
       (4, 33),
       (4, 34),
       (4, 35),
       (4, 37),
       (4, 38),
       (4, 39),
       (4, 40),
       (4, 41),
       (4, 42),
       (4, 44),
       (5, 5),
       (5, 6),
       (5, 7),
       (5, 8),
       (5, 10),
       (5, 11),
       (5, 12),
       (5, 32),
       (5, 34),
       (5, 35),
       (5, 36),
       (5, 38),
       (5, 40),
       (5, 41),
       (5, 43),
       (5, 44),
       (6, 13),
       (6, 14),
       (6, 15),
       (6, 17),
       (7, 18),
       (8, 20),
       (10, 21),
       (12, 22),
       (13, 23),
       (14, 30),
       (18, 46),
       (18, 47),
       (20, 1),
       (20, 2),
       (20, 3),
       (20, 4),
       (20, 5),
       (20, 6),
       (20, 7),
       (20, 9),
       (20, 10),
       (20, 11),
       (20, 12),
       (20, 15),
       (20, 16),
       (20, 17),
       (20, 20),
       (20, 22),
       (20, 24),
       (20, 25),
       (20, 27),
       (20, 29),
       (20, 30),
       (20, 31),
       (20, 32),
       (20, 33),
       (20, 34),
       (20, 35),
       (20, 36),
       (20, 37),
       (20, 38),
       (20, 39),
       (20, 40),
       (20, 41),
       (20, 42),
       (20, 43),
       (20, 44),
       (20, 45),
       (20, 46),
       (20, 47);

CREATE TABLE smile
(
    photo_id     BIGINT REFERENCES photo (id)     NOT NULL,
    traveller_id BIGINT REFERENCES traveller (id) NOT NULL,
    UNIQUE (photo_id, traveller_id)
);

/*DROP TABLE smile;*/

/*SELECT *
FROM smile;*/

INSERT INTO smile (photo_id, traveller_id)
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (2, 3),
       (2, 4),
       (2, 5),
       (3, 2),
       (3, 3),
       (3, 5),
       (4, 2),
       (4, 3),
       (4, 4),
       (5, 2),
       (5, 4),
       (5, 5),
       (6, 3),
       (6, 4),
       (7, 2),
       (7, 4),
       (7, 3),
       (8, 2),
       (8, 3),
       (8, 4),
       (8, 5),
       (9, 3),
       (9, 4),
       (10, 2),
       (10, 5),
       (11, 5),
       (12, 5),
       (12, 4),
       (13, 2),
       (14, 2),
       (14, 3),
       (14, 5),
       (15, 5),
       (16, 2),
       (16, 3),
       (18, 3),
       (19, 3),
       (20, 3),
       (21, 2),
       (21, 3),
       (21, 4),
       (22, 2),
       (23, 2),
       (23, 3),
       (23, 4),
       (24, 2),
       (25, 2),
       (25, 3),
       (25, 4),
       (26, 2),
       (26, 3),
       (26, 4),
       (28, 2),
       (29, 2),
       (32, 2),
       (32, 3),
       (32, 4),
       (32, 5),
       (33, 5),
       (33, 4),
       (34, 4),
       (35, 2),
       (35, 3),
       (35, 4),
       (36, 2),
       (36, 3),
       (37, 3),
       (37, 2),
       (37, 4),
       (38, 4),
       (39, 3),
       (39, 4),
       (40, 4),
       (40, 1),
       (40, 3),
       (40, 2),
       (41, 2),
       (42, 2),
       (43, 4),
       (43, 5),
       (45, 5),
       (46, 5),
       (47, 3),
       (47, 2);