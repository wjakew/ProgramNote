/*
programmer Jakub Wawak
all rights reserved
kubawawak@gmail.com
version (from schema) v1.0
sql script for reloading database to test state
*/

/*
DOPPING ALL TABLES
*/
drop table if exists HASHTAG;
drop table if exists NOTE;
drop table if exists CONTENT;
drop table if exists CONFIGURATION;
drop table if exists LOG;
drop table if exists USER_INPUT;
drop table if exists USER_INFO;
/*
setting all tables
*/
/*
Table USER
contains info about user
DEPENDENCES: NOTHING
*/
CREATE TABLE USER_INFO
(
	user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50),
    user_surname VARCHAR(50),
    user_login VARCHAR(50),
    user_password VARCHAR (50)
);
/*
Table USER_INPUT
contains all user inputs from the console
DEPENDENCES: USER_INFO
*/
CREATE TABLE USER_INPUT
(
	user_input_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    user_input_text VARCHAR(40),
	CONSTRAINT fk_user_input FOREIGN KEY (user_id) REFERENCES USER_INFO(user_id)
);
/*
Table LOG
contains all log from the session
DEPENDENCES: USER_INFO
*/
CREATE TABLE LOG
(
	log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    log_data VARCHAR(150),
    CONSTRAINT fk_log FOREIGN KEY (user_id) REFERENCES USER_INFO(user_id)
);
/*
Table CONFIGURATION
contains configuration data from user
DEPENDENCES USER_INFO
*/
CREATE TABLE CONFIGURATION
(
	configuration_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    configuration_date VARCHAR(40),
    configuration_debug INT,
    configuration_name VARCHAR(30),
    configuration_gui INT,
    configuration_checksum VARCHAR(40),
    configuration_ip VARCHAR(40),
    CONSTRAINT fk_configuration FOREIGN KEY (user_id) REFERENCES USER_INFO(user_id)
);
/*
Table CONTENT
stores info about content of the note
DEPENDENCES: NULL
*/
CREATE TABLE CONTENT
(
	content_id INT AUTO_INCREMENT PRIMARY KEY,
    content_note TEXT
);
/*
Table NOTE
stores info about notes
DEPENDENCES: USER_INFO,CONTENT
*/
CREATE TABLE NOTE
(
	note_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    content_id INT,
    note_date VARCHAR(30),
    note_checksum VARCHAR(30),
    note_title VARCHAR(30),
    CONSTRAINT fk_note_1 FOREIGN KEY (user_id) REFERENCES USER_INFO(user_id),
    CONSTRAINT fk_note_2 FOREIGN KEY (content_id) REFERENCES CONTENT(content_id)
);
/*
Table HASHTAG
stores all hashtag used in the program
DEPENDENCES: USER_INFO,NOTE
*/
CREATE TABLE HASHTAG
(
	hashtag_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    note_id INT,
    hashtag_name VARCHAR(30),
    CONSTRAINT fk_hashtag_1 FOREIGN KEY (user_id) REFERENCES USER_INFO(user_id),
    CONSTRAINT fk_hashtag_2 FOREIGN KEY (note_id) REFERENCES NOTE(note_id)
);
/*
Table SHARE_HISTORY
stores history of shares, this table is always searched of new not done shares
DEPENDENCES: USER_INFO,NOTE
*/
CREATE TABLE SHARE_HISTORY
(
	share_history_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id_from INT,
    user_id_to INT,
    note_id INT,
    share_history_date VARCHAR(30),
    share_history_done INT,
    share_type INT,
    CONSTRAINT fk_sh_1 FOREIGN KEY (user_id_from) REFERENCES USER_INFO(user_id),
    CONSTRAINT fk_sh_2 FOREIGN KEY (user_id_to) REFERENCES USER_INFO(user_id),
    CONSTRAINT fk_sh_3 FOREIGN KEY (note_id) REFERENCES NOTE(note_id)
);
 /*
 Table ADDONS
 stores useful data for the program
 */
CREATE TABLE ADDONS
(	
	addons_id INT AUTO_INCREMENT PRIMARY KEY,
    addons_n1 VARCHAR(100),
    addons_n2 VARCHAR(100),
    addons_n3 VARCHAR(100),
    addons_n4 VARCHAR(100),
    addons_n5 VARCHAR(100)
);
INSERT INTO USER_INFO
(user_name,user_surname,user_login,user_password) 
VALUES 
("Jakub","Wawak","wjakew","test");
INSERT INTO ADDONS
(addons_n1,addons_n2,addons_n3,addons_n4,addons_n5)
VALUES
("main.tes.instruments@gmail.com","m5hdmM0I*bSjnHHyZX7f5QGs7PcZfT4j#YP$^i#y5yJ10!5HH@9$n0RI@2o1Dks$1gjxFA9","","","");
