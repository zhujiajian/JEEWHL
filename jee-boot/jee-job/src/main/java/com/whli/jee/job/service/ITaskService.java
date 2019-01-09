package com.whli.jee.job.service;

import com.whli.jee.core.web.service.IBaseService;
import com.whli.jee.job.entity.Task;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>类功能<p>
 *
 * @author whli
 * @version 2018/12/24 11:15
 */
public interface ITaskService extends IBaseService<Task> {

    /**
     * 开始任务
     * @param entity
     * @return
     */
    @Transactional
    public Boolean startTask(Task entity);

    /**
     * 暂停任务
     * @param entity
     * @return
     */
    @Transactional
    public Boolean stopTask(Task entity);

    /**
     * 开始所有任务
     * @return
     */
    @Transactional
    public Boolean startAll();

    /**
     * 停止所有任务
     * @return
     */
    @Transactional
    public Boolean stopAll();
}
