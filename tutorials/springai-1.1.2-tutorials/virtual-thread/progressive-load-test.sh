#!/bin/bash

echo "=========================================="
echo "Virtual Thread 渐进式压测"
echo "=========================================="

BASE_URL="http://localhost:8080"

# 检查应用是否启动
echo ""
echo "检查应用状态..."
curl -s --max-time 3 $BASE_URL/ping > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "✗ 应用未启动！"
    echo "请先运行: java -jar target/virtual-thread-1.0-SNAPSHOT.jar"
    exit 1
fi

echo "✓ 应用正常运行"
echo ""

# 获取当前配置
echo "=========================================="
echo "当前配置："
echo "=========================================="
grep -E "spring.thread.virtual.enabled|server.tomcat.threads.max|server.tomcat.accept-count" src/main/resources/application.properties
echo ""

# 渐进式压测
echo "=========================================="
echo "开始渐进式压测..."
echo "=========================================="
echo ""

# 测试1: 低并发（10个并发）
echo "【测试1】低并发压测 - 10个并发，每个请求延迟200ms"
echo "命令: hey -n 100 -c 10 -m GET $BASE_URL/delay/200"
echo ""
hey -n 100 -c 10 -m GET $BASE_URL/delay/200
echo ""

sleep 2

# 测试2: 中等并发（50个并发）
echo "【测试2】中等并发压测 - 50个并发，每个请求延迟300ms"
echo "命令: hey -n 200 -c 50 -m GET $BASE_URL/delay/300"
echo ""
hey -n 200 -c 50 -m GET $BASE_URL/delay/300
echo ""

sleep 2

# 测试3: 高并发（100个并发）
echo "【测试3】高并发压测 - 100个并发，每个请求延迟500ms"
echo "命令: hey -n 500 -c 100 -m GET $BASE_URL/delay/500"
echo ""
hey -n 500 -c 100 -m GET $BASE_URL/delay/500
echo ""

sleep 2

# 测试4: 极高并发（200个并发）
echo "【测试4】极高并发压测 - 200个并发，随机延迟200-1000ms"
echo "命令: hey -n 1000 -c 200 -m GET $BASE_URL/random-delay/1000"
echo ""
hey -n 1000 -c 200 -m GET $BASE_URL/random-delay/1000
echo ""

sleep 2

# 测试5: 模拟I/O操作（500个并发）
echo "【测试5】模拟I/O操作 - 500个并发，随机延迟200-1000ms"
echo "命令: hey -n 2000 -c 500 -m GET $BASE_URL/simulate-io"
echo ""
hey -n 2000 -c 500 -m GET $BASE_URL/simulate-io
echo ""

echo "=========================================="
echo "压测完成！"
echo "=========================================="
echo ""
echo "请观察每个测试的结果："
echo "  - Response time (响应时间)"
echo "  - Requests/sec (每秒请求数)"
echo "  - Errors (错误率)"
echo ""
echo "随着并发数增加，虚拟线程模式的优势会越来越明显"
echo ""
