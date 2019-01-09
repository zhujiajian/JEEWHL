package com.whli.jee.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.whli.jee.core.constant.SysConstants;
import com.whli.jee.core.exception.ApplicationException;
import com.whli.jee.core.util.*;
import com.whli.jee.core.web.dao.IBaseDao;
import com.whli.jee.core.web.service.impl.BaseServiceImpl;
import com.whli.jee.system.dao.ISysUserDao;
import com.whli.jee.system.dao.ISysUserRoleDao;
import com.whli.jee.system.entity.SysRole;
import com.whli.jee.system.entity.SysUser;
import com.whli.jee.system.entity.SysUserRole;
import com.whli.jee.system.service.ISysRoleService;
import com.whli.jee.system.service.ISysUserService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public IBaseDao<SysUser> getDao() {
        return sysUserDao;
    }

    //@Transactional
    @Override
    public int add(SysUser entity) {

        SysUser temp = findSysUserByLoginName(entity.getLoginName());
        if (BeanUtils.isNotNull(temp)) {
            throw new ApplicationException("-02060901", "【"+entity.getLoginName()+"】该用户已存在！");
        }

        temp = findSysUserByEmail(entity.getEmail());
        if (BeanUtils.isNotNull(temp)) {
            throw new ApplicationException("-02060902", "【"+entity.getEmail()+"】邮箱已被其他用户绑定！");
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
            throw new ApplicationException("-02060903","登录名不能为空！");
        }

        if (StringUtils.isNullOrBlank(password)){
            throw new ApplicationException("-02060904","用户密码不能为空！");
        }

        SysUser sysUser = findSysUserByLoginNameAndPasswrod(loginName,PwdUtils.md5Encode(password,loginName));
        if (BeanUtils.isNull(sysUser)){
            throw new ApplicationException("-02060905","用户名或密码错误！");
        }

        Integer enabled = sysUser.getEnable();
        if (BeanUtils.isNull(enabled) || enabled.compareTo(0) == 0){
            throw new ApplicationException("-02060906","该用户禁止登录，请联系管理人员！");
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
            throw new ApplicationException("-02060907","请选择需要授权的用户！");
        }

        int rows = 0;
        if (CollectionUtils.isNotNullOrEmpty(roleIds)) {
            List<SysUserRole> userRoles = new ArrayList<SysUserRole>();
            for (String roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                List<SysUserRole> sysUserRoles = sysUserRoleDao.findAll(userRole);
                if (CollectionUtils.isNotNullOrEmpty(sysUserRoles)){
                    SysRole role = sysRoleService.findByPK(roleId);
                    throw new ApplicationException("-02060908","角色【"+role.getName()+"】已存在！");
                }
                userRoles.add(userRole);
                sysUserRoleDao.add(userRole);
                rows++;
            }
        }
        return rows;
    }

    @Override
    public int resetPassword(SysUser entity) {
        int rows = 0;
        if (CollectionUtils.isNullOrEmpty(entity.getIds())){
            throw new ApplicationException("-02060909","请选择需要重置密码的用户！");
        }
        for (String id : entity.getIds()){
            SysUser user = findByPK(id);
            sysUserDao.resetPassword(id,PwdUtils.md5Encode("123456",user.getLoginName()));
            rows++;
        }

        return rows;
    }

    //@Transactional
    @Override
    public int importExcel(InputStream stream) {
        int rows = 0;
        try {
            List<SysUser> users = ExcelUtils.importExcel(stream,SysUser.class,new String[]{"loginName",
                    "no","name","email","phone"});
            if (CollectionUtils.isNotNullOrEmpty(users)){
                for (SysUser entity : users){
                    rows += this.add(entity);
                }

            }
            return rows;
        } catch (Exception e) {
            throw new ApplicationException("-02060910","导入用户错误："+e.getMessage());
        }
    }

    @Override
    public void exportTemplate(SysUser entity, HttpServletResponse response) {
        try {
            ExcelUtils.exportExcel(response, new BaseExcel() {
                @Override
                public String fileName() {
                    return "用户模板";
                }

                @Override
                public LinkedHashMap<String, String> headers() {
                    LinkedHashMap<String,String> headers = new LinkedHashMap<String, String>();
                    headers.put("loginName","用户名");
                    headers.put("no","工号");
                    headers.put("name","用户姓名");
                    headers.put("email","邮件");
                    headers.put("phone","联系方式");
                    return headers;
                }

                @Override
                public List<Map<String, Object>> datas() {
                    SysUser user = new SysUser();
                    user.setLoginName("whli");
                    user.setNo("00001");
                    List<SysUser> lines = new ArrayList<SysUser>();
                    lines.add(user);
                    try {
                        CollectionLikeType type = objectMapper.getTypeFactory().constructCollectionLikeType(List.class,Map.class);
                        return objectMapper.readValue(objectMapper.writeValueAsString(lines),type);
                    } catch (IOException e) {
                        throw new ApplicationException("-04040201",e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            throw new ApplicationException("-04040201","导出用户模板错误："+e.getMessage());
        }
    }
}
