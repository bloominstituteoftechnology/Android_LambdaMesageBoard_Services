package com.example.lambda_messages;

import java.util.ArrayList;

public class MessageBoard{

String title, identifier;

    public MessageBoard(String title, String identifier) {
        this.title = title;
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public String getIdentifier() {
        return identifier;
    }
}
