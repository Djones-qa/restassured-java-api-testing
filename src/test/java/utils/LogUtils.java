package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {

    private static final Logger log = LogManager.getLogger(LogUtils.class);

    public static void info(String message) {
        log.info(message);
    }

    public static void debug(String message) {
        log.debug(message);
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void error(String message, Throwable throwable) {
        log.error(message, throwable);
    }

    public static void startTest(String testName) {
        log.info("========== STARTING TEST: " + testName + " ==========");
    }

    public static void endTest(String testName) {
        log.info("========== FINISHED TEST: " + testName + " ==========");
    }

    public static void pass(String testName) {
        log.info("✅ PASSED: " + testName);
    }

    public static void fail(String testName) {
        log.error("❌ FAILED: " + testName);
    }

    public static void request(String method, String endpoint) {
        log.info("REQUEST: " + method + " " + endpoint);
    }

    public static void response(int statusCode, String body) {
        log.info("RESPONSE: " + statusCode + " | " + body.substring(0, Math.min(body.length(), 100)) + "...");
    }
}
