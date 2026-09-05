package com.helpdesk.ui;

import com.helpdesk.model.Category;
import com.helpdesk.model.Priority;
import com.helpdesk.model.Student;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.service.TicketService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleMenu {
    private final TicketService ticketService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public void run() {
        boolean running = true;
        printWelcome();
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": createTicket(); break;
                    case "2": listTickets(); break;
                    case "3": viewTicket(); break;
                    case "4": searchTickets(); break;
                    case "5": updateTicket(); break;
                    case "6": changeStatus(); break;
                    case "7": printSummary(); break;
                    case "0": running = false; break;
                    default: System.out.println("Invalid choice. Please pick a valid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private void printWelcome() {
        System.out.println("=== Student Help Desk ===");
    }

    private void printMenu() {
        System.out.println("\n1. Create ticket");
        System.out.println("2. List tickets");
        System.out.println("3. View ticket");
        System.out.println("4. Search tickets");
        System.out.println("5. Update ticket");
        System.out.println("6. Change ticket status");
        System.out.println("7. Summary");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void createTicket() {
        System.out.print("Student name: ");
        String name = scanner.nextLine();
        Student student = ticketService.findOrCreateStudent(name);

        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        Category category = readCategory();
        Priority priority = readPriority();

        Ticket ticket = ticketService.createTicket(student, title, description, category, priority);
        System.out.println("Created: " + ticket);
    }

    private void listTickets() {
        List<Ticket> tickets = ticketService.listAll();
        if (tickets.isEmpty()) {
            System.out.println("No tickets yet.");
            return;
        }
        tickets.forEach(System.out::println);
    }

    private void viewTicket() {
        int id = readInt("Ticket ID: ");
        ticketService.findById(id)
                .ifPresentOrElse(
                        t -> System.out.println(t.toDetailedString()),
                        () -> System.out.println("Ticket not found."));
    }

    private void searchTickets() {
        System.out.print("Keyword: ");
        String keyword = scanner.nextLine();
        List<Ticket> results = ticketService.search(keyword);
        if (results.isEmpty()) {
            System.out.println("No matches found.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private void updateTicket() {
        int id = readInt("Ticket ID to update: ");
        System.out.print("New title (blank to keep current): ");
        String title = scanner.nextLine();
        System.out.print("New description (blank to keep current): ");
        String description = scanner.nextLine();

        System.out.print("Update category? (y/n): ");
        Category category = null;
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            category = readCategory();
        }

        System.out.print("Update priority? (y/n): ");
        Priority priority = null;
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            priority = readPriority();
        }

        ticketService.updateTicket(id, title, description.isEmpty() ? null : description, category, priority);
        System.out.println("Ticket updated.");
    }

    private void changeStatus() {
        int id = readInt("Ticket ID: ");
        System.out.println("New status: 1) OPEN 2) IN_PROGRESS 3) RESOLVED 4) CLOSED");
        String choice = scanner.nextLine().trim();
        TicketStatus status;
        switch (choice) {
            case "1": status = TicketStatus.OPEN; break;
            case "2": status = TicketStatus.IN_PROGRESS; break;
            case "3": status = TicketStatus.RESOLVED; break;
            case "4": status = TicketStatus.CLOSED; break;
            default: System.out.println("Invalid status choice."); return;
        }
        ticketService.changeStatus(id, status);
        System.out.println("Status updated.");
    }

    private void printSummary() {
        Map<TicketStatus, Long> summary = ticketService.statusSummary();
        System.out.println("\n--- Ticket Summary ---");
        for (Map.Entry<TicketStatus, Long> entry : summary.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Total: " + ticketService.listAll().size());
    }

    private Category readCategory() {
        System.out.println("Category: 1) TECHNICAL 2) ACADEMIC 3) ADMINISTRATIVE 4) HOSTEL 5) OTHER");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": return Category.TECHNICAL;
            case "2": return Category.ACADEMIC;
            case "3": return Category.ADMINISTRATIVE;
            case "4": return Category.HOSTEL;
            default: return Category.OTHER;
        }
    }

    private Priority readPriority() {
        System.out.println("Priority: 1) LOW 2) MEDIUM 3) HIGH");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": return Priority.LOW;
            case "3": return Priority.HIGH;
            default: return Priority.MEDIUM;
        }
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }
}
