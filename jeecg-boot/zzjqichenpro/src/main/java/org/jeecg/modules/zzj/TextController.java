package org.jeecg.modules.zzj;

import io.swagger.models.Xml;
import org.jeecg.modules.zzj.entity.*;
import org.jeecg.modules.zzj.util.PreOccupancyUtil;
import org.jeecg.modules.zzj.util.XmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test/demo")
@Api(tags="测试helloworld")
@Slf4j
public class TextController {
    @GetMapping(value = "/hello")
    @ApiOperation("helloworld")
    public Result<String> hello() {
        Result<String> result = new Result<String>();
        result.setResult("Hello World!");
        result.setSuccess(true);
        log.info(result.toString());
        return result;
    }

    public static void main(String[] args) throws Exception {
        /*Envelope envelope=new Envelope();
        Body body=new Body();
        AppLogin appLogin=new AppLogin();
        appLogin.setUsername("user1");
        appLogin.setUsername("mypassword");
        body.setAppLogin(appLogin);
        envelope.setBody(body);
        String xml=XmlUtil.convertToXml(envelope,"utf-8");
        System.out.println(xml);*/
    }
}
    /*String xml=new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<CheckIn>\r\n" +
            "   <id>123123123</id>\r\n" +
            "   <Test>\n" +
            "       <id>123123123</id>\n" +
            "       <name>xxxx</name>\n" +
            "    </Test>\n" +
            "   <name>张三</name>\n" +
            "   <List>\n" +
            "       <Test>\n" +
            "           <id>123123123</id>\n" +
            "           <name>xxxx</name>\n" +
            "       </Test>\n" +
            "   </List> \n   " +
            "" +
            "</CheckIn>\n"
    );
    CheckIn inCheck= XmlUtil.XmlToBean(xml,CheckIn.class);
        System.out.println(inCheck.getList().size());
                CheckIn checkIn=new CheckIn();
                checkIn.setId("123123123");
                checkIn.setName("xxxxxx");
                Test test=new Test();
                test.setId("23323");
                test.setName("张三");
                List<Test> list=new ArrayList<>();
        list.add(test);
        checkIn.setList(list);
        checkIn.setTest(test);
        String xmlS= XmlUtil.convertToXml(checkIn,"utf-8");
        System.out.println(xmlS);*/