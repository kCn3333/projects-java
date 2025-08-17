# Job Portal

Job Portal is a web application built with **Spring Boot 3.4.5** that allows users to browse, manage, and apply to job postings.  
The project uses Spring MVC, Spring Security, JPA (Hibernate), PostgreSQL, and Thymeleaf with Bootstrap for the frontend.

---

## 🚀 Technologies

- **Java 21**
- **Spring Boot 3.4.5**
    - Spring Web (MVC + REST)
    - Spring Data JPA (Hibernate)
    - Spring Security
    - Spring Validation
    - Spring Boot DevTools
- **Thymeleaf + Bootstrap 5 (WebJars)**
- **PostgreSQL**
- **Lombok**
- **Flyway**

---

## ⚙️ Configuration

Example `application.properties`:

```properties
spring.application.name=jobportal

spring.datasource.url=jdbc:postgresql://localhost:5432/jobportal
spring.datasource.username=myuser
spring.datasource.password=mypassword
# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
logging.level.org.flywaydb=DEBUG
```
🔑 Make sure you have created a PostgreSQL database named jobportal and a user with the appropriate permissions.
Copy application-example.properties to application.properties and update with your database credentials.
🔑Database Migrations

The project uses Flyway to manage database schema changes.
All migration scripts are located in:
```
src/main/resources/db/migration
```

Files must be named as V<version>__<description>.sql (e.g., V1__init.sql).

Flyway automatically applies migrations at application startup.

Already applied migrations are not repeated, so restarting the app is safe.

## ▶️ Running the application
1. Clone the repository
```
    git clone https://github.com/kcn3333/projects-java/jobportal.git

    cd jobportal
```
2. Run with Maven
```
    ./mvnw spring-boot:run
```

or
```
    mvn spring-boot:run
```
3. Build jar
```
    mvn clean package
    java -jar target/jobportal-0.0.1-SNAPSHOT.jar
```
4. Access

The app will be available at:
👉 http://localhost:8080

## 📌 Features

- User roles

    - Seeker – can browse job offers, apply to jobs, and manage their profile (including profile picture).

    - Recruiter – can create, edit, and delete job offers, view applications, and see how many people applied.

- Account management – user registration and login (Seeker or Recruiter).

- Profile management – including uploading a profile picture.

- Job listings with filters

    - Type: Part-Time, Full-Time, Freelance

    - Remote: Remote-Only, Office-Only, Partial-Remote

    - Date Posted: Today, Last 7 Days, Last 30 Days

## 📂 Project structure
```
    com.kcn.jobportal
        ├── controller   # MVC controllers
        ├── service      # Business logic
        ├── repository   # JPA repositories
        ├── entity       # JPA entities
        ├── config       # Configuration (e.g., Security)
        ├── util         # for upload and download files
        └── JobPortalApplication.java
```
## 📜 License

This project is available under the MIT License.
You are free to use, modify, and distribute it according to the license terms.