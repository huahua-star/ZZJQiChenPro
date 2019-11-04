package org.jeecg.modules.zzj.entity;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Envelope",namespace = "http://schemas.xmlsoap.org/soap/envelope")
public class Envelope {
    @XmlElement(name="soap:Header")
    private Header header;
    @XmlElement(name="soap:Body")
    private Body body;
}
