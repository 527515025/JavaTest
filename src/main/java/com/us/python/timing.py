# python 的定时任务
import schedule
import time
 
def job():
    print("I'm working...")
 
# schedule.every(2).seconds.do(job)
# schedule.every().hour.do(job)
schedule.every().day.at("16:06").do(job)
# schedule.every(5).to(10).days.do(job)
# schedule.every().monday.do(job)
# schedule.every().wednesday.at("13:15").do(job)
 
while True:
    schedule.run_pending()



# python 的定时任务系统
# import datetime
#
# sched_Timer=datetime.datetime(2019,4,1,16,29,0);
# print(sched_Timer)
# flag = 0;
# while True:
#     now = datetime.datetime.now();
#     if now==sched_Timer:
#         print('run Task')
#         flag=1
#     else:
#         if flag==1:
#             sched_Timer=sched_Timer+datetime.timedelta(minutes=1);
#             flag=0
#
#
