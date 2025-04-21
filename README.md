# AI-Assisted Project Management Platform

A comprehensive full-stack application for project and task management with AI-powered effort estimation, built with Spring Boot and React Native.

## Features

- User Management (PMO, Project Manager, Developer roles)
- Project Management
- Task Management
- AI-powered effort estimation
- Jira integration (2-way sync)
- Event-driven architecture with Kafka

## Technology Stack

### Backend

- Java Spring Boot
- PostgreSQL
- Spring Security with Keycloak
- Spring Data JPA
- Apache Kafka
- RESTful APIs
- Flyway for database migrations

### Frontend

- React Native
- React Navigation
- React Native Paper (UI components)
- Axios (API client)
- TypeScript

## Project Structure

```
project-management-application/
├── backend/                     # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── projectmanagement/
│   │   │   │           ├── common/        # Common utilities, exceptions, base classes
│   │   │   │           ├── config/        # Configuration classes
│   │   │   │           ├── user/          # User module
│   │   │   │           ├── project/       # Project module
│   │   │   │           └── task/          # Task module
│   │   │   └── resources/
│   │   │       ├── application.yml        # Application configuration
│   │   │       └── db/migration/          # Database migration scripts
│   │   └── test/                          # Unit and integration tests
│   └── pom.xml                            # Maven dependencies
│
└── frontend/                    # React Native frontend
    ├── src/
    │   ├── components/          # Reusable UI components
    │   ├── screens/             # Application screens
    │   ├── navigation/          # Navigation setup
    │   ├── services/            # API services and utilities
    │   └── App.tsx              # Application entry point
    ├── package.json             # NPM dependencies
    └── tsconfig.json            # TypeScript configuration
```

## Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- PostgreSQL 13 or higher
- Apache Kafka
- Android Studio (for Android development)
- Xcode (for iOS development)

## Setup and Installation

### Backend

1. Configure PostgreSQL:
   ```
   CREATE DATABASE project_management;
   CREATE USER postgres WITH PASSWORD 'postgres';
   GRANT ALL PRIVILEGES ON DATABASE project_management TO postgres;
   ```

2. Start Kafka:
   ```
   # Start Zookeeper
   bin/zookeeper-server-start.sh config/zookeeper.properties
   
   # Start Kafka broker
   bin/kafka-server-start.sh config/server.properties
   ```

3. Configure Keycloak:
   - Setup a new realm: `project-management`
   - Create a client: `project-management-client`
   - Configure roles: `PMO`, `PROJECT_MANAGER`, `DEVELOPER`

4. Run the Spring Boot application:
   ```
   cd backend
   ./mvnw spring-boot:run
   ```

### Frontend

1. Install dependencies:
   ```
   cd frontend
   npm install
   ```

2. Start the Metro bundler:
   ```
   npm start
   ```

3. Run on Android:
   ```
   npm run android
   ```

4. Run on iOS:
   ```
   npm run ios
   ```

## API Documentation

The backend provides RESTful APIs with the following endpoints:

- User Management: `/api/users`
- Project Management: `/api/projects`
- Task Management: `/api/tasks`

Full API documentation can be accessed via Swagger UI at: `http://localhost:8080/api/swagger-ui.html`

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 