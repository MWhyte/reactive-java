package com.codenerve.callback;

class Button {
    public void onClick(ClickEventHandler clickEventHandler) {

        someLongOperation();

        new Thread(clickEventHandler::handleClick).start();
    }

    private void someLongOperation() {
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}