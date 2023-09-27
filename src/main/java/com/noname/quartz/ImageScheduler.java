package com.noname.quartz;

import com.noname.repository.ImageRepository;
import com.noname.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImageScheduler implements Job {

    @Autowired
    private QuartzService quartzService;
    // 실제 이미지 폴더 경로


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // DB에서 모든 이미지 목록 가져오기
        log.info("======ImageScheduler!!");
        quartzService.changeImages();

    }


}