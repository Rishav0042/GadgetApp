package com.mobileshop.service;

import com.mobileshop.dao.ContactDAO;
import com.mobileshop.model.Contact;
import com.mobileshop.util.ValidationUtil;

import java.util.List;

/**
 * Service layer for Contact operations.
 */
public class ContactService {

    private final ContactDAO contactDAO;

    public ContactService() {
        this.contactDAO = new ContactDAO();
    }

    public Contact submitContact(String name, String email, String subject, String message, StringBuilder errorMsg) {
        if (ValidationUtil.isNullOrEmpty(name)) {
            errorMsg.append("Name is required.");
            return null;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            errorMsg.append("Please enter a valid email address.");
            return null;
        }
        if (ValidationUtil.isNullOrEmpty(subject)) {
            errorMsg.append("Subject is required.");
            return null;
        }
        if (ValidationUtil.isNullOrEmpty(message)) {
            errorMsg.append("Message is required.");
            return null;
        }
        Contact contact = new Contact(name, email, subject, message);
        int id = contactDAO.createContact(contact);
        if (id > 0) {
            contact.setId(id);
            return contact;
        }
        errorMsg.append("Failed to submit contact form. Please try again.");
        return null;
    }

    public List<Contact> getAllContacts() {
        return contactDAO.getAllContacts();
    }

    public boolean markAsRead(int contactId) {
        return contactDAO.updateContactStatus(contactId, "read");
    }

    public boolean markAsReplied(int contactId) {
        return contactDAO.updateContactStatus(contactId, "replied");
    }
}
