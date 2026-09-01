# Video Demo Script (3–5 minutes)

## 1. Intro (20s)
"Hi, I'm [name]. This is my ShadowFox Java Developer Internship submission,
covering the Beginner Help Desk console app and the Intermediate Leave
Management REST API."

## 2. Beginner — Help Desk (90s)
- Show the running console app.
- Create a ticket (name, title, description, category, priority).
- List all tickets.
- Update the ticket and change its status: `OPEN → IN_PROGRESS → RESOLVED → CLOSED`.
- Briefly show validation: try to close a ticket that isn't resolved yet, and
  show the error message.

## 3. Intermediate — Leave Management API (2 min)
- Start the API with `mvn spring-boot:run`.
- In Postman: register an employee (`POST /api/employees`).
- Submit a leave request for that employee.
- Show the leave history endpoint.
- Switch to the admin view (`GET /api/admin/leave-requests`).
- Approve the request (`PATCH /api/admin/leave-requests/{id}`) and show the
  status change to `APPROVED`.
- (Optional) Open the H2 console to show the underlying data.

## 4. Wrap-up (20s)
- Mention the layered architecture (controller → service → repository →
  entity) and validation rules.
- Point to the GitHub repository link for full code and README instructions.
