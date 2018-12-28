package com.whli.jee.system.service;

import com.whli.jee.core.web.service.IBaseService;
import com.whli.jee.system.entity.SysRoleMenu;

import java.util.List;

/**
 *
 * @author whli
 * @version 1.0
 * @since 1.0
 * */
public interface ISysRoleMenuService extends IBaseService<SysRoleMenu> {

    /**
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    public int grantByRole(String roleId, List<String> menuIds);
}
