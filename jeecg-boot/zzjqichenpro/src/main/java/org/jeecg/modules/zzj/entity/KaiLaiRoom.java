package org.jeecg.modules.zzj.entity;

import lombok.Data;
/**
 * 根据到店/离店日期查询房型数量
 *
 * 可用房间信息显示
 *
 */
@Data
public class KaiLaiRoom {
    private String hotalName;
    private String type;
    private String number;
    private String description;
    private String price;
    private String features;
    private String rateCode;
}
