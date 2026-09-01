# Intermediate — Employee Leave Management REST API

A Spring Boot REST API for submitting, reviewing, and tracking employee leave
requests, with an embedded H2 database for zero-setup local evaluation.

## Run it

```bash
cd intermediate-leave-api
mvn spring-boot:run
```

Server starts at `http://localhost:8080`.

H2 console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:leavedb`
- User: `sa`
- Password: *(blank)*

A default admin account (`admin@shadowfox.local`) is seeded on startup.

## Architecture

```text
Controller → Service → Repository → Entity → H2 database
```

- `model/` — `Employee`, `LeaveRequest`, `Role`, `LeaveStatus`
- `repository/` — Spring Data JPA repositories
- `service/` — business rules and validation
- `controller/` — REST endpoints
- `dto/` — request payload validation
- `config/DataInitializer` — seeds the default admin

## Endpoints

### Register an employee
```
POST /api/employees
Content-Type: application/json

{
  "name": "Ganesh Goud",
  "email": "ganesh@example.com"
}
```

### Submit a leave request
```
POST /api/employees/{employeeId}/leave-requests
Content-Type: application/json

{
  "startDate": "2026-09-10",
  "endDate": "2026-09-12",
  "reason": "University examination"
}
```

### View leave history
```
GET /api/employees/{employeeId}/leave-requests
```

### Cancel a pending leave request (employee only)
```
DELETE /api/employees/{employeeId}/leave-requests/{leaveId}
```

### View all leave requests (admin)
```
GET /api/admin/leave-requests
X-Admin-Email: admin@shadowfox.local
```

### Approve or reject a request (admin)
```
PATCH /api/admin/leave-requests/{leaveId}
X-Admin-Email: admin@shadowfox.local
Content-Type: application/json

{
  "decision": "APPROVED",
  "comment": "Approved for testing."
}
```

## Validation rules

- Start/end dates cannot be in the past; end date cannot precede start date
- Leave duration cannot exceed the employee's remaining annual balance (default 20 days)
- Only `PENDING` requests can be reviewed or cancelled
- Employees can only cancel their own requests
- Approving a request deducts the day count from the employee's balance

A ready-to-import Postman collection is at
[`postman/Leave-Management-API.postman_collection.json`](./postman/Leave-Management-API.postman_collection.json).
