#### 数据统计

1. 在50次运行（每次运行4个API，跟踪200个用户）中，超过100次还未查询到的次数共有30个，这些会在结果中计空处理
2. 在50次运行（每次运行4个API，跟踪200个用户）中，共进行了511014次查询，平均每个API查询12.77535次（wifi网络可能有波动）
3. 最长查询时间（查询101次）为55秒，API 60s有超时判定，未出现
4. 一级API平均结果写回时间为15.398s（只需要ykid不需要roomid）
5. 二级API平均结果写回时间为20.608s（需要roomid）

#### 运行方法

在pom.xml所在的文件夹下：

mvn clean compile

mvn exec:java -Dexec.mainClass="Main"

参数设置：

Main.java中，有一个MongoDB连接字符串的设置，可以依情况进行修改

ThreadPool.java中，有一个设置线程数量的参数

在Main.java中，可以打开与之前python对应的方法。注意某些方法会传输大量的数据。

#### 结构

Master.call函数表示每隔delaytime进行一次操作，这个操作为taskexecutor，由taskexecutor进行具体的操作。这样每隔delaytime一定会发送一次，而不是A执行完之后一段时间再执行B。

taskexecutor执行Master.gethotlist将当前hotlist的ykid列表使用pushit来发送，rawdoc的ts参数代表发送时间

pushit会创建一个新的ServantFactory线程并加入线程池执行。此时各个线程是不关联的。

ServantFactory会根据传入的字符串来串行执行对应的操作，并将前一个的执行结果传到后一个。

S_打头为对应获取数据的方法。

                collection.insertOne(doc, new SingleResultCallback<Void>() {
                    @Override
                    public void onResult(Void aVoid, Throwable throwable) {
                        Main.havesent ++;
                    }
                });
为插入数据库的 异步 方法。

ThreadPool为线程池的定义部分，可以修改最大线程数量。默认为200。

程序执行时会修改ThreadPool的TotalTrynum（总共的连接次数）和MaxTrynum（执行一次成功的API查询所需要最大的连接次数），如果出现API无法获取的情况会有错误信息输出。

```
ServantEventMain.database = database;
ServantEventMain.initialcollection(false);
```

这里是初始化。如果第二行参数设置为true会在执行前清空所有数据。

代理的更新是通过另开一个线程实现的，为ProxyThread。每十秒钟会执行一次ProxyThread的Run操作。

