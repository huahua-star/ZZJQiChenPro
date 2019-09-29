package org.jeecg.modules.zzj.entity;

import com.sun.xml.internal.txw2.annotation.XmlElement;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Test")
public class Test {
    private String id;
    private String name;
}
