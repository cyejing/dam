## Dam
[![Build](https://github.com/cyejing/dam/actions/workflows/ci.yml/badge.svg)](https://github.com/cyejing/dam/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/cyejing/dam/branch/master/graph/badge.svg?token=MLU4076I9A)](https://codecov.io/gh/cyejing/dam)

**Dam is high performance gateway**

## Features
* Netty
* 响应式网络处理
* 路由表达式灵活匹配Http请求协议体
* 灵活路由配置,功能可插拔配置  
* 基于SPI过滤器扩展,灵活扩展网关功能

## Quick Start
EG: dam-example

## Docs
### 网关处理逻辑
1. 网关优先匹配``global``的路由,确定路由分组
2. 按照路由顺序匹配分组里面的子路由
3. 执行路由过滤器配置
### 路由配置说明
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
  - host: test
    address: localhost:4843 
```

## Benchmark
fork from [spring-cloud-gateway-bench](https://github.com/cyejing/spring-cloud-gateway-bench) [gitee](https://gitee.com/cyejing/spring-cloud-gateway-bench)
