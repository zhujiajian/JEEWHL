package com.whli.jee.job.service.impl;

import java.util.Date;
import java.util.HashSet;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.whli.jee.core.exception.ApplicationException;
import com.whli.jee.core.util.DateUtils;
import com.whli.jee.core.web.dao.IBaseDao;
import com.whli.jee.core.web.service.impl.BaseServiceImpl;
import com.whli.jee.job.dao.ITaskDao;
import com.whli.jee.job.entity.Task;
import com.whli.jee.job.service.ITaskService;

/**
 * <p>定时任务<p>
 *
 * @author whli
 * @version 2018/12/24 11:17
 */
@Service("taskService")
public class TaskServiceImpl extends BaseServiceImpl<Task> implements ITaskService {
	
    @Autowired
    private ITaskDao taskDao;

    @Autowired
    @Qualifier(value = "scheduler")
    private Scheduler scheduler;

    @Override
    public IBaseDao<Task> getDao() {
        return taskDao;
    }

    @Override
    public int add(Task entity) {
        String jobName = entity.getJobName(),
                jobGroup = entity.getJobGroup(),
                cronExpression = entity.getCronExpression(),
                jobDescription = entity.getJobDescription();
        try{
            if (checkExists(jobName, jobGroup)) {
                throw new ApplicationException("-03010301",String.format("Job已经存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(jobDescription).withSchedule(scheduleBuilder).build();

            Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(entity.getJobClass());
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).withDescription(jobDescription).build();
            scheduler.scheduleJob(jobDetail, trigger);
            return 1;
        }catch (Exception e){
            throw new ApplicationException("-03010302","类名不存在或执行表达式错误",e);
        }
    }

    @Override
    public int update(Task entity) {
        String jobName = entity.getJobName(),
                jobGroup = entity.getJobGroup(),
                cronExpression = entity.getCronExpression(),
                jobDescription = entity.getJobDescription(),
                createTime = DateUtils.dateToString(new Date(), DateUtils.DEFAULT_YYYY_MM_DD_HH_MM_SS);

        try {
            if (!checkExists(jobName, jobGroup)) {
                throw new ApplicationException("-03010301",String.format("Job不存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = new JobKey(jobName, jobGroup);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime).withSchedule(cronScheduleBuilder).build();

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobBuilder().withDescription(jobDescription);
            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(cronTrigger);

            scheduler.scheduleJob(jobDetail, triggerSet, true);
            return 1;
        } catch (SchedulerException e) {
            throw new ApplicationException("-03010302","类名不存在或执行表达式错误",e);
        }
    }

    @Override
    public void delete(Task entity) {
        TriggerKey triggerKey = TriggerKey.triggerKey(entity.getJobName(), entity.getJobGroup());
        try {
            if (checkExists(entity.getJobName(), entity.getJobGroup())) {
                scheduler.pauseTrigger(triggerKey); //停止触发器
                scheduler.unscheduleJob(triggerKey); //移除触发器
            }
        } catch (SchedulerException e) {
            throw new ApplicationException("-03010302",e.getMessage());
        }
    }

    /**
     * 验证JOB是否存在
     *
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    private boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }

    @Override
    public Boolean startTask(Task entity) {
        try {
            scheduler.resumeJob(JobKey.jobKey(entity.getJobName(), entity.getJobGroup()));
            return true;
        } catch (Exception e) {
            throw new ApplicationException("-03010302",e.getMessage());
        }
    }

    @Override
    public Boolean stopTask(Task entity) {
        TriggerKey triggerKey = TriggerKey.triggerKey(entity.getJobName(), entity.getJobGroup());
        try {
            if (checkExists(entity.getJobName(), entity.getJobGroup())) {
                scheduler.pauseTrigger(triggerKey); //停止触发器
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("-03010302",e.getMessage());
        }
    }

    @Override
    public Boolean startAll() {
        try {
            scheduler.resumeAll();
            return true;
        } catch (SchedulerException e) {
            throw new ApplicationException("-03010302",e.getMessage());
        }
    }

    @Override
    public Boolean stopAll() {
        try {
            scheduler.pauseAll();
            return true;
        } catch (SchedulerException e) {
            throw new ApplicationException("-03010302",e.getMessage());
        }
    }
}
