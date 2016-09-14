#########################################################################
# File Name: start.sh
# Author: ma6174
# mail: ma6174@163.com
# Created Time: 二  9/13 16:16:41 2016
#########################################################################
#!/bin/bash
nohup java -jar -Dspring.config.location=application.properties  consumer-alarm-switch-1.0-SNAPSHOT.jar > /dev/null 2>&1 &
