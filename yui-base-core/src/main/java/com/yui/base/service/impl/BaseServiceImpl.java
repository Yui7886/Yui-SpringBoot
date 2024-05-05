package com.yui.base.service.impl;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 基础业务服务实现类
 *
 * @author yui
 */
public class BaseServiceImpl<T1 extends BaseMapper<T2>, T2> extends ServiceImpl<T1, T2> {

}
