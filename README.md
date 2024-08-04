# Zadanie Rekruacyjne

This project is a Spring Boot application developed as a recruitment task. It provides an API to list GitHub repositories for a given user, excluding forked repositories, and returns detailed information about each repository's branches and their latest commits. Additionally, it handles cases where the provided GitHub username does not exist.

## Project Structure

The project uses Maven for dependency management and build configuration. The main dependencies are Spring Boot, Lombok, and Project Reactor for reactive programming.

## Dependencies

- **Spring Boot Starter Web**
- **Spring Boot Starter WebFlux**
- **Lombok**
- **Spring Boot Starter Test**
- **Reactor Test**

## Build and Run

### Prerequisites

- **Java 21**
- **Maven**

### Build

To build the project, run the following command:

mvn clean install
mvn spring-boot:run

## API Endpoints

### List GitHub Repositories

**Request:**

- **Method:** GET
- **Endpoint:** `/api/github/{username}/repositories`
- **Headers:** `Accept: application/json`

**Response:**

- **Status 200 OK:**

  ```json
  [
      {
          "repositoryName": "repo-name",
          "ownerLogin": "owner-login",
          "branches": [
              {
                  "name": "branch-name",
                  "lastCommitSha": "commit-sha"
              }
          ]
      }
  ]
