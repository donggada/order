# 📦 Order Management API

## 📝 프로젝트 개요
이 프로젝트는 **주문 관리 시스템**으로, 다음과 같은 주요 기능을 제공합니다:
- **단건 주문 생성**: `OrderRequest` 객체를 받아 주문을 등록
- **엑셀 파일 업로드**: `MultipartFile`을 통해 다수의 주문을 한 번에 등록
- **분산 락(Distributed Lock) 적용**: **Redisson**을 사용하여 동시성 제어
- **전략 패턴**을 적용한 **OrderServiceFactory**로 주문 로직 분리

---

## ⚙️ 주요 기능
### 1. 단건 주문 등록
- **엔드포인트**: `POST /api/v1/order`
- **설명**:
  - `OrderRequest` JSON 객체를 받아 단일 주문을 등록합니다.
  - `Redisson` 기반의 `DistributeLockExecutor`를 통해 동시성 문제를 해결합니다.
  - `OrderServiceFactory`를 통해 `NOMAL` 타입의 주문 로직을 호출합니다.

### 2. 엑셀 주문 등록
- **엔드포인트**: `POST /api/v1/order/upload`
- **설명**:
  - `MultipartFile` 형태의 엑셀 파일을 받아 여러 주문을 한 번에 등록합니다.
  - `UploadService`가 엑셀 파일을 파싱하고 주문 등록을 처리합니다.

---

## 🔐 Redisson을 사용한 이유
- **Redisson**은 Redis를 활용하여 **분산 락(Distributed Lock)**을 구현할 수 있는 라이브러리입니다.
- 이 프로젝트에서는 **동시 주문 요청에 대한 데이터 정합성 문제를 해결**하기 위해 Redisson을 사용했습니다.

### 🔹 왜 Redisson인가?
1. **분산 환경에서의 동시성 제어**  
   - 멀티 서버 환경에서도 Redis를 통해 **글로벌 락**을 적용할 수 있습니다.
   - Spring의 `@Transactional`은 **단일 인스턴스에서만 유효**한 반면, **Redisson**은 **다중 인스턴스 환경에서도 안전한 동시성 제어**가 가능합니다.
2. **Redis의 `SETNX` 명령어를 사용한 락 구현**  
   - `SETNX`는 **분산 환경에서도 원자성(Atomicity)을 보장**하기 때문에, **동시성 문제**를 해결할 수 있습니다.
3. **자동 만료 시간(Lease Time) 설정**  
   - 락을 획득한 작업이 **비정상 종료**되더라도 자동으로 락을 해제하도록 `Lease Time`을 설정할 수 있습니다.
4. **사용의 편리함과 Spring Boot와의 연동성**

---

## 🛠️ 실행 전 준비 사항
### 1. 샘플 엑셀 파일 확인
- `src/main/resources/static` 디렉토리에 **샘플 엑셀 파일**(`sample.xlsx`)이 포함되어 있습니다.

### 2. Redis 실행
- **Redisson**을 사용하기 때문에 **Redis 서버**가 먼저 실행되어야 합니다.
- `docker-compose`를 사용하여 Redis를 간편하게 실행할 수 있습니다.
- 다음 명령어를 통해 Redis를 백그라운드에서 실행합니다:
  ```bash
  docker-compose up -d
  ```

---
📊 ERD (Entity-Relationship Diagram)

이 프로젝트의 ERD(Entity-Relationship Diagram)는 다음과 같습니다.

+--------------+             +----------------+             +-------------+
|    Order     |    1 : N    |    OrderItem    |    N : 1    |   Product   |
|--------------|             |----------------|             |-------------|
| id (PK)      |◀────────────| id (PK)         |────────────▶| id (PK)     |
| customerName |             | order_id (FK)   |             | name        |
| customerAddr |             | product_id (FK) |             | stock       |
+--------------+             | quantity        |             +-------------+
                             +----------------+

🔹 테이블 설명

Order (주문)

id (PK) : 주문 고유 ID

customerName : 주문자 이름

customerAddress : 주문자 주소

OrderItem (주문 항목)

id (PK) : 주문 항목 고유 ID

order_id (FK) : 주문 ID (Order와 다대일 관계)

product_id (FK) : 상품 ID (Product와 다대일 관계)

quantity : 주문 수량

Product (상품)

id (PK) : 상품 고유 ID

name : 상품 이름

stock : 상품 재고 수량

🔹 관계 설명

Order 1 : N OrderItem: 하나의 주문은 여러 개의 주문 항목을 가질 수 있습니다.

OrderItem N : 1 Product: 여러 개의 주문 항목이 하나의 상품을 참조할 수 있습니다.

