# Virtual Thread 性能压测演示

本项目用于演示 Spring Boot 3.2+ 中虚拟线程（Virtual Threads）的性能优势。

## 配置说明

### 传统线程模式
```properties
spring.thread.virtual.enabled=false
server.tomcat.threads.max=2
server.tomcat.threads.min-spare=2
```

### 虚拟线程模式
```properties
spring.thread.virtual.enabled=true
server.tomcat.threads.max=2
server.tomcat.threads.min-spare=2
```

## 测试接口

### 1. `/ping` - 基础健康检查
```bash
curl http://localhost:8080/ping
```

### 2. `/delay/{ms}` - 固定延迟
```bash
curl http://localhost:8080/delay/500
```
延迟指定的毫秒数。

### 3. `/random-delay/{maxMs}` - 随机延迟
```bash
curl http://localhost:8080/random-delay/500
```
延迟100到maxMs之间的随机毫秒数。

### 4. `/simulate-io` - 模拟I/O操作
```bash
curl http://localhost:8080/simulate-io
```
模拟I/O操作，延迟200-1000ms之间的随机时间。

## 压测场景

### 场景1: 低并发压测（传统线程模式）
```bash
hey -n 50 -c 50 -m GET http://localhost:8080/random-delay/500
```
- 50个并发请求
- 每个请求延迟100-500ms
- 预期：由于只有2个线程，大量请求会排队等待

### 场景2: 中等并发压测（传统线程模式）
```bash
hey -n 100 -c 100 -m GET http://localhost:8080/random-delay/1000
```
- 100个并发请求
- 每个请求延迟100-1000ms
- 预期：严重排队，响应时间很长

### 场景3: 高并发压测（虚拟线程模式）
```bash
hey -n 200 -c 200 -m GET http://localhost:8080/simulate-io
```
- 200个并发请求
- 每个请求延迟200-1000ms
- 预期：虚拟线程可以高效处理大量并发

## 性能对比步骤

### 步骤1: 测试传统线程模式

1. 确认配置：
```properties
spring.thread.virtual.enabled=false
server.tomcat.threads.max=2
server.tomcat.threads.min-spare=2
```

2. 启动应用：
```bash
cd virtual-thread
mvn clean install
java -jar target/virtual-thread-1.0-SNAPSHOT.jar
```

3. 运行压测：
```bash
hey -n 100 -c 100 -m GET http://localhost:8080/random-delay/1000
```

4. 记录结果：
- 总请求数
- 响应时间分布
- 错误率
- 每秒请求数（RPS）

### 步骤2: 测试虚拟线程模式

1. 修改配置：
```properties
spring.thread.virtual.enabled=true
server.tomcat.threads.max=2
server.tomcat.threads.min-spare=2
```

2. 重启应用：
```bash
# Ctrl+C 停止应用
java -jar target/virtual-thread-1.0-SNAPSHOT.jar
```

3. 运行相同的压测：
```bash
hey -n 100 -c 100 -m GET http://localhost:8080/random-delay/1000
```

4. 记录结果

### 步骤3: 对比分析

| 指标 | 传统线程模式 | 虚拟线程模式 | 提升 |
|--------|-------------|-------------|------|
| 平均响应时间 | ? | ? | ? |
| P95响应时间 | ? | ? | ? |
| P99响应时间 | ? | ? | ? |
| 每秒请求数 | ? | ? | ? |
| 错误率 | ? | ? | ? |

## 预期结果

### 传统线程模式（spring.thread.virtual.enabled=false）
- **优点**: 资源消耗可控
- **缺点**: 
  - 高并发时响应时间显著增加
  - 大量请求在队列中等待
  - 吞吐量受限于线程池大小（2个线程）
  - P95和P99响应时间会很长

### 虚拟线程模式（spring.thread.virtual.enabled=true）
- **优点**:
  - 可以轻松处理成千上万个并发请求
  - 响应时间更稳定
  - 吞吐量大幅提升
  - P95和P99响应时间显著降低
- **缺点**: 内存消耗略高（但远低于传统线程）

## 技术原理

### 传统线程
- 每个线程占用约1MB栈空间
- 线程切换开销大
- 线程数量受操作系统限制
- 适合CPU密集型任务

### 虚拟线程（Java 21+）
- 每个虚拟线程占用极小内存（几KB）
- 由JVM调度，不依赖操作系统
- 可以轻松创建百万级虚拟线程
- 适合I/O密集型任务

## 压测工具安装

### macOS
```bash
brew install hey
```

### Linux
```bash
wget https://github.com/rakyll/hey/releases/download/v0.1.4/hey_linux_amd64
chmod +x hey_linux_amd64
sudo mv hey_linux_amd64 /usr/local/bin/hey
```

### Windows
使用WSL或下载Windows版本的hey工具。

## 注意事项

1. **Java版本**: 需要Java 21+才能使用虚拟线程
2. **Spring Boot版本**: 需要Spring Boot 3.2+
3. **压测工具**: hey是HTTP压测工具，类似Apache Bench
4. **线程池配置**: 两种模式都使用相同的线程池配置（max=2），这样对比更公平

## 扩展测试

可以尝试不同的并发级别：
- 10个并发: `hey -n 10 -c 10`
- 50个并发: `hey -n 50 -c 50`
- 100个并发: `hey -n 100 -c 100`
- 200个并发: `hey -n 200 -c 200`
- 500个并发: `hey -n 500 -c 500`

观察随着并发增加，两种模式的性能差异会越来越明显。
