INSERT INTO USER_INFO
(user_name,user_surname,user_login,user_password) 
VALUES 
("Jakub","Wawak","wjakew","test");

INSERT INTO USER_INPUT
(user_id,user_input_text)
VALUES
(1,"list");
INSERT INTO USER_INPUT
(user_id,user_input_text)
VALUES
(1,"show -0");

INSERT INTO LOG
(user_id,log_data)
VALUES
(1,"test logowania do bazy danych");
INSERT INTO LOG
(user_id,log_data)
VALUES
(1,"drugi test logowania do bazy danych");

INSERT INTO CONFIGURATION
(user_id,configuration_date,configuration_debug,configuration_name,configuration_gui,configuration_checksum,configuration_ip)
VALUES
(1,"29/11/1996",1,"jakub",1,"jakub-checksum","127.0.1.0");

INSERT INTO CONTENT
(content_note)
VALUES
("witaj w mojej kuchni");

INSERT INTO NOTE
(user_id,content_id,note_date,note_checksum,note_title)
VALUES
(1,1,"29/11/1996","jakub-checksum-note","witaj w kuchni");

INSERT INTO HASHTAG
(user_id,note_id,hashtag_name)
VALUES
(1,1,"testowyhashtag");


