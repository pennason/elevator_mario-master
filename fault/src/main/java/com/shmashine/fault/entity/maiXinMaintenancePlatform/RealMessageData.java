package com.shmashine.fault.entity.maiXinMaintenancePlatform;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实时消息
 *
 * @author jiangheng
 * @version V1.0.0 - 2023/5/29 17:29
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RealMessageData implements Serializable {

    /**
     * 当前服务模式 0: 停止服务 1: 正常运行 2: 检修 3: 消防返回 4: 消防员运行 5: 应急电源运行 6: 地震模式 7: 未知
     */
    private Integer serviceMode;

    /**
     * 运行方向 0：无方向 1：上行 2：下行
     */
    private Integer direction;

    /**
     * 是否在平层、开锁区域 1：true 0：false
     */
    private Integer doorZone;

    /**
     * 楼层
     */
    private String floorPosition;

    /**
     * 是否有人 0：无人，1：有人
     */
    private Integer hasPeople;

    /**
     * 门状态 0: 未知 1: 正在关门 2: 关门到位 3: 正在开门 4: 开门到位 5: 门锁锁止 6: 保持不完全关闭状态
     */
    private Integer doorStatus;

    /**
     * 是否超载 0：正常 1：超载
     */
    private Integer overload;

    /**
     * 曳引机状态 0:待机 1: 曳引机制动器提起 2: 曳引机制动器释放
     */
    private Integer liftCarDriveStatus;

    /**
     * 人数
     */
    private Integer peopleNumber;

    /**
     * 电梯运行速度
     */
    private float speed;

    /**
     * 安全回路状态 0：正常 1：断开
     */
    private Integer safeLoop;

    /**
     * 机房温度
     */
    private Float temperature;

    /**
     * 数据采集时间
     */
    @NotNull(message = "数据采集时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date collectionTime;

}
