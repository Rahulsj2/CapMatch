
-- Insert user permission options --  
INSERT INTO `permissions` (`name`) VALUES ('STUDENT');
INSERT INTO `permissions` (`name`) VALUES ('FACULTY');
INSERT INTO `permissions` (`name`) VALUES ('ADMIN');


-- Insert University Departments --  
INSERT INTO `department` (`department_code`, `name`) VALUES ('CSIS', 'Computer Science & Information Systems');
INSERT INTO `department` (`department_code`, `name`) VALUES ('BA', 'Business Administration');
INSERT INTO `department` (`department_code`, `name`) VALUES ('ENG', 'Engineering');
INSERT INTO `department` (`department_code`, `name`) VALUES ('HSS', 'Humanities & Social Sciences');


-- Insert Majors --  
INSERT INTO `major` (`major_code`, `name`, `department_id`) VALUES ('CS', 'Computer Science', '1');
INSERT INTO `major` (`major_code`, `name`, `department_id`) VALUES ('MIS', 'Management Information Systems', '1');
INSERT INTO `major` (`major_code`, `name`, `department_id`) VALUES ('BA', 'Business Administration', '11');
INSERT INTO `major` (`major_code`, `name`, `department_id`) VALUES ('CE', 'Computer Engineering', '21');
INSERT INTO `major` (`major_code`, `name`, `department_id`) VALUES ('EE', 'Electrical Engineering', '21');
INSERT INTO `major` (`major_code`, `name`, `department_id`) VALUES ('ME', 'Mechanical Engineering', '21');


-- Insert Interests -- 
INSERT INTO `interest` (`description`, `name`) VALUES ('Make computers think', 'Machine Learning');
INSERT INTO `interest` (`description`, `name`) VALUES ('Build the next generation of tech to power the web', 'Web Technologies');
INSERT INTO `interest` (`description`, `name`) VALUES ('Create tools to aid software design and implementation', 'Software Engineering');
INSERT INTO `interest` (`description`, `name`) VALUES ('Connect every device to a single ecosystem', 'Internet of Things');
INSERT INTO `interest` (`description`, `name`) VALUES ('Build mobile apps fast and reliably', 'Mobile App Development');


--  Add SDGs --
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('No one should be poor when there is enough wealth for all humanity', 'No Poverty', '1');
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('No human should die of hunger', 'No Hunger', '2');
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('Everyone should have access to good healthcare and not die needlessly', 'Good Health and Well-Being', '3');
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('Good Schools and education for all', 'Quality Education', '4');
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('Males and Females, let\'s make then more equal', 'Gender Equality', '5');
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('Lets have clean water everywhere', 'Clean Sanitation', '6');
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('Energy should not be expensive and should not harm the evironment either', 'Affordable and Clean Energy', '7');
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('We should all have access to a good job we can thrive on', 'Decent Work and Economic Growth', '8');
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('Let\'s innovate', 'Infrustructure', '9');
INSERT INTO `sdg` (`description`, `name`, `number`) VALUES ('Let\'s give everyone a fair shot at life', 'Reduced Inequalities', '10');

