package com.tinhvan.hd.quartz.configuration;

import com.tinhvan.hd.quartz.job.NotificationQueueJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureJob {

    @Value("${app.scheduled.cron.notification-queue}")
    private String queueJob;

    @Bean
    public JobDetail notificationQueueJobDetails() {
        return JobBuilder.newJob(NotificationQueueJob.class).withIdentity("JobNotificationQueue")
                .storeDurably().build();
    }

    @Bean
    public Trigger notificationQueueJobTrigger(JobDetail jobADetails) {

        return TriggerBuilder.newTrigger().forJob(jobADetails)
                .withIdentity("TriggerNotificationQueue")
                .withSchedule(CronScheduleBuilder.cronSchedule(queueJob))
                .build();
    }
}
