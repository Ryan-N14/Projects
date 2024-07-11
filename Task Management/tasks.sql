-- Ryan Nguyen
-- tasks.sql
-- Creates the database and inserts tables
-- Inserting data into tables

create database nguyen_tasks;
use nguyen_tasks;

-- Creating Tables
create table users(
    user_id INTEGER AUTO_INCREMENT,
    user_name VARCHAR(50),
    password VARCHAR(12),
    name VARCHAR(50),
    date_joined DATE,
    PRIMARY KEY(user_id)
);

create table tasks(
    task_id INTEGER AUTO_INCREMENT,
    user_id INTEGER,
    user_task VARCHAR(50),
    completed BOOLEAN,
    due_date DATE,

    PRIMARY KEY(task_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id)
);


insert into users (user_name, password, name, date_joined)
    VALUES ("nguyenrr", "waterMelon21", "Ryan Nguyen","2024-04-23");

insert into tasks (user_id, user_task, completed, due_date)
    VALUES (1, "Conservation Exam",false, "2024-05-03"), (1, "Quizzam", false, "2024-05-01");