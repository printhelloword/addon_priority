package com.biller.addonpriority.util;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@RefreshScope
public class ValueRefreshConfigBean {
    public static String test;

    public void setValueRefreshConfigBean(@Value("${test}") String test) {
        this.test = test;
    }
    //put getter here

    public static String getTest(){
        return test;
    }
}
