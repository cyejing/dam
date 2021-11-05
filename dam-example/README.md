## 快速体验样例

#### 反向代理
- 启动``DamBootstrap``主函数
- 浏览器打开[localhost:8048](http://localhost:8048)
- 网关成功代理到``baidu.com``

#### 文件服务
- 配置``route.yaml``resource服务的``filter``配置,指定``root``到本地文件的具体目录
- 浏览器打开localhost:8048/file/xxx.txt .即可打开具体文件

