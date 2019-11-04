package org.jeecg.modules.zzj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;

import java.util.List;
import java.util.Map;

public interface KaiLaiOrderMapper  extends BaseMapper<KaiLaiOrder> {
    int generateOrder(Map<String,String> map);

    List<KaiLaiOrder> getOrderByPhone(@Param("phone") String phone);

    KaiLaiOrder getOrderByOrderId(@Param("orderId") String orderId);

    int cancleOrder(@Param("orderId") String orderId);

    int checkInOrder(@Param("orderId") String orderId);

    int checkOutOrder(@Param("orderId") String orderId);

    int updateProfileByRid(Map<String,String> map);
}
