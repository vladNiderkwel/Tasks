CREATE SCHEMA IF NOT EXISTS mydb;
SET SCHEMA mydb;

DROP TABLE IF EXISTS Students;

CREATE TABLE Students (
    id INT auto_increment,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    birth DATE NOT NULL,
    class VARCHAR(100) NOT NULL
);

INSERT INTO Students(name, surname, lastname, birth, class)
    VALUES('Петя', 'Петячкин', 'Петрович', '2000-01-21', 'Группа 1');

INSERT INTO Students(name, surname, lastname, birth, class)
    VALUES('Вася', 'Васечкин', 'Васильевич', '1999-03-20', 'Группа 2');

INSERT INTO Students(name, surname, lastname, birth, class)
    VALUES('Павел', 'Павлов', 'Павлович', '2002-04-25', 'Группа 3');