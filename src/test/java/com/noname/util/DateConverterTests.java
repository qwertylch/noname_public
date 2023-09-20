package com.noname.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@SpringBootTest
public class DateConverterTests {

    static LocalDateTime currentDate = LocalDateTime.now();

    @Test
    void formatToYYYYMMDDTest(){
        String strDate = DateConverter.formatToYYYYMMDD(currentDate);
        log.info("================================================");
        log.info("Converted Result YYMMDD: {}", strDate);
        log.info("================================================");
    }

    @Test
    void formatToMMDDAHHMMTest(){
        String strDate = DateConverter.formatToMMDDAHHMM(currentDate);
        log.info("================================================");
        log.info("Converted Result MMDDHH : {}", strDate);
        log.info("================================================");

    }

    @Test
    void formatTimeAgo(){


        LocalDateTime minusSeconds = LocalDateTime.now().minusSeconds(60);
        LocalDateTime minusMinutes = LocalDateTime.now().minusMinutes(60*23+60);
        LocalDateTime minusHours = LocalDateTime.now().minusHours(24);
        LocalDateTime minusDays = LocalDateTime.now().minusDays(1);
        log.info("================================================");
        log.info("now : {}", LocalDateTime.now());
        log.info("minusSeconds : {}", minusSeconds);
        log.info(DateConverter.formatTimeAgo(minusSeconds));
        log.info("minusMinutes : {}", minusMinutes);
        log.info(DateConverter.formatTimeAgo(minusMinutes));
        log.info("minusHours : {}", minusHours);
        log.info(DateConverter.formatTimeAgo(minusHours));
        log.info("minusDays : {}", minusDays);
        log.info(DateConverter.formatTimeAgo(minusDays));
        log.info("================================================");



    }





}
