create table if not exists timetable (
	course_id int,
	from_city varchar(100),
	to_city varchar(100),
	start_time time,
	stop_time time
);

-- KURS 1 --
insert into timetable values(1,'Zakopane','Krakow','08:00','09:34');
insert into timetable values(1,'Krakow','Katowice','09:36','10:39');
insert into timetable values(1,'Katowice','Czestochowa','11:00','12:56');
insert into timetable values(1,'Czestochowa','Lodz','12:56','14:46');
insert into timetable values(1,'Lodz','Gdansk','14:46','20:06');

-- KURS 2 --
insert into timetable values(2,'Zakopane','Krakow','08:07','09:32');
insert into timetable values(2,'Krakow','Warszawa','09:34','13:39');

-- KURS 3 --
insert into timetable values(3,'Krakow','Warszawa','06:03','09:33');
