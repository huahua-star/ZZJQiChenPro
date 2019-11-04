package org.jeecg.modules.zzj.util;

import lombok.experimental.SuperBuilder;
import org.apache.poi.ss.formula.functions.T;
import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.bind.*;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {
    /*public static String getXmlMessageByName(String xmlResult, String nodeName) throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlResult);
        DefaultXPath xPath = new DefaultXPath("//" + nodeName);
        xPath.setNamespaceURIs(Collections.singletonMap("ns1", "http://cn.gov.chinatax.gt3nf.nfzcpt.service/"));
        List list = xPath.selectNodes(doc);
        if (!list.isEmpty() && list.size() > 0) {
            org.dom4j.Element node = (org.dom4j.Element) list.get(0);
            return node.getText();
        }
        return "";
    }*/





    //xml转换为JavaBena
    @SuppressWarnings("unchecked")
    public static <T> T XmlToBean(String xmlPath,Class<T> load) throws Exception {
        JAXBContext context=JAXBContext.newInstance(load);
        Unmarshaller unmarshaller =context.createUnmarshaller();
        StringReader reader = new StringReader(xmlPath);
        SAXParserFactory sax = SAXParserFactory.newInstance();
        sax.setNamespaceAware(false);
        XMLReader xmlReader = sax.newSAXParser().getXMLReader();
        Source source = new SAXSource(xmlReader, new InputSource(reader));
        //return (T) unmarshaller.unmarshal(source);
        return (T) unmarshaller.unmarshal(new StringReader(xmlPath));
    }
    /**
     * JavaBean转换成xml
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXml(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    /**
     * 将字符串类型的XML 转化成Docunent文档结构</p>
     * @param parseStrXml  待转换的xml 字符串
     * @return
     */
    public static Document strXmlToDocument(String parseStrXml){
        StringReader read = new StringReader(parseStrXml);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();   // 新建立构造器
        Document doc = null;
        try {
            doc = (Document) sb.build(source);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     *  根据目标节点名获取值
     * @param doc 文档结构
     * @param finalNodeName  最终节点名
     * @return
     */
    public static String getValueByElementName(Document doc,String finalNodeName){
        Element root = doc.getRootElement();
        HashMap<String,Object> map=new HashMap<String,Object>();
        //调用getChildAllText方法。获取目标子节点的值
        Map<String,Object> resultmap=getChildAllText(doc, root,map);
        String result=(String)resultmap.get(finalNodeName);
        return result;
    }

    /**
     *  递归获得子节点的值
     * @param doc 文档结构
     * @param e  节点元素
     * @param resultmap  递归将值压入map中
     * @return
     */
    public static Map<String ,Object> getChildAllText(Document doc, Element e,HashMap<String,Object> resultmap)
    {
        if (e != null)
        {
            if (e.getChildren() != null)   //如果存在子节点
            {
                List<Element> list = e.getChildren();
                for (Element el : list)    //循环输出
                {
                    if(el.getChildren().size() > 0)   //如果子节点还存在子节点，则递归获取
                    {
                        getChildAllText(doc, el,resultmap);
                    }
                    else
                    {
                        resultmap.put(el.getName(), el.getTextTrim());  //将叶子节点值压入map
                    }
                }
            }
        }
        return resultmap;
    }
    public static String getSendXml(Map<String,String> map,String sessionId,String methodName){
        String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <KwsSoapHeader xmlns=\"http://www.shijinet.com.cn/kunlun/kws/1.1/\">\n"+
                "      <SessionId>"+sessionId+"</SessionId>\n" +
                "    </KwsSoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n"+
                "    <"+methodName +" xmlns=\"http://www.shijinet.com.cn/kunlun/kws/1.1/\">\n" ;
                for (Map.Entry   key : map.entrySet())
                {
                    if (!key.getKey().equals("sessionId")){
                        xml+="<"+key.getKey()+">"+key.getValue()+"</"+key.getKey()+">\n";
                    }
                }
                xml+="</"+methodName+">\n"+
                "  </soap:Body>\n" +
                "</soap:Envelope>";
                return xml;
    }
    public static String getSendNSXml(Map<String,String> map,String sessionId,String methodName){
        String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <KwsSoapHeader xmlns=\"http://www.shijinet.com.cn/kunlun/kws/1.1/\">\n"+
                "      <SessionId>"+sessionId+"</SessionId>\n" +
                "    </KwsSoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n"+
                "    <ns:"+methodName +" xmlns=\"http://www.shijinet.com.cn/kunlun/kws/1.1/\">\n" +
                "       <ns:request>\n " ;
        for (Map.Entry   key : map.entrySet())
        {
            xml+="<ns:"+key.getKey()+">"+key.getValue()+"</ns:"+key.getKey()+">\n";
        }
        xml+="      </ns:request>" +
                "</"+methodName+">\n"+
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        return xml;
    }
}
