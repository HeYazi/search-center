-- 创建库
create database if not exists db_search_center;

-- 切换库
use db_search_center;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 插入数据
INSERT INTO user (userAccount, userPassword, unionId, mpOpenId, userName, userAvatar, userProfile, userRole, createTime,
                  updateTime, isDelete)
VALUES ('john123', 'password123', 'unionid123', 'mpopenid123', '张三', 'https://example.com/avatar.jpg', '我是张三',
        'user',
        '2023-07-19 10:00:00', '2023-07-19 10:00:00', 0),
       ('jane456', 'password456', NULL, 'mpopenid456', '李四', 'https://example.com/avatar.jpg', '我是李四', 'user',
        '2023-07-19 11:00:00', '2023-07-19 11:00:00', 0),
       ('alice789', 'alicepass789', 'unionid789', 'mpopenid789', '王五', 'https://example.com/avatar.jpg', '我是王五', 'user',
        '2023-07-19 12:00:00', '2023-07-19 12:00:00', 0)
        ,
       ('bob101', 'bobpass101', NULL, 'mpopenid101', '赵六', 'https://example.com/avatar.jpg', '我是赵六', 'user',
        '2023-07-19 13:00:00', '2023-07-19 13:00:00', 0)
        ,
       ('emily202', 'emilypass202', 'unionid202', NULL, '刘七', 'https://example.com/avatar.jpg', '我是刘七', 'user',
        '2023-07-19 14:00:00', '2023-07-19 14:00:00', 0);

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';
