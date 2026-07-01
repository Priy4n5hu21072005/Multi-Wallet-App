# Multiwallet

Multiwallet is a Spring Boot application designed to manage multiple wallets. It provides a robust backend architecture using Java 26, Spring Boot 3.5, and PostgreSQL. 

## 🏗 Project Structure

The project follows a standard Spring Boot layered architecture:

```
multiwallet/
├── .mvn/                   # Maven wrapper files
├── src/
│   ├── main/
│   │   ├── java/com/example/multiwallet/
│   │   │   ├── controller/ # REST API endpoints
│   │   │   ├── entity/     # Database entities / JPA models
│   │   │   ├── repository/ # Spring Data JPA repositories
│   │   │   ├── service/    # Business logic layer
│   │   │   └── MultiwalletApplication.java # Application entry point
│   │   └── resources/
│   │       ├── static/     # Static web assets
│   │       ├── templates/  # Server-side views (e.g., Thymeleaf)
│   │       └── application.properties # Application configuration
│   └── test/               # Unit and integration tests
├── mvnw, mvnw.cmd          # Maven wrapper scripts
└── pom.xml                 # Maven configuration and dependencies
```

## 🛠️ Tech Stack

* **Java:** 26
* **Framework:** Spring Boot 3.5.16
* **Database:** PostgreSQL
* **ORM:** Spring Data JPA / Hibernate
* **Build Tool:** Maven
* **Utilities:** Lombok (for boilerplate code reduction), Spring Boot Validation

## ⚙️ Prerequisites

Before you begin, ensure you have met the following requirements:
* **Java Development Kit (JDK) 26** installed.
* **PostgreSQL** database server running locally.
* **Maven** installed (optional, as the Maven Wrapper `mvnw` is included).

## 🗄️ Database Configuration

1. Create a PostgreSQL database named `multiwallet_db`.
2. Update the `src/main/resources/application.properties` file with your database credentials if they differ from the default setup:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/multiwallet_db
spring.datasource.username=postgres
spring.datasource.password=Priyanshu@217 # Replace with your local Postgres password

# Hibernate configuration (Update strategy will automatically create/update tables)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 🚀 Running the Application

You can run the application directly using the provided Maven wrapper.

**On Windows:**
```cmd
.\mvnw spring-boot:run
```

**On Linux/macOS:**
```bash
./mvnw spring-boot:run
```

The application will start on the default port, usually `http://localhost:8080`.

## 📦 Building for Production

To build a runnable JAR file for production deployment, use the following command:

**On Windows:**
```cmd
.\mvnw clean package
```

**On Linux/macOS:**
```bash
./mvnw clean package
```

The compiled JAR file will be located in the `target/` directory. You can run it using:
```bash
java -jar target/multiwallet-0.0.1-SNAPSHOT.jar
```

## 🧪 Testing

To run the automated test suite, use the following command:

**On Windows:**
```cmd
.\mvnw test
```

**On Linux/macOS:**
```bash
./mvnw test
```

## 🤝 Contributing

(Add your contribution guidelines here)
