# 🛠️ Complaint Service

Complaint Service is a microservice for managing customer complaints.\
The application is built with **Spring Boot**, uses **PostgreSQL** as a database, and provides API documentation via **Swagger UI**.

---

## 🚀 Running the Application

### Running with Docker

To run the application in a Docker container, follow these steps:

1. Make sure you have **Docker** installed.
2. Start the application with:
   ```sh
   docker-compose up --build
   ```
3. Once the service is running, you can access:
   - **API:** [`http://localhost:8090`](http://localhost:8090)
   - **Swagger UI:** [`http://localhost:8090/swagger-ui.html`](http://localhost:8090/swagger-ui.html)

To stop the containers, use:

```sh
docker-compose down
```

---

### Running Without Docker

#### ✅ **Requirements**

- **Java 21** or later
- **Maven 3.8+**
- **PostgreSQL 15+** (if using an external database)

#### 🔹 **1. Configure the Database**

If you want to use a local PostgreSQL database, make sure:

- You have started the database and created a schema `complaint_db`
- You have a user `complaint_user` with the password `StrongPassword572!%`

Alternatively, you can configure an **in-memory database** (H2) for testing by modifying `application.yml`.

#### 🔹 **2. Build and Run**

1. Clone the repository:
   ```sh
   git clone https://github.com/patrykbadowski12/complaint-service.git
   cd complaint-service
   ```
2. Build the application:
   ```sh
   mvn clean package
   ```
3. Run the application:
   ```sh
   mvn clean spring-boot:run
   ```

The application should now be available at [`http://localhost:8090`](http://localhost:8090).

---

## 📚 API Documentation (Swagger UI)

Swagger UI is available at:\
🔗 [`http://localhost:8090/swagger-ui.html`](http://localhost:8090/swagger-ui.html)

Here you can find detailed API specifications and test endpoints.

---

## 🛠️ Technologies & Tools

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL / H2**
- **Swagger (Springdoc OpenAPI)**
- **Docker & Docker Compose**
- **JUnit 5 + Mockito**

---

## 📌 API Endpoints

| Method   | Endpoint                  | Description                    |
| -------- | ------------------------- | ------------------------------ |
| `POST`   | `/api/v1/complaints`      | Creates a new complaint        |
| `GET`    | `/api/v1/complaints`      | Retrieves a list of complaints |
| `GET`    | `/api/v1/complaints/{id}` | Retrieves complaint details    |
| `PUT`    | `/api/v1/complaints/{id}` | Updates a complaint            |

---

## 🔍 Running Tests

To execute unit and integration tests, run:

```sh
mvn test
```
