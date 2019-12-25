package com.demo.netty.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class ConvertUtil {
    public static final String OTU_PROTOCOL_START_SIGN = "(";
    public static final String OTU_PROTOCOL_END_SIGN = ")";

    public static String decrypt(String in){
        if (StringUtils.isBlank(in)) {
            return null;
        }
        if ("()".equals(in)) {
            return "()";
        }
        if (in.startsWith(OTU_PROTOCOL_START_SIGN)) {
            in = in.substring(1, in.lastIndexOf(OTU_PROTOCOL_END_SIGN));
        }
        if (StringUtils.isBlank(in)) {
            return null;
        }
        //是否是加密消息
        boolean isEncryptMsg = in.startsWith("*");
        String strData;
        //寻址消息没有加密, 其它消息都是加密的
        if ("a1".equalsIgnoreCase(in.split("\\|")[1])) {
            strData = in;
            log.info("解析加密消息：{}", outBracket(strData));
        } else {
            if (isEncryptMsg) {
                strData = CRCUtil.asiccCrcDecode(in);
            } else {
                strData = CRCUtil.isEncoded(in);
            }
        }
        String result = "(1" + strData + OTU_PROTOCOL_END_SIGN;
        return result;
    }

    public static String encrypt(String in){
        if(in.startsWith(OTU_PROTOCOL_START_SIGN)){
            in = in.substring(1,in.indexOf(OTU_PROTOCOL_END_SIGN));
        }
        if(in.startsWith("1")){
            in = in.substring(1);
        }
        return outBracket(CRCUtil.asiccCrcEncode(in));
    }


    /** 在消息最外层加上括号 */
    public static String outBracket(String content){
        return StringUtils.join(OTU_PROTOCOL_START_SIGN, content, OTU_PROTOCOL_END_SIGN);
    }
}
