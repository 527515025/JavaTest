#Docker

##docker 机制与原理

docker是lxc的管理器，lxc是cgroup的管理工具，cgroup是namespace的用户空间的管理接口。namespace是linux内核在task_struct中对进程组管理的基础机制。

  Linux 内核从版本 2.4.19 开始陆续引入了 namespace 的概念。其目的是将某个特定的全局系统资源（global system resource）通过抽象方法使得namespace 中的进程看起来拥有它们自己的隔离的全局系统资源实例

#Docker

##批量删除已停止的容器

```
docker rm `docker ps -a |awk '{print $1}' |grep -v 1c3e744ba21d| grep [0-9a-z]`
```

##docker 机制与原理

docker是lxc的管理器，lxc是cgroup的管理工具，cgroup是namespace的用户空间的管理接口。namespace是linux内核在task_struct中对进程组管理的基础机制。

  Linux 内核从版本 2.4.19 开始陆续引入了 namespace 的概念。其目的是将某个特定的全局系统资源（global system resource）通过抽象方法使得namespace 中的进程看起来拥有它们自己的隔离的全局系统资源实例

#