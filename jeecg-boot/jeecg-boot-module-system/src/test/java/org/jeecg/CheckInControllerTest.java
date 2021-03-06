package org.jeecg;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.zzj.entity.CheckIn;
import org.jeecg.modules.zzj.mapper.CheckInMapper;
import org.jeecg.modules.zzj.service.ICheckInService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest( classes =JeecgApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckInControllerTest {

    @Resource
    private ICheckInService checkInService;
    @Resource
    private CheckInMapper mapper;
    @Test
    public void add() {
        //判断该人在今天是否已经入住
        //request.getParameterMap 将前端页面 发送的所有 参数 都以 key value的形式传递过来，
        // 且转换为Map<String,String[]>类型的 且不能修改
        /*QueryWrapper<CheckIn> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("idnumber","123");
        queryWrapper.eq("state","1");
        Integer i=mapper.selectCount(queryWrapper);
        List<Map<String,Object>> list=mapper.selectMaps(queryWrapper);
        System.out.println(i);
        for (Map<String,Object> map : list){
            for (Map.Entry<String,Object> obj : map.entrySet()){
                System.out.println("key:"+obj.getKey()+" value:"+obj.getValue());
            }
        }*/
        /*CheckIn checkIn=checkInService.getOne(new QueryWrapper<CheckIn>().eq("idnumber","123"));
        System.out.println(checkIn);
        Map<String, Object> map=checkInService.getMap(new QueryWrapper<CheckIn>());
        for (Map.Entry<String, Object> obj : map.entrySet()){
            System.out.println("key:"+obj.getKey()+"value:"+obj.getValue());
        }
        List<CheckIn> list=checkInService.list();
        System.out.println(list.size());
        for(CheckIn cc : list){
            System.out.println(cc);
        }
        List<CheckIn> handList=mapper.getAll();
        for(CheckIn cc : list){
            System.out.println(cc);
        }*/
        List<CheckIn> XMLhandList=mapper.xmlgetAll();
        for(CheckIn cc : XMLhandList){
            System.out.println(cc);
        }
    }
}