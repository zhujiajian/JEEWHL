package com.whli.jee.job.controller;

import com.whli.jee.core.page.Page;
import com.whli.jee.core.web.entity.ResponseBean;
import com.whli.jee.core.web.controller.BaseController;
import com.whli.jee.job.entity.JobLog;
import com.whli.jee.job.service.IJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * <em>类或方法作用描述</em>
 * @author whli
 * @version 2018/9/4 9:26
 * */
@RestController
@RequestMapping(value="/scheduler/jobLog")
public class JobLogController extends BaseController<JobLog>{

	@Autowired
	private IJobLogService jobLogService;

    /**
     * 分页查询
     * @param entity
	 * @param req
     * @return
	 * @throws Exception
     */
    @PostMapping(value = "/findByPage")
	@Override
    public ResponseBean findByPage(@RequestBody JobLog entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        if (entity != null) {
            Page<JobLog> page = new Page<JobLog>(entity.getCurrentPage(),entity.getPageSize());
            page = jobLogService.findByPage(entity, page);
            responseBean.setCount(page.getTotal());
            responseBean.setResults(page.getPageRecords());
        }
        return responseBean;
    }

    /**
     * 添加entity
     * @param entity
	 * @param req
     * @return
	 * @throws Exception
     */
    @PostMapping(value = "/add")
	@Override
    public ResponseBean add(@RequestBody JobLog entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = jobLogService.add(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("新增定时任务日志成功！");
        }
        return responseBean;
    }

    /**
     * 更新entity
     * @param entity
	 * @param req
     * @return
	 * @throws Exception
     */
    @PostMapping(value = "/update")
	@Override
    public ResponseBean update(@RequestBody JobLog entity, HttpServletRequest req) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        int rows = jobLogService.update(entity);
        if (rows > 0) {
            responseBean.setResults(true);
            responseBean.setMessage("更新定时任务日志成功！");
        }
        return responseBean;
    }

    /**
     * 删除entity
     * @param entity
	 * @param req
     * @return
	 * @throws Exception
     */
    @PostMapping(value = "/delete")
	@Override
    public ResponseBean delete(@RequestBody JobLog entity, HttpServletRequest req)
        throws Exception {
        ResponseBean responseBean = new ResponseBean();
        jobLogService.deleteMore(entity);
        responseBean.setResults(true);
        responseBean.setMessage("删除定时任务成功！");
        return responseBean;
    }

	/**
	 * 根据主键查询entity
	 * @param entity
	 * @param req
	 * @return
	 * @throws Exception
	 */
    @PostMapping(value = "/findByPK")
	@Override
	public JobLog findByPK(@RequestBody JobLog entity, HttpServletRequest req) throws Exception{
		return jobLogService.findByPK(entity.getId());
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
    public JobLog findByNo(@RequestBody JobLog entity, HttpServletRequest req) throws Exception {
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
    public JobLog findByName(@RequestBody JobLog entity, HttpServletRequest req) throws Exception {
        return null;
    }

    /**
     * 查询所有数据
     *
     * @param entity
	 * @param req
     * @return
	 * @throws Exception
     */
    @PostMapping(value = "/findAll")
	@Override
    public List<JobLog> findAll(@RequestBody JobLog entity, HttpServletRequest req) throws Exception {
        return jobLogService.findAll(entity);
    }
}

