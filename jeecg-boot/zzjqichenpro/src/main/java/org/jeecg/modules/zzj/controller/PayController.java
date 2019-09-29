package org.jeecg.modules.zzj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.zzj.common.ReturnCode;
import org.jeecg.modules.zzj.common.ReturnMessage;
import org.jeecg.modules.zzj.common.umsips;
import org.jeecg.modules.zzj.entity.CheckIn;
import org.jeecg.modules.zzj.entity.TblTxnp;
import org.jeecg.modules.zzj.service.ITblTxnpService;
import org.jeecg.modules.zzj.util.PreOccupancyUtil;
import org.jeecg.modules.zzj.util.SetResultUtil;
import org.jeecg.modules.zzj.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Api(tags="支付相关功能")
@RestController
@RequestMapping("/zzj/pay")
public class PayController {
    @Autowired
    private ITblTxnpService tblTxnpService;

    static int iRet;
    static char strMemo[] = new char[1024];

    public final static String nonce_str = UuidUtils.getUUID();
    //@Value("${qrDir}")
    private String qrDir;
    //@Value("${alipay.apikey}")
    private String apikey;


    @AutoLog(value = "获取二维码")
    @ApiOperation(value="获取二维码", notes="获取二维码")
    @RequestMapping(value = "/getQrImage")
    public void getQrImage(String filePath, HttpServletRequest request, HttpServletResponse response) {
        log.info("进入getQrImage()方法filePath:{}", filePath);
        try {
            log.info("根据filePath获取到二维码图片");
            FileInputStream inputStream = new FileInputStream(filePath);
            int i = inputStream.available();
            byte[] buff = new byte[i];
            inputStream.read(buff);
            response.setContentType("image/*");
            OutputStream out = response.getOutputStream();
            out.write(buff);
            // 关闭响应输出流
            out.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            log.error("getQrImage()方法出现异常:{}", e.getMessage());
        } catch (IOException e) {
            log.error("getQrImage()方法出现异常:{}", e.getMessage());
        } catch (Exception e) {
            log.error("getQrImage()方法出现异常:{}", e.getMessage());
        }
    }
    @AutoLog(value = "删除二维码")
    @ApiOperation(value="删除二维码", notes="删除二维码")
    @RequestMapping(value = "/deleteQrImage")
    public void deleteQrImage(String filePath) {
        log.info("deleteQrImage()方法filePath:{}", filePath);
        try {
            log.info("根据filePath删除文件");
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            log.error("deleteQrImage()方法出现异常:{}", e.getMessage());
        }
    }
    /**
     * 预授权支付二维码
     *
     * @param payType
     * @param amount
     * @param roomNum
     * @param reservationNumber
     * @return
     */
    @AutoLog(value = "预授权支付二维码")
    @ApiOperation(value="预授权支付二维码", notes="预授权支付二维码")
    @RequestMapping(value = "/empowerpay")
    public Result<TblTxnp> empowerpay(HttpServletResponse response, Integer payType, String amount, String roomNum, String reservationNumber) {
        Result<TblTxnp> result = new Result<TblTxnp>();
        log.info("empowerpay()方法payType:amount:{}roomNum:{}reservationNumber:{}", payType, amount, roomNum, reservationNumber);
        try {
            String outTradeNo = UUID.randomUUID().toString().replace("-", "");
            TblTxnp tbl = new TblTxnp();
            log.info("判断金额是否为零");
            if (amount.equals("0") || amount == "0") {
                log.info("empowerpay()方法执行结束return:{}", ReturnCode.postSuccess, ReturnMessage.moneyZeroError);
                result.setCode(ReturnCode.postSuccess);
                result.setMessage(ReturnMessage.moneyZeroError);
                return result;
            }
            //预授权Util
            PreOccupancyUtil empower = new PreOccupancyUtil();
            log.info("金额不为空 生成二维码");
            Map qrcode = null;
            qrcode = empower.qrcode(payType,amount,qrDir,apikey,nonce_str);
            //}
            tbl.setId(UuidUtils.getUUID());
            tbl.setPreamount(new BigDecimal(amount));
            tbl.setOrderid(outTradeNo);//自己生产的订单号
            tbl.setPreOrderid(reservationNumber);
            tbl.setPaymethod(payType.toString());
            log.info("empowerpay()方法执行结束return:{}",ReturnCode.postSuccess);
           /* Map resultMap = new HashMap();
            resultMap.put("outTradeNo", outTradeNo);
            resultMap.put("filePath", "C:/qrImage/qr-20190529154229714.png");
            return R.ok(resultMap);*/

            if(qrcode!=null){
                //支付宝返回的订单号
                tbl.setOrderid((String) qrcode.get("outTradeNo"));
                tbl.setRoomnum(roomNum);
                tbl.setState("1");
                tbl.setPaytype("1");//预授权支付
                tbl.setAmount(new BigDecimal("0"));
                tblTxnpService.save(tbl);
                result.setCode(ReturnCode.postSuccess);
                result.setMessage(ReturnMessage.success);
                log.info("empowerpay()方法执行结束return:{}",result);
                return result;
            }
            result.setCode(ReturnCode.parameterError);
            result.setMessage(ReturnMessage.logicError);
            log.error("empowerpay()方法执行结束return:{}",result);
            return result;
        } catch (Exception e) {
            log.error("empowerpay()方法出现异常:{}", e.getMessage());
            result.setCode(ReturnCode.parameterError);
            result.setMessage(ReturnMessage.logicError);
            return result;
        }
    }


    @AutoLog(value = "预授权支付状态轮询")
    @ApiOperation(value="预授权支付状态轮询", notes="预授权支付状态轮询")
    @RequestMapping(value = "/query_order")
    public Result<TblTxnp> query_order(HttpServletResponse response,Integer payType, String outTradeNo, String roomNum) {
        log.info("进入query_order()方法payType:{}outTradeNo:{}", payType, outTradeNo);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            PreOccupancyUtil empower = new PreOccupancyUtil();
            log.info("查询支付状态");
            TblTxnp tbl = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid",outTradeNo));
            /*if(payType == 2){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String nowTime = sdf.format(personList.get(0).getCreateTime());
                 BillsDemoController bills = new BillsDemoController();
                Map qrcode =  bills.billQuery(response,outTradeNo,mid, tid, msgSrc,
                        instMid, nowTime, key, APIurl);

                String backType = (String)qrcode.get("status");
                if(backType.equals("TRADE_SUCCESS") || backType =="TRADE_SUCCESS"){
                    //支付成功
                    String merOrderId =  (String) qrcode.get("merOrderId");
                    for (int i = 0; i < personList.size(); i++) {
                        personList.get(i).setStatus(0);
                        personList.get(i).setCompleteTime(new Date());
                        personList.get(i).setOutTradeNo(merOrderId);
                    }
                }else{
                    return R.error();
                }
            }else{*/
            String qrcode = empower.query_order(payType,outTradeNo,apikey,nonce_str);
            JSONObject jsonObj = JSON.parseObject(qrcode);
            String message = jsonObj.get("message").toString();
            System.out.println(message);

            if(message == "SUCCESS" || message.equals("SUCCESS")) {
                String paystatus = jsonObj.get("paystatus").toString();
                System.out.println(paystatus);
                //paystatus 1支付成功 0待付款 2付款失败
                if(paystatus == "1" ||paystatus.equals("1")){
                    tbl.setState("2");
                    tbl.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }else if(paystatus == "0" ||paystatus.equals("0")){
                    result.setMessage(ReturnMessage.forPay);
                    result.setCode(ReturnCode.logicError);
                    return result;
                }else{
                    result.setMessage(ReturnMessage.payFalse);
                    result.setCode(ReturnCode.logicError);
                    return result;
                }
            }else{
                result.setMessage(ReturnMessage.payFalse);
                result.setCode(ReturnCode.logicError);
                return result;
            }

            //}
            //正式环境删除for 直接解开 上面的就可用
            /*for (int i = 0; i < personList.size(); i++) {
                personList.get(i).setStatus(0);
                personList.get(i).setCompleteTime(new Date());
            }
             System.out.println(personList);
             */

            log.info("检测到支付成功修改支付状态");
            tblTxnpService.updateById(tbl);
            result.setCode(ReturnCode.postSuccess);
            result.setMessage(ReturnMessage.success);
            return result;
        } catch (Exception e) {
            log.error("query_order()方法出现异常:{}", e.getMessage());
            result.setMessage(ReturnMessage.Exception);
            result.setCode(ReturnCode.logicError);
            return result;
        }
    }

    /**
     * 查询是否超额
     *
     * @param payType
     * @param outTradeNo
     * @return
     */
    @AutoLog(value = "查询是否超额")
    @ApiOperation(value="查询是否超额", notes="查询是否超额")
    @RequestMapping(value = "/query_excess")
    public Result<TblTxnp> query_excess(Integer payType, String outTradeNo, String amount) {
        log.info("进入query_excess()payType:{}outTradeNo:{}amount:{}", payType, outTradeNo, amount);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("查询要支付金额是否超额");
            PreOccupancyUtil empower = new PreOccupancyUtil();
            String qrcode = empower.query_order(payType, outTradeNo, apikey, nonce_str);
            JSONObject jsonObj = JSON.parseObject(qrcode);
            String paymoney = jsonObj.get("paymoney").toString();//订单的金额
            log.info("获取冻结金额");
            Double a = Double.parseDouble(paymoney);//冻结金额
            log.info("获取已用金额");
            Double b = Double.parseDouble(amount);//已用金额
            BigDecimal p1 = new BigDecimal(paymoney);
            BigDecimal p2 = new BigDecimal(amount);
            log.info("判断已用金额是否大于冻结金额");
            if (a >= b) {
                log.info("未超额query_excess()方法结束return:{}", R.ok(p1.subtract(p2) + ""));
                result.setMessage(p1.subtract(p2)+"");
                result.setCode(ReturnCode.postSuccess);
                return result;//未超额
            } else {
                log.info("超额query_excess()方法结束return:{}", SetResultUtil.setResult(result,ReturnMessage.preExcess+":"+p1.subtract(p2),ReturnCode.preExcess,null,true));
                return SetResultUtil.setResult(result,ReturnMessage.preExcess+":"+p1.subtract(p2)+"",ReturnCode.preExcess,null,true);//超额
            }
        } catch (Exception e) {
            log.error("query_excess()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setResult(result,ReturnMessage.Exception,ReturnCode.erorDateBase,null,false);//异常
        }
    }

    /**
     * 预授权完成
     * 支付的订单金额，执行完成预授权操作，订单金额打款至商家账户
     *
     * @param amount            余额
     * @param reservationNumber opera预定号
     * @return
     */
    @AutoLog(value = "预授权完成")
    @ApiOperation(value="预授权完成", notes="预授权完成")
    @RequestMapping(value = "/finish_order")
    public Result<TblTxnp> finish_order(HttpServletResponse response,String amount, String reservationNumber) {
        log.info("进入finish_order()方法amount:{}reservationNumber:{}", amount,reservationNumber);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            String finish;
            if (amount == null || amount.equals("")) {
                return SetResultUtil.setResult(result,ReturnMessage.moneyZeroError,ReturnCode.lackParameter,null,false);
            }
            //查询预授权信息
            TblTxnp tblTxnps = tblTxnpService
                    .getOne(new QueryWrapper<TblTxnp>().eq("preOrderid", reservationNumber));

            if (tblTxnps == null ) {
                return SetResultUtil.setResult(result,ReturnMessage.orderNotFount,ReturnCode.parameterError,null,false);
            }

            log.info("判断消费金额是否大于预授权金额");
            Double amounts = Double.parseDouble(amount);
            log.info("需要结算的金额amounts:{}" , amounts);
            Double money = Double.parseDouble(tblTxnps.getAmount().toString());
            log.info("预授权金额money:{}" , money);
            if (amounts > money) {
                BigDecimal b1 = new BigDecimal(Double.toString(money));
                BigDecimal b2 = new BigDecimal(Double.toString(amounts));
                log.info("超出预授权金额amounts:{}" ,b2.subtract(b1).doubleValue());
                return SetResultUtil.setResult(result,ReturnMessage.preExcess,ReturnCode.preExcess,null,true);//超额
            } else {
                log.info("结算金额amount:{}",amounts);
                int payType = Integer.parseInt(tblTxnps.getPaymethod());
                log.info("判断支付类型reservationType:{}",payType);
                /*if (reservationType == "nuion" || reservationType.equals("nuion")) {
                    BillsDemoController bills = new BillsDemoController();
                     Double amounts = Double.parseDouble(amount) * 100;
                    String totalFee = amounts.intValue() + "";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String nowTime = sdf.format(personLists.get(0).getCreateTime());
                    Map donePay =  bills( response, totalFee, personLists.get(0).getOutTradeNo(), mid, tid, msgSrc,
                             instMid, nowTime, key, APIurl);
                    String yesPay = (String) donePay.get("secureStatus");
                    //ALL_COMPLETED 担保完成，PARTLY_COMPLETED 担保部分完成
                    if(yesPay =="UNCOMPLETED" ||yesPay.equals("UNCOMPLETED")){
                        //担保未完成	担保未完成的交易不允许直接做反交易
                        return R.error("订单未完成");
                    }
                    if(yesPay =="CANCELED" ||yesPay.equals("CANCELED")){
                        //担保未完成	担保未完成的交易不允许直接做反交易
                        return R.error("订单已撤销无法结算");
                    }
                }*/
                if(payType==0){//银联插卡
                    String cpInReq ;
                    money = Double.parseDouble(amount) * 100;
                    String qian = String.format("%012d", money.intValue());
                    // 订单号前六位是授权号 七位到13后几位是 银行卡号
                    String outTradeNo =tblTxnps.getOrderid();
                    log.info("预授权订单号outTradeNo:{}",outTradeNo);
                    String warrantyNo = outTradeNo.substring(0, 6);//授权号
                    String thistime = outTradeNo.substring(6, 12);//交易时间
                    String card = outTradeNo.substring(12);//银行卡
                    char strMemo[] = new char[1024-card.length()];
                    for (int j =  0; j < 1024-card.length(); j++) {
                        strMemo[j] = ' ';
                    }
                    String str = String.valueOf(strMemo);
                    cpInReq = "00000000" + "11111111" + "10"
                            + qian+ "333333" +  thistime + "555555555555"
                            + warrantyNo + "777777" + card+str + "888";
                    log.info("设置入参");
                    iRet = umsips.instanceDll.UMS_SetReq(cpInReq);
                    if(iRet!=0){
                        log.error("UMS_SetReq()结束参数传入失败iRet:{}",iRet);
                        return SetResultUtil.setResult(result,ReturnMessage.parameterFailure,ReturnCode.parameterError,null,false);
                    }
                }

                //链接外网时候使用：正式结算
                if(payType<=1){
                    PreOccupancyUtil r = new PreOccupancyUtil();
                    finish = r.finish_order(payType,tblTxnps.getOrderid(),amounts.toString(),apikey,nonce_str);
                    if(!checkPreResult(finish)){
                        log.error("finish_order()方法结束return:{}","结算失败，500");
                        return SetResultUtil.setResult(result,ReturnMessage.prePayFailure,ReturnCode.erorDateBase,null,false);
                    }
                }
            }
            //修改预授权为已结算
            tblTxnps.setState("2");
            log.info("检测到结算成功添加实际支付金额");
            tblTxnps.setAmount(new BigDecimal(amount));
/*            personLists.get(0).setCompleteTime(new Date());*/
            Boolean upperson =  tblTxnpService.updateById(tblTxnps);
            log.info("finish_order()方法结束");
            return SetResultUtil.setResult(result,ReturnMessage.success,ReturnCode.postSuccess,tblTxnps,true);
        } catch (Exception e) {
            log.error("finish_order()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setResult(result,ReturnMessage.Exception,ReturnCode.erorDateBase,null,false);//异常
        }
    }
    /**
     *  检测预授权结算情况
     * @param finish 预授权返回的json字符串
     * @return
     */
    private boolean checkPreResult(String finish){
        if(finish == null){
            log.error("finish空");
            return false;
        }
        JSONObject jsonObj = JSON.parseObject(finish);
        String status = jsonObj.get("status").toString();
        if(status != "10000" && !status.equals("10000")){
            log.error("结算失败");
            return false;
        }
        log.info("结算成功");
        return true;
    }
    /**
     * 预授权撤销
     *
     * @param payType
     * @param outTradeNo
     * @param amount
     * @param reservationNumber
     * @return
     */
    @AutoLog(value = "预授权撤销")
    @ApiOperation(value="预授权撤销", notes="预授权撤销")
    @RequestMapping(value = "/cancel_order")
    public Result<TblTxnp> cancel_order(Integer payType, String outTradeNo, String amount, String reservationNumber) {
        log.info("进入cancel_order()方法");
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("根据outTradeNo撤销订单");
            PreOccupancyUtil r = new PreOccupancyUtil();
            String cancel = r.cancel_order(payType, outTradeNo, apikey, nonce_str);
            JSONObject jsonObj = JSON.parseObject(cancel);
            String status = jsonObj.get("status").toString();
            log.info("判断是否撤销成功");
            if (status == "10000" || status.equals("10000")) {
                TblTxnp tblTxnp = tblTxnpService.getOne(new QueryWrapper<TblTxnp>().eq("orderid", outTradeNo));
                tblTxnp.setState("0");
                tblTxnp.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                tblTxnp.setAmount(new BigDecimal(amount));
                log.info("检测到支付成功添加实际支付金额");
                log.info("修改入住信息中的预授权字段");
                tblTxnpService.updateById(tblTxnp);
                log.info("cancel_order()方法结束return:{}", "成功撤销。");
                return SetResultUtil.setResult(result,ReturnMessage.success,ReturnCode.postSuccess,tblTxnp,true);
            }
            log.info("cancel_order()方法结束return:{}","撤销错误");
            return SetResultUtil.setResult(result,ReturnMessage.revocationFailure,ReturnCode.erorDateBase,null,false);
        } catch (Exception e) {
            log.error("cancel_order()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setResult(result,ReturnMessage.Exception,ReturnCode.erorDateBase,null,false);//异常
        }
    }
    /**
     * 第一步
     * 银联预授权 与 预授权完成
     * @param amount
     * @param dealType
     * @param reservationNumber
     * @param roomNum
     * @return
     */
    @AutoLog(value = "银联预授权")
    @ApiOperation(value="银联预授权", notes="银联预授权")
    @RequestMapping(value = "/UMS_Init")
    private Result<TblTxnp> UMS_SetReq(String amount,Integer dealType,String reservationNumber,String roomNum){
        log.info("UMS_SetReq()进入amount:{}dealType:{}reservationNumber{}roomNum{}",amount,dealType,reservationNumber,roomNum);
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            //Todo  需要 金额 amount 转成 000000000001格式  单位为分
            if(reservationNumber==null){
                return SetResultUtil.setResult(result,ReturnMessage.lackParameter,ReturnCode.lackParameter,null,false);
            }
            //初始化
            log.info("初始化");
            iRet = UMS_Init();
            log.info("进入UMS_Init()方法");
            if(iRet!=0){
                log.error("UMS_Init(),UMS_SetReq()方法结束初始化失败");
                return SetResultUtil.setErrorResult(result);
            }
            log.info("UMS_Init()结束");
            //银行卡的卡号#加上空格凑够1024
            for (int i = 0; i < 1024; i++) {
                strMemo[i] = ' ';
            }
            Date thisTime = new Date();
            SimpleDateFormat fmt=new SimpleDateFormat("YYYYMMDD");
            String cpInReq ;
            //0普通支付 1创建预授权订单
            log.info("根据dealType字段判断支付类型dealType:{}",dealType);
            String payForm ="00"; //普通支付
            if(dealType == 1){
                //预授权支付
                log.info("添加预授权");
                payForm = "08";
            }
            log.info("设置入参");
            Double money = Double.parseDouble(amount) * 100;
            String qian = String.format("%012d", money.intValue());
            cpInReq = "00000000" + "11111111" + payForm
                    + qian + "333333" +  fmt.format(thisTime) + "555555555555"
                    + "666666" + "777777" + String.valueOf(strMemo) + "888";
            iRet = umsips.instanceDll.UMS_SetReq(cpInReq);
            if(iRet!=0){
                log.error("UMS_SetReq()结束参数传入失败iRet:{}",iRet);
                return SetResultUtil.setErrorResult(result);
            }
            log.info("参数传入成功添加订单信息");
            TblTxnp tre = new TblTxnp();
            tre.setId(UuidUtils.getUUID());
            tre.setPreamount(new BigDecimal(amount));
            tre.setPreOrderid(reservationNumber);
            tre.setOrderid(UUID.randomUUID().toString().replace("-", ""));
            tre.setRoomnum(roomNum);
            tre.setCreateTime(new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()));
            // 状态码
            tre.setState("2");
            tre.setPaytype(dealType+"");
            tre.setPaymethod("0");
            tre.setAmount(new BigDecimal("0"));
            tblTxnpService.save(tre);
            log.info("UMS_SetReq()结束");
            return UMS_EnterCard();
        }catch (Exception e) {
            log.error("UMS_SetReq()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }
    }

    /**
     * 进卡操作
     * @return
     */
    private Result<TblTxnp> UMS_EnterCard()  {
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("进入UMS_EnterCard()方法");
            iRet = umsips.instanceDll.UMS_EnterCard();
            if(iRet>2){
                log.error("UMS_EnterCard()结束进卡操作失败iRet:{}",iRet);
                return  SetResultUtil.setErrorResult(result);
            }
            log.error("UMS_EnterCard()结束");
            return SetResultUtil.setResult(result,ReturnMessage.success,ReturnCode.getSuccess,null,true);
        }catch (Exception e){
            log.error("UMS_EnterCard()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setErrorResult(result);
        }

    }
    /**
     * 第三部
     * 卡的位置
     * @return
     */
    @RequestMapping(value = "/UMS_CheckCard")
    public Result<TblTxnp> UMS_CheckCard() {
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("进入UMS_CheckCard()方法");
            byte[] state_out = new byte[1];
            iRet = umsips.instanceDll.UMS_CheckCard(state_out);
            log.info("读取卡的位置iRet:{}",iRet);
            //TODO 有可能需要弹卡
            if (state_out[0] == 52) {
                log.error("UMS_CheckCard()卡在插卡器卡口位置");
                //UMS_EjectCard("卡在插卡器卡口位置,");
                return SetResultUtil.setErrorMsgResult(result,"UMS_CheckCard()结束卡在插卡器卡口位置");
            }else if (state_out[0] == 53) {
                log.error("UMS_CheckCard()结束未检测到卡");
                return SetResultUtil.setErrorMsgResult(result,"UMS_CheckCard()未检测到卡");
            }else if (state_out[0] == 56) {
                log.error("UMS_CheckCard()结束卡在电子现金感应器上");
                //UMS_EjectCard("卡在电子现金感应器上,");
                return SetResultUtil.setErrorMsgResult(result,"UMS_CheckCard()结束卡在电子现金感应器上");
            }else if (state_out[0] == 57){
                log.error("UMS_CheckCard()结束卡在非接联机感应器上");
                //UMS_EjectCard("卡在非接联机感应器上,");
                return SetResultUtil.setErrorMsgResult(result,"卡在非接联机感应器上");
            }
            log.info("UMS_CheckCard()结束");
            return SetResultUtil.setSuccessResult(result);
        } catch (Exception e){
            log.error("UMS_CheckCard()方法出现异常:{}", e.getMessage());
            return SetResultUtil.setExceptionResult(result);
        }

    }


    /**
     * 第四步
     * 读取卡的信息
     * @return
     */
    @RequestMapping(value = "/UMS_ReadCard")
    public Result<TblTxnp> UMS_ReadCard() {
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("进入UMS_ReadCard()方法");
            byte[] cpData = new byte[20];
            //读取卡的信息
            iRet = umsips.instanceDll.UMS_ReadCard(cpData);
            log.info("读取卡信息cpData:{}",new String(cpData));
            if(iRet>4){
                log.error("UMS_ReadCard()结束读取卡信息失败iRet:{}",iRet);
                UMS_EjectCard("读取卡信息失败");
                return SetResultUtil.setErrorMsgResult(result,"读取卡信息失败");
            }
            //开启密码键盘
            int keyboard =UMS_StartPin().getCode();
            if(keyboard != 200){
                UMS_EjectCard("开启密码键盘失败,");
                return SetResultUtil.setErrorMsgResult(result,"开启密码键盘失败");
            }
            return SetResultUtil.setSuccessResult(result);
        } catch (Exception e){
            log.error("UMS_ReadCard()方法出现异常:{}", e.getMessage());
            UMS_EjectCard("读卡信息接口出现异常,");
            return SetResultUtil.setExceptionResult(result);
        }

    }

    /**
     * 轮训客户输入的密码
     * @return
     */
    @RequestMapping(value = "/UMS_GetOnePass")
    public Result<TblTxnp> UMS_GetOnePass() {
        Result<TblTxnp> result = new Result<TblTxnp>();
        log.info("轮训客户输入的密码");
        byte key_out[] = new byte[1];
        while (true) {
            iRet = umsips.instanceDll.UMS_GetOnePass(key_out);
            if (iRet != 0) {
                log.error("UMS_ReadCard()结束输入密码失败");
                return SetResultUtil.setErrorMsgResult(result,"输入密码失败");
            }
            if (key_out[0] == 13) {
                log.info("UMS_ReadCard()结束密码输入完成");
                break;
            } else if (key_out[0] == 2) {
                log.error("UMS_ReadCard()结束密码输入超时");
                //return R.error(2,"密码输入超时");
                return SetResultUtil.setErrorMsgResult(result,"密码输入超时");
            } else if (key_out[0] == 27) {
                log.info("UMS_ReadCard()结束用户取消");
                break;
            } else if (key_out[0] == 8) {
                log.info("退格");
            }
            try {
                Thread.sleep(600);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // RetCode.setText("启动加密返回:"+iRet+"");
        }
        return SetResultUtil.setSuccessResult(result);
    }

    /**
     * 第五步
     * 获取密码
     */
    @RequestMapping(value = "/UMS_GetPin")
    public Result<TblTxnp> UMS_GetPin(String reservationNumber) {
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("进入UMS_GetOnePass()方法");
            iRet = umsips.instanceDll.UMS_GetPin();
            System.out.println("取密文返回:" + iRet);
            if (iRet != 0){
                log.error("UMS_GetOnePass()方法结束读取秘钥失败");
                UMS_EjectCard("读取秘钥失败,");
                return SetResultUtil.setErrorMsgResult(result,"读取秘钥失败");
            }
            log.info("UMS_GetOnePass()方法结束");
            return UMS_TransCard(reservationNumber);
        }catch (Exception e){
            log.error("UMS_GetOnePass()方法出现异常:{}", e.getMessage());
            UMS_EjectCard("UMS_GetOnePass()方法异常,");
            return SetResultUtil.setExceptionResult(result);
        }
    }

    /**
     * 第六步
     * 交易
     * @return
     */
    //@RequestMapping(value = "/UMS_TransCard")
    public Result<TblTxnp> UMS_TransCard(String reservationNumber) throws UnsupportedEncodingException {
        Result<TblTxnp> result = new Result<TblTxnp>();
        try {
            log.info("进入UMS_TransCard()方法reservationNumber:{}",reservationNumber);
            byte[] strReq = new byte[3000];
            byte[] strResp = new byte[3000];
            log.info("确认交易");
            iRet = umsips.instanceDll.UMS_TransCard(strReq, strResp);
            String thisTime =  new String(subBytes(new String(strResp,"gbk").getBytes(), 90, 6));//交易时间
            String warrantyNo = new String(subBytes(new String(strResp,"gbk").getBytes(), 108, 6)); // 授权号
            String cardNo = new String(subBytes(new String(strResp,"gbk").getBytes(), 42, 20)); // 卡号
            String  zong = warrantyNo+thisTime+cardNo;
            TblTxnp tblTxn=tblTxnpService.getOne(new QueryWrapper<TblTxnp>().ge("preOrderid",reservationNumber));
            if(zong!=null){
                tblTxn.setOrderid(zong);
            }
            log.info("修改订单中的订单号用于预授权结算");
            tblTxnpService.updateById(tblTxn);
            log.info("调用弹卡操作");
            //交易成功弹卡
            UMS_EjectCard("");
            log.info("弹卡成功执行读卡器关闭操作");
            iRet = umsips.instanceDll.UMS_CardClose();
            if (iRet != 0){
                log.error("读卡器关闭失败UMS_TransCard()方法结束");
                return SetResultUtil.setErrorMsgResult(result,"读卡器关闭失败");
            }
            log.info("读卡器关闭成功UMS_TransCard()方法结束");
            return SetResultUtil.setSuccessResult(result,new String(strResp,"gbk"));
        } catch (Exception e){
            log.error("UMS_TransCard()方法出现异常:{}", e.getMessage());
            UMS_EjectCard("UMS_TransCard()方法出现异常,");
            return SetResultUtil.setExceptionResult(result);
        }

    }

    /**
     * 弹卡
     * @return
     */
    @RequestMapping(value = "/UMS_EjectCard")
    public Result<TblTxnp> UMS_EjectCard(String shibai) {
        Result<TblTxnp> result = new Result<TblTxnp>();
        iRet = umsips.instanceDll.UMS_EjectCard();
        if(iRet!=0){
            //TODO 可以加入吞卡
            log.error("弹卡失败调用吞卡操作，提醒入住人找前台那银行卡");
            iRet =  umsips.instanceDll.UMS_CardSwallow();
            if(iRet!=0){
                log.error("UMS_TransCard()方法结束吞卡失败");
                return SetResultUtil.setErrorMsgResult(result,"UMS_TransCard()方法结束吞卡失败");
            }
            log.error("UMS_TransCard()方法结束弹卡失败，已吞卡");
            return SetResultUtil.setErrorMsgResult(result,"UMS_TransCard()方法结束弹卡失败，已吞卡");
        }
        return SetResultUtil.setSuccessResult(result,shibai+"弹卡成功");
    }

    //初始化
    public int UMS_Init() {
        iRet = umsips.instanceDll.UMS_Init(1);
        return iRet;
    }

    /**
     * 开启密码键盘
     * @return
     */
    public Result<TblTxnp> UMS_StartPin() {
        Result<TblTxnp> result = new Result<TblTxnp>();
        log.info("进入UMS_StartPin()方法");
        iRet = umsips.instanceDll.UMS_StartPin();
        if(iRet!=0){
            log.error("UMS_StartPin()开启密码键盘失败iRet:{}",iRet);
            return SetResultUtil.setResult(result,ReturnMessage.openKeyboardError,ReturnCode.erorDateBase,null,false);
        }
        return SetResultUtil.setResult(result,ReturnMessage.success,ReturnCode.getSuccess,null,true);
    }

    /**
     * 读取返回信息
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }


}
