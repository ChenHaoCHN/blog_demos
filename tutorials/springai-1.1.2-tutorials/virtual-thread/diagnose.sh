#!/bin/bash

echo "=========================================="
echo "Virtual Thread 诊断工具"
echo "=========================================="

echo ""
echo "1. 检查Java版本..."
java -version 2>&1 | head -1

echo ""
echo "2. 检查是否支持虚拟线程..."
java -version 2>&1 | grep -q "21" && echo "✓ Java 21 - 支持虚拟线程" || echo "✗ Java版本不支持虚拟线程"

echo ""
echo "3. 检查当前配置..."
echo "=========================================="
grep -E "spring.thread.virtual.enabled|server.tomcat.threads.max" virtual-thread/src/main/resources/application.properties
echo ""

echo "4. 检查应用是否启动..."
curl -s --max-time 3 http://localhost:8080/ping > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✓ 应用正常运行"
else
    echo "✗ 应用未启动"
    echo ""
    echo "请先启动应用："
    echo "  cd virtual-thread && java -jar target/virtual-thread-1.0-SNAPSHOT.jar"
    exit 1
fi

echo ""
echo "5. 测试基础接口..."
echo "=========================================="
echo "测试 /ping 接口..."
curl -s http://localhost:8080/ping
echo ""

echo "测试 /delay/100 接口..."
time curl -s http://localhost:8080/delay/100
echo ""

echo "6. 虚拟线程配置说明"
echo "=========================================="
echo ""
echo "虚拟线程需要以下条件："
echo "  1. Java 21+"
echo "  2. Spring Boot 3.2+"
echo "  3. spring.thread.virtual.enabled=true"
echo ""
echo "如果虚拟线程没有生效，可能原因："
echo "  - Spring Boot版本过低（需要3.2+）"
echo "  - Java版本不支持虚拟线程（需要21+）"
echo "  - 配置文件中的属性名错误"
echo "  - 需要重启应用才能生效"
echo ""
echo "=========================================="
echo "诊断完成！"
echo "=========================================="
