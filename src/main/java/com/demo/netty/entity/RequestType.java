package com.demo.netty.entity;

import lombok.Getter;

@Getter
public enum  RequestType {
    AG("a1","寻址请求"),
    AS("a3","登录请求"),
    QUERY("1","查询"),
    QUERY_ACK("2","查询回复"),
    SETTING("3","设置"),
    SETTING_ACK("4","设置回复"),
    WRITE("5","写入"),
    PUBLISH("7","发布"),
    PUBLISH_ACK("8","发布回复"),


    ;

    private String function;
    private String description;

    RequestType(String function,String description){
        this.description = description;
        this.function = function;
    }
}
