package base;

import com.aventstack.extentreports.ExtentTest;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ApiClient;
import utils.ExtentTestManager;
import utils.JsonParser;
import utils.OTPGenerator;

public class BaseTest extends BasePage {

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeMethod
    public void setUp(ITestResult result) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Chrome bắt buộc phải chạy headless cho CI/CD vì tự run trên github không có UI
//        options.addArguments("--headless=new");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-dev-shm-usage");

        driver.set(new ChromeDriver(options));
        getDriver().manage().window().maximize();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            // Sleep 3s
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getDriver().quit();
            driver.remove();
        }
    }

    protected ExtentTest getExtentTest() {
        return ExtentTestManager.getTest();
    }

    public String getOtpCode(String secretKey) throws CodeGenerationException {
        OTPGenerator otpGenerator = new OTPGenerator();
        return otpGenerator.generateOTP(secretKey);
    }

    public String getAuthLoginInfo(String baseUrl, String email, String password) throws Exception {
        ApiClient apiClient = new ApiClient();
        return apiClient.login(baseUrl, email, password);
    }

    public String getAccessToken(String jsonResponse) {
        return JsonParser.getNestedValue(jsonResponse, "data", "accessToken");
    }

    public String getMeInfo(String baseUrl, String accessToken) throws Exception {
        ApiClient apiClient = new ApiClient();
        return apiClient.getMe(baseUrl, accessToken);
    }

    public String getTotpSecretKey(String baseUrl, String email, String password) throws Exception {
        String authLoginInfo = getAuthLoginInfo(baseUrl, email, password);
        String accessToken = getAccessToken(authLoginInfo);
        String meInfo = getMeInfo(baseUrl, accessToken);
        return JsonParser.getTotpSecret(meInfo);
    }

}
