package org.jeecg.modules.zzj.util;

import org.apache.commons.io.IOUtils;
import org.jdom2.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 港中旅接口工具类
 *
 */
public class GangZhongLvInterfaceHttpFactory {
    //ip地址 根据港中旅提供而变动
    private static String host = "58.251.19.224";

    /**
     * 登陆港中旅
     * @return
     * @throws Exception
     */
    public static String  AppLogin() throws Exception{
        //不传入数据时默认位AppLogin接口 返回SessionId
        String GzlUrl="http://58.251.19.224:8081/kws_www/SecurityService.asmx?op=AppLogin";
        String SOAPAction = "http://www.shijinet.com.cn/kunlun/kws/1.1/AppLogin";
        String XmlS = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <AppLogin xmlns=\"http://www.shijinet.com.cn/kunlun/kws/1.1/\">\n" +
                    "      <username>QC</username>\n" +
                    "      <password>8888</password>\n" +
                    "    </AppLogin>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
        String xml = new String();
        //设置soap请求报文的相关属性
        //url从soapUI的request1的RAW标签的POST获取，url中不要有空格
        URL u = new URL(GzlUrl);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        //Host，Content-Type，SOAPAction从soapUI的request1的RAW标签的Host，Content-Typ，SOAPActione获取
        conn.setRequestProperty("Host", host);
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(XmlS.length()));
        conn.setRequestProperty("SOAPAction", SOAPAction);
        conn.setRequestMethod("POST");
        //定义输出流
        OutputStream output = conn.getOutputStream();
        if (null != XmlS) {
            byte[] b = XmlS.toString().getBytes("utf-8");
            //发送soap请求报文
            output.write(b, 0, b.length);
        }
        output.flush();
        output.close();
        //定义输入流，获取soap响应报文
        InputStream input = conn.getInputStream();
        //需设置编码格式，否则会乱码
        xml= IOUtils.toString(input, "UTF-8");
        input.close();

        return xml;
    }

    /**
     * 港中旅接口工具类
     * @param GzlUrl 港中旅接口路径
     * @param SOAPAction soap路径
     * @param XmlS String类型xml数据
     * @return
     * @throws Exception
     */
    public static String soapSpecialConnection(String GzlUrl,String SOAPAction,String XmlS,String sessionId) throws Exception{
        //不传入数据时默认位AppLogin接口 返回SessionId
        String xml = new String();
        //设置soap请求报文的相关属性
        //url从soapUI的request1的RAW标签的POST获取，url中不要有空格
        URL u = new URL(GzlUrl);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        //Host，Content-Type，SOAPAction从soapUI的request1的RAW标签的Host，Content-Typ，SOAPActione获取
        conn.setRequestProperty("Host", host);
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(XmlS.length()));
        conn.setRequestProperty("SOAPAction", SOAPAction);
        conn.setRequestMethod("POST");
        //定义输出流
        OutputStream output = conn.getOutputStream();
        if (null != XmlS) {
            byte[] b = XmlS.toString().getBytes("utf-8");
            //发送soap请求报文
            output.write(b, 0, b.length);
        }
        output.flush();
        output.close();
        //定义输入流，获取soap响应报文
        InputStream input = conn.getInputStream();
        //需设置编码格式，否则会乱码
        xml= IOUtils.toString(input, "UTF-8");
        input.close();
        //不等于空的时候注销
        if(GzlUrl !="" || !GzlUrl.equals("")){
            System.out.println("注销登陆");
            XmlS = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Header>\n" +
                    "    <KwsSoapHeader xmlns=\"http://www.shijinet.com.cn/kunlun/kws/1.1/\">\n" +
                    "      <SessionId>"+sessionId+"</SessionId>\n" +
                    "      <RetCode></RetCode>\n" +
                    "      <ErrReason></ErrReason>\n" +
                    "    </KwsSoapHeader>\n" +
                    "  </soap:Header>\n" +
                    "  <soap:Body>\n" +
                    "    <AppLoginOut xmlns=\"http://www.shijinet.com.cn/kunlun/kws/1.1/\">\n" +
                    "      <sid>"+sessionId+"</sid>\n" +
                    "    </AppLoginOut>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            GzlUrl = "http://58.251.19.224:8081/kws_www/SecurityService.asmx?op=AppLoginOut";
            SOAPAction = "http://www.shijinet.com.cn/kunlun/kws/1.1/AppLoginOut";
            String zhuxiao = zhuxiao(GzlUrl,SOAPAction,XmlS);
            Document document=XmlUtil.strXmlToDocument(zhuxiao);
            String RetCode=XmlUtil.getValueByElementName(document,"RetCode");
            if(!"6001".equals(RetCode)){
                return "注销失败";
            }
        }
        return xml;
    }
    public static String zhuxiao(String GzlUrl,String SOAPAction,String XmlS) throws Exception{
        //不传入数据时默认位AppLogin接口 返回SessionId
        String xml = new String();
        //设置soap请求报文的相关属性
        //url从soapUI的request1的RAW标签的POST获取，url中不要有空格
        URL u = new URL(GzlUrl);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        //Host，Content-Type，SOAPAction从soapUI的request1的RAW标签的Host，Content-Typ，SOAPActione获取
        conn.setRequestProperty("Host", host);
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(XmlS.length()));
        conn.setRequestProperty("SOAPAction", SOAPAction);
        conn.setRequestMethod("POST");
        //定义输出流
        OutputStream output = conn.getOutputStream();
        if (null != XmlS) {
            byte[] b = XmlS.toString().getBytes("utf-8");
            //发送soap请求报文
            output.write(b, 0, b.length);
        }
        output.flush();
        output.close();
        //定义输入流，获取soap响应报文
        InputStream input = conn.getInputStream();
        //需设置编码格式，否则会乱码
        xml= IOUtils.toString(input, "UTF-8");
        input.close();
        return xml;
    }
}
