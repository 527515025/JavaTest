# -*- coding: utf-8 -*-

from datetime import datetime
import os
from apscheduler.schedulers.blocking import BlockingScheduler
import os


# 执行任务
def tick():
    print('调用脚本! The time is: %s' % datetime.now())
    # 调用shell 脚本
    n=os.system('sh /xxx/test.sh')
    print('脚本执行完毕 ! The time is: %s' % datetime.now())
if __name__ == '__main__':
    # 配置调度器
    scheduler = BlockingScheduler()
    # scheduler.add_job(tick, 'cron', second=3)
    # scheduler.add_job(tick, 'cron',  month='*', day='*', hour='17',minute='13')
    scheduler.add_job(tick, 'cron', day='1st wed', hour='6')

    try:
        scheduler.start()
    except (KeyboardInterrupt, SystemExit):
        pass
