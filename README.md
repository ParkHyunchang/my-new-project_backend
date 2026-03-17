# my-new-project_backend (Backend)

Spring Boot 3 기반 백엔드 API 서버입니다.

## 기술 스택

- **Java 17**
- **Spring Boot 3.2**
- **Spring Data JPA**
- **H2 (로컬)** / **MySQL 8.0 (운영)**
- **Lombok**

## 로컬 실행 방법

### 사전 준비

- [JDK 17+](https://adoptium.net/) 설치 확인

```bash
java -version
```

### 실행 (2가지)

| 구분 | DB | 명령어 | 비고 |
|------|-----|--------|------|
| **① H2 인메모리** | 메모리 DB (재시작 시 초기화) | `./mvnw spring-boot:run` | MySQL 설치 불필요, 빠른 테스트용 |
| **② NAS MySQL** | 실제 DB (my_new_project_db) | `./mvnw spring-boot:run "-Dspring-boot.run.profiles=local-mysql"` | NAS와 같은 네트워크 필요 |

```bash
# ① H2 인메모리 (기본)
./mvnw spring-boot:run

# ② NAS MySQL 연결 (PowerShell)
./mvnw spring-boot:run "-Dspring-boot.run.profiles=local-mysql"

# ② NAS MySQL 연결 (CMD / Bash)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local-mysql
```

서버가 실행되면 [http://localhost:3210](http://localhost:3210) 에서 API 응답을 확인할 수 있습니다.

### H2 콘솔 (①번 실행 시)

[http://localhost:3210/h2-console](http://localhost:3210/h2-console) 접속

| 항목     | 값                   |
| -------- | -------------------- |
| JDBC URL | `jdbc:h2:mem:testdb` |
| Username | `sa`                 |
| Password | (없음, 빈칸)         |

### 빌드

```bash
./mvnw clean package -DskipTests
```

빌드 결과물은 `target/` 폴더에 `.jar` 파일로 생성됩니다.

## 환경별 프로파일

| 프로파일       | DB                              | 용도                       |
| -------------- | ------------------------------- | -------------------------- |
| `local`        | H2 인메모리                     | 로컬 개발 (DB 없이 테스트) |
| `local-mysql`  | NAS MySQL (125.141.20.218:3306) | 로컬에서 NAS DB 작업       |
| `docker`       | MySQL 8.0 (Docker)              | NAS Docker 배포            |
| `nas`          | MySQL 8.0 (NAS)                 | NAS 직접 실행              |

## Docker로 실행 (NAS 배포)

`/volume1/docker/my-new-project_backend/` 경로에 `.env` 파일 생성:

```env
BACKEND_IMAGE=ghcr.io/parkhyunchang/my-new-project_backend:latest
MYSQL_ROOT_PASSWORD=gusckd88!
MYSQL_DATABASE=my_new_project_db
MYSQL_USER=hyunchang88
MYSQL_PASSWORD=gusckd88!
```

```bash
docker-compose up -d
```

| 서비스      | 포트 |
| ----------- | ---- |
| Backend API | 3210 |
| MySQL       | 3306 |
