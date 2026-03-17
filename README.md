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

> 로컬 환경은 H2 인메모리 DB를 사용하므로 **MySQL 설치 불필요**합니다.

### 실행

```bash
# Maven Wrapper 사용 (권장)
./mvnw spring-boot:run

# 또는 Maven 직접 사용
mvn spring-boot:run
```

서버가 실행되면 [http://localhost:3210](http://localhost:3210) 에서 API 응답을 확인할 수 있습니다.

### H2 콘솔 (로컬 DB 확인)

로컬 실행 중 브라우저에서 [http://localhost:3210/h2-console](http://localhost:3210/h2-console) 접속

| 항목 | 값 |
|------|-----|
| JDBC URL | `jdbc:h2:mem:testdb` |
| Username | `sa` |
| Password | (없음, 빈칸) |

### 빌드

```bash
./mvnw clean package -DskipTests
```

빌드 결과물은 `target/` 폴더에 `.jar` 파일로 생성됩니다.

## 환경별 프로파일

| 프로파일 | DB | 실행 조건 |
|----------|-----|----------|
| `local` (기본) | H2 인메모리 | 로컬 개발 |
| `docker` | MySQL 8.0 | Docker Compose |

## Docker로 실행 (MySQL 포함)

`.env` 파일을 먼저 생성하세요:

```env
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=new_project_db
MYSQL_USER=myuser
MYSQL_PASSWORD=mypassword
```

```bash
docker-compose up -d
```

| 서비스 | 포트 |
|--------|------|
| Backend API | 3210 |
| MySQL | 3316 |
