#!/bin/bash

echo "=========================================="
echo "Virtual Thread 功能测试"
echo "=========================================="

BASE_URL="http://localhost:8080"

echo ""
echo "1. 测试ping接口..."
curl -s $BASE_URL/ping
echo ""

echo ""
echo "2. 测试固定延迟接口（500ms）..."
time curl -s $BASE_URL/delay/500
echo ""

echo ""
echo "3. 测试随机延迟接口（最大1000ms）..."
curl -s $BASE_URL/random-delay/1000
echo ""

echo ""
echo "4. 测试模拟I/O接口..."
curl -s $BASE_URL/simulate-io
echo ""

echo ""
echo "=========================================="
echo "功能测试完成！"
echo "=========================================="
echo ""
echo "接下来可以进行压测："
echo "  hey -n 100 -c 100 -m GET $BASE_URL/random-delay/1000"
echo ""
