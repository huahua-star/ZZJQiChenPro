package org.jeecg.modules.zzj.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="soap:Header")
public class Header {
    @XmlElement(name="KwsSoapHeader")
    private KwsSoapHeader kwsSoapHeader;
}
