package com.whli.jee.system.controller;

import com.whli.jee.core.anotation.AuthorPermit;
import com.whli.jee.core.page.Page;
import com.whli.jee.core.util.PwdUtils;
import com.whli.jee.core.web.controller.BaseController;
import com.whli.jee.core.web.entity.ResponseBean;
import com.whli.jee.system.entity.SysUser;
import com.whli.jee.system.entity.SysUserRole;
import com.whli.jee.system.service.ISysUserRoleService;
import com.whli.jee.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author whli
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/system/sysUser")
public class SysUserController extends BaseController<SysUser> {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    /**
     * @Desc 用户登录
     * @Author whli
     * @Version 1.0
     * @Date 2018/6/3 10:19
     * @Params [username, password]
     * @Return com.whli.jee.core.web.entity.ResponseBean
     */
    @AuthorPermit
    @PostMapping(value = "/login")
    public ResponseBean login(String username,String password) throws Exception{
        ResponseBean responseBean = new ResponseBean();
        SysUser loginUser = sysUserService.login(username,password);
        responseBean.setResults(loginUser);
        return responseBean;
    }

    /**
     * @Desc 用户退出登录
     * @Author whli
     * @Version 1.0
     * @Date 2018/6/3 10:19
     * @Params
     * @Return
     */
    @AuthorPermit
    @PostMapping(value = "/logout")
    public ResponseBean logout(HttpServletRequest request) throws Exception{
        ResponseBean responseBean = new ResponseBean();
        boolean flag = sysUserService.logout(request);
        responseBean.setResults(flag);
        return responseBean;
    }

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByPage")
    @Override
    public ResponseBean findByPage(@RequestBody SysUser entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        if (entity != null) {
            Page<SysUser> page = new Page<SysUser>(entity.getCurrentPage(),entity.getPageSize());
            page = sysUserService.findByPage(entity,page);
            responseBean.setCount(page.getTotal());
            responseBean.setResults(page.getPageRecords());
        }
        return responseBean;
    }

    /**
     * 添加entity
     *
     * @return
     */
    @PostMapping(value = "/add")
    @Override
    public ResponseBean add(@RequestBody SysUser entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = sysUserService.add(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("新增用户成功！");
        }
        return responseBean;
    }

    /**
     * 更新entity
     *
     * @return
     */
    @PostMapping(value = "/update")
    @Override
    public ResponseBean update(@RequestBody SysUser entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = sysUserService.update(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("修改用户成功！");
        }
        return responseBean;
    }

    /**
     * 删除entity
     *
     * @return
     */
    @PostMapping(value = "/delete")
    @Override
    public ResponseBean delete(@RequestBody SysUser entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        sysUserService.deleteMore(entity);
        responseBean.setResults(true);
        responseBean.setMessage("删除用户成功！");
        return responseBean;
    }

    /**
     * 根据主键查询entity
     *
     * @return
     */
    @PostMapping(value = "/findByPK")
    @Override
    public SysUser findByPK(@RequestBody SysUser entity, HttpServletRequest req) throws Exception {
        return sysUserService.findByPK(entity.getId());
    }

    /**
     * 根据编码查询entity
     *
     * @return
     */
    @PostMapping(value = "/findByNo")
    @Override
    public SysUser findByNo(SysUser entity, HttpServletRequest req) throws Exception {
        return sysUserService.findByNo(entity.getLoginName());
    }

    /**
     * 根据名称查询entity
     *
     * @return
     */
    @PostMapping(value = "/findByName")
    @Override
    public SysUser findByName(SysUser entity, HttpServletRequest req) throws Exception {
        return sysUserService.findByName(entity.getName());
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    @PostMapping(value = "/findAll")
    @Override
    public List<SysUser> findAll(@RequestBody SysUser entity, HttpServletRequest req) throws Exception {
        return sysUserService.findAll(entity);
    }

    /**
     * 授权用户角色
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/grantUser")
    public ResponseBean grantUser(@RequestBody SysUser entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = sysUserService.grantByUser(entity.getId(),entity.getRoleIds());
        if (rows > 0){
            responseBean.setResults(true);
            responseBean.setMessage("授权用户成功！");
        }
        return responseBean;
    }

    /**
     * 根据用户删除对应角色关系
     * @param entity
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/deleteRoleByUser")
    public ResponseBean deleteRoleByUser(@RequestBody SysUserRole entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        sysUserRoleService.deleteByUser(entity);
        responseBean.setResults(true);
        responseBean.setMessage("删除角色成功！");
        return responseBean;
    }

    /**
     * 更新entity
     *
     * @return
     */
    @PostMapping(value = "/resetPassword")
    public ResponseBean resetPassword(@RequestBody SysUser entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = sysUserService.resetPassword(entity.getId(), PwdUtils.md5Encode("123456",entity.getLoginName()));
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("重置密码成功！");
        }
        return responseBean;
    }
}

