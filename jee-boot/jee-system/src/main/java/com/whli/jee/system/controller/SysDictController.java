package com.whli.jee.system.controller;

import com.whli.jee.core.page.Page;
import com.whli.jee.core.util.BeanUtils;
import com.whli.jee.core.util.StringUtils;
import com.whli.jee.core.web.controller.BaseController;
import com.whli.jee.core.web.entity.ResponseBean;
import com.whli.jee.system.entity.SysDict;
import com.whli.jee.system.service.ISysDictService;
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
@RequestMapping(value = "/system/sysDict")
public class SysDictController extends BaseController<SysDict> {

    @Autowired
    private ISysDictService sysDictService;

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByPage")
    @Override
    public ResponseBean findByPage(@RequestBody SysDict entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        if (BeanUtils.isNotNull(entity)){
            if (StringUtils.isNullOrBlank(entity.getParentId())){
                entity.setParentId("");
            }
            Page<SysDict> page = new Page<SysDict>(entity.getCurrentPage(),entity.getPageSize());
            page = sysDictService.findByPage(entity, page);
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
    public ResponseBean add(@RequestBody SysDict entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        entity.setEnable(1);
        int rows = sysDictService.add(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("新增数据字典成功！");
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
    public ResponseBean update(@RequestBody SysDict entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = sysDictService.update(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("修改数据字典成功！");
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
    public ResponseBean delete(@RequestBody SysDict entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        sysDictService.deleteMore(entity);
        responseBean.setResults(true);
        responseBean.setMessage("删除数据字典成功！");
        return responseBean;
    }

    /**
     * 根据主键查询entity
     *
     * @return
     */
    @PostMapping(value = "/findByPK")
    @Override
    public SysDict findByPK(@RequestBody SysDict entity, HttpServletRequest req) throws Exception {
        return sysDictService.findByPK(entity.getId());
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
    public SysDict findByNo(@RequestBody SysDict entity, HttpServletRequest req) throws Exception {
        return sysDictService.findByNo(entity.getValue());
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
    public SysDict findByName(@RequestBody SysDict entity, HttpServletRequest req) throws Exception {
        return sysDictService.findByName(entity.getName());
    }

    /**
     * 查询所有数据
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/findAll")
    public List<SysDict> findAll(@RequestBody SysDict entity, HttpServletRequest req) throws Exception {
        return sysDictService.findAll(entity);
    }

    /**
     * 依据父字典值查询子字典
     *
     * @return
     */
    @PostMapping(value = "/findByParentValue")
    public List<SysDict> findByParentValue(@RequestBody SysDict entity, HttpServletRequest req) throws Exception {
        return sysDictService.findByParentValue(entity.getValue());
    }

    /**
     * 查询同级别下是否存在同一序号
     * @param entity
     * @return
     */
    @PostMapping(value = "/findByParentIdAndSort")
    public String findByParentIdAndSort(SysDict entity){
        SysDict temp = sysDictService.findByParentIdAndSort(entity);
        if (BeanUtils.isNotNull(temp)){
            return "false";
        }
        return "true";
    }
}

