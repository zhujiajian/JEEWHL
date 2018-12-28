package com.whli.jee.system.service;

import com.whli.jee.core.web.service.IBaseService;
import com.whli.jee.system.entity.SysUserRole;

/**
 *
 * @author whli
 * @version 1.0
 * @since 1.0
 * */
public interface ISysUserRoleService extends IBaseService<SysUserRole> {

    /**
     * 根据用户删除对应角色关系
     * @param entity
     */
    public void deleteByUser(SysUserRole entity);
}
