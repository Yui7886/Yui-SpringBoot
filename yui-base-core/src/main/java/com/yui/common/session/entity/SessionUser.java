package com.yui.common.session.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 会话用户
 *
 * @author yui
 */
@Data
public class SessionUser implements Serializable {

    /**
     * 人员名称
     */
    private String personId;
    private String userName; //登录名称
    private String password; //密码

    private Long departmentId; //部门ID
    private String departmentName; //部门名称
    private String positionName; //职位名称
    private String personName; //人员名称
    private String personSex; //人员性别
    private String personBirthday; //人员生日

    // private String token; //token字符串

    private List<String> powerList;  //权限标识

    private List<String> roleList;  //角色标识或编码

    private String lastLoginTime; //本次登录时间

    private String UUID; //本次登录的uuid

    private String Url;  //本次登录的url

    private String ip; //本次登录访问ip


}
