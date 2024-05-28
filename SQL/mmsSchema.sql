-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mms
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mms
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mms` DEFAULT CHARACTER SET utf8 ;
USE `mms` ;

-- -----------------------------------------------------
-- Table `mms`.`admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`admin` (
  `admin_id` INT NOT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`admin_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`mess`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`mess` (
  `mess_id` INT NOT NULL,
  `mess_name` VARCHAR(45) NULL DEFAULT NULL,
  `capacity` INT NULL DEFAULT NULL,
  PRIMARY KEY (`mess_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`student` (
  `student_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `hostel` VARCHAR(45) NULL DEFAULT NULL,
  `room` INT NULL DEFAULT NULL,
  `mess_id` INT NULL DEFAULT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`student_id`),
  INDEX `mess_id_idx` (`mess_id` ASC) VISIBLE,
  CONSTRAINT `mess_id`
    FOREIGN KEY (`mess_id`)
    REFERENCES `mms`.`mess` (`mess_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`feedback`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`feedback` (
  `feedback_id` INT NOT NULL AUTO_INCREMENT,
  `student_id` INT NOT NULL,
  `message` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`feedback_id`),
  INDEX `student_id_idx` (`student_id` ASC) VISIBLE,
  CONSTRAINT `st_id`
    FOREIGN KEY (`student_id`)
    REFERENCES `mms`.`student` (`student_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`leave_apply`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`leave_apply` (
  `leave_id` INT NOT NULL AUTO_INCREMENT,
  `student_id` INT NOT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  `status` ENUM('pending', 'approved', 'rejected') NULL DEFAULT NULL,
  `message` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`leave_id`),
  INDEX `student_id_idx` (`student_id` ASC) VISIBLE,
  CONSTRAINT `fk_leave_student_id`
    FOREIGN KEY (`student_id`)
    REFERENCES `mms`.`student` (`student_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 24
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`leftover`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`leftover` (
  `mess_id` INT NOT NULL,
  `time` ENUM('breakfast', 'lunch', 'dinner') NOT NULL,
  `total_prepared` INT NULL DEFAULT NULL,
  `total_attended` INT NULL DEFAULT NULL,
  `total_leftover` INT NULL DEFAULT NULL,
  PRIMARY KEY (`mess_id`, `time`),
  CONSTRAINT `fk_leftover_mess_id`
    FOREIGN KEY (`mess_id`)
    REFERENCES `mms`.`mess` (`mess_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`manager`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`manager` (
  `manager_id` INT NOT NULL,
  `mess_id` INT NULL DEFAULT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`manager_id`),
  INDEX `mess_id_idx` (`mess_id` ASC) VISIBLE,
  CONSTRAINT `fk_manager_mess_id`
    FOREIGN KEY (`mess_id`)
    REFERENCES `mms`.`mess` (`mess_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`meal_owner`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`meal_owner` (
  `meal_id` INT NOT NULL,
  `student_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`meal_id`),
  INDEX `student_id_idx` (`student_id` ASC) VISIBLE,
  CONSTRAINT `student_id`
    FOREIGN KEY (`student_id`)
    REFERENCES `mms`.`student` (`student_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`meal_booking`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`meal_booking` (
  `meal_id` INT NOT NULL,
  `time` ENUM('breakfast', 'lunch', 'dinner') NOT NULL,
  `booked` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`meal_id`, `time`),
  CONSTRAINT `meal_id`
    FOREIGN KEY (`meal_id`)
    REFERENCES `mms`.`meal_owner` (`meal_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`requests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`requests` (
  `student_id` INT NULL DEFAULT NULL,
  `meal_id` INT NOT NULL,
  `time` ENUM('breakfast', 'lunch', 'dinner') NOT NULL,
  `food_package` INT NULL DEFAULT '0',
  `addon` INT NULL DEFAULT '0',
  PRIMARY KEY (`meal_id`, `time`),
  INDEX `fk_requests_student_id` (`student_id` ASC) VISIBLE,
  CONSTRAINT `fk_requests_meal_owner_`
    FOREIGN KEY (`meal_id`)
    REFERENCES `mms`.`meal_owner` (`meal_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_requests_student_id`
    FOREIGN KEY (`student_id`)
    REFERENCES `mms`.`student` (`student_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mms`.`responds`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mms`.`responds` (
  `feedback_id` INT NOT NULL,
  `admin_id` INT NULL DEFAULT NULL,
  `remark` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`feedback_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
