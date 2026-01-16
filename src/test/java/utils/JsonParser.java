package utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Cách sử dụng
 *
 * // Lấy giá trị đơn giản
 * String token = JsonParser.getValue(response, "token");
 *
 * // Lấy giá trị lồng nhau, ví dụ: {"data": {"user": {"name": "admin"}}}
 * String name = JsonParser.getNestedValue(response, "data", "user", "name");
 *
 * // Lấy TOTP secret
 * String secret = JsonParser.getTotpSecret(response);
 */
public class JsonParser {
    public static String getValue(String jsonResponse, String key) {
        JSONObject json = new JSONObject(jsonResponse);
        return json.optString(key, null);
    }

    public static String getNestedValue(String jsonResponse, String... keys) {
        JSONObject json = new JSONObject(jsonResponse);
        for (int i = 0; i < keys.length - 1; i++) {
            json = json.optJSONObject(keys[i]);
            if (json == null) return null;
        }
        return json.optString(keys[keys.length - 1], null);
    }

    public static String getTotpSecret(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONArray configs = json.getJSONObject("data").getJSONArray("twoFactorConfigs");
        for (int i = 0; i < configs.length(); i++) {
            JSONObject config = configs.getJSONObject(i);
            if ("TOTP".equals(config.getString("type"))) {
                return config.optString("secret", null);
            }
        }
        return null;
    }
}
