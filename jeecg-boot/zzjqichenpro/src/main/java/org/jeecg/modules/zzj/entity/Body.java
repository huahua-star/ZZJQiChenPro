package org.jeecg.modules.zzj.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="soap:Body")
public class Body {
    @XmlElement(name="AppLoginResponse")
    private AppLoginResponse appLoginResponse;
}
