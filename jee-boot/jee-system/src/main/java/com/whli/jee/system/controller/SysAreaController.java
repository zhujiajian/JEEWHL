package com.whli.jee.system.controller;

import com.whli.jee.core.page.Page;
import com.whli.jee.core.util.BeanUtils;
import com.whli.jee.core.web.controller.BaseController;
import com.whli.jee.core.web.entity.ResponseBean;
import com.whli.jee.system.entity.SysArea;
import com.whli.jee.system.service.ISysAreaService;
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
@RequestMapping(value = "/system/sysArea")
public class SysAreaController extends BaseController<SysArea> {

    @Autowired
    private ISysAreaService sysAreaService;

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByPage")
    @Override
    public ResponseBean findByPage(@RequestBody SysArea entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        if (entity != null) {
            Page<SysArea> page = new Page<SysArea>(entity.getCurrentPage(),entity.getPageSize());
            page = sysAreaService.findByPage(entity, page);
            responseBean.setCount(page.getTotal());
            responseBean.setResults(page.getPageRecords());
        }
        return responseBean;
    }

    /**
     * 添加entity
     * @return
     */
    @PostMapping(value = "/add")
    @Override
    public ResponseBean add(@RequestBody SysArea entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        entity.setEnable(1);
        int rows = sysAreaService.add(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("新增区域成功！");
        }
        return responseBean;
    }

    /**
     * 更新entity
     * @return
     */
    @PostMapping(value = "/update")
    @Override
    public ResponseBean update(@RequestBody SysArea entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = sysAreaService.update(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("修改区域成功！");
        }
        return responseBean;
    }

    /**
     * 删除entity
     * @return
     */
    @PostMapping(value = "/delete")
    @Override
    public ResponseBean delete(@RequestBody SysArea entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        sysAreaService.deleteMore(entity);
        responseBean.setResults(true);
        responseBean.setMessage( "删除区域成功！");
        return responseBean;
    }

    /**
     * 根据主键查询entity
     * @return
     */
    @PostMapping(value = "/findByPK")
    @Override
    public SysArea findByPK(@RequestBody SysArea entity, HttpServletRequest req) throws Exception {
        return sysAreaService.findByPK(entity.getId());
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
    public SysArea findByNo(@RequestBody SysArea entity, HttpServletRequest req) throws Exception {
        return sysAreaService.findByNo(entity.getCode());
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
    public SysArea findByName(@RequestBody SysArea entity, HttpServletRequest req) throws Exception {
        return sysAreaService.findByName(entity.getName());
    }

    /**
     * 查询所有数据
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/findAll")
    @Override
    public List<SysArea> findAll(@RequestBody SysArea entity, HttpServletRequest req) throws Exception {
        return sysAreaService.findAll(entity);
    }

    /**
     * 查询同级别下是否存在同一序号
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByParentIdAndSort")
    public String findByParentIdAndSort(SysArea entity){
        SysArea temp = sysAreaService.findByParentIdAndSort(entity);
        if (BeanUtils.isNotNull(temp)){
            return "false";
        }
        return "true";
    }
}

