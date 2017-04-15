CREATE TABLE `bms`.`book` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `isbn` VARCHAR(100) NOT NULL,
  `author` VARCHAR(45) NOT NULL,
  `press` VARCHAR(45) NOT NULL,
  `in_date` DATETIME NULL,
  `out_date` DATETIME NULL,
  `state` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

  INSERT INTO `bms`.`book` (`name`, `isbn`, `author`, `press`) VALUES ('极简SpringBoot教程', '88888888', '陈光剑', '电子工业出版社');

