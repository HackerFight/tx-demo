/*
Navicat MySQL Data Transfer

Source Server         : qiuguan_aliyun_mysql
Source Server Version : 80018
Source Database       : t_user

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2021-12-10 09:50:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `age` tinyint(4) DEFAULT NULL,
  `phone` char(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='测试表';
