create table school(
    school_id int primary key,
    name varchar(30),
    number int
);

create table subject(
    subject_id int primary key,
    name varchar(30),
    email varchar(30),
    school_id int,
    foreign key (school_id) references school(school_id) on delete cascade
);