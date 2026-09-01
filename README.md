# Shadow Fox

Two small Java backend projects: a console-based support ticket system and a
REST API for employee leave management.

## Projects

| Project | Tech Stack | Folder |
|---|---|---|
| Help Desk | Java 17, console app | [`beginner-helpdesk/`](./beginner-helpdesk) |
| Leave Management API | Java 17, Spring Boot, JPA, H2 | [`intermediate-leave-api/`](./intermediate-leave-api) |

## Quick start

### Help Desk

```bash
cd beginner-helpdesk
javac -d out src/main/java/com/shadowfox/helpdesk/App.java src/main/java/com/shadowfox/helpdesk/model/*.java src/main/java/com/shadowfox/helpdesk/service/*.java src/main/java/com/shadowfox/helpdesk/ui/*.java
java -cp out com.shadowfox.helpdesk.App
```

### Leave Management API

```bash
cd intermediate-leave-api
mvn spring-boot:run
```

Runs at `http://localhost:8080`. Full endpoint documentation and sample
requests are in [`intermediate-leave-api/README.md`](./intermediate-leave-api/README.md).

## Repository structure

```text
shadow-fox/
├── beginner-helpdesk/       # Java console app
├── intermediate-leave-api/  # Spring Boot REST API
└── README.md
```
