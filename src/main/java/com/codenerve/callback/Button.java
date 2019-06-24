package com.codenerve.callback;

class Button {
    public void onClick(ClickEventHandler clickHandler) {
        clickHandler.handleClick();
    }
}