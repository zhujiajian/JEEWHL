package com.whli.jee.core.web.service.impl;


import com.whli.jee.core.exception.BusinessException;
import com.whli.jee.core.page.Page;
import com.whli.jee.core.util.*;
import com.whli.jee.core.web.dao.IBaseDao;
import com.whli.jee.core.web.entity.BaseEntity;
import com.whli.jee.core.web.service.IBaseService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by whli on 2017/10/29.
 */
public class BaseServiceImpl<T extends BaseEntity> implements IBaseService<T> {

    private IBaseDao<T> dao;

    public IBaseDao<T> getDao(){
        return  dao;
    }

    /**
     * 增加
     * @param entity
     * @return
     */
    @Override
    public int add(T entity){
        if(BeanUtils.isNull(entity)){
            throw new BusinessException("新增数据不能为空！");
        }
        entity.setId(BeanUtils.getUUID());
        entity.setCreateBy(StringUtils.isNullOrBlank(WebUtils.getLoginName()) ? "anonymous" : WebUtils.getLoginName());
        entity.setCreateDate(new Date());
        return getDao().add(entity);
    }

    /**
     * 批量增加
     * @param entities
     * @return
     */
    @Override
    public int addMore(List<T> entities){
        if(CollectionUtils.isNullOrEmpty(entities)){
            throw new BusinessException("新增数据不能为空！");
        }
        int rows = 0;
        for(T entity:entities){
            entity.setId(BeanUtils.getUUID());
            entity.setCreateBy(StringUtils.isNullOrBlank(WebUtils.getLoginName()) ? "anonymous" : WebUtils.getLoginName());
            entity.setCreateDate(new Date());
            getDao().add(entity);
            rows++;
        }
        return rows;
    }

    /**
     * 更新
     * @param entity
     * @return
     */
    @Override
    public int update(T entity){
        if(BeanUtils.isNull(entity)){
            throw new BusinessException("修改数据不能为空！");
        }
        entity.setUpdateBy(StringUtils.isNullOrBlank(WebUtils.getLoginName()) ? "anonymous" : WebUtils.getLoginName());
        entity.setUpdateDate(new Date());
        return getDao().update(entity);
    }

    /**
     * 批量更新
     * @param entities
     * @return
     */
    @Override
    public int updateMore(List<T> entities){
        if(CollectionUtils.isNullOrEmpty(entities)){
            throw new BusinessException("修改数据不能为空！");
        }
        int rows = 0;
        for(T entity:entities){
            entity.setUpdateBy(StringUtils.isNullOrBlank(WebUtils.getLoginName()) ? "anonymous" : WebUtils.getLoginName());
            entity.setUpdateDate(new Date());
            getDao().update(entity);
            rows++;
        }
        return rows;
    }

    /**
     * 删除
     * @param entity
     */
    @Override
    public void delete(T entity){
        if(StringUtils.isNullOrBlank(entity.getId())){
            throw new BusinessException("请选择需要删除的数据！");
        }
        getDao().delete(entity.getId());
    }

    /**
     * 批量删除
     * @param entity
     * @return
     */
    @Override
    public void deleteMore(T entity){
        if(CollectionUtils.isNullOrEmpty(entity.getIds())){
            throw new BusinessException("请选择需要删除的数据！");
        }
        getDao().deleteMore(entity.getIds());
    }

    /**
     * 主键id查询
     * @param id
     * @return
     */
    @Override
    public T findByPK(String id){
        if(StringUtils.isNullOrBlank(id)){
            throw new BusinessException("请选择需要查询的数据！");
        }
        T entity = getDao().findByPK(id);
        if(BeanUtils.isNull(entity)){
            throw  new BusinessException("查询的数据不存在或已删除！");
        }
        return entity;
    }

    @Override
    public T findByNo(String No) {
        if(StringUtils.isNullOrBlank(No)){
            throw new BusinessException("请选择需要查询的数据！");
        }
        T entity = getDao().findByNo(No);
        if(BeanUtils.isNull(entity)){
            throw  new BusinessException("查询的数据不存在或已删除！");
        }
        return entity;
    }

    @Override
    public T findByName(String name) {
        if(StringUtils.isNullOrBlank(name)){
            throw new BusinessException("请选择需要查询的数据！");
        }
        T entity = getDao().findByName(name);
        if(BeanUtils.isNull(entity)){
            throw  new BusinessException("查询的数据不存在或已删除！");
        }
        return entity;
    }

    /**
     * 分页查询
     * @param entity
     * @param page
     * @return
     */
    @Override
    public Page<T> findByPage(T entity, Page<T> page){
        List<T> records = getDao().findByPage(entity,page);
        if (page == null){
            page = new Page<T>();
        }
        page.setPageRecords(records);
        return page;
    }

    /**
     * 查询所有
     * @param entity
     * @return
     */
    @Override
    public List<T> findAll(T entity){
        return getDao().findAll(entity);
    }

    /**
     * 导出Excel
     * @param response
     */
    @Override
    public void exportExcel(T entity, HttpServletResponse response) {

    }

    /**
     * 导入Excel
     * @param file
     * @return
     */
    @Override
    public int importExcel(File file) {
        return 0;
    }

    /**
     * 导入Excel
     * @param stream
     * @return
     */
    @Override
    public int importExcel(InputStream stream) {
        return 0;
    }

    /**
     * 导入模板
     * @param response
     */
    @Override
    public void exportTemplate(T entity, HttpServletResponse response) {

    }

}
