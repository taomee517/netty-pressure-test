package com.demo.netty.entity;

import lombok.Data;

@Data
public class MockDevice {
    private boolean agFinish;

    private String imei;
    private String tag101Info;
    private String tag102Info = "460043206209430";
    private String tag103Info = "89860412101870499429";
    private String tag104Info = "ovt.otubase.103eb,05010913,release";
    private String tag105Info = "e2.1,0";
    private String tag106Info = "203";
    private String tag10cInfo = "010,010,010,000,010,010,000,000,000,000,001,001,001,010,010,010,000,000,000,000,000,000";
    private String tag10dInfo = "ff3305d93437584d43135027";
    private String tag112Info = "1";
    private String tag622Info = "0";
    private String tag6a3Info;


    private String tag206Info;
    private String tag20fInfo;
    private String tag281Info;
    private String tag282Info;

    private String tag331Info = "12090d03092f,1,E,10629.7228,N,2937.1144,0,0,9,2200,2222222,22222,000000,110,22,1,7454,0,212,505";

    private String tag443Info;

    /**控制及执行结果*/
    //上锁
    private String tag411Info;
    //解锁
    private String tag412Info;
    //寻车
    private String tag413Info;
    //静音
    private String tag414Info;
    //点火
    private String tag415Info;
    //熄火
    private String tag416Info;
    //关窗
    private String tag417Info;
    //开窗
    private String tag418Info;
    //关天窗
    private String tag419Info;
    //开天窗
    private String tag41aInfo;
    //通油
    private String tag41bInfo;
    //断油
    private String tag41cInfo;
    //强制断油
    private String tag41dInfo;
    //授权
    private String tag41eInfo;
    //夺权
    private String tag41fInfo;

    /**控制*/
    //上锁
    private String tag511Info;
    //解锁
    private String tag512Info;
    //寻车
    private String tag513Info;
    //静音
    private String tag514Info;
    //点火
    private String tag515Info;
    //熄火
    private String tag516Info;
    //关窗
    private String tag517Info;
    //开窗
    private String tag518Info;
    //关天窗
    private String tag519Info;
    //开天窗
    private String tag51aInfo;
    //通油
    private String tag51bInfo;
    //断油
    private String tag51cInfo;
    //强制断油
    private String tag51dInfo;
    //授权
    private String tag51eInfo;
    //夺权
    private String tag51fInfo;


    private String tag613Info;
    private String tag315Info = "8_btu.CC2640.0_0702.release.0_BT_M_B1b.0.00_mac1804edfdbb59_300,4_flashkey.v1.1_0481.release.0_OST_TYT_A1b.01.0008_300";



    //    private List<String> attachInfos = Arrays.asList("8_btu.CC2640.0_0702.release.0_BT_M_B1b.0.00_mac1804edfdbb59_300","4_flashkey.v1.1_0481.release.0_OST_TYT_A1b.01.0008_300");
}
