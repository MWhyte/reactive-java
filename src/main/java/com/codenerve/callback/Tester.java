package com.codenerve.callback;

import org.apache.log4j.Logger;

public class Tester {

    private final static Logger logger = Logger.getLogger(Tester.class);

    public static void main(String[] args) {

        Button button = new Button();
        ClickHandler clickHandler = new ClickHandler();

        //pass the clickHandler to do the default operation
        button.onClick(clickHandler);

        Button button1 = new Button();

        //pass the interface to implement own operation
        button1.onClick(() -> logger.info("Button Clicked"));
    }
}