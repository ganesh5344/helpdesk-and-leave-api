package com.shadowfox.helpdesk;

import com.shadowfox.helpdesk.service.TicketService;
import com.shadowfox.helpdesk.ui.ConsoleMenu;

public class App {
    public static void main(String[] args) {
        TicketService ticketService = new TicketService();
        ConsoleMenu menu = new ConsoleMenu(ticketService);
        menu.run();
    }
}
