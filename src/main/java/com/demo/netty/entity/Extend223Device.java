package com.demo.netty.entity;

import lombok.Data;

@Data
public class Extend223Device extends MockDevice {
    /**工作模式 1-正常 2-展车 3-深度休眠*/
    private String tag223Info = "2";
}
