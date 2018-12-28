package com.whli.jee.system.service.impl;

import com.whli.jee.core.constant.SysConstants;
import com.whli.jee.core.exception.ApplicationException;
import com.whli.jee.core.util.*;
import com.whli.jee.core.web.dao.IBaseDao;
import com.whli.jee.core.web.service.impl.BaseServiceImpl;
import com.whli.jee.system.dao.ISysUserDao;
import com.whli.jee.system.dao.ISysUserRoleDao;
import com.whli.jee.system.entity.SysUser;
import com.whli.jee.system.entity.SysUserRole;
import com.whli.jee.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author whli
 * @version 1.0
 * @since 1.0
 */
@Service(value = "sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements ISysUserService {

    @Autowired
    private ISysUserDao sysUserDao;
    @Autowired
    private ISysUserRoleDao sysUserRoleDao;

    @Override
    public IBaseDao<SysUser> getDao() {
        return sysUserDao;
    }

    @Override
    public int add(SysUser entity) {

        SysUser temp = findSysUserByLoginName(entity.getLoginName());
        if (BeanUtils.isNotNull(temp)) {
            throw new ApplicationException("-02010801", "该用户已存在！");
        }

        temp = findSysUserByEmail(entity.getEmail());
        if (BeanUtils.isNotNull(temp)) {
            throw new ApplicationException("-02010802", "邮箱已被其他用户绑定！");
        }

        entity.setEnable(1);
        entity.setPassword(PwdUtils.md5Encode("123456", entity.getLoginName()));

        return super.add(entity);
    }

    @Override
    public int update(SysUser entity) {
        if (StringUtils.isNotNullOrBlank(entity.getPassword())){
            entity.setPassword(PwdUtils.md5Encode(entity.getPassword(), entity.getLoginName()));
        }
        return super.update(entity);
    }

    /**
     * @Desc 用户登录
     * @Author whli
     * @Version 1.0
     * @Date 2018/6/3 0:20
     * @Params [loginName, password]
     * @Return java.lang.String
     */
    @Override
    public SysUser login(String loginName, String password) {
        if (StringUtils.isNullOrBlank(loginName)){
            throw new ApplicationException("-01010101","登录名不能为空！");
        }

        if (StringUtils.isNullOrBlank(password)){
            throw new ApplicationException("-01010102","用户密码不能为空！");
        }

        SysUser sysUser = findSysUserByLoginNameAndPasswrod(loginName,PwdUtils.md5Encode(password,loginName));
        if (BeanUtils.isNull(sysUser)){
            throw new ApplicationException("-01010103","用户名或密码错误！");
        }

        Integer enabled = sysUser.getEnable();
        if (BeanUtils.isNull(enabled) || enabled.compareTo(0) == 0){
            throw new ApplicationException("-01010104","该用户禁止登录，请联系管理人员！");
        }
        String token = BeanUtils.getUUID();
        JedisUtils.hSet(token, SysConstants.LOGIN_NAME, sysUser.getLoginName());
        JedisUtils.hSet(token, SysConstants.LOGIN_USERID, sysUser.getId());
        JedisUtils.expireDefault(token);
        sysUser.setToken(token);
        sysUser.setPassword("");
        return sysUser;
    }

    /**
     * @Desc 用户退出登录
     * @Author whli
     * @Version 1.0
     * @Date 2018/6/3 0:20
     * @Params [token]
     * @Return java.lang.String
     */
    @Override
    public boolean logout(HttpServletRequest request) {
        String token = request.getHeader(SysConstants.AUTHORIZATION);
        return JedisUtils.delete(token);
    }

    /**
     * 根据登录名检查用户是否存在
     *
     * @param loginName
     * @return
     */
    @Override
    public SysUser findSysUserByLoginName(String loginName) {
        SysUser currentUser = null;
        if (StringUtils.isNotNullOrBlank(loginName)) {
            currentUser = sysUserDao.findSysUserByLoginName(loginName);
        }
        return currentUser;
    }

    @Override
    public SysUser findSysUserByLoginNameAndPasswrod(String loginName, String password) {
        if (StringUtils.isNotNullOrBlank(loginName) && StringUtils.isNotNullOrBlank(password)) {
            return sysUserDao.findSysUserByLoginNameAndPassword(loginName, password);
        }
        return null;
    }

    @Override
    public SysUser findSysUserByEmail(String email) {
        SysUser currentUser = null;
        if (StringUtils.isNotNullOrBlank(email)) {
            currentUser = sysUserDao.findSysUserByEmail(email);
        }
        return currentUser;
    }

    @Override
    public int grantByUser(String userId, List<String> roleIds) {

        if (StringUtils.isNullOrBlank(userId)){
            throw new ApplicationException("-01010105","请选择需要授权的用户！");
        }

        int rows = 0;
        if (CollectionUtils.isNotNullOrEmpty(roleIds)) {
            List<SysUserRole> userRoles = new ArrayList<SysUserRole>();
            for (String roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                userRoles.add(userRole);
                sysUserRoleDao.add(userRole);
                rows++;
            }
        }
        return rows;
    }

    @Override
    public int resetPassword(String id, String password) {
        if (StringUtils.isNullOrBlank(id) || StringUtils.isNullOrBlank(password)){
            throw new ApplicationException("-01010106","新密码不能为空！");
        }

        return sysUserDao.resetPassword(id,password);
    }
}
