@XmlSchemaTypes(value={
        @XmlSchemaType(namespace = "http://schemas.xmlsoap.org/soap/envelope",name = "envelope"),
        @XmlSchemaType(namespace = "http://www.w3.org/2001/XMLSchema-instance",name = "instance"),
        @XmlSchemaType(namespace = "http://www.w3.org/2001/XMLSchema",name = "XMLSchema"),
        @XmlSchemaType(namespace = "http://www.shijinet.com.cn/kunlun/kws/1.1",name = "kws")
})
package org.jeecg.modules.zzj.entity;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSchemaTypes;