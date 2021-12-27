use fairnsimple;
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194039','Abhyudita','Singh','abhyudita.singh@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592', STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194084','Utkarsh','Rai','utkarsh.rai@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194116','Saurabh','Singh','saurabh.singh@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194008','Aditya','Darji','aditya.rajudarji@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194009','Sanskar','Jain','sanskar.jain@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194150','Hitkarsh','Singh','hitkarsh.singh@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194104','Guha','Sadasivam','guha.sadasivam@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194175','Ishan','Gupta','ishan.gupta@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194181','Bhavi','Khator','bhavi.khator@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194204','Mohit','Pandey','mohit.p@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
INSERT INTO student (registrationNo,firstName,lastName,emailID,password,profilePic) VALUES  ('20194134','Radhika','Gupta','radhika.gupta@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM);
 


INSERT INTO teacher (teacherID,firstName,lastName,emailID,password,profilePic) VALUES  ('5103','Shashwati','Banerjee','shashwati@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592', STREAM  );
INSERT INTO teacher (teacherID,firstName,lastName,emailID,password,profilePic) VALUES  ('5105','Dinesh','Kumar','dinesh.kumar@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM );
INSERT INTO teacher (teacherID,firstName,lastName,emailID,password,profilePic) VALUES  ('5102','Dinesh','Singh','dinesh_singh@mnnit.ac.in','5d41402abc4b2a76b9719d911017c592',STREAM );

INSERT INTO course (courseName, courseDescription, courseCode, teacherID) VALUES ('Operations Systems', 'In this course students will study the basic facilities provided in modern operating systems.', '633n95BD', '5102');
INSERT INTO course (courseName, courseDescription, courseCode, teacherID) VALUES ('Database Management', 'In this course we will study the basic functions and capabilities of database management systems.', 'Utf3HBVe', '5103');
INSERT INTO course (courseName, courseDescription, courseCode, teacherID) VALUES ('Operations Research', 'In this course students will study some common operations research models and algorithms.', 'uUVkEOtX', '5105');


INSERT INTO enrollment VALUES (1,20194039);
INSERT INTO enrollment VALUES (2,20194039);
INSERT INTO enrollment VALUES (3,20194039);
INSERT INTO enrollment VALUES (1,20194084);
INSERT INTO enrollment VALUES (2,20194084);
INSERT INTO enrollment VALUES (3,20194084);
INSERT INTO enrollment VALUES (1,20194116);
INSERT INTO enrollment VALUES (2,20194116);
INSERT INTO enrollment VALUES (3,20194116);
INSERT INTO enrollment VALUES (1,20194008);
INSERT INTO enrollment VALUES (2,20194008);
INSERT INTO enrollment VALUES (3,20194008);
INSERT INTO enrollment VALUES (1,20194009);
INSERT INTO enrollment VALUES (2,20194009);
INSERT INTO enrollment VALUES (3,20194009);
INSERT INTO enrollment VALUES (1,20194150);
INSERT INTO enrollment VALUES (2,20194150);
INSERT INTO enrollment VALUES (2,20194104);
INSERT INTO enrollment VALUES (3,20194104);
INSERT INTO enrollment VALUES (1,20194175);
INSERT INTO enrollment VALUES (3,20194175);
INSERT INTO enrollment VALUES (1,20194181);
INSERT INTO enrollment VALUES (2,20194204);
