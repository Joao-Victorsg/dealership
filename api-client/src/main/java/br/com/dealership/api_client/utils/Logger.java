package br.com.dealership.api_client.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class Logger {

    public static void info(String logMessage, Object object){
        log.info(logMessage,object);
    }

    public static void info(String logMessage){
        log.info(logMessage);
    }

    public static void error(String logMessage, Throwable throwable){
        log.error(logMessage,throwable);
    }
}