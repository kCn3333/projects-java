 📦 Orders API 
---
is a RESTful backend service for managing **users, products, and orders**. 
It provides a secure authentication system with JWT tokens, role-based access control, and CRUD operations for products and orders. 
The API communicates exclusively using **JSON request and response bodies**, making it easy to integrate with frontend applications or other services. 
It is designed for scalability and maintainability, using PostgreSQL for persistence and Flyway for database migrations. 
API documentation is automatically generated via **Swagger UI**.
---
## 🚀 Technologies

- **Java 24**
- **Maven** 
- **Spring Boot 3.5.5**
    - Spring Web (MVC + REST)
    - Spring Data JPA (Hibernate)
    - Spring Security + JWT
    - Spring Validation
    - Spring Boot DevTools
- **Springdoc OpenAPI 3 / Swagger UI**
- **PostgreSQL**
- **Lombok**
- **Flyway**

---
## ⚙️ Configuration


Before running the project, you need to configure your local environment. The project uses Spring Boot application properties and expects sensitive data (like database credentials and JWT secret) to be defined locally.

#### 1. Create your local properties file

Copy the example configuration:
```
cp src/main/resources/application.example-properties src/main/resources/application.properties
```

#### 2. Fill in your values


`spring.datasource.url` – your PostgreSQL database URL, e.g., `jdbc:postgresql://localhost:5432/shop_db`

`spring.datasource.username` – your database username

`spring.datasource.password` – your database password

`spring.jwt.secret` – a long, random secret for signing JWT tokens

You can generate one using:
```
openssl rand -hex 32
```

This generates a secure 64-character hexadecimal string.

`spring.jwt.expiration` – token expiration in milliseconds (default: 900000 for 15 minutes)

Example `application.properties`:
```
spring.application.name=orders-api
# DB
spring.datasource.url=jdbc:postgresql://localhost:5432/shop_db
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.datasource.driver-class-name=org.postgresql.Driver
# Swagger URL
springdoc.swagger-ui.path=/docs
# JWT
spring.jwt.secret=cfe243f4599747553e338c32db5d3817d6fac07687068c0c7c94e377c4a97021
spring.jwt.expiration=900000
# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
logging.level.org.flywaydb=DEBUG
```
🔑 Make sure you have created a PostgreSQL database named `shop_db` and a user with the appropriate permissions.

🔑 Database Migrations. The project uses Flyway to manage database schema changes. All migration scripts are located in: `src/main/resources/db/migration`


Files must be named using the `V__description.sql` format (e.g., `V1__init.sql`). Flyway automatically applies any pending migrations at application startup.

Currently, the project includes:

`V1__create_tables.sql` – creates the initial database schema.

`V2__sample_products.sql` – inserts sample products into the products table.

These scripts are executed in order, so V2__sample_products.sql runs after the initial schema is created.

⚠️ Note: Flyway only applies migrations that haven’t been applied yet. If you modify an existing migration after it has been executed, changes will not be applied automatically. To apply new changes, create a new migration version (e.g., `V3__update_products.sql`).

Database Schema

## ▶️ Running the application
### 1. Clone the repository
```
   git clone https://github.com/kcn3333/projects-java.git

   cd projects-java/orders-api
   ```
### 2. Run with Maven
```
   ./mvnw spring-boot:run
   ```
   or
```
   mvn spring-boot:run
   ```
### 3. Build jar
```
   mvn clean package
   java -jar target/orders-api-0.0.1-SNAPSHOT.jar
   ```
### 4. Access
   The app will be available at: 👉 http://localhost:8080/

---

## 🐳 Running with Docker
The application can also be run inside Docker containers using Spring Boot + PostgreSQL.

1. Build the application image
   From the project root:

```
docker build -t orders-api-app:latest .
```
2. Run with Docker Compose
   A docker-compose.yml file is provided to run both the application and PostgreSQL. Start everything with:
```
docker-compose up -d
```
This will start two containers:

orders-db → PostgreSQL database (exposed on port DB_PORT)

orders-api-app → Spring Boot application (exposed on port APP_PORT)

Example docker-compose.yml:
```yml
version: '3.9'

services:
  db:
    image: postgres:15-alpine
    container_name: orders-db
    ports:
      - "${DB_PORT}:5432"
    environment:
      POSTGRES_DB: ${DB_NAME}
      POSTGRES_USER: ${DB_USER}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - pgdata:/var/lib/postgresql/data
    networks:
      - orders-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${DB_USER} -d ${DB_NAME}"]
      interval: 5s
      timeout: 5s
      retries: 5

  app:
    image: kcn333/orders-api-app:latest
    container_name: orders-api-app
    ports:
      - "${APP_PORT}:8080"
    environment:
      DB_HOST: ${DB_HOST}
      DB_PORT: ${DB_PORT}
      DB_USER: ${DB_USER}
      DB_PASSWORD: ${DB_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
      JWT_EXPIRATION: ${JWT_EXPIRATION}
      SPRING_APPLICATION_NAME: ${APP_NAME}
    depends_on:
      db:
        condition: service_healthy
    networks:
      - orders-network

volumes:
  pgdata:

networks:
  orders-network:
    driver: bridge
```
3. Environment variables
   Create a .env file in the project root with the following:
```
# Database
DB_HOST=db
DB_PORT=5432
DB_USER=myuser
DB_PASSWORD=mypassword
DB_NAME=shop_db

# JWT
JWT_SECRET=cfe243f4599747553e338c32db5d3817d6fac07687068c0c7c94e377c4a97021
JWT_EXPIRATION=900000

# Spring app
APP_NAME=orders-api
APP_PORT=8080
```
4. Access

    Swagger UI → localhost:APP_PORT/docs

    Database → localhost:DB_PORT


5. Stop containers
```
   docker-compose down
```
## 📌 Features
When the application is started for the first time, the **first registered user is automatically assigned the ADMIN role**, which allows managing products and order statuses. 
Subsequent users are registered with the USER role by default.
### Authorization & Roles

USER – can place orders and view own orders.

ADMIN – can manage products and view/update all orders.

PUBLIC - endpoints don’t require authentication; other endpoints require login.

JWT token must be sent in header:

```
    Authorization: Bearer <token>
   ```

### Swagger

Swagger UI available at:

http://localhost:8080/docs


JWT Bearer is supported via `@SecurityScheme(name="bearerAuth", ...)`.

---

## 📡 Endpoints
```
>> Authentication
Method	    Endpoint	                Description	                    Role
POST	    /api/auth/register	        Register a new user	            PUBLIC
POST	    /api/auth/login	            Authenticate user, returns JWT	 PUBLIC
>> Users
Method	    Endpoint	                Description                    	Role
GET	        /api/users/info	            Get current user info	        USER / ADMIN
DELETE	    /api/users	                Delete current user	            USER / ADMIN
PUT	        /api/users/password	        Update password         	    USER / ADMIN
>> Products
Method	    Endpoint	                Description	                Role
GET	        /api/products	            List all products	        PUBLIC
GET	        /api/products/{id}	        Get product details	        PUBLIC
POST	    /api/products	            Add a new product	        ADMIN
PUT	        /api/products/{id}	        Update product	            ADMIN
DELETE	    /api/products/{id}	        Delete product	            ADMIN
>> Orders
Method	    Endpoint	                Description	                Role
GET	        /api/orders                	List current user's orders	USER / ADMIN
POST	    /api/orders	                Create new order	        USER / ADMIN
GET	        /api/orders/{orderId}	    Get order details	        ADMIN
PUT	        /api/orders/{orderId}/status	Update order status	    ADMIN
```


## 📂 Project structure
```
com.kcn.orders_api
    ├── config           # Security and Swagger configuration
    ├── controller       # REST controllers
    ├── dto              # Request and Response DTOs
    │   ├── request
    │   ├── response
    │   └── exception
    ├── model            # JPA entities: User, Authority, Product, Order, OrderItem
    ├── repository       # Spring Data JPA repositories
    ├── service          # Business logic
    ├── security         # JWT Authentication
    ├── exception        # Exception Handler
    └── util             # Utility classes e.g. AuthenticationUtils
```
## 📜 License

This project is available under the MIT License.
You are free to use, modify, and distribute it according to the license terms.