package com.example.nirvansharma.ttsecure;

/**
 * Created by Nirvan Sharma on 10/22/2017.
 */

public class EmailRecipient {
    String emailAddress;

    public EmailRecipient(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public EmailRecipient() {
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
