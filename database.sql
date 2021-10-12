use fairnsimple;

create table student(registrationNo int primary key, firstName varchar(15),lastName
 varchar(15), emailID varchar(50), salt varchar(50),password varchar(50), lastActive
 datetime);
 
 create table teacher(teacherID int primary key, firstName varchar(15),lastName
 varchar(15), emailID varchar(50), salt varchar(50),password varchar(50), lastActive
 datetime);
 
 create table course(courseID int primary key, courseCode varchar(8), courseName
 varchar(20), teacherID int, foreign key (teacherID) references teacher(teacherID)
 on delete set null);
  
create table enrollment(courseID int, registrationNo int, foreign key (courseID) 
references course(courseID) on delete cascade, foreign key (registrationNo) 
references student(registrationNo) on delete cascade);

 create table exam(examID int primary key, courseID int, proctorID int,
 title varchar(20),description varchar(250), maximumMarks int, 
 questionPaper varchar(50), answerKey varchar(50), startTime datetime,
 endTime datetime, foreign key (courseID) references course(courseID) 
 on delete cascade, foreign key (proctorID) references teacher(teacherID) on 
 delete set null);
 
create table submission(submissionID int primary key, registrationNo int, examID int,
answers varchar(50), marks int, grade varchar(2), foreign key (examID) 
references exam(examID) on delete set null, foreign key (registrationNo) 
references student(registrationNo) on delete cascade);

