package com.finley.springboot.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileService
{
    private final Logger log = Logger.getLogger(getClass());

    public boolean getStatus(String filename, String modifyKey) {
        BufferedReader br = null;

        boolean status = false;
        try
        {
            br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null)
                if (line.contains(modifyKey)) {
                    String[] parts = line.split("=");
                    if ((parts != null) && (parts.length == 2)) {
                        status = Boolean.valueOf(parts[1].trim()).booleanValue();
                        break;
                    }
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    this.log.error(e.getStackTrace());
                }
            }
        }
        return status;
    }

    public boolean modify(String filename, String modifyKey, boolean isAlarm)
    {
        BufferedReader br = null;

        StringBuffer buf = new StringBuffer();
        boolean isModified = false;
        try
        {
            br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(modifyKey)) {
                    String[] parts = line.split("=");
                    if ((parts != null) && (parts.length == 2)) {
                        this.log.info("origin status: " + parts[1] + ", new status:" + isAlarm);
                        line = parts[0] + "=" + isAlarm;
                        isModified = true;
                    }
                    buf.append(line);
                } else {
                    buf.append(line);
                }
                buf.append(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            this.log.error(e.getStackTrace());
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        write(filename, buf.toString());
        return isModified;
    }

    public void write(String filePath, String content)
    {
        BufferedWriter bw = null;
        try
        {
            bw = new BufferedWriter(new FileWriter(filePath));

            bw.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (bw != null)
                try {
                    bw.close();
                } catch (IOException e) {
                    this.log.error(e.getStackTrace());
                }
        }
    }
}