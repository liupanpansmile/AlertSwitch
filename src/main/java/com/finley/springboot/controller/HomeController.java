package com.finley.springboot.controller;

import com.finley.springboot.service.CmdService;
import com.finley.springboot.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class HomeController {
    Logger                log   = LoggerFactory.getLogger(HomeController.class);

    @Value("${properties_file_path}")
    private String        propertiesFile;

    @Value("${alarm_key}")
    private String        alarmKey;

    @Value("${restart_cmd}")
    private String        commands;

    @Autowired
    private FileService   fileService;

    @Autowired
    private CmdService    cmdService;
    private AtomicInteger count = new AtomicInteger(1);

    @RequestMapping({"/alert/modify"})
    Map<String, Object> modify(@RequestParam(name = "alarm") boolean alarm) {
        this.log.info(" url : /alert/modify, param alarm is " + alarm + ",execution times: " + this.count.getAndIncrement());
        boolean isSuccess = false;
        Map result = new HashMap();
        boolean status = this.fileService.getStatus(this.propertiesFile, this.alarmKey);
        if (status == alarm) {
            this.log.info("Alarm status is already " + alarm + ", So Did not execute command");
            result.put("message", "Alarm status is already " + alarm);
            result.put("result", Boolean.valueOf(false));
        } else {
            boolean isModified = this.fileService.modify(this.propertiesFile, this.alarmKey, alarm);
            if (isModified) {
                isSuccess = this.cmdService.execute(getCommand(commands));
            }
            this.log.info("Alarm status has updated and executed given command , isSuccess:" + isSuccess);
            result.put("message", "Alarm Status have modified and restarted");
            result.put("result", Boolean.valueOf(isSuccess));
        }
        return result;
    }

    private List<String[]> getCommand(String cmds) {
        if (StringUtils.isEmpty(cmds)) {
            return null;
        }
        List<String[]> list = new ArrayList<>();
        String[] cmdAndParamertArray = cmds.split(",");
        for (String cmdAndParameter : cmdAndParamertArray) {
            String[] parts = cmdAndParameter.split(" ");
            if (parts != null && parts.length != 0) {
                list.add(parts);
            }
        }
        return list;
    }
}
