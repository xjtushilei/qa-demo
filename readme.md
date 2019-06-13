# 准备

- jdk1.8
- es（如果需要es作为后端匹配算法）
    - 下载  https://dev-1252377804.cos.ap-beijing.myqcloud.com/elasticsearch-6.2.4-with-ik.zip  该es已经集成ik中文分词，同时该es版本是最新的免费版本，再往上的es版本es这家公司改了开源协议，商业环境暂时不推荐使用。 
    - 安装 
        - windows下
            - 安装到服务 `bin\elasticsearch-service.bat install`
            - 不安装服务直接运行 双击`bin\elasticsearch.bat`
        - linux下
            - `bin目录下`后台运行即可 `./elasticsearch -d` ，需确保`elasticsearch` 文件可执行权限
            

# 启动

### 编译

根目录下运行
- windows下 `gradlew build`
- linux下 `./gradlew build`   需确保`gradlew`文件可执行权限

第一次编译会下载指定版本gradle工具，所以慢

编译结果为： `build\libs\auto-qa-0.0.1.jar`

### 运行

修改配置文件以确保可以运行，然后执行

`java -jar build\libs\auto-qa-0.0.1.jar`

linux下可以后台执行，`nohup java -jar build\libs\auto-qa-0.0.1.jar &` ,windows下的命令行不支持后台运行，关掉命令行服务就终止了



# 文件说明

1. `src\main\resources\application.yml`  
    1. 本服务的本地端口
    1. 微信相关配置
    1. 不同文件夹的日志级别配置
1. `src\main\java\com\xjtu\dlc\autoqa\config`
    1. `CorsConfig.java` 浏览器端跨域设置
    1.  `DataConfig.java` 数据存放位置
    1.  `SwaggerConfig.java` api文档配置
1. `src\main\java\com.xjtu.dlc.autoqa.weixin.handler.MenuHandler.java`  
    - 处理菜单栏中的事件
1. `src\main\java\com.xjtu.dlc.autoqa.weixin.handler.MsgHandler.java`  
    - 第41行修改微信使用的后台api，可以使用目前实现的两个，如文件说明6
    - 37行的 userID 就是我们微信的所有用户的唯一id，传统软件绑定就是将这个id和自己系统的用户id进行绑定
1. `src\main\java\com.xjtu.dlc.autoqa.qa.data.ExcelHandler.java` 处理excel文件，文件需要放在文件说明2.2文件中提到的文件夹中
    - 支持文件夹嵌套
    - 仅支持 ".xls"后缀的文件，其他类型的文件会自动忽略
1. `src\main\java\com.xjtu.dlc.autoqa.qa.controller.MainController.java`  web端的api
1. `src\main\java\ccom.xjtu.dlc.autoqa.qa.answer` 存放了和后端处理策略相关的文件
    - `EsHandler` es实现排序
    - `CosineSimilarAlgorithmHandler` 网上随便找的一个相似度计算的
1. `src\main\java\com.xjtu.dlc.autoqa.weixin.controller`
    -  `WechatController`    微信端进行通信的api（鉴权，正常的交互）
    -  `WxMenuController` 微信菜单栏设置
1. `src\main\resources\menu\menu.json` 菜单栏的设置文件
1. `src\main\resources\static\` web的前端文件

# 功能说明

1. http://localhost:8041/  访问index.html前端文件
1. http://localhost:8041/swagger-ui.html#/ 查看api文档，可以进行api测试和微信菜单栏设置
1. https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index 微信测试公众号设置
1. https://xjtushilei.github.io/es-head/ 在开发时候，运行了es的电脑上可以打开查看里面的数据和进行搜索


# 开发测试
## ngrok
微信端 需要使用 ngrok 才能在开发机上直接测试微信服务，微信不支持ip，且不支持80以外的端口
 
- 下载 https://dashboard.ngrok.com/get-started  并注册官网账号
- 首次使用 `ngrok authtoken xxxxxxxxxxxxxxxxxxxx`
- 以后使用 `ngrok http 8041`   以本地后端服务的port为准

# 部署
需要需要域名指向指定服务器ip，然后服务器上进行nginx反向代理。

nginx（配置文件一般位于 usr/local/nginx/conf/中的ngxin.conf或 hosts文件夹里） 反向代理举例：

```
server {
    listen 80;
    server_name   wechat.yotta.xjtushilei.com;
    index       index.html index.htm;
    location / {
        proxy_pass   http://localhost:8041/;
    }
}
```