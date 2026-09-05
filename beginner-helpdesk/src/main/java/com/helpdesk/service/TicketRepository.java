package com.helpdesk.service;

import com.helpdesk.model.Student;
import com.helpdesk.model.Ticket;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository {
    private static final String DATA_FILE = "helpdesk-data.ser";

    @SuppressWarnings("unchecked")
    public StoredData load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new StoredData(new ArrayList<>(), new ArrayList<>(), 1, 1);
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            List<Ticket> tickets = (List<Ticket>) in.readObject();
            List<Student> students = (List<Student>) in.readObject();
            int nextTicketId = in.readInt();
            int nextStudentId = in.readInt();
            return new StoredData(tickets, students, nextTicketId, nextStudentId);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load saved tickets, starting fresh (" + e.getMessage() + ").");
            return new StoredData(new ArrayList<>(), new ArrayList<>(), 1, 1);
        }
    }

    public void save(List<Ticket> tickets, List<Student> students, int nextTicketId, int nextStudentId) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(tickets);
            out.writeObject(students);
            out.writeInt(nextTicketId);
            out.writeInt(nextStudentId);
        } catch (IOException e) {
            System.out.println("Warning: could not save tickets to disk (" + e.getMessage() + ").");
        }
    }

    public static class StoredData {
        public final List<Ticket> tickets;
        public final List<Student> students;
        public final int nextTicketId;
        public final int nextStudentId;

        public StoredData(List<Ticket> tickets, List<Student> students, int nextTicketId, int nextStudentId) {
            this.tickets = tickets;
            this.students = students;
            this.nextTicketId = nextTicketId;
            this.nextStudentId = nextStudentId;
        }
    }
}
