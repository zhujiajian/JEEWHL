package com.whli.jee.system.controller;

import com.whli.jee.core.page.Page;
import com.whli.jee.core.util.BeanUtils;
import com.whli.jee.core.util.CollectionUtils;
import com.whli.jee.core.util.StringUtils;
import com.whli.jee.core.util.WebUtils;
import com.whli.jee.core.web.controller.BaseController;
import com.whli.jee.core.web.entity.BaseTree;
import com.whli.jee.core.web.entity.ResponseBean;
import com.whli.jee.system.entity.SysDict;
import com.whli.jee.system.entity.SysMenu;
import com.whli.jee.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统资源
 * @author whli
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/system/sysMenu")
public class SysMenuController extends BaseController<SysMenu> {

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByPage")
    @Override
    public ResponseBean findByPage(@RequestBody SysMenu entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        if (BeanUtils.isNotNull(entity)){
            if (StringUtils.isNullOrBlank(entity.getParentId())){
                entity.setParentId("");
            }
            Page<SysMenu> page = new Page<SysMenu>(entity.getCurrentPage(),entity.getPageSize());
            page = sysMenuService.findByPage(entity, page);
            responseBean.setCount(page.getTotal());
            responseBean.setResults(page.getPageRecords());
        }
        return responseBean;
    }

    /**
     * 添加entity
     * @param entity
     * @return
     */
    @PostMapping(value = "/add")
    @Override
    public ResponseBean add(@RequestBody SysMenu entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        entity.setEnable(1);
        int rows = sysMenuService.add(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage( "新增系统资源成功！");
        }
        return responseBean;
    }

    /**
     * 更新entity
     * @param entity
     * @return
     */
    @PostMapping(value = "/update")
    @Override
    public ResponseBean update(@RequestBody SysMenu entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = sysMenuService.update(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("修改系统资源成功！");
        }
        return responseBean;
    }

    /**
     * 删除entity
     * @param entity
     * @return
     */
    @PostMapping(value = "/delete")
    @Override
    public ResponseBean delete(@RequestBody SysMenu entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        sysMenuService.deleteMore(entity);
        responseBean.setResults(true);
        responseBean.setMessage( "删除系统资源成功！");
        return responseBean;
    }

    /**
     * 根据主键查询entity
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByPK")
    @Override
    public SysMenu findByPK(@RequestBody SysMenu entity, HttpServletRequest req) throws Exception {
        return sysMenuService.findByPK(entity.getId());
    }

    /**
     * 根据编码查询
     * @param entity
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/findByNo")
    @Override
    public SysMenu findByNo(SysMenu entity, HttpServletRequest req) throws Exception {
        return null;
    }

    /**
     * 根据名称查询
     * @param entity
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/findByName")
    @Override
    public SysMenu findByName(SysMenu entity, HttpServletRequest req) throws Exception {
        return sysMenuService.findByName(entity.getName());
    }

    /**
     * 查询所有数据
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/findAll")
    @Override
    public List<SysMenu> findAll(@RequestBody SysMenu entity, HttpServletRequest req) throws Exception {
        return sysMenuService.findAll(entity);
    }

    /**
     * 根据父ID查询菜单
     *
     * @return
     */
    @PostMapping(value = "/findByTree")
    public List<BaseTree> findByTree(@RequestBody SysMenu entity, HttpServletRequest req) throws Exception {
        return sysMenuService.findByTree(entity.getRoleId());
    }

    /**
     * 根据登录用户及其角色查询菜单
     *
     * @param entity
     * @param rq
     * @return
     */
    @PostMapping(value = "/menuTree")
    public List<SysMenu> menuTree(@RequestBody SysMenu entity, HttpServletRequest rq) throws Exception {
        //获取父菜单
        List<SysMenu> sysMenus = sysMenuService.findMenusByUserId(WebUtils.getLoginUserId(), entity.getParentId());
        return sysMenus;
    }

    /**
     * 根据登录用户及其角色查询菜单
     *
     * @param entity
     * @param rq
     * @return
     */
    @PostMapping(value = "/getButtons")
    public List<SysMenu> getButtons(@RequestBody SysMenu entity, HttpServletRequest rq) throws Exception {
        //获取父菜单
        List<SysMenu> buttons = sysMenuService.findButtonsByParentUrlAndUserId(WebUtils.getLoginUserId(), entity.getHref());
        return buttons;
    }

    /**
     * 查询同级别下是否存在同一序号
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByParentIdAndSort")
    public String findByParentIdAndSort(SysMenu entity){
        SysMenu temp = sysMenuService.findByParentIdAndSort(entity);
        if (BeanUtils.isNotNull(temp)){
            return "false";
        }
        return "true";
    }
}

