package com.codenerve.callback;

import org.apache.log4j.Logger;

public class ClickHandler implements ClickEventHandler {

    private final static Logger logger = Logger.getLogger(ClickHandler.class);
    public void handleClick() {
        logger.info("Clicked");
    }
}