#httpdb 

1、基于网络的JDBC实现，无需暴露数据库访问地址，账号，密码等信息，即可实现数据的快速增加、删除、修改、更新操作
2、分别基于Mina、Netty、Grizzly三大高性能IO通讯框架实现高性能的数据传输与处理服务（待实现）
3、基于此组件可进行二次开发，实现多数据源集中式查询服务（展望）。
	例1：在一个系统中的数据集模块，我们可以实现采集不同应用的数据，从而无需将常用的基础数据，各个数据库进行同步更新；解决数据时效性和一致性问题
	例2：


http://www.xdocin.com/httpdb.html#guide

J2EE Web应用
JDBC驱动
使用HTTP/HTTPS协议
数据传输使用AES强加密
仅支持查询
简单、轻量
LGPL协议开源
 
#使用说明
下载HttpDB安装包，部署到Java应用服务器
修改WEB-INF/httpdb.xml配置数据源
WEB-INF/lib下加入你配置的数据库的JDBC驱动
下载HttpDB的JDBC驱动加入到你的Java应用中调用
驱动类:com.hg.httpdb.Driver
URL地址:http://[服务IP]:[端口]/[Context Path(通常为httpdb)]/httpdb
用户名:HttpDB数据库名称
口令:配置的口令
下载
HttpDB安装包
JDBC驱动
源代码
HttpDBTest.java
示例