/*
Navicat Premium Dump SQL

Source Server         : mycon
Source Server Type    : MySQL
Source Server Version : 80037 (8.0.37)
Source Host           : localhost:3306
Source Schema         : weave

Target Server Type    : MySQL
Target Server Version : 80037 (8.0.37)
File Encoding         : 65001

Date: 27/07/2025 12:10:01
*/

CREATE DATABASE weave CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE weave;

SET NAMES utf8mb4;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for badge
-- ----------------------------
DROP TABLE IF EXISTS `badge`;

CREATE TABLE `badge` (
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `type` enum('LEVEL', 'LIMITED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of badge
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `level` int NOT NULL,
    `parent_id` bigint NULL DEFAULT NULL,
    `sort_order` int NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UK46ccwnsi9409t36lurvtyljak` (`name` ASC) USING BTREE,
    INDEX `FK2y94svpmqttx80mshyny85wqr` (`parent_id` ASC) USING BTREE,
    CONSTRAINT `FK2y94svpmqttx80mshyny85wqr` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.261235',
        1943140917649215488,
        '2025-07-10 02:51:11.261235',
        '民间文学',
        '包括神话、传说、民间故事、民间歌谣、谚语、谜语等各种形式的民间文学作品',
        'FL',
        1,
        NULL,
        1
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.294265',
        1943140917888290816,
        '2025-07-10 02:51:11.294265',
        '传统音乐',
        '包括民间音乐、传统声乐、传统器乐等音乐形式',
        'TM',
        1,
        NULL,
        2
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.295265',
        1943140917892485120,
        '2025-07-10 02:51:11.295265',
        '传统舞蹈',
        '包括民间舞蹈、宗教舞蹈、宫廷舞蹈等各种传统舞蹈形式',
        'TD',
        1,
        NULL,
        3
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.295265',
        1943140917892485121,
        '2025-07-10 02:51:11.295265',
        '传统戏剧',
        '包括地方戏曲、民间戏剧、宗教戏剧等戏剧形式',
        'TT',
        1,
        NULL,
        4
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.295265',
        1943140917892485122,
        '2025-07-10 02:51:11.295265',
        '曲艺',
        '包括相声、评书、快板、大鼓、弹词等说唱艺术形式',
        'QY',
        1,
        NULL,
        5
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.295265',
        1943140917892485123,
        '2025-07-10 02:51:11.295265',
        '传统体育、游艺与杂技',
        '包括武术、传统体育竞技、民间游戏、杂技等',
        'TS',
        1,
        NULL,
        6
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.295265',
        1943140917892485124,
        '2025-07-10 02:51:11.295265',
        '传统美术',
        '包括绘画、雕塑、建筑装饰、服饰、民间工艺美术等',
        'TA',
        1,
        NULL,
        7
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.296275',
        1943140917896679424,
        '2025-07-10 02:51:11.296275',
        '传统技艺',
        '包括传统手工技艺、传统制作技艺、传统医药炮制技艺等',
        'TC',
        1,
        NULL,
        8
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.296275',
        1943140917896679425,
        '2025-07-10 02:51:11.296275',
        '传统医药',
        '包括传统中医药理论、诊疗方法、制药技艺、养生保健等',
        'TM',
        1,
        NULL,
        9
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.297275',
        1943140917900873728,
        '2025-07-10 02:51:11.297275',
        '民俗',
        '包括节庆习俗、人生礼俗、民间信仰、传统礼仪等民俗文化',
        'MS',
        1,
        NULL,
        10
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.298273',
        1943140917905068032,
        '2025-07-10 02:51:11.298273',
        '神话传说',
        '包括创世神话、英雄传说、历史传说等',
        'FL01',
        2,
        1943140917649215488,
        1
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.298273',
        1943140917905068033,
        '2025-07-10 02:51:11.298273',
        '民间故事',
        '包括生活故事、动物故事、笑话等',
        'FL02',
        2,
        1943140917649215488,
        2
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.298273',
        1943140917905068034,
        '2025-07-10 02:51:11.298273',
        '民间歌谣',
        '包括劳动歌、仪式歌、生活歌等',
        'FL03',
        2,
        1943140917649215488,
        3
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.299265',
        1943140917905068035,
        '2025-07-10 02:51:11.299265',
        '谚语俗语',
        '包括生产谚语、生活谚语、社会谚语等',
        'FL04',
        2,
        1943140917649215488,
        4
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.299265',
        1943140917909262336,
        '2025-07-10 02:51:11.299265',
        '民歌',
        '包括山歌、田歌、渔歌、牧歌等',
        'TM01',
        2,
        1943140917888290816,
        1
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.300272',
        1943140917909262337,
        '2025-07-10 02:51:11.300272',
        '民族器乐',
        '包括吹奏乐、拉弦乐、弹拨乐、打击乐等',
        'TM02',
        2,
        1943140917888290816,
        2
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.300272',
        1943140917913456640,
        '2025-07-10 02:51:11.300272',
        '戏曲音乐',
        '包括各地方戏曲的音乐部分',
        'TM03',
        2,
        1943140917888290816,
        3
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.300272',
        1943140917913456641,
        '2025-07-10 02:51:11.300272',
        '宗教音乐',
        '包括佛教音乐、道教音乐、民间信仰音乐等',
        'TM04',
        2,
        1943140917888290816,
        4
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.301270',
        1943140917917650944,
        '2025-07-10 02:51:11.301270',
        '民间舞蹈',
        '包括汉族民间舞、少数民族舞蹈等',
        'TD01',
        2,
        1943140917892485120,
        1
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.301270',
        1943140917917650945,
        '2025-07-10 02:51:11.301270',
        '宗教舞蹈',
        '包括佛教舞蹈、道教舞蹈、萨满舞等',
        'TD02',
        2,
        1943140917892485120,
        2
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.301270',
        1943140917917650946,
        '2025-07-10 02:51:11.301270',
        '宫廷舞蹈',
        '包括古代宫廷雅乐舞蹈',
        'TD03',
        2,
        1943140917892485120,
        3
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.301270',
        1943140917917650947,
        '2025-07-10 02:51:11.301270',
        '仪式舞蹈',
        '包括祭祀舞蹈、庆典舞蹈等',
        'TD04',
        2,
        1943140917892485120,
        4
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.302267',
        1943140917921845248,
        '2025-07-10 02:51:11.302267',
        '京剧',
        '中国国粹，四大名旦等经典剧目',
        'TT01',
        2,
        1943140917892485121,
        1
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.302267',
        1943140917921845249,
        '2025-07-10 02:51:11.302267',
        '昆曲',
        '百戏之祖，世界非物质文化遗产',
        'TT02',
        2,
        1943140917892485121,
        2
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.302267',
        1943140917921845250,
        '2025-07-10 02:51:11.302267',
        '地方戏曲',
        '包括豫剧、越剧、黄梅戏、评剧等',
        'TT03',
        2,
        1943140917892485121,
        3
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.302267',
        1943140917921845251,
        '2025-07-10 02:51:11.302267',
        '皮影戏',
        '传统民间戏剧形式',
        'TT04',
        2,
        1943140917892485121,
        4
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.303263',
        1943140917921845252,
        '2025-07-10 02:51:11.303263',
        '纺织印染技艺',
        '包括丝绸织造、蜡染、扎染等',
        'TC01',
        2,
        1943140917896679424,
        1
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.303263',
        1943140917926039552,
        '2025-07-10 02:51:11.303263',
        '金属加工技艺',
        '包括铸造、锻造、金银细工等',
        'TC02',
        2,
        1943140917896679424,
        2
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.303263',
        1943140917926039553,
        '2025-07-10 02:51:11.303263',
        '陶瓷烧制技艺',
        '包括各地名窑的制瓷技艺',
        'TC03',
        2,
        1943140917896679424,
        3
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.303263',
        1943140917926039554,
        '2025-07-10 02:51:11.303263',
        '木作技艺',
        '包括传统建筑木作、家具制作等',
        'TC04',
        2,
        1943140917896679424,
        4
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.304268',
        1943140917930233856,
        '2025-07-10 02:51:11.304268',
        '食品制作技艺',
        '包括传统酿造、糕点制作等',
        'TC05',
        2,
        1943140917896679424,
        5
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.304268',
        1943140917930233857,
        '2025-07-10 02:51:11.304268',
        '节庆习俗',
        '包括春节、清明、端午、中秋等传统节日习俗',
        'MS01',
        2,
        1943140917900873728,
        1
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.304268',
        1943140917930233858,
        '2025-07-10 02:51:11.304268',
        '人生礼俗',
        '包括诞生礼、成年礼、婚礼、丧礼等',
        'MS02',
        2,
        1943140917900873728,
        2
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 02:51:11.305263',
        1943140917934428160,
        '2025-07-10 02:51:11.305263',
        '民间信仰',
        '包括祖先崇拜、自然崇拜、民间神灵信仰等',
        'MS03',
        2,
        1943140917900873728,
        3
    );

INSERT INTO
    `category`
VALUES (
        '2025-07-10 03:18:21.708588',
        1943147756231200768,
        '2025-07-10 03:18:21.708588',
        '生产商贸习俗',
        '包括农业习俗、手工业习俗、商贸习俗等',
        'MS04',
        2,
        1943140917900873728,
        4
    );

-- ----------------------------
-- Table structure for check_in
-- ----------------------------
DROP TABLE IF EXISTS `check_in`;

CREATE TABLE `check_in` (
    `check_date` date NOT NULL,
    `consecutive_days` int NULL DEFAULT NULL,
    `is_补签` bit(1) NULL DEFAULT NULL,
    `points_earned` int NULL DEFAULT NULL,
    `check_time` datetime(6) NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `user_id` bigint NOT NULL,
    `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `device_info` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UK44bu08d5kalkps607l66jlck4` (
        `user_id` ASC,
        `check_date` ASC
    ) USING BTREE,
    CONSTRAINT `FKev5gubilot6y4q9w7kc4cl2ln` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of check_in
-- ----------------------------

-- ----------------------------
-- Table structure for check_in_stats
-- ----------------------------
DROP TABLE IF EXISTS `check_in_stats`;

CREATE TABLE `check_in_stats` (
    `available_make_up` int NULL DEFAULT NULL,
    `current_consecutive_days` int NULL DEFAULT NULL,
    `first_check_date` date NULL DEFAULT NULL,
    `last_check_date` date NULL DEFAULT NULL,
    `make_up_count` int NULL DEFAULT NULL,
    `max_consecutive_days` int NULL DEFAULT NULL,
    `this_month_check_days` int NULL DEFAULT NULL,
    `this_year_check_days` int NULL DEFAULT NULL,
    `total_check_days` int NULL DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `total_points_earned` bigint NULL DEFAULT NULL,
    `updated_at` datetime(6) NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UKq090k9ajv50l9irmp7mohvd6r` (`user_id` ASC) USING BTREE,
    CONSTRAINT `FKcgnlv2ypvuejufeta1uj2vb8y` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of check_in_stats
-- ----------------------------
INSERT INTO
    `check_in_stats`
VALUES (
        3,
        0,
        NULL,
        NULL,
        0,
        0,
        0,
        0,
        0,
        '2025-07-01 07:03:41.142443',
        1939942970149179392,
        0,
        '2025-07-01 07:03:41.142443',
        1939884796922695680
    );

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
    `like_count` int NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `parent_id` bigint NULL DEFAULT NULL,
    `post_id` bigint NULL DEFAULT NULL,
    `resource_id` bigint NULL DEFAULT NULL,
    `updated_at` datetime(6) NOT NULL,
    `user_id` bigint NOT NULL,
    `content` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `status` enum(
        'DELETED',
        'HIDDEN',
        'PUBLISHED'
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FKde3rfu96lep00br5ov0mdieyt` (`parent_id` ASC) USING BTREE,
    INDEX `FKs1slvnkuemjsq2kj4h3vhx7i1` (`post_id` ASC) USING BTREE,
    INDEX `FKsr204l01eftkycxvpt4cs2q43` (`resource_id` ASC) USING BTREE,
    INDEX `FK8kcum44fvpupyw6f5baccx25c` (`user_id` ASC) USING BTREE,
    CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKde3rfu96lep00br5ov0mdieyt` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKs1slvnkuemjsq2kj4h3vhx7i1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKsr204l01eftkycxvpt4cs2q43` FOREIGN KEY (`resource_id`) REFERENCES `ich_resource` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;

CREATE TABLE `follow` (
    `created_at` datetime(6) NOT NULL,
    `follower_id` bigint NOT NULL,
    `following_id` bigint NOT NULL,
    `id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `status` enum('ACTIVE', 'BLOCKED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UKfb7ln73htigy8q3cx7ebyho3c` (
        `follower_id` ASC,
        `following_id` ASC
    ) USING BTREE,
    INDEX `FKqme6uru2g9wx9iysttk542esm` (`following_id` ASC) USING BTREE,
    CONSTRAINT `FKmow2qk674plvwyb4wqln37svv` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKqme6uru2g9wx9iysttk542esm` FOREIGN KEY (`following_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO
    `follow`
VALUES (
        '2025-07-08 14:22:34.897933',
        1942588502281687040,
        1939884796922695680,
        1942590136999088128,
        '2025-07-08 14:22:34.897933',
        'ACTIVE'
    );

INSERT INTO
    `follow`
VALUES (
        '2025-07-08 14:24:17.585913',
        1939884796922695680,
        1942588502281687040,
        1942590567703777280,
        '2025-07-08 14:24:17.585913',
        'ACTIVE'
    );

-- ----------------------------
-- Table structure for ich_resource
-- ----------------------------
DROP TABLE IF EXISTS `ich_resource`;

CREATE TABLE `ich_resource` (
    `category_id` bigint NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `uploader_id` bigint NOT NULL,
    `copyright_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `content_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `type` enum('DYNAMIC', 'STATIC') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FKhdr8h146byrc0u8vhlc4w4d8f` (`category_id` ASC) USING BTREE,
    INDEX `FKq3isc2nqt5lw1uc5ot16bu29f` (`uploader_id` ASC) USING BTREE,
    CONSTRAINT `FKhdr8h146byrc0u8vhlc4w4d8f` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKq3isc2nqt5lw1uc5ot16bu29f` FOREIGN KEY (`uploader_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ich_resource
-- ----------------------------

-- ----------------------------
-- Table structure for interest_tag
-- ----------------------------
DROP TABLE IF EXISTS `interest_tag`;

CREATE TABLE `interest_tag` (
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UK6dut057l1iviykn9tap7r7mtq` (`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interest_tag
-- ----------------------------

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;

CREATE TABLE `post` (
    `comment_count` bigint NOT NULL,
    `is_hot` bit(1) NOT NULL,
    `is_top` bit(1) NOT NULL,
    `like_count` bigint NOT NULL,
    `share_count` bigint NOT NULL,
    `view_count` bigint NOT NULL,
    `author_id` bigint NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `content` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `status` enum(
        'DELETED',
        'DRAFT',
        'HIDDEN',
        'PUBLISHED'
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `type` enum(
        'DISCUSSION',
        'IMAGE',
        'LINK',
        'QUESTION',
        'TEXT',
        'VIDEO'
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FK12njtf8e0jmyb45lqfpt6ad89` (`author_id` ASC) USING BTREE,
    CONSTRAINT `FK12njtf8e0jmyb45lqfpt6ad89` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO
    `post`
VALUES (
        0,
        b'0',
        b'0',
        0,
        0,
        0,
        1939884796922695680,
        '2025-07-11 11:29:28.666826',
        1943633737493385216,
        '2025-07-11 11:35:04.468229',
        '看看潮汕英歌舞',
        '英歌舞,潮汕,甜妹',
        '[\"https://loremflickr.com/400/400?lock=7326805739230008\"]',
        '老铁们，今天和王力宏来到了潮汕潮安区，来看看这英歌舞这么个事',
        'DELETED',
        'IMAGE'
    );

INSERT INTO
    `post`
VALUES (
        0,
        b'0',
        b'0',
        1,
        0,
        1,
        1939884796922695680,
        '2025-07-11 11:35:21.543812',
        1943635217659072512,
        '2025-07-11 11:35:21.543812',
        '看看潮汕英歌舞',
        '英歌舞,潮汕,甜妹',
        '[\"https://loremflickr.com/400/400?lock=7326805739230008\"]',
        '老铁们，今天和王力宏来到了潮汕潮安区，来看看这英歌舞这么个事',
        'PUBLISHED',
        'IMAGE'
    );

-- ----------------------------
-- Table structure for post_interest_tag
-- ----------------------------
DROP TABLE IF EXISTS `post_interest_tag`;

CREATE TABLE `post_interest_tag` (
    `post_id` bigint NOT NULL,
    `tag_id` bigint NOT NULL,
    PRIMARY KEY (`post_id`, `tag_id`) USING BTREE,
    INDEX `FK32nstvjkeeoo4jdiyw2gbhkjp` (`tag_id` ASC) USING BTREE,
    CONSTRAINT `FK32nstvjkeeoo4jdiyw2gbhkjp` FOREIGN KEY (`tag_id`) REFERENCES `interest_tag` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKr9wupv46r39xdb2vkq8teoo1u` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_interest_tag
-- ----------------------------

-- ----------------------------
-- Table structure for post_like
-- ----------------------------
DROP TABLE IF EXISTS `post_like`;

CREATE TABLE `post_like` (
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `post_id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `user_id` bigint NOT NULL,
    `type` enum('DISLIKE', 'LIKE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UKpmmko3h7yonaqhy5gxvnmdeue` (`user_id` ASC, `post_id` ASC) USING BTREE,
    INDEX `FKj7iy0k7n3d0vkh8o7ibjna884` (`post_id` ASC) USING BTREE,
    CONSTRAINT `FKhuh7nn7libqf645su27ytx21m` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKj7iy0k7n3d0vkh8o7ibjna884` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_like
-- ----------------------------
INSERT INTO
    `post_like`
VALUES (
        '2025-07-11 14:25:03.473934',
        1943677923680718848,
        1943635217659072512,
        '2025-07-11 14:25:03.473934',
        1939884796922695680,
        'LIKE'
    );

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UK8sewwnpamngi6b1dwaa88askk` (`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------

-- ----------------------------
-- Table structure for ugc_work
-- ----------------------------
DROP TABLE IF EXISTS `ugc_work`;

CREATE TABLE `ugc_work` (
    `author_id` bigint NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `content_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
    `status` enum(
        'APPROVED',
        'PENDING',
        'REJECTED'
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FKcpwctglwv9blaohhujx5r22ye` (`author_id` ASC) USING BTREE,
    CONSTRAINT `FKcpwctglwv9blaohhujx5r22ye` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ugc_work
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `birthday` date NULL DEFAULT NULL,
    `is_active` bit(1) NOT NULL,
    `level` int NULL DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `id` bigint NOT NULL,
    `last_login_time` datetime(6) NULL DEFAULT NULL,
    `points` bigint NULL DEFAULT NULL,
    `updated_at` datetime(6) NOT NULL,
    `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `id_card_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `profession` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `qq_open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `wechat_open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `bio` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UKcnjwxx5favk5ycqajjt17fwy1` (`mobile` ASC) USING BTREE,
    UNIQUE INDEX `UKsb8bbouer5wak8vyiiy4pf2bx` (`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO
    `user`
VALUES (
        '2005-09-06',
        b'1',
        1,
        '2025-07-01 03:12:31.580553',
        1939884796922695680,
        NULL,
        0,
        '2025-07-01 07:01:50.937215',
        '其他',
        NULL,
        '13709670518',
        'Aseubel',
        NULL,
        'mobile_13709670518',
        'DongGuan',
        NULL,
        '$2a$10$yxcinQrQnwMvbR0fRCxJMe0d0mIWEXGh7o.mNz4X1flj3xpmLvnEq',
        '后端开发者',
        NULL,
        NULL,
        '一个普通开发者',
        NULL
    );

INSERT INTO
    `user`
VALUES (
        NULL,
        b'1',
        1,
        '2025-07-08 14:16:05.174373',
        1942588502281687040,
        NULL,
        0,
        '2025-07-08 14:16:05.174373',
        NULL,
        NULL,
        '13266002451',
        'Starflow',
        NULL,
        'Starflow',
        NULL,
        NULL,
        '$2a$10$cpXwRhi.PSwJoKNr.VmQOuNDBVHH3d013YHM1Y1.9.IrchBxPbNGW',
        NULL,
        NULL,
        NULL,
        NULL,
        NULL
    );

-- ----------------------------
-- Table structure for user_interest_tag
-- ----------------------------
DROP TABLE IF EXISTS `user_interest_tag`;

CREATE TABLE `user_interest_tag` (
    `tag_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`tag_id`, `user_id`) USING BTREE,
    INDEX `FKh1cy4g6mf0wf36forsr27pcm1` (`user_id` ASC) USING BTREE,
    CONSTRAINT `FKh1cy4g6mf0wf36forsr27pcm1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKl4u9cn6y4h1dxm70em2ho4oq` FOREIGN KEY (`tag_id`) REFERENCES `interest_tag` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_interest_tag
-- ----------------------------

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
    `role_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`role_id`, `user_id`) USING BTREE,
    INDEX `FK859n2jvi8ivhui0rl0esws6o` (`user_id` ASC) USING BTREE,
    CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;