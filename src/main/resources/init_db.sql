/*
Navicat MySQL Data Transfer

Source Server         : 本机MYSQL-低权限
Source Server Version : 50610
Source Host           : 10.10.50.57:3305
Source Database       : stock_db

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2017-07-17 18:26:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_stocks
-- ----------------------------
DROP TABLE IF EXISTS `tb_stocks`;
CREATE TABLE `tb_stocks` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `code` varchar(12) DEFAULT NULL,
  `stock_exchange` varchar(16) DEFAULT NULL,
  `stock_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_stocks
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `usertype` varchar(2) DEFAULT NULL COMMENT '用户类型',
  `enabled` int(2) DEFAULT NULL COMMENT '是否可用',
  `realname` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `qq` varchar(14) DEFAULT NULL COMMENT 'QQ',
  `email` varchar(100) DEFAULT NULL,
  `tel` varchar(255) DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('6', 'zhkm_cb@163.com', '$2a$04$Yiesu5cPnI4lj6/4cC5D7e81QNiAFtIgWaiK5YwMDzNNisXdRu6Uy', null, null, null, null, null, null);
INSERT INTO `tb_user` VALUES ('7', 'zhkm_cb@126.com', '$2a$04$A.XKMCLNEv1myEopaQxi1.Rjmb68B14rVYc44HTvugTP52QLChhaW', null, null, null, null, null, null);
