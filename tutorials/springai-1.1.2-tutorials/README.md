
## 压测
- 使用hey压测，100并发，100000次请求，可以观察进度
```bash
hey -n 100000 -c 1000 http://localhost:8080/ping

hey -n 100 -c 10 http://localhost:8080/delay/500
```