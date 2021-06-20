package com.midcielab;

import com.midcielab.handler.FeedsHandler;

public class FeedsClient {

    public static void main(String[] args) {
        FeedsHandler.getInstance().process();
    }
}
