# ShadowFox Java Developer Internship — Intermediate Submission

This repository contains the completed **Beginner** and **Intermediate** level
tasks for the ShadowFox Java Developer Internship.

## Projects

| Level | Project | Tech Stack | Folder |
|---|---|---|---|
| Beginner | Student Help Desk / Support Ticket System | Java 17, console app | [`beginner-helpdesk/`](./beginner-helpdesk) |
| Intermediate | Employee Leave Management REST API | Java 17, Spring Boot, JPA, H2 | [`intermediate-leave-api/`](./intermediate-leave-api) |

## Quick start

### Beginner — Help Desk

```bash
cd beginner-helpdesk
javac -d out src/main/java/com/shadowfox/helpdesk/App.java src/main/java/com/shadowfox/helpdesk/model/*.java src/main/java/com/shadowfox/helpdesk/service/*.java src/main/java/com/shadowfox/helpdesk/ui/*.java
java -cp out com.shadowfox.helpdesk.App
```

### Intermediate — Leave Management API

```bash
cd intermediate-leave-api
mvn spring-boot:run
```

Runs at `http://localhost:8080`. Full endpoint documentation and sample requests
are in [`intermediate-leave-api/README.md`](./intermediate-leave-api/README.md).

## Repository structure

```text
shadow-fox/
├── beginner-helpdesk/       # Java console app (Beginner task)
├── intermediate-leave-api/  # Spring Boot REST API (Intermediate task)
├── docs/
│   └── demo-script.md       # 3-5 minute video demo outline
└── README.md
```

## Submission scope

This repository covers the Beginner and Intermediate levels only, as required
for the "Intermediate" option on the ShadowFox submission form. No live
deployment, PPT, internship report, or cybersecurity report is included, as
those are optional per the form.
