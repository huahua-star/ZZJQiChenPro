package org.jeecg.modules.zzj.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.xml.bind.annotation.*;

/**
 * @Description: 用户实体类
 * @Author: jeecg-boot
 * @Date:   2019-09-16
 * @Version: V1.0
 */
@Data
@TableName("qc_checkin")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_checkin对象", description="入住功能")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckIn {
    
	/**表id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "表id")
	private java.lang.String id;
	/**身份证*/
	@Excel(name = "身份证", width = 15)
    @ApiModelProperty(value = "身份证")
	private java.lang.String idnumber;
	/**客户姓名*/
	@Excel(name = "客户姓名", width = 15)
    @ApiModelProperty(value = "客户姓名")
	private java.lang.String name;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
	private java.lang.String gender;
	/**出生日期*/
	@Excel(name = "出生日期", width = 15)
    @ApiModelProperty(value = "出生日期")
	private java.lang.String birthday;
	/**民族*/
	@Excel(name = "民族", width = 15)
    @ApiModelProperty(value = "民族")
	private java.lang.String national;
	/**身份证住址*/
	@Excel(name = "身份证住址", width = 15)
    @ApiModelProperty(value = "身份证住址")
	private java.lang.String idaddress;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
	private java.lang.String phonenum;
	/**房间号*/
	@Excel(name = "房间号", width = 15)
    @ApiModelProperty(value = "房间号")
	private java.lang.String roomnum;
	/**真实入住时间*/
	@Excel(name = "预定入住时间", width = 15)
	@ApiModelProperty(value = "预定入住时间")
	private java.lang.String begintime;
	/**真实离店时间*/
	@Excel(name = "预定离店时间", width = 15)
	@ApiModelProperty(value = "预定离店时间")
	private java.lang.String endtime;

	/**真实入住时间*/
	@Excel(name = "真实入住时间", width = 15)
    @ApiModelProperty(value = "真实入住时间")
	private java.lang.String truebegintime;
	/**真实离店时间*/
	@Excel(name = "真实离店时间", width = 15)
    @ApiModelProperty(value = "真实离店时间")
	private java.lang.String trueendtime;
	/**订单号*/
	@Excel(name = "订单号", width = 15)
    @ApiModelProperty(value = "订单号")
	private java.lang.String orderid;
	/**预订天数*/
	@Excel(name = "预订天数", width = 15)
    @ApiModelProperty(value = "预订天数")
	private java.lang.String preday;
	/**是否是同住人 0 是同住人 1 不是同住人*/
	@Excel(name = "是否是已入住 0 未入住 1 已入住", width = 15)
	@ApiModelProperty(value = "是否是已入住 0 未入住 1 已入住")
	private java.lang.String state;
	/**是否是同住人 0 是同住人 1 不是同住人*/
	@Excel(name = "是否是同住人 0 是同住人 1 不是同住人", width = 15)
    @ApiModelProperty(value = "是否是同住人 0 是同住人 1 不是同住人")
	private java.lang.String isflag;
	/**pdf-name*/
	@Excel(name = "pdf-name", width = 15)
    @ApiModelProperty(value = "pdf-name")
	private java.lang.String pdfname;
	/**头像url*/
	@Excel(name = "头像url", width = 15)
    @ApiModelProperty(value = "头像url")
	private java.lang.String imgurl;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
	private java.lang.String createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15)
    @ApiModelProperty(value = "修改时间")
	private java.lang.String updateTime;

}
