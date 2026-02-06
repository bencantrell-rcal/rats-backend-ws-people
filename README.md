# RATS People App Backend

Backend service that supports the Rcal People App, providing endpoints and data management for roles, teams, skills, mappings, and organizational structures.

## Overview

This application:
- Serves REST endpoints for people, roles, teams, skills, and mapping data.
- Contains logic for managing hierarchical structures and entity mappings.
- Deploys to a GKE Autopilot cluster using Cloud Build and Artifact Registry.
- Connects to a Cloud SQL MySQL instance (with optional local DB setup).

## People Mappings Overview

Entities in the DB are mapped in simple entity:entity pairs. In both the endpoints and the database, mappings are from 
the lower entity to the higher entity, with the hierarchy being:
1. roles
2. people == teams
3. skills

Note that people cannot be mapped to teams and vice versa.
In the adding and removing endpoints, the endpoints can be remembered as "adding an X to a Y". I.e. /skill/{}/teams{} is
"adding a skill to a team".

## Current Known Issues

- SOP skill sync logic needs refinement. Need to parse all files in all subdirectories under 'Manufacturing processes'.
 Title = File name in title case. Description = 'Description' section in SOP (See SOP template).
- Need to remove pagination from people endpoint to allow filtering

## Technology Stack

- Java 21
- Spring Boot
- Maven
- MySQL (Cloud SQL)
- GKE Autopilot (GCP)
- Cloud Build CI/CD
- Artifact Registry
- Swagger/OpenAPI

## Project Structure
- src/main/java/com/rcal
    - ops_scheduler
        - configuration   // _contains configuration for DB Connection, Swagger, etc._
        - controller      // _contains endpoints_
        - entity          // _contains data objects from the DB_
        - model           // _contains DTOs and other custom objects_
        - repository
            - read          // _contains read operation definitions performed on DB_
            - write         // _contains write operation definitions performed on DB_
        - service         // _contains the primary logical operations and bridges the repository and controller_


## API Documentation

Swagger UI:
- Local: http://localhost:8080/swagger-ui/index.html#/
- Test: https://people.test.rcal.internal/api/swagger-ui/index.html#/
- Prod: https://people.rcal.internal/api/swagger-ui/index.html#/

## Local Development

### Prerequisites
- Java 21
- Maven
- Access to DB credentials (local or cloud)

### Running Locally

1. Import the project as a Maven project.
2. Run Maven goals:
    - `clean`
    - `install`
3. Configure the DB connection (local instructions below).
4. Run `PeopleApplication` from your IDE.

### Local Database Setup

Instructions:
https://docs.google.com/document/d/1PSyU2tE2JJdQ6Bu2IdPBkMX7Sx5986E5GFAMCMqs7OM/edit?tab=t.57vty4foi6l7

## Deployment Workflow

1. Pull latest `master`.
2. Create or update your feature branch.
3. Commit and push changes.
4. Cloud Build automatically builds and publishes `latest` image.
5. Trigger deployment to test in Cloud Build → Triggers.
6. Verify functionality in the test environment.
7. Open a PR into `master`.
8. After merge, manually trigger production deployment.
9. Validate release; rollback if needed.

## Environments

Frontend (requires RCAL WiFi or VPN):
- Test: https://people.test.rcal.internal/
- Prod: https://people.rcal.internal/

Backend:
- Same host paths as above, under `/api/`.

## Useful Links

- GitHub Repository:  
  https://github.com/bencantrell-rcal/rats-backend-ws-people

- Cloud Build:  
  https://console.cloud.google.com/cloud-build/dashboard?project=noble-helper-469522-j1

- Artifact Registry:  
  https://console.cloud.google.com/artifacts/docker/noble-helper-469522-j1/us-south1/rats-backend-ws-people

- Kubernetes Engine:  
  https://console.cloud.google.com/kubernetes/list/overview?project=noble-helper-469522-j1

- Rcal Database Overview:  
  https://docs.google.com/document/d/1PSyU2tE2JJdQ6Bu2IdPBkMX7Sx5986E5GFAMCMqs7OM/edit?tab=t.57vty4foi6l7

## License

Internal Rcal proprietary software. Not for public distribution.

