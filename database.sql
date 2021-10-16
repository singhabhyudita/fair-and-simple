use fairnsimple;

create table if not exists student(registrationNo int primary key, firstName varchar(15),lastName
 varchar(15), emailID varchar(50), salt varchar(50),password varchar(50), lastActive
 datetime);
 
 create table if not exists teacher(teacherID int primary key, firstName varchar(15),lastName
 varchar(15), emailID varchar(50), salt varchar(50),password varchar(50), lastActive
 datetime);
 
 create table if not exists course(courseID int primary key, courseCode varchar(8), courseName
 varchar(20), teacherID int, foreign key (teacherID) references teacher(teacherID)
 on delete set null);
  
create table if not exists enrollment(courseID int, registrationNo int, foreign key (courseID) 
references course(courseID) on delete cascade, foreign key (registrationNo) 
references student(registrationNo) on delete cascade);

--  create table exam(examID int primary key, courseID int, proctorID int,
--  title varchar(20),description varchar(250), maximumMarks int, 
--  questionPaper varchar(50), answerKey varchar(50), startTime datetime,
--  endTime datetime, foreign key (courseID) references course(courseID) 
--  on delete cascade, foreign key (proctorID) references teacher(teacherID) on 
--  delete set null);

 create table if not exists exam(examID int primary key AUTO_INCREMENT, courseID int, proctorID int,
 title varchar(20), description varchar(250), maximumMarks int, startTime datetime,
 endTime datetime, foreign key (courseID) references course(courseID) on delete cascade,
 foreign key (proctorID) references teacher(teacherID) on delete set null);

create table if not exists exam_questions(questionID int primary key AUTO_INCREMENT, examID int,
question varchar(200), optionA varchar(200), optionB varchar(200), optionC varchar(200),
optionD varchar(200), correctOption int, foreign key (examID) references exam(examID) on delete cascade);
 
create table if not exists submission(submissionID int primary key AUTO_INCREMENT, registrationNo int, examID int,
questionID int, selectedOption int, foreign key (examID) references exam(examID) on delete set null,
foreign key (registrationNo) references student(registrationNo) on delete cascade);

create table if not exists result(resultID int primary key AUTO_INCREMENT, registrationNo int,
examID int, marksObtained int, foreign key (registrationNo) references student(registrationNo) on delete cascade,
foreign key (examID) references exam(examID) on delete set null);
