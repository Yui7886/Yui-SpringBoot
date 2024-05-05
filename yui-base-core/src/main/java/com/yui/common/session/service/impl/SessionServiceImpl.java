package com.yui.common.session.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.yui.common.session.entity.SessionUser;
import com.yui.common.session.service.ISessionService;
import org.springframework.stereotype.Service;

/**
 * 修改为 sa-token 会话方式
 *
 * @author yui
 */
@Service
public class SessionServiceImpl implements ISessionService {

    public static final String SESSION_KEY = "$$sessionUserKey$$";

    private SessionUser sessionUser;

    private String uuid;

    private String url;

    private String ipAddress;

    /**
     * 获取sa-token会话中的用户对象信息
     *
     * @return {@link SessionUser}
     */
    private SessionUser getThisSessionUser() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        Object obj = StpUtil.getSession().get(SESSION_KEY);
        return obj != null ? (SessionUser) obj : null;
    }

    /**
     * 获取session对象
     *
     * @return {@link SaSession}
     */
    @Override
    public SaSession getSession() {
        return StpUtil.getSession();
    }

    /**
     * 使用sa-token进行会话缓存用户信息
     *
     * @param sessionUser 会话用户
     */
    @Override
    public void setSessionUser(SessionUser sessionUser) {
        if (sessionUser != null) {
            // 不为空设置本次的UUID
            sessionUser.setUUID(IdUtil.simpleUUID());
        }
        // 放入sa-token会话中,线程安全放入
        StpUtil.getSession().set(SESSION_KEY, sessionUser);

    }

    @Override
    public String getUserId() {
        SessionUser thisSessionUser = getThisSessionUser();
        return thisSessionUser != null ? thisSessionUser.getPersonId() : null;
    }

    @Override
    public String getPersonName() {
        SessionUser thisSessionUser = getThisSessionUser();
        if (thisSessionUser == null) {
            return "游客";
        }
        return thisSessionUser.getPersonName();
    }

    @Override
    public String getToken() {
        SessionUser thisSessionUser = getThisSessionUser();
        if (thisSessionUser == null) {
            return null;
        }
        // 得到登录后的token
        return StpUtil.getTokenInfo().getTokenValue();
    }

    @Override
    public SessionUser getSessionUser() {
        return getThisSessionUser();
    }

    @Override
    public void setUuid(String uuid) {
        SessionUser thisSessionUser = getThisSessionUser();
        if (thisSessionUser == null) {
            this.uuid = uuid;
        } else {
            thisSessionUser.setUUID(uuid);
        }
    }

    /**
     * 获取 UUID
     *
     * @return {@link String}
     */
    @Override
    public String getUuid() {
        SessionUser thisSessionUser = getThisSessionUser();
        if (thisSessionUser == null) {
            return this.uuid;
        }
        return thisSessionUser.getUUID();
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取接口地址
     *
     * @return {@link String}
     */
    @Override
    public String getUrl() {
        SessionUser thisSessionUser = getThisSessionUser();
        if (thisSessionUser == null) {
            return this.url;
        }
        return thisSessionUser.getUrl();
    }

    @Override
    public void setIp(String ip) {
        this.ipAddress = ip;
    }

    /**
     * 获取登录 IP
     *
     * @return {@link String}
     */
    @Override
    public String getIp() {
        SessionUser thisSessionUser = getThisSessionUser();
        if (thisSessionUser == null) {
            return this.ipAddress;
        }
        return thisSessionUser.getIp();
    }

}
