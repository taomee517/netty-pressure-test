package com.demo.netty.util;

import com.demo.netty.entity.MockDevice;
import com.demo.netty.entity.RequestType;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

public class MessageBuilder {

    public static String buildAgAsMsg(RequestType type, MockDevice device) throws Exception{
        String head = "1*9e|" + type.getFunction();
        String imeiInfo = "101," + device.getImei();
        String msg = StringUtils.joinWith("|", head, imeiInfo, buildLoginContentExceptImei(device));
        msg = StringUtils.removeEnd(msg, "|");
        msg = outQuote(msg);
        return msg;
    }

    public static String buildRespMsg(RequestType type, MockDevice device, String tag) throws Exception{
        String head = "1*9e|" + type.getFunction();
        String msg = StringUtils.joinWith("|", head, buildContentByTag(device,tag));
        msg = StringUtils.removeEnd(msg, "|");
        msg = outQuote(msg);
        return msg;
    }

    public static String buildNotSupportResp(String tag) throws Exception{
        String msg = "1*9e|7,443," + tag + "|";
        msg = outQuote(msg);
        return msg;
    }

    public static String buildLoginContentExceptImei(MockDevice device) throws Exception{
        StringBuilder sb = new StringBuilder();
//        Map<String,String> tagInfoMap = new HashMap();
        Class<?> clazz = MockDevice.class;
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length>0) {
            for (Method m : methods) {
                if(StringUtils.countMatches(m.getName(),"getTag")>0){
                    String tag = StringUtils.substringBetween(m.getName(),"getTag","Info");
                    String tagInfo = ((String) m.invoke(device));
//                    tagInfoMap.put(tag,tagInfo);
                    sb.append(tag);
                    sb.append(",");
                    sb.append(tagInfo);
                    sb.append("|");
                }
            }
        }
        return sb.toString();
    }

    public static String buildContentByTag(MockDevice device,String tag) throws Exception{
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = MockDevice.class;
        String methodName = StringUtils.join("getTag", tag, "Info");
        Method method = clazz.getDeclaredMethod(methodName);
        if (Objects.nonNull(method)) {
            String tagInfo = ((String) method.invoke(device));
            sb.append(tag);
            sb.append(",");
            sb.append(tagInfo);
            sb.append("|");
        }
        return sb.toString();
    }

    public static void deviceInfoSetting(MockDevice device,String tag,String value) throws Exception{
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = MockDevice.class;
        String methodName = StringUtils.join("setTag", tag, "Info");
        Method method = clazz.getDeclaredMethod(methodName,String.class);
        if (Objects.nonNull(method)) {
            method.invoke(device,value);
        }
    }

    public static boolean isSupportedTag(MockDevice device,String tag) throws Exception{
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = MockDevice.class;
        String methodName = StringUtils.join("getTag", tag, "Info");
        Method method = clazz.getDeclaredMethod(methodName);
        if (Objects.nonNull(method)) {
            return true;
        }
        return false;
    }


    public static String outQuote(String msg){
        return "(" + msg + ")";
    }
}
