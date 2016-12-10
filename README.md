# AlertSwitch
Expose an port for switching of alert system


#自定义配置 
可以修改 application.properties logback.xml,例如修改port，日志路径,执行start.sh脚本

#eg:  
### 开启报警：  
curl http://127.0.0.1:8081/alert/modify?alarm=true  
### 关闭报警：  
curl http://127.0.0.1:8081/alert/modify?alarm=false
