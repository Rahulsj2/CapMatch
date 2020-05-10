
-- Insert user permission options --  
INSERT INTO `stack-o`.`permissions` (`name`) VALUES ('STUDENT');
INSERT INTO `stack-o`.`permissions` (`name`) VALUES ('FACULTY');
INSERT INTO `stack-o`.`permissions` (`name`) VALUES ('ADMIN');


-- Insert University Departments --  
INSERT INTO `stack-o`.`department` (`department_code`, `name`) VALUES ('CSIS', 'Computer Science & Information Systems');
INSERT INTO `stack-o`.`department` (`department_code`, `name`) VALUES ('BA', 'Business Administration');
INSERT INTO `stack-o`.`department` (`department_code`, `name`) VALUES ('ENG', 'Engineering');
INSERT INTO `stack-o`.`department` (`department_code`, `name`) VALUES ('HSS', 'Humanities & Social Sciences');


-- Insert Majors --  
INSERT INTO `stack-o`.`major` (`major_code`, `name`, `department_id`) VALUES ('CS', 'Computer Science', '1');
INSERT INTO `stack-o`.`major` (`major_code`, `name`, `department_id`) VALUES ('MIS', 'Management Information Systems', '1');
INSERT INTO `stack-o`.`major` (`major_code`, `name`, `department_id`) VALUES ('BA', 'Business Administration', '2');
INSERT INTO `stack-o`.`major` (`major_code`, `name`, `department_id`) VALUES ('CE', 'Computer Engineering', '3');
INSERT INTO `stack-o`.`major` (`major_code`, `name`, `department_id`) VALUES ('EE', 'Electrical Engineering', '3');
INSERT INTO `stack-o`.`major` (`major_code`, `name`, `department_id`) VALUES ('ME', 'Mechanical Engineering', '3');


