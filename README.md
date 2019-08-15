# springboot + dubbo + txlcn 分布式事务demo

#### 一、首先springboot集成dubbo
1、结构示例
```
project
    |
    |->api
    |
    |->bank(被调用方) alice 在银行的账户
    |
    |->people(调用方) alice 钱包的金额
```
2、dubbo依赖
```
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>0.2.0</version>
</dependency>
```
2.1、在项目的启动类加入以下注解在可以
```
@EnableDubbo
```

3、配置文件配置，bank和people同样配置，name要不一样
```
dubbo.application.name=dubbo-people
demo.service.version=1.0.0
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880
# 192.168.223.160 为zookeeper服务器IP
dubbo.registry.address=zookeeper://192.168.223.160:2181
dubbo.consumer.timeout=5000
# dubbo新版对qos重新构建，所以不配置可能会出现 java.net.BindException: Address already in use: bind
dubbo.application.qos-enable=false
```
3.1、dubbo调用方和被调用方service中的注解
```
# dubbo调用方
@Reference(version = "${demo.service.version}")
private DepositWithdrawService depositWithdrawService;

# dubbo被调用方注解
@Service(version = "${demo.service.version}")
public class BankService implements DepositWithdrawService {
}

# DepositWithdrawService 来自api模块
```
4、zookeeper单机服务搭建
```
cd /opt
wget https://archive.apache.org/dist/zookeeper/zookeeper-3.4.5/zookeeper-3.4.5.tar.gz
tar -zxvf zookeeper-3.4.5.tar.gz
cd zookeeper-3.4.9/conf/
cp zoo_sample.cfg zoo.cfg
# 配置环境变量
vim /etc/profile
# 下滑到最下方，加入以下配置
export ZOOKEEPER_HOME=/opt/zookeeper-3.4.5
export PATH=.:$ZOOKEEPER_HOME/bin:$JAVA_HOME/bin:$PATH
# 使环境生效
source /et/profile
# 启动zookeeper
cd /opt/zookeeper-3.4.5/bin
zkService.sh start
# 查看启动状态
zkService.sh status
# 如果出现以下为启动成功
JMX enabled by default
Using config: /opt/zookeeper-3.4.5/bin/../conf/zoo.cfg
Mode: standalone
```
#### 二、集成txlcn
[TX-LCN官方文档](http://www.txlcn.org/zh-cn/docs/start.html)

TX-LCN 主要有两个模块，Tx-Client(TC) Tx-Manager(TM). TC作为微服务下的依赖，TM是独立的服务。

1、启动Tx-Manager(TM)服务

1.1、TM启动需要一个数据库
```
CREATE DATABASE /*!32312 IF NOT EXISTS*/`tx-manager` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `tx-manager`;

/*Table structure for table `t_tx_exception` */

DROP TABLE IF EXISTS `t_tx_exception`;

CREATE TABLE `t_tx_exception` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(64) DEFAULT NULL,
  `unit_id` varchar(32) DEFAULT NULL,
  `mod_id` varchar(128) DEFAULT NULL,
  `transaction_state` tinyint(4) DEFAULT NULL,
  `registrar` tinyint(4) DEFAULT NULL,
  `remark` varchar(4096) DEFAULT NULL,
  `ex_state` tinyint(4) DEFAULT NULL COMMENT '0未解决 1已解决',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
1.2、首先到github.com下载TM项目
```
# 下载项目
https://github.com/codingapi/tx-lcn
# 编译jar包
mvn clean install -Dmaven.test.skip=true
```
1.3、修改txlcn-tm配置文件内容
```
spring.application.name=TransactionManager
server.port=7970
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/tx-manager?characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123
spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto=update

# TxManager Host Ip
tx-lcn.manager.host=127.0.0.1
# TxClient连接请求端口
tx-lcn.manager.port=8070
tx-lcn.manager.admin-key=123456

# 心跳检测时间(ms)
tx-lcn.manager.heart-time=15000
# 分布式事务执行总时间
tx-lcn.manager.dtx-time=30000
#参数延迟删除时间单位ms
tx-lcn.message.netty.attr-delay-time=10000
tx-lcn.manager.concurrent-level=128
# 开启日志
tx-lcn.logger.enabled=true
logging.level.com.codingapi=debug
#redisIp
#spring.redis.host=127.0.0.1
#redis端口
#spring.redis.port=6379
#redis密码
#spring.redis.password=
```
1.4、启动txlcn-tm
```
cd txlcn-tm
->Run TMApplication
```
2、Tx-Client(TC)配置

2.1、加入依赖
```
<dependency>
    <groupId>com.codingapi.txlcn</groupId>
    <artifactId>txlcn-tc</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
<dependency>
    <groupId>com.codingapi.txlcn</groupId>
    <artifactId>txlcn-txmsg-netty</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
```
2.2、在项目启动类加入注解
```
@EnableDistributedTransaction
```
2.3、在项目的调用方的service中使用以下注解
```
@LcnTransaction
```
2.4、在项目的被调用方的service中使用以下注解、
```
@TxTransaction
```
#### 三、测试代码
在调用方的service的代码中手动写入异常 int s = 1/0;然后调用调用方controller接口，查看被调用方事务是否回滚，如果回滚则成功
