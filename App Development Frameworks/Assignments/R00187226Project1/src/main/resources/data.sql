insert into director(director_id, first_name, last_name, active) values
(1, 'Quentin', 'Tarantino', 1),
(2, 'Steven', 'Spielberg', 1),
(3, 'Peter', 'Jackson', 1),
(4, 'Alfred', 'Hitchcock', 0);

insert into movie(movie_id, title, release_year, takings, director_id) values
(1, 'Inglourious Basterds', 2009, 321457747, 1),
(2, 'Django Unchained', 2012, 426074373, 1),
(3, 'Jurassic Park', 1993, 1109802321, 2),
(4, 'The Lord of the Rings: The Fellowship of the Ring', 2001, 898094742, 3),
(5, 'The Hobbit: An Unexpected Journey', 2012, 1017030651, 3),
(6, 'Psycho', 1960, 32052925, 4);