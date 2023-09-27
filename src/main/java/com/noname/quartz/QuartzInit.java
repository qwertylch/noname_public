package com.noname.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzInit {

    private final Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            //스케줄러 초기화 -> DB도 CLAER
            scheduler.clear();
            //Job 리스너 등록
            scheduler.getListenerManager().addJobListener(new QuartzJobListener());
            //Trigger 리스너 등록a
            scheduler.getListenerManager().addTriggerListener(new QuartzTriggerListener());

            //Job 생성 및 Scheduler에 등록
//            addJob(PendingScheduler.class, "PendingScheduler", "PendingScheduler 입니다", "0/5 * * * * ?");
//            addJob(HelloScheduler.class, "HelloScheduler", "Hello Scheduler 입니다", "* 30 * * * ?");
//            addJob(InbidScheduler.class, "InbidScheduler", "InbidScheduler 입니다", "0/5 * * * * ?");
//            addJob(ImageScheduler.class, "ImageScheduler", "ImageScheduler 입니다", "0/5 * * * * ?");

        } catch (Exception e){
            log.error("addJob error : {}", e);

        }
    }

    //Job 추가
    public <T extends Job> void addJob(Class<? extends Job> job ,String name, String desc, String cron) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(job, name, desc);
        Trigger trigger = buildCronTrigger(cron);
        if(scheduler.checkExists(jobDetail.getKey())) scheduler.deleteJob(jobDetail.getKey());
        scheduler.scheduleJob(jobDetail,trigger);
    }

    //JobDetail 생성
    public <T extends Job> JobDetail buildJobDetail(Class<? extends Job> job, String name, String desc) {
        JobDataMap jobDataMap = new JobDataMap();

        return JobBuilder
                .newJob(job)
                .withIdentity(name)
                .withDescription(desc)
                .build();
    }

    //Trigger 생성
    private Trigger buildCronTrigger(String cronExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();
    }


}
