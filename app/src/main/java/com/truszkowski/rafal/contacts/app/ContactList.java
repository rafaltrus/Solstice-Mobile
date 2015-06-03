package com.truszkowski.rafal.contacts.app;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ContactList {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Contact> LIST = new ArrayList<Contact>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Contact> MAP = new HashMap<String, Contact>();

    /**
     * A dummy item representing a piece of content.
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
