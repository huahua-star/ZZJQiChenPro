package org.jeecg.modules.zzj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: mini吧
 * @Author: jeecg-boot
 * @Date:   2019-09-19
 * @Version: V1.0
 */
@Data
@TableName("qc_food")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="qc_food对象", description="mini吧")
public class Food {
    
	/**mini吧*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "mini吧")
	private java.lang.Integer id;
	/**价格*/
	@Excel(name = "价格", width = 15)
    @ApiModelProperty(value = "价格")
	private java.math.BigDecimal price;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
	private java.lang.String type;
}
