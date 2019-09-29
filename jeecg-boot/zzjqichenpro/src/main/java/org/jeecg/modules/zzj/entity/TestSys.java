package org.jeecg.modules.zzj.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 测试T_SYS
 * @Author: jeecg-boot
 * @Date:   2019-09-10
 * @Version: V1.0
 */
@Data
@TableName("test_sys")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="test_sys对象", description="测试T_SYS")
public class TestSys {
    
	/**主键id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键id")
	private java.lang.String id;
	/**登录账号*/
	@Excel(name = "登录账号", width = 15)
    @ApiModelProperty(value = "登录账号")
	private java.lang.String username;
	/**真实姓名*/
	@Excel(name = "真实姓名", width = 15)
    @ApiModelProperty(value = "真实姓名")
	private java.lang.String realname;
	/**密码*/
	@Excel(name = "密码", width = 15)
    @ApiModelProperty(value = "密码")
	private java.lang.String password;
	/**md5密码盐*/
	@Excel(name = "md5密码盐", width = 15)
    @ApiModelProperty(value = "md5密码盐")
	private java.lang.String salt;
	/**头像*/
	@Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
	private java.lang.String avatar;
	/**生日*/
	@Excel(name = "生日", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生日")
	private java.util.Date birthday;
	/**性别(0-默认未知,1-男,2-女)*/
	@Excel(name = "性别(0-默认未知,1-男,2-女)", width = 15)
    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
	private java.lang.Integer sex;
	/**电子邮件*/
	@Excel(name = "电子邮件", width = 15)
    @ApiModelProperty(value = "电子邮件")
	private java.lang.String email;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
	private java.lang.String phone;
	/**机构编码*/
	@Excel(name = "机构编码", width = 15)
    @ApiModelProperty(value = "机构编码")
	private java.lang.String orgCode;
	/**性别(1-正常,2-冻结)*/
	@Excel(name = "性别(1-正常,2-冻结)", width = 15)
    @ApiModelProperty(value = "性别(1-正常,2-冻结)")
	private java.lang.Integer status;
	/**删除状态(0-正常,1-已删除)*/
	@Excel(name = "删除状态(0-正常,1-已删除)", width = 15)
    @ApiModelProperty(value = "删除状态(0-正常,1-已删除)")
	private java.lang.Integer delFlag;
	/**同步工作流引擎(1-同步,0-不同步)*/
	@Excel(name = "同步工作流引擎(1-同步,0-不同步)", width = 15)
    @ApiModelProperty(value = "同步工作流引擎(1-同步,0-不同步)")
	private java.lang.Integer activitiSync;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
}
