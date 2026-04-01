package utils;

import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    static {
        try {
            properties = new Properties();
            properties.load(ConfigReader.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getContentType() {
        return properties.getProperty("content.type", "application/json");
    }

    public static int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("connection.timeout", "5000"));
    }

    public static long getResponseTimeout() {
        return Long.parseLong(properties.getProperty("response.timeout", "2000"));
    }

    public static int getTestUserId() {
        return Integer.parseInt(properties.getProperty("test.user.id", "1"));
    }

    public static int getTestPostId() {
        return Integer.parseInt(properties.getProperty("test.post.id", "1"));
    }

    public static int getTotalUsers() {
        return Integer.parseInt(properties.getProperty("total.users", "10"));
    }

    public static int getTotalPosts() {
        return Integer.parseInt(properties.getProperty("total.posts", "100"));
    }

    public static String getReportPath() {
        return properties.getProperty("report.path", "reports/APITestReport.html");
    }

    public static String getReportTitle() {
        return properties.getProperty("report.title", "API Test Report");
    }

    public static String getReportName() {
        return properties.getProperty("report.name", "API Test Results");
    }

    public static String getTesterName() {
        return properties.getProperty("tester.name", "Darrius Jones");
    }
}
