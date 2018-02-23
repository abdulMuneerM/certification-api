CREATE TABLE IF NOT EXISTS `course` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) NOT NULL,
  `cost` DECIMAL(9, 2)  NOT NULL,
  `created_at` BIGINT NOT NULL,
  `last_modified_at` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course_index_name` (`name`),
  KEY `course_index_created_at` (`created_at`),
  KEY `course_index_last_modified_at` (`last_modified_at`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `course_registration` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` CHAR(64) NOT NULL,
  `course_id` BIGINT  NOT NULL,
  `created_at` BIGINT NOT NULL,
  `result` TINYINT DEFAULT NULL,
  `last_modified_at` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course_registration_index_user_id` (`user_id`),
  KEY `course_registration_index_course_id` (`course_id`),
  KEY `course_registration_index_created_at` (`created_at`),
  KEY `course_registration_index_result` (`result`),
  KEY `course_index_last_modified_at` (`last_modified_at`)
) ENGINE=InnoDB;