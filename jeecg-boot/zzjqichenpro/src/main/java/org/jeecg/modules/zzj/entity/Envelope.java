package org.jeecg.modules.zzj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="soap:Envelope")
public class Envelope {
    @XmlElement(name="soap:Header")
    private Header header;
    @XmlElement(name="soap:Body")
    private Body body;
}
