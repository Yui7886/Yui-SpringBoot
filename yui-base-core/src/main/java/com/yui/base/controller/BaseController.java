package com.yui.base.controller;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;


/**
 * 基础业务接口控制器
 *
 * @author yui
 */
@Controller
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Value("${spring.profiles.active:develop}")
    private String profileName;

    /**
     * 把实体类转成JSON字符串
     *
     * @param obj 实体类
     * @return {@link String}
     */
    protected String toString(Object obj) {
        return JSONUtil.toJsonStr(obj);
    }

}
