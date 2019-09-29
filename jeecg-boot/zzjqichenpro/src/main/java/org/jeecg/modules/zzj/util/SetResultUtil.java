package org.jeecg.modules.zzj.util;

import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;

public class SetResultUtil {
    public static <T> Result<T> setResult(Result<T> result,String message,Integer code,T returnResult,Boolean flag){
        result.setMessage(message);
        result.setSuccess(flag);
        result.setResult(returnResult);
        result.setCode(code);
        return result;
    }
}
