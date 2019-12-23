/*
package com.tinhvan.hd.quartz.configuration;

import com.tinhvan.hd.quartz.job.ContractSendFileJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureJob {
    @Bean
    public JobDetail contractSendFileJobDetails() {
        return JobBuilder.newJob(ContractSendFileJob.class).withIdentity("ContractSendFileJob")
                .storeDurably().build();
    }

    @Bean
    public Trigger contractSendFileJobTrigger(JobDetail jobADetails) {
        return TriggerBuilder.newTrigger().forJob(jobADetails)
                .withIdentity("ContractSendFileTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * ? * * *"))
                .build();
    }
}
*/
