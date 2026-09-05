# Student Help Desk

A Java console application for logging and tracking support tickets.

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
javac -d out src/main/java/com/helpdesk/App.java src/main/java/com/helpdesk/model/*.java src/main/java/com/helpdesk/service/*.java src/main/java/com/helpdesk/ui/*.java
java -cp out com.helpdesk.App
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

- `model/` — `Ticket`, `Student`, `Category`, `TicketStatus`, `Priority`
- `service/TicketService` — business logic, student lookups, and validation
- `ui/ConsoleMenu` — user interaction loop

Student names are deduplicated: entering the same name twice reuses the same
`Student` record instead of creating a duplicate. Ticket IDs are assigned from
a single incrementing counter, so duplicate ticket IDs are impossible by
construction.
