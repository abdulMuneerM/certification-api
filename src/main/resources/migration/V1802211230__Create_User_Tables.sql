CREATE TABLE IF NOT EXISTS `user` (
  `id` CHAR(64) NOT NULL,
  `name` VARCHAR(256) NOT NULL,
  `created_at` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_index_name` (`name`),
  KEY `user_index_created_at` (`created_at`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `user_credential` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` CHAR(64) NOT NULL,
  `email` VARCHAR(128) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_credential_unique_user_id` (`user_id`),
  UNIQUE KEY `user_credential_unique_email` (`email`),
  KEY `user_credential_index_email_password` (`email`,`password`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(64) NOT NULL,
  `description` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_index_role` (`role`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` CHAR(64) NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_role_unique_user_role` (`user_id`,`role_id`),
  KEY `user_role_index_user_id` (`user_id`)
) ENGINE=InnoDB;

INSERT INTO role (id, role,description) VALUES
('1', 'ADMIN', 'Admin Role'),
('2', 'STUDENT', 'Student role');