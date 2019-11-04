package org.jeecg.modules.zzj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("kailaiorder")
public class KaiLaiOrder {
    /**表id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
    private java.lang.String id;
    /*订单号   resv_name_id  */
    private String resvNameId;
    /*个人信息id  name_id*/
    private String nameId;
    /* 姓名全拼         alt_name */
    private String altName;
    /* 姓         last_name*/
    private String lastName;
    /* 名      first_name*/
    private String firstName;
    /*      language*/
    private String language;
    /*
    订单状态 resv_status
    分为 checkin  入住状态
         duein  预抵达状态
    */
    private String resvStatus;
    /*      订单类型  resv_type
            记住 checkin 和 6 pm     */
    private String resvType;
    /*  该订单的房价   rate_finally    */
    private String rateFinally;
    /*    价格代码    rate_code   */
    private String rateCode;
    /*   包价   Packages      */
    private String packages;
    /*   房间类型  room_type  */
    private String roomType;
    /*   房间号  room_no  */
    private String roomNo;
    /*  层数  floor   ????  */
    private String floor;
    /*  过夜  数量   ????  nights             */
    private String nights;
    /*  预定入住时间 begin_date */
    private String beginDate;
    /*  预定离店时间 end_date */
    private String endDate;
    /*  original_end_date   */
    private String originalEndDate;
    /*  actual_check_in_date  */
    private String actualCheckInDate;
    /*  actual_check_out_date  */
    private String actualCheckOutDate;
    /*  手机号   phone */
    private String phone;
    /* 身份证号 identityCard */
    private String idEntityCard;

}
