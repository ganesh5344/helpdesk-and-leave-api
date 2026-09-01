# Beginner — Student Help Desk / Support Ticket System

A Java console application for logging and tracking student support tickets.

## Features

- Create tickets with student name, title, description, category, and priority
- List, view, and search tickets by keyword
- Update ticket details
- Track status: `OPEN → IN_PROGRESS → RESOLVED → CLOSED`
- Validation: no empty titles/names, no editing closed tickets, must be
  `RESOLVED` before `CLOSED`

## Run it

```bash
cd beginner-helpdesk
javac -d out src/main/java/com/shadowfox/helpdesk/App.java src/main/java/com/shadowfox/helpdesk/model/*.java src/main/java/com/shadowfox/helpdesk/service/*.java src/main/java/com/shadowfox/helpdesk/ui/*.java
java -cp out com.shadowfox.helpdesk.App
```

## Menu

```text
1. Create ticket
2. List tickets
3. View ticket
4. Search tickets
5. Update ticket
6. Change ticket status
0. Exit
```

## Design

- `model/` — `Ticket`, `TicketStatus`, `Priority`
- `service/TicketService` — business logic and validation
- `ui/ConsoleMenu` — user interaction loop
