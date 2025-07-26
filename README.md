# Lumina AI Backend

The **Lumina AI Backend** is a robust Spring Boot-based application designed to serve as the core server-side component of the Lumina AI ecosystem. This repository focuses on managing user authentication, session persistence, and asynchronous communication, leveraging Supabase PostgreSQL for scalable data storage. It provides a secure and efficient foundation for integrating with AI-driven microservices.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Optimizations](#optimizations)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Overview
The Lumina AI Backend is engineered to handle user sessions, authentication, and real-time interactions with a focus on performance and scalability. It stores session data in Supabase PostgreSQL, ensuring reliable persistence and accessibility. Built with asynchronous communication capabilities, it supports seamless integration with external services, making it a critical component for the Lumina AI platform.

## Features
- **User Session Management**: Persists and manages user sessions in Supabase PostgreSQL, enabling secure state tracking.
- **Authentication**: Implements robust authentication workflows with session-based security.
- **Asynchronous Communication**: Utilizes async processing to handle requests efficiently, improving response times.
- **Logout Functionality**: Provides a dedicated endpoint to invalidate sessions and ensure secure user logout.
- **Scalable Architecture**: Designed to scale with growing user bases and transaction volumes.
- **Error Handling**: Includes comprehensive error management for robust operation.

## Tech Stack
- **Framework**: Spring Boot 3.3.4
- **Language**: Java
- **Database**: PostgreSQL (via Supabase)
- **ORM**: Hibernate 6.5.3.Final
- **Connection Pooling**: HikariCP 5.1.0
- **Build Tool**: Maven
- **Security**: Spring Security
- **Asynchronous Support**: Spring WebFlux (optional, configurable)

## Installation

### Prerequisites
- Java 17+
- Maven
- Git
- Docker (for running Redis or containerized app)  

### Steps
1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/lumina-ai-backend.git
   cd lumina-ai-backend
   
2.Set Up Environment Variables
Create a .env file in the root directory or set system variables with the following, replacing placeholders with your Supabase credentials.
Note: Do not embed credentials in SUPABASE_URL; use separate SUPABASE_USERNAME and SUPABASE_PASSWORD for security.
You can also update src/main/resources/application.properties directly with these values, but ensure the file is not committed to version control.

3.Install Dependencies
mvn clean install

4.Run the Application
mvn spring-boot:run

