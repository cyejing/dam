## Dam
[![Build](https://github.com/plus-all/dam/actions/workflows/ci.yml/badge.svg)](https://github.com/plus-all/dam/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/plus-all/dam/branch/master/graph/badge.svg?token=MLU4076I9A)](https://codecov.io/gh/plus-all/dam)

**Dam is High-Performance API Gateway**

## Features
* Netty
* 高性能响应式网络处理
* 路由表达式灵活匹配Http请求协议体
* 网关功能可插拔配置  
* 可基于SPI扩展网关功能

## Quick Start
EG: dam-example
### 网关快速启动
添加依赖
```xml
<dependency>
    <groupId>cn.cyejing</groupId>
    <artifactId>dam-core</artifactId>
    <version>${new.version}</version>
</dependency>
```
启动方法
```java
public static void main(String[] args) {
    new DamContainer(ConfigLoader.getInstance().load(args)).start();
}
```
配置route.yaml
```yaml
#route.yaml
routes:
  - id: 196105db9a384d7a93f8102ae46684cb  # 唯一Id
    group: test # 路由分组
    order: 100  # 路由匹配顺序
    global: true # 是否全局路由
    protocol: http # 路由协议
    expressionStr: Path.AntMatch('/replace/**') # 路由匹配表达式
    filterConfigs: # 路由过滤器配置
    - name: rewrite
      param:
        regex: "/replace/(.*)"
        replacement: "/$\\1"
    - name: proxy
      param:
        uri: "lb://test"
instances:
  - group: test
    uri: localhost:4843 
```

### 网关处理逻辑
1. 将请求与路由进行匹配
2. 优先匹配``global``路由,确定``group``分组
3. 按顺序遍历``group``分组下的所有路由,直到``expressionStr``匹配正确
4. 执行该路由过滤器配置

## Docs

[中文文档](docs/cn/README.md?type=blob)

## Benchmark

fork from [spring-cloud-gateway-bench](https://github.com/cyejing/spring-cloud-gateway-bench) [gitee](https://gitee.com/cyejing/spring-cloud-gateway-bench)

TL;DR

Proxy | Avg Latency | Avg Req/Sec/Thread
-- | -- | --
dam | 2.04ms | 107.869k
spring cloud gateway | 4.68ms | 43.827k
linkered | 5.23ms | 41.988k
zuul | 11.08ms | 22.757k
none | 3.25ms | 161.243k
