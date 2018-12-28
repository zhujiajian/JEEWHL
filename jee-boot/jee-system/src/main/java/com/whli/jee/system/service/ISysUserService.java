package com.whli.jee.system.service;

import com.whli.jee.core.web.service.IBaseService;
import com.whli.jee.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author whli
 * @version 1.0
 * @since 1.0
 * */
public interface ISysUserService extends IBaseService<SysUser> {

    /**
     * @Desc 用户登录
     * @Author whli
     * @Version 1.0
     * @Date 2018/6/3 0:19
     * @Params [loginName, password]
     * @Return java.lang.String
     */
    public SysUser login(String loginName, String password);

    /**
     * @Desc 用户退出登录
     * @Author whli
     * @Version 1.0
     * @Date 2018/6/3 0:20
     * @Params [token]
     * @Return java.lang.String
     */
    public boolean logout(HttpServletRequest request);

    /**
     * 由登录用户名查询用户
     * @param loginName
     * @return
     */
    public SysUser findSysUserByLoginName(String loginName);

    /**
     * 由登录用户名及密码查询用户
     * @param loginName
     * @return
     */
    public SysUser findSysUserByLoginNameAndPasswrod(String loginName, String password);

    /**
     * 由邮箱查询用户
     * @param email
     * @return
     */
    public SysUser findSysUserByEmail(String email);

    /**
     * @Desc 授权用户角色权限
     * @Author whli
     * @Version 1.0
     * @Date 2018/6/3 0:13
     * @Params [userId, roleIds]
     * @Return void
     */
    public int grantByUser(String userId, List<String> roleIds);

    /**
     * 重置密码
     * @param id
     * @param password
     * @return
     */
    public int resetPassword(String id, String password);
}
