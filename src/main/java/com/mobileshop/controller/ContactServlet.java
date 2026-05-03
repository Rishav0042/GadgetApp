package com.mobileshop.controller;

import com.mobileshop.model.Contact;
import com.mobileshop.service.ContactService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Contact controller - handles contact form submissions.
 */
public class ContactServlet extends HttpServlet {

    private final ContactService contactService = new ContactService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "form";

        switch (action) {
            case "form":
                request.getRequestDispatcher("/contact.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("/contact.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        StringBuilder errorMsg = new StringBuilder();
        Contact contact = contactService.submitContact(name, email, subject, message, errorMsg);

        if (contact != null) {
            request.setAttribute("success", "Thank you for contacting us! We will get back to you soon.");
        } else {
            request.setAttribute("error", errorMsg.toString());
            request.setAttribute("contactName", name);
            request.setAttribute("contactEmail", email);
            request.setAttribute("contactSubject", subject);
            request.setAttribute("contactMessage", message);
        }
        request.getRequestDispatcher("/contact.jsp").forward(request, response);
    }
}
