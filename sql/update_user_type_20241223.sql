-- 用户类型字段修改SQL
-- 修改时间：2024-12-23
-- 修改内容：将user_type字段的含义从"1：网页端，2：微信小程序，3：管理员"改为"1：学生，2：老师，3：管理员"

-- 1. 修改表注释
ALTER TABLE `sys_user` 
MODIFY COLUMN `user_type` int(11) DEFAULT NULL COMMENT '用户类型（1：学生，2：老师，3：管理员）';

-- 2. 数据迁移脚本（如果已有数据需要迁移）
-- 注意：根据实际情况调整以下迁移逻辑

-- 2.1 将原来的网页端用户(1)迁移为学生(1) - 值不变，含义改变
-- 2.2 将原来的微信小程序用户(2)迁移为老师(2) - 值不变，含义改变  
-- 2.3 管理员(3)保持不变

-- 如果需要重新分配用户类型，可以执行以下更新语句：
-- UPDATE sys_user SET user_type = 1 WHERE user_type = 1; -- 原网页端用户变为学生
-- UPDATE sys_user SET user_type = 2 WHERE user_type = 2; -- 原微信小程序用户变为老师
-- UPDATE sys_user SET user_type = 3 WHERE user_type = 3; -- 管理员保持不变

-- 3. 创建索引优化查询性能
CREATE INDEX IF NOT EXISTS `idx_user_type` ON `sys_user`(`user_type`);

-- 4. 如果需要添加新的约束或检查
-- ALTER TABLE `sys_user` ADD CONSTRAINT `chk_user_type` CHECK (`user_type` IN (1, 2, 3));

-- 回滚语句（如果需要撤销修改）
-- ALTER TABLE `sys_user` 
-- MODIFY COLUMN `user_type` int(11) DEFAULT NULL COMMENT '用户类型（1：网页端，2：微信小程序，3：管理员）';