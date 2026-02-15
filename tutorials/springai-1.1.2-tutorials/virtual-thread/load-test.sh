#!/bin/bash

echo "=========================================="
echo "Virtual Thread 性能压测脚本"
echo "=========================================="

BASE_URL="http://localhost:8080"

echo ""
echo "测试1: 基础ping测试（验证服务可用）"
curl -s $BASE_URL/ping
echo ""

echo ""
echo "测试2: 单个延迟测试（500ms）"
time curl -s $BASE_URL/delay/500
echo ""

echo ""
echo "=========================================="
echo "并发压测 - 传统线程模式 (spring.thread.virtual.enabled=false)"
echo "=========================================="

echo ""
echo "测试3: 并发50个请求，每个请求延迟200-500ms"
echo "命令: hey -n 50 -c 50 -m GET $BASE_URL/random-delay/500"
echo ""

echo "测试4: 并发100个请求，每个请求延迟200-1000ms"
echo "命令: hey -n 100 -c 100 -m GET $BASE_URL/random-delay/1000"
echo ""

echo "测试5: 模拟I/O操作，并发200个请求"
echo "命令: hey -n 200 -c 200 -m GET $BASE_URL/simulate-io"
echo ""

echo ""
echo "=========================================="
echo "使用说明："
echo "=========================================="
echo ""
echo "1. 先测试传统线程模式（spring.thread.virtual.enabled=false）："
echo "   - 修改 application.properties 中的 spring.thread.virtual.enabled=false"
echo "   - 运行: mvn clean install && java -jar target/virtual-thread-1.0-SNAPSHOT.jar"
echo "   - 在另一个终端运行压测命令"
echo ""
echo "2. 再测试虚拟线程模式（spring.thread.virtual.enabled=true）："
echo "   - 修改 application.properties 中的 spring.thread.virtual.enabled=true"
echo "   - 重启应用"
echo "   - 运行相同的压测命令"
echo ""
echo "3. 对比两种模式的性能指标："
echo "   - 响应时间 (Response time)"
echo "   - 吞吐量 (Requests/sec)"
echo "   - 错误率 (Errors)"
echo ""
echo "=========================================="
echo "安装hey压测工具："
echo "=========================================="
echo "macOS: brew install hey"
echo "Linux: 下载 https://github.com/rakyll/hey/releases"
echo "Windows: 使用WSL或下载Windows版本"
echo ""
echo "=========================================="
