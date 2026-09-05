package com.helpdesk;

import com.helpdesk.service.TicketService;
import com.helpdesk.ui.ConsoleMenu;

public class App {
    public static void main(String[] args) {
        TicketService ticketService = new TicketService();
        ConsoleMenu menu = new ConsoleMenu(ticketService);
        menu.run();
    }
}
