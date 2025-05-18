CREATE TABLE IF NOT EXISTS students
(
id INT PRIMARY KEY AUTO_INCREMENT,
name varchar(100) NOT NULL,
kana_name varchar(100) NOT NULL,
nickname varchar(100),
email varchar(100) NOT NULL,
area varchar(100),
age int,
sex varchar(100),
remark TEXT,
is_deleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses
(
id INT PRIMARY KEY AUTO_INCREMENT,
student_id INT NOT NULL,
course_name varchar(100) NOT NULL,
course_start_at TIMESTAMP,
course_end_at TIMESTAMP
);