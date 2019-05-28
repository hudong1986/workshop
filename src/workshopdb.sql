/*
Navicat MySQL Data Transfer

Source Server         : localMysql
Source Server Version : 50617
Source Host           : localhost:3306
Source Database       : workshopdb

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2017-12-04 14:02:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `attendance`
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `dept_name` varchar(20) NOT NULL,
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:正常上班   1:请假  2:加班 ',
  `beg_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `phone` (`phone`),
  KEY `user_name` (`user_name`),
  KEY `up_down_id` (`up_down_id`),
  KEY `beg_time` (`beg_time`),
  KEY `dept_name` (`end_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of attendance
-- ----------------------------

-- ----------------------------
-- Table structure for `finish_statistic`
-- ----------------------------
DROP TABLE IF EXISTS `finish_statistic`;
CREATE TABLE `finish_statistic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `dept_name` varchar(20) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_name` varchar(20) NOT NULL,
  `num` int(11) NOT NULL,
  `get_money` decimal(10,2) NOT NULL,
  `addtime` datetime NOT NULL,
  `add_user_id` int(11) NOT NULL,
  `add_user_name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `phone` (`phone`),
  KEY `up_down_id` (`up_down_id`),
  KEY `product_id` (`product_id`),
  KEY `addtime` (`addtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finish_statistic
-- ----------------------------

-- ----------------------------
-- Table structure for `goods_in_out`
-- ----------------------------
DROP TABLE IF EXISTS `goods_in_out`;
CREATE TABLE `goods_in_out` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL,
  `product_name` varchar(20) NOT NULL,
  `batchNo` varchar(30) NOT NULL,
  `num` int(11) NOT NULL COMMENT '数量',
  `total` decimal(10,2) NOT NULL,
  `add_phone` varchar(11) NOT NULL,
  `add_user_name` varchar(20) NOT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `dept_name` varchar(20) NOT NULL,
  `direction` varchar(20) NOT NULL DEFAULT '出库' COMMENT '出库、入库等',
  `out_business_name` varchar(50) DEFAULT NULL,
  `out_business_address` varchar(50) DEFAULT NULL,
  `out_business_phone` varchar(50) DEFAULT NULL,
  `addtime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `batchNo` (`batchNo`),
  KEY `up_down_id` (`up_down_id`),
  KEY `addtime` (`addtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods_in_out
-- ----------------------------

-- ----------------------------
-- Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(20) NOT NULL,
  `out_in` decimal(6,2) NOT NULL DEFAULT '0.00' COMMENT '按记件考虑每一个提成多少元',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '商品类别 0：自生产商品   1：进货商品',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_dept`
-- ----------------------------
DROP TABLE IF EXISTS `pt_dept`;
CREATE TABLE `pt_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(20) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `up_down_id` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1010 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_dept
-- ----------------------------
INSERT INTO `pt_dept` VALUES ('1000', '总运营', '1000', '1000-');
INSERT INTO `pt_dept` VALUES ('1001', '人事部', '1000', '1000-1001-');
INSERT INTO `pt_dept` VALUES ('1002', '财务部', '1000', '1000-1002-');
INSERT INTO `pt_dept` VALUES ('1003', '生产厂1', '1000', '1000-1003-');
INSERT INTO `pt_dept` VALUES ('1006', '车间1', '1003', '1000-1003-1006-');
INSERT INTO `pt_dept` VALUES ('1007', '车间2', '1003', '1000-1003-1007-');
INSERT INTO `pt_dept` VALUES ('1008', '车间3', '1003', '1000-1003-1008-');
INSERT INTO `pt_dept` VALUES ('1009', '车间4', '1003', '1000-1003-1009-');

-- ----------------------------
-- Table structure for `pt_role`
-- ----------------------------
DROP TABLE IF EXISTS `pt_role`;
CREATE TABLE `pt_role` (
  `role_code` varchar(20) NOT NULL,
  `role_name` varchar(20) NOT NULL,
  PRIMARY KEY (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_role
-- ----------------------------
INSERT INTO `pt_role` VALUES ('ADMIN', '管理员');
INSERT INTO `pt_role` VALUES ('FactoryManager', '厂长');
INSERT INTO `pt_role` VALUES ('FinanceAssistant', '财务助理');
INSERT INTO `pt_role` VALUES ('FinanceManager', '财务经理');
INSERT INTO `pt_role` VALUES ('HrAssistant', '人事助理');
INSERT INTO `pt_role` VALUES ('HrManager', '人事经理');
INSERT INTO `pt_role` VALUES ('ProductManager', '生产经理');
INSERT INTO `pt_role` VALUES ('TeamLeader', '组长');
INSERT INTO `pt_role` VALUES ('WorkshopEmployee', '普通员工');
INSERT INTO `pt_role` VALUES ('WorkshopManager', '车间主任');

-- ----------------------------
-- Table structure for `pt_user`
-- ----------------------------
DROP TABLE IF EXISTS `pt_user`;
CREATE TABLE `pt_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL,
  `user_pwd` varchar(32) NOT NULL,
  `body_id` varchar(20) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `role_code` varchar(20) NOT NULL,
  `role_name` varchar(20) NOT NULL,
  `dept_id` int(11) NOT NULL,
  `dept_name` varchar(20) NOT NULL,
  `real_name` varchar(20) NOT NULL,
  `pic_url` varchar(200) DEFAULT '',
  `sex` bit(1) NOT NULL DEFAULT b'1' COMMENT '1男 0女',
  `add_time` datetime NOT NULL,
  `leave_time` datetime DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0在职 1离职',
  `up_down_id` varchar(50) NOT NULL,
  `job_name` varchar(20) NOT NULL,
  `fix_money` int(11) NOT NULL COMMENT '固定或基本工资',
  `work_days` tinyint(4) NOT NULL DEFAULT '22' COMMENT '固定工资的天数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  KEY `role_code` (`role_code`),
  KEY `dept_id` (`dept_id`),
  KEY `state` (`state`),
  KEY `up_down_id` (`up_down_id`),
  CONSTRAINT `pt_user_ibfk_1` FOREIGN KEY (`role_code`) REFERENCES `pt_role` (`role_code`),
  CONSTRAINT `pt_user_ibfk_2` FOREIGN KEY (`dept_id`) REFERENCES `pt_dept` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_user
-- ----------------------------
INSERT INTO `pt_user` VALUES ('1', '18782404805', 'E10ADC3949BA59ABBE56E057F20F883E', null, null, 'HrManager', '人事经理', '1001', '人事部', '王经理', '50945663-d5e4-445f-8bf9-ed8dd9cccf90.jpg', '', '2017-07-02 11:03:39', null, '2017-11-06 11:38:19', '0', '1000-1001-', '人事经理', '5000', '22');
INSERT INTO `pt_user` VALUES ('2', '13799234823', 'E10ADC3949BA59ABBE56E057F20F883E', null, null, 'WorkshopEmployee', '普通员工', '1006', '车间1', '李四', 'ed0d993c-4181-4acc-a07d-4c045d35441a.jpg', '', '2017-07-04 22:52:24', null, '2017-08-06 12:41:39', '0', '1000-1003-1006-', '电工', '1000', '22');
INSERT INTO `pt_user` VALUES ('3', '18242342322', 'E10ADC3949BA59ABBE56E057F20F883E', null, null, 'WorkshopManager', '车间主任', '1006', '车间1', '王一', 'default_header.png', '', '2017-07-08 21:53:55', null, '2017-08-08 21:54:11', '0', '1000-1003-1006-', '车间主任', '4000', '22');

-- ----------------------------
-- Table structure for `reward`
-- ----------------------------
DROP TABLE IF EXISTS `reward`;
CREATE TABLE `reward` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `dept_name` varchar(20) NOT NULL,
  `money` decimal(10,2) NOT NULL,
  `remark` varchar(100) NOT NULL,
  `obj_month` varchar(10) NOT NULL,
  `add_user_phone` varchar(11) NOT NULL,
  `add_user_name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `phone` (`phone`),
  KEY `up_down_id` (`up_down_id`),
  KEY `obj_month` (`obj_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reward
-- ----------------------------

-- ----------------------------
-- Table structure for `salary`
-- ----------------------------
DROP TABLE IF EXISTS `salary`;
CREATE TABLE `salary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `dept_name` varchar(20) NOT NULL,
  `fix_money` decimal(10,2) NOT NULL DEFAULT '0.00',
  `fix_money_des` varchar(500) DEFAULT NULL,
  `achievement` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '绩效或记件提成',
  `achievement_des` varchar(2000) DEFAULT NULL,
  `holiday_reduce` decimal(10,2) NOT NULL DEFAULT '0.00',
  `holiday_des` varchar(2000) DEFAULT NULL,
  `add_work` decimal(10,2) NOT NULL DEFAULT '0.00',
  `add_work_des` varchar(2000) DEFAULT NULL,
  `fuli_money` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '福利补助',
  `fuli_des` varchar(2000) DEFAULT NULL,
  `reward_money` decimal(10,2) NOT NULL DEFAULT '0.00',
  `reward_des` varchar(2000) DEFAULT NULL,
  `total` decimal(10,2) NOT NULL DEFAULT '0.00',
  `month` varchar(10) NOT NULL,
  `other_reduce` decimal(10,2) NOT NULL DEFAULT '0.00',
  `other_reduce_des` varchar(500) DEFAULT NULL,
  `other_add` decimal(10,2) NOT NULL DEFAULT '0.00',
  `other_add_des` varchar(500) DEFAULT NULL,
  `addtime` datetime NOT NULL,
  `add_user_id` int(11) NOT NULL,
  `add_user_name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `phone` (`phone`),
  KEY `up_down_id` (`up_down_id`),
  KEY `month` (`month`),
  CONSTRAINT `salary_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `pt_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of salary
-- ----------------------------

-- ----------------------------
-- Table structure for `system_param`
-- ----------------------------
DROP TABLE IF EXISTS `system_param`;
CREATE TABLE `system_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sys_key` varchar(50) NOT NULL,
  `sys_value` varchar(100) NOT NULL,
  `remark` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_key` (`sys_key`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_param
-- ----------------------------
INSERT INTO `system_param` VALUES ('1', '全勤', '100', '每月没有请假');
INSERT INTO `system_param` VALUES ('2', '交通补助', '10', '每天的交通补助');
INSERT INTO `system_param` VALUES ('3', '餐补', '10', '每天的餐补');
INSERT INTO `system_param` VALUES ('4', '加班工资', '20', '每小时元');
