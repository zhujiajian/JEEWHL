package com.whli.jee.job.config;

import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>类功能<p>
 *
 * @author whli
 * @version 2018/12/24 10:10
 */
@Configuration
public class QuartzConfiguration {

    @Autowired
    private MyJobFactory jobFactory;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;


    @Bean
    public SchedulerFactoryBean schedulerFactory() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // 自定义Job Factory，用于Spring注入
        factory.setJobFactory(jobFactory);
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);

        factory.setApplicationContextSchedulerContextKey("applicationContextKey");

        // 加载quartz数据源配置
        factory.setQuartzProperties(quartzProperties());

        // 延时启动
        factory.setStartupDelay(60);
        return factory;
    }

    @Bean(value = "scheduler")
    public Scheduler scheduler() throws IOException {
        return schedulerFactory().getScheduler();
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        //设置参数
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName","myQuartzScheduler");
        properties.setProperty("org.quartz.scheduler.instanceId","AUTO");
        properties.setProperty("org.quartz.scheduler.rmi.export","false");
        properties.setProperty("org.quartz.scheduler.rmi.proxy","false");
        properties.setProperty("org.quartz.scheduler.wrapJobExecutionInUserTransaction","false");
        properties.setProperty("org.quartz.threadPool.class","org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount","5");
        properties.setProperty("org.quartz.threadPool.threadPriority","5");
        properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread","true");
        properties.setProperty("org.quartz.jobStore.misfireThreshold","5000");
        properties.setProperty("org.quartz.jobStore.class","org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.setProperty("org.quartz.jobStore.tablePrefix","QRTZ_");
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval","15000");
        properties.setProperty("org.quartz.jobStore.isClustered","true");
        properties.setProperty("org.quartz.jobStore.dataSource","qzDS");
        properties.setProperty("org.quartz.dataSource.qzDS.maxConnections","10");
        properties.setProperty("org.quartz.dataSource.qzDS.driver",driverClassName);
        properties.setProperty("org.quartz.dataSource.qzDS.URL",dbUrl);
        properties.setProperty("org.quartz.dataSource.qzDS.user",username);
        properties.setProperty("org.quartz.dataSource.qzDS.password",password);
        properties.setProperty("org.quartz.dataSource.qzDS.connectionProvider.class",
                "com.whli.jee.core.config.DruidConnectionProvider");
        //定义参数
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setProperties(properties);
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /*
     * quartz初始化监听器
     */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }
}
