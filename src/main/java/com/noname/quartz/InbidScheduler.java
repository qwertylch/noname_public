package com.noname.quartz;

import com.noname.entity.ProductAuction;
import com.noname.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class InbidScheduler implements Job {

    @Autowired
    private QuartzService quartzService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("======InbidScheduler!!");
        List<ProductAuction> auctionList = quartzService.ChangeInbid();


    }
}