
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


-- Insert Interests -- 
INSERT INTO `stack-o`.`interest` (`description`, `name`) VALUES ('Make computers think', 'Machine Learning');
INSERT INTO `stack-o`.`interest` (`description`, `name`) VALUES ('Build the next generation of tech to power the web', 'Web Technologies');
INSERT INTO `stack-o`.`interest` (`description`, `name`) VALUES ('Create tools to aid software design and implementation', 'Software Engineering');
INSERT INTO `stack-o`.`interest` (`description`, `name`) VALUES ('Connect every device to a single ecosystem', 'Internet of Things');
INSERT INTO `stack-o`.`interest` (`description`, `name`) VALUES ('Build mobile apps fast and reliably', 'Mobile App Development');


--  Add SDGs --
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('No one should be poor when there is enough wealth for all humanity', 'No Poverty', '1');
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('No human should die of hunger', 'No Hunger', '2');
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('Everyone should have access to good healthcare and not die needlessly', 'Good Health and Well-Being', '3');
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('Good Schools and education for all', 'Quality Education', '4');
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('Males and Females, let\'s make then more equal', 'Gender Equality', '5');
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('Lets have clean water everywhere', 'Clean Sanitation', '6');
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('Energy should not be expensive and should not harm the evironment either', 'Affordable and Clean Energy', '7');
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('We should all have access to a good job we can thrive on', 'Decent Work and Economic Growth', '8');
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('Let\'s innovate', 'Infrustructure', '9');
INSERT INTO `stack-o`.`sdg` (`description`, `name`, `number`) VALUES ('Let\'s give everyone a fair shot at life', 'Reduced Inequalities', '10');




