package com.demo.netty.util;

import com.demo.netty.entity.MockDevice;
import com.demo.netty.entity.RequestType;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

public class MessageBuilder {
    private static Map<String,Boolean> SUPPORTED_MAP = new HashMap<>();

    public static String buildAgAsMsg(RequestType type, MockDevice device) throws Exception{
        String head = "1*9e|" + type.getFunction();
        String msg = StringUtils.joinWith("|", head, buildLoginContentExceptImei(device));
        msg = outQuote(msg);
        return msg;
    }

    public static String buildRespMsg(RequestType type, MockDevice device, String tag) throws Exception{
        String head = "1*9e|" + type.getFunction();
        String msg;
        if (RequestType.PUBLISH_ACK.equals(type)) {
            msg = StringUtils.joinWith("|", head, tag);
        } else {
            msg = StringUtils.joinWith("|", head, buildContentByTag(device,tag));
        }
        msg = outQuote(msg);
        return msg;
    }

    public static String buildNotSupportResp(String tag) throws Exception{
        String msg = "1*9e|7|443," + tag + "|";
        msg = outQuote(msg);
        return msg;
    }

    public static String buildLoginContentExceptImei(MockDevice device) throws Exception{
        StringBuilder sb = new StringBuilder();
        List<String> loginTags = Arrays.asList("106","101","102","103","622","104","105","10d","112");
        Class<?> clazz = MockDevice.class;
        for (String tag:loginTags) {
            String tagInfo = null;
            if (StringUtils.equals("101",tag)) {
                tagInfo = device.getImei();
            }else{
                String methodName = StringUtils.join("getTag",tag,"Info");
                Method method = clazz.getDeclaredMethod(methodName);
                tagInfo = ((String) method.invoke(device));
            }
            sb.append(tag);
            sb.append(",");
            sb.append(tagInfo);
            sb.append("|");
        }
        return sb.toString();
    }

    public static String buildContentByTag(MockDevice device,String tag) throws Exception{
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = device.getClass();
        String methodName = StringUtils.join("getTag", tag, "Info");
        Method method = clazz.getMethod(methodName);
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
        Class<?> clazz = device.getClass();
        String methodName = StringUtils.join("setTag", tag, "Info");
        Method method = clazz.getMethod(methodName,String.class);
        if (Objects.nonNull(method)) {
            method.invoke(device,value);
        }
    }

    public static boolean isSupportedTag(MockDevice device,String tag) throws Exception{
        String supportedKey = StringUtils.join(device.getImei(),"-", tag);
        Boolean isSupported = SUPPORTED_MAP.get(supportedKey);
        if (Objects.nonNull(isSupported)) {
            return isSupported;
        }
        Class<?> clazz = device.getClass();
        String methodName = StringUtils.join("getTag", tag, "Info");
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if(methodName.equalsIgnoreCase(m.getName())){
                SUPPORTED_MAP.put(supportedKey,Boolean.TRUE);
                return true;
            }
        }
        SUPPORTED_MAP.put(supportedKey,Boolean.FALSE);
        return false;

    }


    public static String outQuote(String msg){
        return "(" + msg + ")";
    }
}
