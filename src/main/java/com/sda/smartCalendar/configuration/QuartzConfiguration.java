package com.sda.smartCalendar.configuration;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class QuartzConfiguration {

    @Bean
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = new StdSchedulerFactory(addProp()).getScheduler();
        scheduler.start();
        return scheduler;
    }

    private Properties addProp() {

        Properties properties = new Properties();
        Map<String, String> props = new HashMap<>();

        props.put("org.quartz.scheduler.instanceName", "myScheduler");
        props.put("org.quartz.threadPool.threadCount", "3");
        props.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

        properties.putAll(props);

        return properties;
    }

}
