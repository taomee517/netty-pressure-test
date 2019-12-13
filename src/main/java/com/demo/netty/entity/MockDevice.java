package com.demo.netty.entity;

import lombok.Data;

@Data
public class MockDevice {
    private boolean agFinish;

    private String imei;
    private String tag102Info = "460043206209430";
    private String tag103Info = "89860412101870499429";
    private String tag104Info = "ovt.otubase.103eb,05010913,release";
    private String tag105Info = "e2.1,0";
    private String tag106Info = "203";
    private String tag10dInfo = "ff3305d93437584d43135027";
    private String tag112Info = "1";
    //工作模式 1-正常 2-展车 3-深度休眠
    private String tag223Info = "2";
    private String tag281Info;
    private String tag282Info;
    private String tag613Info;

    private String tag315Info = "8_btu.CC2640.0_0702.release.0_BT_M_B1b.0.00_mac1804edfdbb59_300,4_flashkey.v1.1_0481.release.0_OST_TYT_A1b.01.0008_300";



    //    private List<String> attachInfos = Arrays.asList("8_btu.CC2640.0_0702.release.0_BT_M_B1b.0.00_mac1804edfdbb59_300","4_flashkey.v1.1_0481.release.0_OST_TYT_A1b.01.0008_300");
}
