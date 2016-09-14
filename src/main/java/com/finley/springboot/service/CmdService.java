package com.finley.springboot.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class CmdService {
    private final Logger log = Logger.getLogger(getClass());

    public boolean execute(List<String[]> cmds) {
        boolean status = true;
        if (CollectionUtils.isNotEmpty(cmds)) {
            for (String[] cmd : cmds) {
                status = executeSingle(cmd);
                if (!status) {
                    return status;
                }
            }
        }
        return status;
    }

    public boolean executeSingle(String[] cmd) {
        boolean status = true;
        try {
            Process pro = Runtime.getRuntime().exec(cmd);
            if (log.isDebugEnabled()) {
                pro.waitFor();
                InputStream in = pro.getInputStream();
                String message = IOUtils.toString(in);
                log.debug(message);
            }
        } catch (InterruptedException e) {
            status = false;
            log.error(String.format("execute %s failed,msg:%s ", cmd, e.getMessage()), e);
        } catch (IOException e) {
            status = false;

            log.error(String.format("execute %s failed,msg:%s ", cmd, e.getMessage()), e);
        }
        return status;
    }
}
