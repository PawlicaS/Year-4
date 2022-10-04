create table director(
    director_id int primary key,
    first_name varchar(30),
    last_name varchar(30),
    active int
);

create table movie(
    movie_id int primary key,
    title varchar(100),
    release_year int,
    takings int,
    director_id int,
    foreign key (director_id) references director(director_id) on delete cascade
);