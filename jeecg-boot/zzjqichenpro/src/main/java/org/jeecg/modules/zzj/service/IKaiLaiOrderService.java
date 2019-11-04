package org.jeecg.modules.zzj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;

import java.util.List;
import java.util.Map;

public interface IKaiLaiOrderService  extends IService<KaiLaiOrder> {
    int generateOrder(Map<String,String> map);
    List<KaiLaiOrder> getOrderByPhone(String phone);
    KaiLaiOrder getOrderByOrderId(String orderId);
    int cancleOrder(String orderId);
    int checkInOrder(String orderId);
    int checkOutOrder(String orderId);
    int updateProfileByRid(Map<String,String> map);
}
