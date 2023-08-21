CREATE TABLE `account`
(
    `id`            int          NOT NULL,
    `user_id`       int          NOT NULL,
    `user_account`  varchar(100) NOT NULL,
    `register_date` datetime     NOT NULL,
    `role`          varchar(50)  NOT NULL DEFAULT 'USER',
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`, `user_account`, `register_date`, `role`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;