package com.whli.jee.system.dao;

import com.whli.jee.core.web.dao.IBaseDao;
import com.whli.jee.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author whli
 * @version 1.0
 * @since 1.0
 * */
@Repository
public interface ISysUserDao extends IBaseDao<SysUser> {

    /**
     * 由登录用户名查询用户
     * @param loginName
     * @return
     */
    public SysUser findSysUserByLoginName(@Param("loginName") String loginName);

    /**
     * 由登录用户名查询用户
     * @param loginName
     * @return
     */
    public SysUser findSysUserByLoginNameAndPassword(@Param("loginName") String loginName, @Param("password") String password);

    /**
     * 由邮箱查询用户
     * @param email
     * @return
     */
    public SysUser findSysUserByEmail(@Param("email") String email);

    /**
     * 重置密码
     * @param id
     * @param password
     * @return
     */
    public int resetPassword(String id, String password);
}
