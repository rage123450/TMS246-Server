/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : tms247

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 08/01/2023 03:28:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for emoticon
-- ----------------------------
DROP TABLE IF EXISTS `emoticon`;
CREATE TABLE `emoticon`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `charid` int(0) NOT NULL DEFAULT 0,
  `emoticonid` int(0) NOT NULL DEFAULT 0,
  `time` bigint(0) NOT NULL DEFAULT 0,
  `bookmarks` text CHARACTER SET big5 COLLATE big5_chinese_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `charid`(`charid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for emoticon_saved
-- ----------------------------
DROP TABLE IF EXISTS `emoticon_saved`;
CREATE TABLE `emoticon_saved`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `charid` int(0) NOT NULL DEFAULT 0,
  `emoticonid` int(0) NOT NULL DEFAULT 0,
  `chat` varchar(50) CHARACTER SET big5 COLLATE big5_chinese_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `charid`(`charid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
