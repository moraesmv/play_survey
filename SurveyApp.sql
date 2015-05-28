-- Valentina Studio --
-- MySQL dump --
-- ---------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
-- ---------------------------------------------------------


-- CREATE TABLE "Users" ------------------------------------
CREATE TABLE `Users` ( 
	`id` Int( 255 ) UNSIGNED ZEROFILL AUTO_INCREMENT NOT NULL COMMENT 'id to distinguishe each user', 
	`email` Text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'email for ligin', 
	`passwd` VarChar( 255 ) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'passwd' COMMENT 'password for login', 
	`name` Text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'name of user', 
	`accesslevel` Int( 255 ) UNSIGNED ZEROFILL NOT NULL DEFAULT '000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001' COMMENT 'level of user interaction with the system', 
	`json` Text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'json values here',
	 PRIMARY KEY ( `id` )
 )
CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT 'store user information for accessing the application'
ENGINE = InnoDB
AUTO_INCREMENT = 1;
-- ---------------------------------------------------------


-- CREATE TABLE "survey" -----------------------------------
CREATE TABLE `survey` ( 
	`id` Int( 255 ) UNSIGNED ZEROFILL AUTO_INCREMENT NOT NULL COMMENT 'to differentiate the surveys', 
	`userid` Int( 255 ) NOT NULL COMMENT 'who created the surveys', 
	`json` Text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'json values here',
	 PRIMARY KEY ( `id` )
 )
CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT 'to store the surveys created'
ENGINE = InnoDB
AUTO_INCREMENT = 1;
-- ---------------------------------------------------------


-- CREATE TABLE "template" ---------------------------------
CREATE TABLE `template` ( 
	`id` Int( 255 ) UNSIGNED ZEROFILL AUTO_INCREMENT NOT NULL COMMENT 'to distinguishe templates', 
	`user id` Int( 255 ) NOT NULL COMMENT 'the user that created the template', 
	`json` Text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'json values here',
	 PRIMARY KEY ( `id` )
 )
CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT 'templates to be used to create surveys'
ENGINE = InnoDB
AUTO_INCREMENT = 1;
-- ---------------------------------------------------------


-- CREATE TABLE "results" ----------------------------------
CREATE TABLE `results` ( 
	`id` Int( 255 ) UNSIGNED ZEROFILL AUTO_INCREMENT NOT NULL COMMENT 'to discriminate results', 
	`surveyid` Int( 255 ) NOT NULL COMMENT 'to link to a unique survey', 
	`json` Text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'json values here',
	 PRIMARY KEY ( `id` )
 )
CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT 'save the results of the surveys'
ENGINE = InnoDB
AUTO_INCREMENT = 1;
-- ---------------------------------------------------------


-- Dump data of "Users" ------------------------------------
-- ---------------------------------------------------------


-- Dump data of "survey" -----------------------------------
-- ---------------------------------------------------------


-- Dump data of "template" ---------------------------------
-- ---------------------------------------------------------


-- Dump data of "results" ----------------------------------
-- ---------------------------------------------------------


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
-- ---------------------------------------------------------


