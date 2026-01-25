# Spring Boot RabbitMQ Project

## Tính năng chính

### Producer

- **REST API**: POST `/send` để gửi message
- **Persistent Messages**: Message không bị mất khi RabbitMQ restart
- **JSON Serialization**: Tự động convert Order object sang JSON

### Consumer

- **Manual Acknowledgment**: Kiểm soát hoàn toàn việc ACK/NACK
- **Dead Letter Queue**: Message lỗi tự động chuyển sang DLQ
- **Validation**: Kiểm tra orderId trước khi xử lý
- **Processing Time**: Simulate 3s processing

## Chạy Project

### 1. Build và Run với Docker Compose

```powershell
cd KTTKPM_DHKTPM18C_22655541_TranNgocHuyen
docker-compose up -d --build
```

### 2. Kiểm tra Logs

```powershell
# Xem logs của tất cả services
docker-compose logs -f

# Xem logs từng service
docker-compose logs -f producer
docker-compose logs -f consumer
docker-compose logs -f rabbitmq

# Kiểm tra Consumer đã xử lý thành công chưa
docker logs consumer 2>&1 | Select-String "Process success" | Select-Object -Last 1
```

### 3. Test Producer API

#### Gửi message hợp lệ (sẽ xử lý thành công)

```bash
curl -X POST http://localhost:8080/send -H "Content-Type: application/json" -d '{"message": "Order #123", "orderId": "ORD-001"}'
```

Response:

```json
{
  "status": "sent",
  "dataSent": {
    "message": "Order #123",
    "orderId": "ORD-001",
    "timestamp": "2026-01-25T04:37:48.241944214"
  }
}
```

#### Gửi message không hợp lệ (sẽ vào DLQ)

```bash
curl -X POST http://localhost:8080/send -H "Content-Type: application/json" -d '{"message": "Invalid Order", "orderId": ""}'
```

### 4. Quản lý RabbitMQ

- **Management UI**: http://localhost:15672
- **Username**: `user`
- **Password**: `password`

## Troubleshooting

### 1. Port already allocated

```powershell
# Tìm container đang chiếm port
docker ps -a | Select-String -Pattern "5672|rabbitmq"

# Stop và remove
docker stop rabbitmq-server spring-boot-app
docker rm rabbitmq rabbitmq-server spring-boot-app
```

### 2. Container không start được

```powershell
# Xóa containers và volumes
docker-compose down -v

# Rebuild
docker-compose up -d --build
```

### 3. Producer không kết nối được RabbitMQ

- Kiểm tra RabbitMQ đã healthy chưa: `docker-compose ps`
- Xem logs: `docker-compose logs rabbitmq`

### 4. Consumer không nhận message

```powershell
# Kiểm tra queue đã tạo chưa
docker-compose logs consumer

# Vào RabbitMQ UI xem có message trong queue không
# http://localhost:15672
```

### 5. Message không vào DLQ

- Kiểm tra queue configuration có `x-dead-letter-exchange` chưa
- Xem logs consumer có throw exception không

## Stop Project

````powershell
## Stop Project

```bash
# Stop containers
docker-compose down

# Stop và xóa volumes
docker-compose down -v
````
