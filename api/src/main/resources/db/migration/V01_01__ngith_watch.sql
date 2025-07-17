use
`elevator_master`;

ALTER TABLE `tbl_elevator`
    ADD `night_watch_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '开启夜间守护模式，1:开启，0:关闭' AFTER `i_pm_status`,
    ADD `night_watch_start_time` TIME NULL DEFAULT '23:00:00' COMMENT '夜间守护开始时间' AFTER `night_watch_status`,
    ADD `night_watch_end_time` TIME NULL DEFAULT '05:00:00' COMMENT '夜间守护结束时间' AFTER `night_watch_start_time`;