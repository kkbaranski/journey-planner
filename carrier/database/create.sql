create table if not exists public.timetable (
	course_id int,
	from_city varchar(100),
	to_city varchar(100),
	start_time time,
	stop_time time check( stop_time > start_time )
);

-- KURS 1 --
insert into timetable values(1,'Zakopane','Krakow','8:00','9:34');
insert into timetable values(1,'Krakow','Katowice','9:36','10:39');
insert into timetable values(1,'Katowice','Czestochowa','11:00','12:56');
insert into timetable values(1,'Czestochowa','Lodz','12:56','14:46');
insert into timetable values(1,'Lodz','Gdansk','14:46','20:06');

-- KURS 2 --
insert into timetable values(2,'Zakopane','Krakow','8:07','9:32');
insert into timetable values(2,'Krakow','Warszawa','9:34','13:39');

-- KURS 3 --
insert into timetable values(3,'Krakow','Warszawa','6:03','9:33');