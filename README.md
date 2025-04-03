# Workout Tracker API

## Overview

The Workout Tracker API is a backend application designed to help users manage their fitness routines. It allows users to create and manage workout plans, schedule workouts, track completed exercises, and generate reports on past workouts. The API is built with Spring Boot, uses JWT-based authentication for security, and provides a fully documented OpenAPI (Swagger) interface for easy interaction.
This project was developed as part of the Fitness Workout Tracker challenge on [roadmap.sh](https://roadmap.sh/projects/fitness-workout-tracker).

## Features

- **User Authentication:** Sign up and log in with JWT-based authentication.

- **Workout Plans:** Create, update, and manage workout plans (accessible only by the creator or admins).

- **Workout Schedules:** Schedule workouts and mark them as completed.

- **Exercises:** Manage a list of exercises to include in workout plans.

- **Reports:** Generate reports on past workouts, with optional date range filtering.

- **Role-Based Access:** Admins can manage all users; regular users can only manage their own data.

- **API Documentation:** Fully documented with Swagger UI (accessible at `/swagger-ui/index.html`).

## Tech Stack

- **Java:** 21 (or compatible version)

- **Spring Boot:** 3.2.x

- **Spring Security:** For authentication and authorization

- **JWT:** For secure token-based authentication

- **Spring Data JPA:** For database interactions

- **H2 Database:** In-memory database for development and testing

- **Maven:** Dependency management and build tool

- **Swagger (Springdoc):** API documentation

- **JUnit 5 & Mockito:** For unit and integration tests

## Prerequisites

Before running the project, ensure you have the following installed:

- Java 21 (or a compatible version)

- Maven (for dependency management and building the project)

- A code editor or IDE (e.g., IntelliJ IDEA, Eclipse, VS Code)

## Getting Started

1. Clone the Repository

   ```bash
   git clone https://github.com/Egnoel/workout_tracker_API
   ```

   ```bash
   cd workout-tracker
   ```

2. Build the Project
   Use Maven to build the project and download dependencies:

   ```bash
      mvn clean install
   ```

3. Run the Application
   Run the Spring Boot application:

   ```bash
   mvn spring-boot:run
   ```

The application will start on http://localhost:8080.

4. Access the API Documentation
   Open your browser and navigate to:
   `http://localhost:8080/swagger-ui/index.html`

The Swagger UI provides a complete interface to test all API endpoints.

## API Endpoints Overview

Here’s a quick overview of the main API endpoints:

**Authentication:**

- `POST /api/auth/signup`: Register a new user.

- `POST /api/auth/login`: Log in and receive a JWT token.

**Users (Admin only):**

- `GET /api/users`: List all users.

- `GET /api/users/{id}`: Get a user by ID.

**Exercises:**

- `GET /api/exercises`: List all exercises.

- `POST /api/exercises`: Create a new exercise (authenticated users only).

**Workout Plans:**

- `GET /api/workout-plans`: List the authenticated user’s workout plans.

- `POST /api/workout-plans`: Create a new workout plan (authenticated users only).

**Workout Schedules:**

- `GET /api/workout-schedules`: List the authenticated user’s schedules.

- `POST /api/workout-schedules`: Schedule a workout (authenticated users only).

**Reports:**

- `GET /api/reports/past-workouts`: Generate a report of past workouts (authenticated users only).

For detailed endpoint information, refer to the Swagger UI.

**Authentication**

Most endpoints require authentication. To authenticate:
Sign up or log in using `/api/auth/signup` or `/api/auth/login`.

Copy the JWT token from the response.

In Swagger UI, click the "Authorize" button and enter the token in the format: Bearer <your-token>.

**Running Tests**

The project includes unit and integration tests for controllers, services, and repositories.

1. Configure the Test Environment
   Ensure the byte-buddy-agent is configured to avoid Mockito warnings (see Mockito Agent Configuration (#mockito-agent-configuration) below).
2. Run Tests
   Execute all tests using Maven:

   ```bash
   mvn test
   ```

## Future Improvements

- **Add pagination to endpoints** like /api/workout-plans and /api/reports/past-workouts.

- **Implement a frontend** (e.g., using React or Angular) to consume the API.

- **Deploy the API** to a cloud provider (e.g., Heroku, AWS).

- **Enhance reports** with additional metrics (e.g., total weight lifted, workout duration).

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.

2. Create a new branch (git checkout -b feature/your-feature).

3. Commit your changes (git commit -m "Add your feature").

4. Push to the branch (git push origin feature/your-feature).

5. Open a Pull Request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
Acknowledgments
This project was inspired by the Fitness Workout Tracker challenge on roadmap.sh.

Thanks to the Spring Boot and Swagger communities for their excellent tools and documentation.

## Contact

For questions or feedback, feel free to reach out:

Email: egnoel@hotmail.com

GitHub: Egnoel
