# python 脚本调用 shell 执行Java jar 包
import os
import datetime

# 获取当前时间
now = datetime.datetime.now();
print(now);
# 获取起始时间
start=now-datetime.timedelta(days=2);
start = start.strftime('%Y-%m-%d');
# 获取结束时间
end = now-datetime.timedelta(days=1);
end = end.strftime('%Y-%m-%d');
print(start);
print(end);
# 包装参数
suffix=" 15:00:00";
startTime=start+suffix;
endTime=end+suffix;

# 执行命令
command ="java -jar /Users/yangyibo/GitWork/springboot-redis-delete/target/springboot-redis-delete-1.0.0.jar --startTime=\""+startTime+"\" --endTime=\""+endTime+"\"";
# print(command);
os.system(command);
