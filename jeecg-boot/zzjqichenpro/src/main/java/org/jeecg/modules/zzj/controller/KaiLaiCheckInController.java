package org.jeecg.modules.zzj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.zzj.TTCEPackage.K7X0Util;
import org.jeecg.modules.zzj.entity.CheckIn;
import org.jeecg.modules.zzj.entity.KaiLaiOrder;
import org.jeecg.modules.zzj.entity.KaiLaiRoom;
import org.jeecg.modules.zzj.service.ICheckInService;
import org.jeecg.modules.zzj.service.IKaiLaiOrderService;
import org.jeecg.modules.zzj.service.IKaiLaiRoomService;
import org.jeecg.modules.zzj.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 凯莱对接 皆为调用对方给出的存储过程 （非接口对接）
 */

/**
 * @Description: 凯莱入住功能
 * @Author: jeecg-boot
 * @Date:   2019-10-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="凯莱入住功能")
@RestController
@RequestMapping("/zzj/KaiLaiCheckIn")
public class KaiLaiCheckInController {

    @Autowired
    private IKaiLaiRoomService iKaiLaiRoomService;

    @Autowired
    private IKaiLaiOrderService iKaiLaiOrderService;

    @Value("${sdk.ComHandle}")
    private Integer comHandle;

    @Value("${cardUrl}")
    private String cardUrl;
    /**
     * 无预订单入住--查询可用房型及数量等信息
     * @param
     * @return
     */
    @AutoLog(value = "无预订单入住--查询可用房型及数量等信息")
    @ApiOperation(value="无预订单入住-selectRoomInformation", notes="无预订单入住-selectRoomInformation")
    @GetMapping(value = "/selectRoomInformation")
    public Result<Object> selectRoomInformation(String beginTime,String endTime) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        String message="";
        if (CheckMapUtil.checkMap(map)){
            message="缺少参数";
            SetResultUtil.setLackParamResult(result,message);
        }
        // 查询凯莱的 可用房间 （暂时都为 全天房）
        List<KaiLaiRoom> list= iKaiLaiRoomService.SelectKaiLaiRoom(map);
        String returnString= JSON.toJSONString(list);
        message="成功查询";
        SetResultUtil.setSuccessResult(result,message,returnString);
        return result;
    }

    /**
     * 生成订单
     * 返回true 为生成成功
     */
    @AutoLog(value = "无预订单入住--生成预订单")
    @ApiOperation(value="无预订单入住-generateOrder", notes="无预订单入住-generateOrder")
    @GetMapping(value = "/generateOrder")
    public Result<Object> generateOrder(String beginTime,String endTime,String number,String roomType,String phone){
        Result<Object> result = new Result<Object>();
        String message="";
        Map<String,String> map=new HashMap<>();
        map.put("beginTime",beginTime);//预定入住时间
        map.put("endTime",endTime);//预定离店时间
        map.put("number",number);//预定房间数量
        map.put("roomType",roomType);//房型代码
        map.put("phone",phone);//手机号
        if (CheckMapUtil.checkMap(map)){
            message="缺少参数";
            SetResultUtil.setLackParamResult(result,message);
        }
        int returnResult=iKaiLaiOrderService.generateOrder(map);
        if (returnResult==1){
            SetResultUtil.setSuccessResult(result);
        }else{
            message="创建预订单失败";
            SetResultUtil.setErrorMsgResult(result,message);
        }
        return result;
    }


    /**
     * 无预订单入住-- 返回订单详细信息
     * @param
     * @return
     */
    @AutoLog(value = "无预订单入住--返回订单详细信息")
    @ApiOperation(value="无预订单入住-getOrderByPhone", notes="无预订单入住-getOrderByPhone")
    @GetMapping(value = "/getOrderByPhone")
    public Result<Object> getOrderByPhone(String phone) throws Exception {
        Result<Object> result = new Result<Object>();
        List<KaiLaiOrder> list=iKaiLaiOrderService.getOrderByPhone(phone);
        if (list.size()>0){
            String data=JSON.toJSONString(list);
            SetResultUtil.setSuccessResult(result,"成功查询",data);
        }else{
            String message="无该手机号的预定信息";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }

    /**
     * 根据订单号查询订单信息
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号查询订单信息")
    @ApiOperation(value="根据订单号查询订单信息-getOrderByOrderId", notes="根据订单号查询订单信息-getOrderByOrderId")
    @GetMapping(value = "/getOrderByOrderId")
    public Result<Object> getOrderByOrderId(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
        if (null!=kaiLaiOrder){
            KaiLaiOrder kSelect=iKaiLaiOrderService.getOne(new QueryWrapper<KaiLaiOrder>().eq("resv_name_id",orderId));
            System.out.println(kSelect);
            if(null==kSelect){
                iKaiLaiOrderService.save(kaiLaiOrder);
            }
            SetResultUtil.setSuccessResult(result,"成功查询",kaiLaiOrder);
        }else{
            String message="无该订单号";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }
    /**
     * 根据订单号撤销预订单信息
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号撤销预订单信息")
    @ApiOperation(value="根据订单号撤销预订单信息-cancleOrder", notes="根据订单号撤销预订单信息-cancleOrder")
    @GetMapping(value = "/cancleOrder")
    public Result<Object> cancleOrder(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        int returnKey=iKaiLaiOrderService.cancleOrder(orderId);
        if(returnKey==1)
        {
            SetResultUtil.setSuccessResult(result,"撤销该预订单成功");
        }else{
            String message="撤销失败";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }
    /**
     * 根据订单号修改预订单为已入住状态
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号--修改状态为入住")
    @ApiOperation(value="根据订单号修改状态为入住-checkInOrder", notes="根据订单号修改状态为入住-checkInOrder")
    @GetMapping(value = "/checkInOrder")
    public Result<Object> checkInOrder(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        KaiLaiOrder kaiLaiOrder=iKaiLaiOrderService.getOrderByOrderId(orderId);
        //判断该房间是否绑定了 入住人
        if(StringUtil.isNullOrEmpty(kaiLaiOrder.getIdEntityCard())){
            SetResultUtil.setErrorMsgResult(result,"该房间还未绑定入住人信息");
            return result;
        }
        int returnKey=iKaiLaiOrderService.checkInOrder(orderId);
        if(returnKey==1)
        {
            SetResultUtil.setSuccessResult(result,"修改订单状态为入住成功");
        }else{
            String message="修改订单状态为入住失败";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }
    /**
     * 根据订单号修改预订单为已退房状态
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号--修改状态为退房")
    @ApiOperation(value="根据订单号修改状态为退房-checkOutOrder", notes="根据订单号修改状态为退房-checkOutOrder")
    @GetMapping(value = "/checkOutOrder")
    public Result<Object> checkOutOrder(String orderId) throws Exception {
        Result<Object> result = new Result<Object>();
        int returnKey=iKaiLaiOrderService.checkOutOrder(orderId);
        if(returnKey==1)
        {
            SetResultUtil.setSuccessResult(result,"修改订单状态为退房状态成功");
        }else{
            String message="修改订单状态为退房状态失败";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }

    /**
     * 根据订单号 修改预订单信息  添加入住人
     * @param
     * @return
     */
    @AutoLog(value = "根据订单号--添加入住人")
    @ApiOperation(value="根据订单号添加入住人-checkOutOrder", notes="根据订单号添加入住人-checkOutOrder")
    @GetMapping(value = "/updateProfileByRid")
    public Result<Object> updateProfileByRid(String orderId,String surName,String reputation,String name,
            String gender,String borthDay,String nation,String address,String idNumber) throws Exception {
        Result<Object> result = new Result<Object>();
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("surName",surName);
        map.put("reputation",reputation);
        map.put("name",name);
        map.put("gender",gender);
        map.put("borthDay",borthDay);
        map.put("nation",nation);
        map.put("address",address);
        map.put("idNumber",idNumber);
        boolean flag=CheckMapUtil.checkMap(map);
        if(!flag){
            SetResultUtil.setLackParamResult(result,"缺少参数");
            return result;
        }
        int returnKey=iKaiLaiOrderService.updateProfileByRid(map);
        if(returnKey==1)
        {
            SetResultUtil.setSuccessResult(result,"修改订单信息-添加入住人成功");
        }else{
            String message="修改订单信息-添加入住人失败";
            SetResultUtil.setNotFoundResult(result,message);
        }
        return result;
    }


}
