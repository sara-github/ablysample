package com.example.helloworld;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class HelloWorld {
    private static final Logger logger = LogManager.getLogger(HelloWorld.class);
    public static void main(String[] args) {
        logger.info("Got the log4j2 loggs....");
        print();
    }

    public static String print() {
        return "Hello World";
    }
}
