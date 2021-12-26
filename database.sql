use fairnsimple;

create table if not exists student(registrationNo int primary key, firstName varchar(15),lastName
 varchar(15), emailID varchar(50),password varchar(50), lastActive
 datetime, profilePic blob);
 
 create table if not exists teacher(teacherID int primary key, firstName varchar(15),lastName
 varchar(15), emailID varchar(50), password varchar(50), lastActive
 datetime, profilePic blob);
 
 create table if not exists course(courseID int primary key auto_increment, courseCode varchar(8), courseName
 varchar(20), courseDescription varchar(200), teacherID int, foreign key (teacherID) references teacher(teacherID)
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
 teacherID int, title varchar(20), description varchar(250), maximumMarks int, startTime datetime,
 endTime datetime, foreign key (courseID) references course(courseID) on delete cascade,
 foreign key (teacherID) references teacher(teacherID) on delete set null,
 foreign key (proctorID) references teacher(teacherID) on delete set null);

create table if not exists exam_questions(questionID int primary key AUTO_INCREMENT, examID int,
question varchar(200), optionA varchar(200), optionB varchar(200), optionC varchar(200),
optionD varchar(200), correctOption int, foreign key (examID) references exam(examID) on delete cascade);
 
create table if not exists submission(submissionID int primary key AUTO_INCREMENT, registrationNo int, examID int,
questionID int, selectedOption int, foreign key (examID) references exam(examID) on delete set null,
foreign key (registrationNo) references student(registrationNo) on delete cascade);

create table message(messageID int primary key auto_increment, senderID int, courseID int, text varchar(10000),
image longblob,sentAt timestamp, isStudent bool, foreign key (courseID) references fairnsimple.course(courseID));

create table answer_files(
examID int(11) references exam(examId),
registrationNo int(11) references student(registrationNo),
courseID int(11) references course(courseID),
answerPath varchar(200) not null);

create table exam_questions_marks(
registrationNo int(11) references student(registrationNo),
examID int(11) references exam(examId),
questionID int(11) references exam_questions(questionID),
correct int(11));

create table objective_response(
questionID int(11) references exam_questions(questionID),
registrationNo int(11) references student(registrationNo),
marked int(11),
examID int(11) references exam(examId));

create table proctor_port(
examID int(11) references exam(examID),
proctorPort int(11));

