/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : shop_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2019-01-12 20:54:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_buyinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_buyinfo`;
CREATE TABLE `t_buyinfo` (
  `buyId` int(11) NOT NULL auto_increment COMMENT '进货id',
  `productObj` int(11) NOT NULL COMMENT '进货商品',
  `shopObj` int(11) NOT NULL COMMENT '进货店铺',
  `supplierObj` int(11) NOT NULL COMMENT '供应商',
  `buyPrice` float NOT NULL COMMENT '进货单价',
  `buyCount` int(11) NOT NULL COMMENT '进货数量',
  `buyDate` varchar(20) default NULL COMMENT '进货日期',
  `managerObj` varchar(20) NOT NULL COMMENT '进货管理员',
  `buyMemo` varchar(500) default NULL COMMENT '进货备注',
  PRIMARY KEY  (`buyId`),
  KEY `productObj` (`productObj`),
  KEY `shopObj` (`shopObj`),
  KEY `supplierObj` (`supplierObj`),
  KEY `managerObj` (`managerObj`),
  CONSTRAINT `t_buyinfo_ibfk_1` FOREIGN KEY (`productObj`) REFERENCES `t_product` (`productId`),
  CONSTRAINT `t_buyinfo_ibfk_2` FOREIGN KEY (`shopObj`) REFERENCES `t_shop` (`shopId`),
  CONSTRAINT `t_buyinfo_ibfk_3` FOREIGN KEY (`supplierObj`) REFERENCES `t_supplier` (`supplierId`),
  CONSTRAINT `t_buyinfo_ibfk_4` FOREIGN KEY (`managerObj`) REFERENCES `t_manager` (`managerUserName`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_buyinfo
-- ----------------------------
INSERT INTO `t_buyinfo` VALUES ('1', '1', '1', '1', '260', '80', '2019-01-02', 'sp001', '111');
INSERT INTO `t_buyinfo` VALUES ('2', '1', '1', '1', '265', '20', '2019-01-12', 'sp001', '测试');
INSERT INTO `t_buyinfo` VALUES ('3', '2', '1', '1', '18', '90', '2019-01-12', 'sp001', '测试进货');

-- ----------------------------
-- Table structure for `t_customer`
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer` (
  `customerId` int(11) NOT NULL auto_increment COMMENT '客户id',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `regTime` varchar(20) default NULL COMMENT '加入时间',
  PRIMARY KEY  (`customerId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_customer
-- ----------------------------
INSERT INTO `t_customer` VALUES ('1', '凌天龙', '男', '2019-01-02', '13050843953', '锦江区旺区路', '2019-01-08 14:53:55');
INSERT INTO `t_customer` VALUES ('2', '李铭', '男', '2019-01-10', '13083493042', '成都市王德渠路', '2019-01-12 20:49:01');

-- ----------------------------
-- Table structure for `t_manager`
-- ----------------------------
DROP TABLE IF EXISTS `t_manager`;
CREATE TABLE `t_manager` (
  `managerUserName` varchar(20) NOT NULL COMMENT 'managerUserName',
  `password` varchar(20) NOT NULL COMMENT '登录密码',
  `shopObj` int(11) NOT NULL COMMENT '负责店铺',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `sex` varchar(4) NOT NULL COMMENT '性别',
  `birthday` varchar(20) default NULL COMMENT '出生日期',
  `userPhoto` varchar(60) NOT NULL COMMENT '管理照片',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `memo` varchar(800) default NULL COMMENT '备注信息',
  PRIMARY KEY  (`managerUserName`),
  KEY `shopObj` (`shopObj`),
  CONSTRAINT `t_manager_ibfk_1` FOREIGN KEY (`shopObj`) REFERENCES `t_shop` (`shopId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_manager
-- ----------------------------
INSERT INTO `t_manager` VALUES ('sp001', '123', '1', '黄小龙', '男', '2019-01-03', 'upload/ec9b81a7-3a86-40fe-9bbf-8267574d5708.jpg', '13959342344', 'test');
INSERT INTO `t_manager` VALUES ('sp002', '123', '2', '张晓彤', '女', '2019-01-09', 'upload/473b1543-c3b7-4b9e-9f5f-3c3848e536bc.jpg', '13590835342', '测试');

-- ----------------------------
-- Table structure for `t_product`
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `productId` int(11) NOT NULL auto_increment COMMENT '商品id',
  `productClassObj` int(11) NOT NULL COMMENT '商品类别',
  `productName` varchar(50) NOT NULL COMMENT '商品名称',
  `mainImage` varchar(60) NOT NULL COMMENT '商品主图',
  `price` float NOT NULL COMMENT '商品价格',
  `productDesc` varchar(8000) default NULL COMMENT '商品描述',
  `addTime` varchar(20) default NULL COMMENT '发布时间',
  PRIMARY KEY  (`productId`),
  KEY `productClassObj` (`productClassObj`),
  CONSTRAINT `t_product_ibfk_1` FOREIGN KEY (`productClassObj`) REFERENCES `t_productclass` (`productClassId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES ('1', '1', '苏泊尔电饭煲', 'upload/7627b7cf-37e5-4443-8836-7f639af4d880.jpg', '298', '<ul style=\"list-style-type: none;\" class=\" list-paddingleft-2\"><li><p>证书编号：2013010718646628</p></li><li><p>证书状态：有效</p></li><li><p>申请人名称：浙江苏泊尔家电制造有限公司</p></li><li><p>制造商名称：浙江苏泊尔家电制造有限公司</p></li><li><p>产品名称：豪华智能电饭煲</p></li><li><p>3C产品型号：CFXB30FZ16Q-60 3.0L 600W; CFXB40FZ16Q-75, CFXB40FC...</p></li><li><p>3C规格型号：CFXB30FZ16Q-60,CFXB30FC866-60; 3.0L 600W; CFXB40FZ...</p></li><li><p>产品名称：SUPOR/苏泊尔 CFXB50FC91...</p></li><li><p>容量:&nbsp;5L</p></li><li><p>控制方式:&nbsp;微电脑式</p></li><li><p>内胆材质:&nbsp;釜胆</p></li><li><p>形状:&nbsp;方形</p></li><li><p>加热方式:&nbsp;底盘加热</p></li><li><p>适用人数:&nbsp;2人-8人</p></li><li><p>售后服务:&nbsp;全国联保</p></li><li><p>品牌:&nbsp;SUPOR/苏泊尔</p></li><li><p>型号:&nbsp;CFXB50FC9133Q-75</p></li><li><p>电饭煲多功能:&nbsp;蛋糕&nbsp;煮粥&nbsp;预约&nbsp;煮饭&nbsp;杂粮</p></li><li><p>颜色分类:&nbsp;灰色</p></li></ul><p><br/></p>', '2019-01-08 14:53:03');
INSERT INTO `t_product` VALUES ('2', '2', '公牛插座USB插排插线板', 'upload/21d54afb-bbee-40b5-b55f-e1565804af1b.jpg', '25', '<p>&nbsp; &nbsp; 品牌名称：<span class=\"J_EbrandLogo\" style=\"margin: 0px; padding: 0px; color: rgb(51, 51, 51);\">BULL/公牛</span></p><p class=\"attr-list-hd tm-clear\" style=\"margin-top: 0px; margin-bottom: 0px; padding: 5px 20px; line-height: 22px; color: rgb(153, 153, 153); font-family: tahoma, arial, 微软雅黑, sans-serif; font-size: 12px; white-space: normal; background-color: rgb(255, 255, 255);\"><span style=\"margin: 0px; padding: 0px; font-weight: 700; float: left;\">产品参数：</span></p><ul style=\"list-style-type: none;\" class=\" list-paddingleft-2\"><li><p>证书编号：2016010201931890</p></li><li><p>证书状态：有效</p></li><li><p>产品名称：延长线插座</p></li><li><p>3C规格型号：GN-B50A0、GN-B5080、GN-B5060、GN-B5040、GN-B5030、GN-B5...</p></li><li><p>品牌:&nbsp;BULL/公牛</p></li><li><p>型号:&nbsp;GN-B5120</p></li><li><p>颜色分类:&nbsp;六位带USB总控全长1.8米（机械开关）B333U&nbsp;三位带USB分控 全长1.8米(机械开关)B3033&nbsp;三位带USB总控全长1.8米(机械开关)B403U&nbsp;三位分控3米B5033&nbsp;六位分控1.8米B5063&nbsp;五位分控1.8米B5053&nbsp;四位分控1.8米B5043&nbsp;三位分控1.8米B5033&nbsp;十位总控3米(超功率保护)B50A0&nbsp;黑色三位带USB总控全长1.8米(机械开关)B403H&nbsp;十位总控1.8米B5550&nbsp;八位总控5米(超功率保护)B5080&nbsp;八位总控3米(超功率保护)B5080&nbsp;八位总控5米B5440&nbsp;八位总控3米B5440&nbsp;八位总控1.8米B5440&nbsp;六位总控3米B5060&nbsp;六位总控1.8米B5060&nbsp;四位总控3米B5220&nbsp;四位总控1.8米B5220&nbsp;四位总控1.8米B5040&nbsp;六位总控3米B5330&nbsp;六位总控1.8米B5330&nbsp;三位总控5米B5030&nbsp;三位总控1.8米B5030&nbsp;三位总控1.8米B5120&nbsp;二位总控1.8米B5110</p></li><li><p>线长:&nbsp;3m&nbsp;10m及以上&nbsp;1.8m</p></li><li><p>接线板插位数:&nbsp;4插位&nbsp;6插位&nbsp;8插位</p></li><li><p>适用场景:&nbsp;家用</p></li></ul><p><br/></p>', '2019-01-12 20:47:06');

-- ----------------------------
-- Table structure for `t_productclass`
-- ----------------------------
DROP TABLE IF EXISTS `t_productclass`;
CREATE TABLE `t_productclass` (
  `productClassId` int(11) NOT NULL auto_increment COMMENT '商品类别id',
  `prodcutClassName` varchar(20) NOT NULL COMMENT '商品类别名称',
  `classDesc` varchar(500) default NULL COMMENT '类别描述',
  PRIMARY KEY  (`productClassId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_productclass
-- ----------------------------
INSERT INTO `t_productclass` VALUES ('1', '厨具类', '测试');
INSERT INTO `t_productclass` VALUES ('2', '电器类', '电子产品');

-- ----------------------------
-- Table structure for `t_sell`
-- ----------------------------
DROP TABLE IF EXISTS `t_sell`;
CREATE TABLE `t_sell` (
  `sellId` int(11) NOT NULL auto_increment COMMENT '销售id',
  `productObj` int(11) NOT NULL COMMENT '销售商品',
  `shopObj` int(11) NOT NULL COMMENT '销售店铺',
  `sellPrice` float NOT NULL COMMENT '销售价格',
  `sellCount` int(11) NOT NULL COMMENT '销售数量',
  `customerObj` int(11) NOT NULL COMMENT '销售客户',
  `sellTime` varchar(20) default NULL COMMENT '销售时间',
  `managerObj` varchar(20) NOT NULL COMMENT '操作管理员',
  `memo` varchar(800) default NULL COMMENT '销售备注',
  PRIMARY KEY  (`sellId`),
  KEY `productObj` (`productObj`),
  KEY `shopObj` (`shopObj`),
  KEY `customerObj` (`customerObj`),
  KEY `managerObj` (`managerObj`),
  CONSTRAINT `t_sell_ibfk_1` FOREIGN KEY (`productObj`) REFERENCES `t_product` (`productId`),
  CONSTRAINT `t_sell_ibfk_2` FOREIGN KEY (`shopObj`) REFERENCES `t_shop` (`shopId`),
  CONSTRAINT `t_sell_ibfk_3` FOREIGN KEY (`customerObj`) REFERENCES `t_customer` (`customerId`),
  CONSTRAINT `t_sell_ibfk_4` FOREIGN KEY (`managerObj`) REFERENCES `t_manager` (`managerUserName`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sell
-- ----------------------------
INSERT INTO `t_sell` VALUES ('1', '1', '1', '320', '20', '1', '2019-01-08 14:56:17', 'sp001', '好');
INSERT INTO `t_sell` VALUES ('2', '1', '1', '330', '10', '1', '2019-01-12 19:25:07', 'sp001', '测试');
INSERT INTO `t_sell` VALUES ('3', '2', '1', '28', '10', '2', '2019-01-12 20:52:58', 'sp001', '测试');

-- ----------------------------
-- Table structure for `t_shop`
-- ----------------------------
DROP TABLE IF EXISTS `t_shop`;
CREATE TABLE `t_shop` (
  `shopId` int(11) NOT NULL auto_increment COMMENT '店铺id',
  `shopName` varchar(20) NOT NULL COMMENT '店铺名称',
  `connectPerson` varchar(20) NOT NULL COMMENT '联系人',
  `connectPhone` varchar(20) NOT NULL COMMENT '联系电话',
  `address` varchar(80) NOT NULL COMMENT '店铺地址',
  PRIMARY KEY  (`shopId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_shop
-- ----------------------------
INSERT INTO `t_shop` VALUES ('1', '成都建设路店', '王忠伟', '028-82983043', '建设路112号');
INSERT INTO `t_shop` VALUES ('2', '太升南路分店', '王静', '028-83948124', '太升南路10号');

-- ----------------------------
-- Table structure for `t_stock`
-- ----------------------------
DROP TABLE IF EXISTS `t_stock`;
CREATE TABLE `t_stock` (
  `stockId` int(11) NOT NULL auto_increment COMMENT '库存id',
  `productObj` int(11) NOT NULL COMMENT '商品名称',
  `shopObj` int(11) NOT NULL COMMENT '所属店铺',
  `leftCount` int(11) NOT NULL COMMENT '剩余库存量',
  `memo` varchar(800) default NULL COMMENT '备注信息',
  PRIMARY KEY  (`stockId`),
  KEY `productObj` (`productObj`),
  KEY `shopObj` (`shopObj`),
  CONSTRAINT `t_stock_ibfk_1` FOREIGN KEY (`productObj`) REFERENCES `t_product` (`productId`),
  CONSTRAINT `t_stock_ibfk_2` FOREIGN KEY (`shopObj`) REFERENCES `t_shop` (`shopId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_stock
-- ----------------------------
INSERT INTO `t_stock` VALUES ('1', '1', '1', '40', 'test');
INSERT INTO `t_stock` VALUES ('2', '1', '2', '50', '');
INSERT INTO `t_stock` VALUES ('3', '2', '1', '80', '');

-- ----------------------------
-- Table structure for `t_supplier`
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier` (
  `supplierId` int(11) NOT NULL auto_increment COMMENT '供应商id',
  `supplierName` varchar(50) NOT NULL COMMENT '供应商名称',
  `farendaibiao` varchar(20) NOT NULL COMMENT '法人代表',
  `telephone` varchar(20) NOT NULL COMMENT '供应商电话',
  `address` varchar(60) NOT NULL COMMENT '供应商地址',
  PRIMARY KEY  (`supplierId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_supplier
-- ----------------------------
INSERT INTO `t_supplier` VALUES ('1', '四川总代理供应商', '凌天龙', '028-83940834', '028-82985012');
INSERT INTO `t_supplier` VALUES ('2', '西南代理总部', '王夏天', '028-83985082', '成都市双流区航空路10号');

-- ----------------------------
-- Table structure for `t_transfer`
-- ----------------------------
DROP TABLE IF EXISTS `t_transfer`;
CREATE TABLE `t_transfer` (
  `transferId` int(11) NOT NULL auto_increment COMMENT '调拨id',
  `productObj` int(11) NOT NULL COMMENT '调拨商品',
  `transferCount` int(11) NOT NULL COMMENT '调拨数量',
  `shopObj1` int(11) NOT NULL COMMENT '调出店铺',
  `shopObj2` int(11) NOT NULL COMMENT '调入店铺',
  `transferTime` varchar(20) default NULL COMMENT '调拨时间',
  `memo` varchar(800) default NULL COMMENT '调拨备注',
  PRIMARY KEY  (`transferId`),
  KEY `productObj` (`productObj`),
  KEY `shopObj1` (`shopObj1`),
  KEY `shopObj2` (`shopObj2`),
  CONSTRAINT `t_transfer_ibfk_1` FOREIGN KEY (`productObj`) REFERENCES `t_product` (`productId`),
  CONSTRAINT `t_transfer_ibfk_2` FOREIGN KEY (`shopObj1`) REFERENCES `t_shop` (`shopId`),
  CONSTRAINT `t_transfer_ibfk_3` FOREIGN KEY (`shopObj2`) REFERENCES `t_shop` (`shopId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_transfer
-- ----------------------------
INSERT INTO `t_transfer` VALUES ('3', '1', '30', '1', '2', '2019-01-12 19:55:42', '测试');
INSERT INTO `t_transfer` VALUES ('4', '1', '20', '2', '1', '2019-01-12 20:34:27', '测试');
INSERT INTO `t_transfer` VALUES ('5', '1', '40', '1', '2', '2019-01-12 20:42:58', '测试调拨');
