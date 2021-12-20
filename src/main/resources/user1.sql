/*
Navicat MySQL Data Transfer

Source Server         : qiuguan_aliyun_mysql
Source Server Version : 80018
Source Host           : rm-bp18s2vkm33kn6fncwo.mysql.rds.aliyuncs.com:3306
Source Database       : t_user

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2021-12-14 15:30:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user1
-- ----------------------------
DROP TABLE IF EXISTS `user1`;
CREATE TABLE `user1` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
