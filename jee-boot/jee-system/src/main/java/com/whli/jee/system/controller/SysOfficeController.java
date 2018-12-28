package com.whli.jee.system.controller;

import com.whli.jee.core.page.Page;
import com.whli.jee.core.util.BeanUtils;
import com.whli.jee.core.web.controller.BaseController;
import com.whli.jee.core.web.entity.ResponseBean;
import com.whli.jee.system.entity.SysOffice;
import com.whli.jee.system.service.ISysOfficeService;
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
@RequestMapping(value = "/system/sysOffice")
public class SysOfficeController extends BaseController<SysOffice> {

    @Autowired
    private ISysOfficeService sysOfficeService;

    /**
     * 分页查询
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByPage")
    @Override
    public ResponseBean findByPage(@RequestBody SysOffice entity,  HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        if (entity != null) {
            Page<SysOffice> page = new Page<SysOffice>(entity.getCurrentPage(),entity.getPageSize());
            page = sysOfficeService.findByPage(entity, page);
            responseBean.setCount(page.getTotal());
            responseBean.setResults(page.getPageRecords());
        }
        return responseBean;
    }

    /**
     * 添加entity
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/add")
    @Override
    public ResponseBean add(@RequestBody SysOffice entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        entity.setEnable(1);
        int rows = sysOfficeService.add(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("新增部门成功");
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
    public ResponseBean update(@RequestBody SysOffice entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = sysOfficeService.update(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("修改部门成功！");
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
    public ResponseBean delete(@RequestBody SysOffice entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        sysOfficeService.deleteMore(entity);
        responseBean.setResults(true);
        responseBean.setMessage("删除部门成功！");
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
    public SysOffice findByPK(@RequestBody SysOffice entity, HttpServletRequest req) throws Exception {
        return sysOfficeService.findByPK(entity.getId());
    }

    /**
     * 根据父ID查询
     * @param entity
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/findByNo")
    @Override
    public SysOffice findByNo(SysOffice entity, HttpServletRequest req) throws Exception {
        return null;
    }

    /**
     * 根据名称进行查询
     * @param entity
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/findByName")
    @Override
    public SysOffice findByName(SysOffice entity, HttpServletRequest req) throws Exception {
        return sysOfficeService.findByName(entity.getName());
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    @PostMapping(value = "/findAll")
    @Override
    public List<SysOffice> findAll(@RequestBody SysOffice entity, HttpServletRequest req) throws Exception {
        return sysOfficeService.findAll(entity);
    }

    /**
     * 查询同级别下是否存在同一序号
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByParentIdAndSort")
    public String findByParentIdAndSort(SysOffice entity){
        SysOffice temp = sysOfficeService.findByParentIdAndSort(entity);
        if (BeanUtils.isNotNull(temp)){
            return "false";
        }
        return "true";
    }
}

