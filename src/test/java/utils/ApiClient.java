package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Cách sử dụng
 *
 * ApiClient apiClient = new ApiClient();
 * String response = apiClient.login("http://your-api-url.com", "admin@gmail.com", "be12345678@Ab");
 */
public class ApiClient {
    public String login(String baseUrl, String usernameOrEmail, String password) throws Exception {
        String[] command = {
            "curl", "-X", "POST",
            baseUrl + "/api/auth/login",
            "-H", "Content-Type: application/json",
            "-d", String.format("{\"usernameOrEmail\":\"%s\",\"password\":\"%s\"}", usernameOrEmail, password)
        };

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        process.waitFor();
        return response.toString();
    }

    /**
     * Cách sử dụng
     *
     * ApiClient apiClient = new ApiClient();
     * String response = apiClient.getMe("http://your-api-url.com", "your-token-here");
     */
    public String getMe(String baseUrl, String accessToken) throws Exception {
        String[] command = {
            "curl", "-X", "GET",
            baseUrl + "/api/auth/me",
            "-H", "Authorization: Bearer " + accessToken
        };

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        process.waitFor();
        return response.toString();
    }
}
