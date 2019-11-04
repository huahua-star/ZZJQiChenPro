package org.jeecg.modules.zzj.util;

import io.netty.util.internal.StringUtil;

import java.util.Map;

public class CheckMapUtil {
    public static boolean checkMap(Map<String,String> map){
        boolean flag=true;
        for (Map.Entry<String,String> entry : map.entrySet()){
            if (StringUtil.isNullOrEmpty(entry.getValue())){
                flag=false;
                break;
            }
        }
        return  flag;
    }

}
