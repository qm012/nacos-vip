# Nacos 单机/集群的vip地址服务中心

## Current project archive， New language development address。
### https://github.com/qm012/nacos-address

## 简介

&emsp;&emsp;&emsp;基于Nacos（官方网站:http://nacos.io ）的额外web服务器，针对`服务端`和`客户端`地址寻址，减少改动(服务端和客户端项目)，方便动态扩容和管理。
适用于自建Nacos的单机或集群管理，[阿里云的MSE微服务引擎托管](https://cn.aliyun.com/product/aliware/mse)则不需要考虑，官方已经处理好。<br/><br/>
&emsp;&emsp;&emsp;在去年公司需要使用配置中心时，通过调研和选型，最终使用Nacos来作为配置中心和注册中心。在使用的过程中，我们
也发现了一些问题，在客户端项目中(spring cloud)和其他客户端(SpringBoot，Go，Node.js，Python...等等)我们在配置服务器地址时 `spring.cloud.nacos.config.serverAddr=127.0.0.1:8848,127.0.0.2:8848,127.0.0.3:8848`，
如果我们有100+个客户端，地址发生变化，是不是需要对100个客户端都进行更改，这样的方式并不灵活。官方文档和博客上也没有讲的很细，基本只有配置`serverAddr`的方式，而服务端集群模式下，下线或上线新的服务端机器，
我们需要在`cluster.conf`进行地址变更，也会出现类似的场景，所以去看源码和调研，产生了这个项目。<br/><br/>
&emsp;&emsp;&emsp;适配客户端和服务端，提供使用方式和部署文档、对不同存储方式的地址列表的统一API管理、对Docker的支持等等，同时也欢迎大家使用、建议、并贡献。如果对大家有所帮助或将来有所帮助，欢迎`Star`一下哦

## 存储支持
- [x] Redis (单机+集群) 
- [x] Cache (单机)
- [ ] 内嵌derby (单机+集群) (开发中)

## 功能
- [x] Docker支持 
- [ ] 脚本启动 (开发中)

## 参数

  * nacos-vip系统参数
     
 | 参数名 | 含义 | 可选值 | 默认值 |
 | ------------ | ------------ | ------------ | ------------ |
 | nacos-vip.accessToken(-D)     | 操作api的秘钥(从Header头的`Access-Token`获取) | String    | NULL |
 | nacos-vip.clusterListSize(-D) | 操作api的传递ip列表大小的界限值               | Integer    | NULL |
 | nacos-vip.standalone(-D)      | 是否单机模式                               | true/false | false |

  * nacos-vip数据源参数
     
 | 参数名 | 含义 | 可选值 | 默认值 |
 | ------------ | ------------ | ------------ | ------------ |
 | spring.redis.host(-D)     | redis地址 | String  | NULL |
 | spring.redis.port(-D)     | redis端口 | int     | NULL |
 | spring.redis.database(-D) | redis库   | int     | NULL |
 | spring.redis.password(-D) | redis密码 | String  | NULL |

## 使用

### jar包方式使用

1. 拉取项目到本地编译或下载已经打好的[nacos-vip-1.0.2.jar](https://gitee.com/qm0123/nacos-vip/attach_files/458888/download)包
2. 启动
    * 注:无论单机或集群启动，都建议使用配置redis方式。
    1. 单机 (-Dnacos-vip.standalone=true)
        1. 配置redis:   redis存储数据
        2. 不配置redis: Cache存储数据(数据的生命周期到当前进程结束后)
    2. 集群 (无需配置:默认为false)
        1. 配置redis:   redis存储数据  
        2. 不配置redis: 内嵌derby存储数据(开发中)
        
    3. 单机或集群启动时如果已配置redis将会把redis作为存储模式(也可拉取代码在代码中修改 `application.properties`)
       ```
       java -jar -Dspring.redis.database=1 -Dspring.redis.host=127.0.0.1 -Dspring.redis.port=6379 -Dspring.redis.password=123456 nacos-vip-1.0.2.jar > logs/catalina.out 2>&1 & 
       ```  
    4. 单机启动不配置redis
       ```
       java -jar -Dnacos-vip.standalone=true nacos-vip-1.0.2.jar > logs/catalina.out 2>&1 & 
       ```

### docker方式使用

1. 参数选项
    * -v 宿主机路径:/home/nacos-vip/logs
    * -e JAVA_OPT="-Dnacos-vip.standalone=true"
    * -p 宿主机端口:8849
    * --name 容器名称
    * -d 后台运行
    * 其余选项请参考docker运行相关命令
    
2. 运行(默认会从DockerHub拉取 无需docker pull)
   
    * 关于集群和单机的模式和相关参数传递请参考 jar包方式使用的描述信息
    
   ```
   docker run -d qm0123/nacos-vip
   ```

       
## nacos服务端和客户端配置部署

### 使用代理(nginx)

```
server {
    # 监听端口，对应客户端和服务端发起请求所指定的连接点端口 默认值8080，无需更改
    listen                 8080;
    # 自定义域名值 对应客户端和服务端配置的域名
    server_name            nacos-vip.aliyun.com;
    location / {
        # nacos vip 服务的ip:port,此处也可以使用upstream 代理
        proxy_pass         http://127.0.0.1:8849/;
        proxy_set_header   Host             $host:$server_port;
        proxy_set_header   X-Real-IP        $remote_addr;
        proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        proxy_set_header   Access-Control-Allow-Origin  *;
    }
}
```

### Nacos服务端

1. 单机
    1. 启动
        ```
        sh startup.sh -m standalone
        ```
2. 集群
    1. 启动
        ```
        sh startup.sh
        ```
    2. 配置
        1. 当前最新版本提供配置集群列表的方式
            1. 解压目录nacos/的conf目录下，有配置文件cluster.conf更改 ip:port 列表
            2. application.properties配置系统参数获取 nacos.member.list=192.168.16.101:8847?raft_port=8807，192.168.16.101?raft_port=8808，192.168.16.101:8849?raft_port=8809
            3. AddressServer vip模式的寻址方式 (推荐使用)
        2. 针对第3种方式的配置
            1. 从环境变量读取
                1. windows 我得电脑->右键属性->高级系统设置->环境变量->新建环境变量 变量名:address_server_domain 变量值: 自定义域名值(例如:nacos-vip.aliyun.com(对应nginx代理的 server_name))
                2. linux 同理
            2. 从Nacos系统参数读取(推荐)
                * 注:(外置/内置)数据源配置方式请参考Nacos（官方网站:http://nacos.io）
                ```
                 # 需要更改 初始化寻址模式
                 nacos.core.member.lookup.type=address-server
                 # 需要更改(自己设定的域名即可) 对应nginx代理的 server_name 
                 address.server.domain=nacos-vip.aliyun.com 
                 # 无需更改 nacos的获取地址请求的端口默认8080
                 # address.server.port=8080
                 # 需要更改 nacos的获取地址请求的链接是 /nacos/serverlist ，但是客户端和服务端的获取列表协议并不兼容，所以我们需要新开一个接口去兼容服务端
                 address.server.url=/nacos/server/serverlist
                ```
         3. 我们正常启动，使用下面的Open api方式就可以操作nacos的服务列表了，服务端就可以正常发现了(建议在nacos-vip服务启动后通过`Open API`操作初始化好服务列表数据)


### Nacos客户端

1. 配置(spring cloud)
   ```
   spring:
      cloud:
        nacos:
          config:
            file-extension: yaml
            prefix: ${spring.application.name}
            # 连接Nacos Server指定的连接点 我们将域名放入对应的 endpoint 上
            endpoint: nacos-vip.aliyun.com
          discovery:
            endpoint: nacos-vip.aliyun.com
   ```
   
2. 域名指定地址的方式
    1. 本地hosts配置
        ```
        127.0.0.1 nacos-vip.aliyun.com
        ```
    2. 局域网内 使用 DNS 解析域名
    3. 阿里云内网的负载SLB(免费，客户端部署到阿里云服务器时就无需指定域名了，唯一的缺点可能某些时刻在大区下配额不足.)
    
3. 类似于其他客户端(SpringBoot，Go，Node.js，Python...等等)接入的方式同理

4. 针对于客户端，无论服务端是单机还是集群，我们都可以使用这种方式，方便以后的扩展，一次配置永久使用(除非域名更改的情况下) 在也不用为注册中心和服务中心扩容需要更新所有的服务而担心了。

## Open API 指南

### 查询nacos服务地址列表-客户端

1. 请求示例
    ```
    curl http://127.0.0.1:8849/nacos/serverlist
    ```
2. 返回示例
    ```
    127.0.0.1
    127.0.0.2
    127.0.0.3

    ```    
   
### 查询nacos服务地址列表-服务端

1. 请求示例
    ```
    curl http://127.0.0.1:8849/nacos/server/serverlist
    ```
2. 返回示例
    ```
    {
        "code": 200，
        "message": null，
        "data": "127.0.0.1\n127.0.0.2\n127.0.0.3\n"
    }
    ```
    
### 增加nacos服务端地址

1. 请求示例
    ```
    curl http://127.0.0.1:8849/nacos/serverlist -X POST -H "Content-Type:application/json" -H "Access-Token:" -d '{"clusterIps": ["127.0.0.1","127.0.0.2","127.0.0.3"]}' -v
    ```
2. 返回示例
    ```
   {
       "code": 200，
       "message": null，
       "data": null
   }
    ```

### 移除nacos服务端地址

1. 请求示例
    ```
    curl http://127.0.0.1:8849/nacos/serverlist -X DELETE -H "Content-Type:application/json" -H "Access-Token:" -d '{"clusterIps": ["127.0.0.1"]}' -v
    ```

2. 返回示例
    ```
   {
       "code": 200，
       "message": null，
       "data": null
   }
    ```
       
### 清空nacos服务端地址

1. 请求示例
    ```
    curl http://127.0.0.1:8849/nacos/serverlist/all -X DELETE -H "Content-Type:application/json" -H "Access-Token:" -v
    ```
2. 返回示例
    ```
   {
       "code": 200，
       "message": null，
       "data": null
   }
    ```
