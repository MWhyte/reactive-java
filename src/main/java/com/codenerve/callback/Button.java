package com.codenerve.callback;

class Button {
    public void onClick(ClickEventHandler clickHandler) {
        new Thread(clickHandler::handleClick).start();
    }
}