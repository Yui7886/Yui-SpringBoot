package com.yui.common.session.service;

import cn.dev33.satoken.session.SaSession;
import com.yui.common.session.entity.SessionUser;

/**
 * Session 服务类
 *
 * @author yui
 */
public interface ISessionService {

    /**
     * 获取 userId
     *
     * @return {@link String}
     */
    String getUserId();

    /**
     * 获取人员姓名
     *
     * @return {@link String}
     */
    String getPersonName();

    /**
     * 获取 session
     *
     * @return {@link SaSession}
     */
    SaSession getSession();

    /**
     * 获取 token
     *
     * @return {@link String}
     */
    String getToken();

    /**
     * 获取会话用户
     *
     * @return {@link SessionUser}
     */
    SessionUser getSessionUser();

    /**
     * 使用sa-token进行会话缓存用户信息
     *
     * @param sessionUser 会话用户
     */
    void setSessionUser(SessionUser sessionUser);

    /**
     * 设置 UUID
     *
     * @param uuid UUID
     */
    void setUuid(String uuid);

    /**
     * 获取 UUID
     *
     * @return {@link String}
     */
    String getUuid();

    /**
     * 设置接口地址
     *
     * @param url 网址
     */
    void setUrl(String url);

    /**
     * 获取接口地址
     *
     * @return {@link String}
     */
    String getUrl();

    /**
     * 设置登录 IP
     *
     * @param ip ip
     */
    void setIp(String ip);

    /**
     * 获取登录 IP
     *
     * @return {@link String}
     */
    String getIp();

}
