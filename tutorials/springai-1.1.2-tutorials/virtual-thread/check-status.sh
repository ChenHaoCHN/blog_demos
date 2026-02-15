#!/bin/bash

echo "=========================================="
echo "Virtual Thread 应用状态检查"
echo "=========================================="

BASE_URL="http://localhost:8080"

echo ""
echo "1. 检查应用是否启动..."
curl -s --max-time 3 $BASE_URL/ping 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✓ 应用正常运行"
else
    echo "✗ 应用未启动或端口被占用"
    echo ""
    echo "请检查："
    echo "  - 应用是否已启动：java -jar target/virtual-thread-1.0-SNAPSHOT.jar"
    echo "  - 端口8080是否被占用：lsof -i :8080"
    exit 1
fi

echo ""
echo "2. 测试基础接口..."
curl -s $BASE_URL/ping
echo ""

echo ""
echo "3. 测试短延迟（50ms）..."
time curl -s $BASE_URL/delay/50
echo ""

echo ""
echo "4. 测试中等延迟（500ms）..."
time curl -s $BASE_URL/delay/500
echo ""

echo ""
echo "=========================================="
echo "状态检查完成！"
echo "=========================================="
echo ""
echo "如果以上测试都通过，可以进行压测"
echo ""
