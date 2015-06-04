package com.truszkowski.rafal.contacts.app;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class responsible for holding the list fo Contact objects
 */
public class ContactList {

    /**
     * An array of Contact objects.
     */
    public static List<Contact> LIST = new ArrayList<Contact>();

    /**
     * A map of Contact objects, by contact name.
     */
    public static Map<String, Contact> MAP = new HashMap<String, Contact>();

    /**
     * An item representing a contact.
     */
    public static class Contact {
        String name;
        String employeeId;
        String company;
        String detailsURL;
        String smallImageURL;
        String birthDate;
        JSONObject phone;

        public Contact(String name, String employeeId, String company, String detailsURL, String smallImageURL, String birthDate, JSONObject phone) {
            this.name = name;
            this.employeeId = employeeId;
            this.company = company;
            this.detailsURL = detailsURL;
            this.smallImageURL = smallImageURL;
            this.birthDate = birthDate;
            this.phone = phone;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
