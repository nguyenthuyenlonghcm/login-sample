import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import listeners.ExtentTestListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ExtentTestListener.class)
public class LoginTest extends BaseTest {
    private final long _timeOut = 10;
    private final long _verifyTimeOut = 3;
    private final String _baseUrl = "https://stag.osyamazakiglobel.club";
    private final String _email = "admin39@gmail.com";
//    private final String _email = "admin38@gmail.com";
//    private final String _email = "admin29@gmail.com";
    private final String _password = "be12345678@Ab";
    private final String _newPassword = "123456789@Ab";
    /*
    Screen: LOGIN
        Step1: Access login screen
        Step2: Input Email
        Step3: Input Password
        Step4: Click LOGIN button
    Is first login?
        TRUE:
            --1. First login--
            Screen: FIRST LOGIN CHANGE PASSWORD
                Step1: Input temporary password
                Step2: Input new password
                Step3: Input confirm password
                Step4: Click CHANGE button
            Screen: FIRST LOGIN AUTHENT
                Step1: Select [Authenticator App] radio button
                Step2: Click SET UP WITH AUTHENTICATION APP button
            Screen: SETUP 2FA - AUTHENTICATOR APP
                Step1: Input authentication code
                Step2: Click VERIFY button
            Screen: RECOVERY CODES
                Step1: Click FINISH SETUP button
            Screen: BACK OFFICE
                VP1: Verify accessing BACK OFFICE screen successfully
        FALSE:
            --2. After first login--
            Screen: LOGIN
                Step1: Access login screen
                Step2: Input Email
                Step3: Input New Password
                Step4: Click LOGIN button
                    Is trusted device?
                        TRUE:
                            Screen: Login with authent app
                                Step1: Input authentication code
                                Step2: Click VERIFY button
                            Screen: BACK OFFICE
                                VP1: Verify accessing BACK OFFICE screen successfully
                        FALSE:
                            Screen: BACK OFFICE
                                VP1: Verify accessing BACK OFFICE screen successfully
     */
    @Test
    public void loginTest() throws Exception {
        WebDriver webDriver = getDriver();
        ExtentTest extentTest = getExtentTest();

//        String secretKey = "TUSYZ6LS5LFT7RC56PQPA5MOKSBRLZGS"; // (admin39)
//        String secretKey = "CJ5P4SB2PFGPX7VUMNLPNMNT74Z66DW3"; // (admin38)
//        String otpCode = getOtpCode(secretKey);
//        getExtentTest().info("OTP Code: " + otpCode);

//        String authLoginInfo = getAuthLoginInfo(this._baseUrl, this._email, this._newPassword);
//        extentTest.info(authLoginInfo);
//        String accessToken = getAccessToken(authLoginInfo);
//        extentTest.info("Access Token: " + accessToken);
//        String meInfo = getMeInfo(this._baseUrl, accessToken);
//        extentTest.info("Me Info: " + meInfo);
//        String secretKey = getTotpSecretKey(this._baseUrl, this._email, this._newPassword);
//        extentTest.info("Secret key: " + secretKey);

        extentTest.info("Email: " + this._email);
        login(this._password);
        By byWrongPassToast = By.xpath("//div[text()='The username or password you entered is incorrect.']");
        // Is first Login?
        if (isFirstLogin(webDriver, byWrongPassToast, this._verifyTimeOut)) { // First login
            extentTest.info("FIRST LOGIN");
            changePassword();
            firstLoginAuthent();
            setupTwoFaAuthentApp();
            recoveryCode();
        } else { // After first login
            extentTest.info("AFTER FIRST LOGIN");
            login(this._newPassword);
            By byLogoutBtn = By.xpath("//button[span[text()='Logout']]");
            if (isTrustedDevice(webDriver, byLogoutBtn, this._verifyTimeOut)) { // Trusted device
                extentTest.info("TRUSTED DEVICE!");
            } else { // Untrusted device
                extentTest.info("UNTRUSTED DEVICE!");
                String secretKey = "TUSYZ6LS5LFT7RC56PQPA5MOKSBRLZGS"; // (admin39)
//                String secretKey = "CJ5P4SB2PFGPX7VUMNLPNMNT74Z66DW3"; // (admin38)
//                String secretKey = getTotpSecretKey(this._baseUrl, this._email, this._newPassword);
                loginTwoFaAuthentApp(secretKey);
            }
        }
        verifyAccessBackOffice();
    }

    private void login(String password) {
        WebDriver webDriver = getDriver();
        ExtentTest extentTest = getExtentTest();

        // Screen: LOGIN
        extentTest.info("Screen: LOGIN");
        // Step1: Access login screen
        extentTest.info("Step1: Access login screen");
        webDriver.get("https://backoffice.osyamazakiglobel.club/login");
        // Step2: Input Email
        extentTest.info("Step2: Input Email");
        By byEmailTxt = By.xpath("//input[@name='usernameOrEmail']");
        sendKeys(webDriver, byEmailTxt, this._timeOut, this._email);
        // Step3: Input Password
        extentTest.info("Step3: Input Password");
        By byPasswordTxt = By.xpath("//input[@name='password']");
        sendKeys(webDriver, byPasswordTxt, this._timeOut, password);
        // Step4: Click LOGIN button
        extentTest.info("Step4: Click LOGIN button");
        By byLoginBtn = By.xpath("//button[span[text()='LOGIN']]");
        click(webDriver, byLoginBtn, this._timeOut);
    }

    private void changePassword() {
        WebDriver webDriver = getDriver();
        ExtentTest extentTest = getExtentTest();

        // Screen: FIRST LOGIN CHANGE PASSWORD
        extentTest.info("Screen: FIRST LOGIN CHANGE PASSWORD");
        // Step1: Input temporary password
        extentTest.info("Step1: Input temporary password");
        By byTemporaryPassword = By.xpath("//input[@placeholder='TEMPORARY PASSWORD']");
        sendKeys(webDriver, byTemporaryPassword, this._timeOut, this._password);
        // Step2: Input new password
        extentTest.info("Step2: Input new password");
        By byNewPassword = By.xpath("//input[@placeholder='NEW PASSWORD']");
        sendKeys(webDriver, byNewPassword, this._timeOut, this._newPassword);
        // Step3: Input confirm password
        extentTest.info("Step3: Input confirm password");
        By byConfirmPassword = By.xpath("//input[@placeholder='CONFIRM PASSWORD']");
        sendKeys(webDriver, byConfirmPassword, this._timeOut, this._newPassword);
        // Step4: Click CHANGE button
        extentTest.info("Step4: Click CHANGE button");
        By byChangeBtn = By.xpath("//button[span[text()='CHANGE']]");
        click(webDriver, byChangeBtn, this._timeOut);
    }

    private void firstLoginAuthent() {
        WebDriver webDriver = getDriver();
        ExtentTest extentTest = getExtentTest();

        // Screen: FIRST LOGIN AUTHENT
        extentTest.info("Screen: FIRST LOGIN AUTHENT");
        // Step1: Select [Authenticator App] radio button
        extentTest.info("Step1: Select [Authenticator App] radio button");
        By byAuthentRadioRad = By.id("TOTP");
        click(webDriver, byAuthentRadioRad, this._timeOut);
        // Step2: Click SET UP WITH AUTHENTICATION APP button
        extentTest.info("Step2: Click SET UP WITH AUTHENTICATION APP button");
        By bySetUpWithAuthentAppBtn = By.xpath("//button[span[text()='SET UP WITH AUTHENTICATOR APP']]");
        click(webDriver, bySetUpWithAuthentAppBtn, this._timeOut);
    }

    private void setupTwoFaAuthentApp() throws CodeGenerationException {
        WebDriver webDriver = getDriver();
        ExtentTest extentTest = getExtentTest();

        // Screen: SETUP 2FA - AUTHENTICATOR APP
        extentTest.info("Screen: SETUP 2FA - AUTHENTICATOR APP");
        // Step1: Input authentication code
        extentTest.info("Step1: Input authentication code");
        By bySecretKeyLbl = By.xpath("(//span[text()='If you cannot scan the QR code, enter the key manually:'])/following-sibling::span");
        By byAuthenticationCodeTxt = By.xpath("//input[@name='code']");
        String secretKey = getText(webDriver, bySecretKeyLbl, this._timeOut);
        String otpCode = getOtpCode(secretKey);
        getExtentTest().info("OTP Code: " + otpCode);
        sendKeys(webDriver, byAuthenticationCodeTxt, this._timeOut, otpCode);
        // Step2: Click VERIFY button
        extentTest.info("Step2: Click VERIFY button");
        By byVerifyBtn = By.xpath("//button[span[text()='VERIFY']]");
        click(webDriver, byVerifyBtn, this._timeOut);
    }

    private void recoveryCode() {
        WebDriver webDriver = getDriver();
        ExtentTest extentTest = getExtentTest();

        // Screen: RECOVERY CODES
        extentTest.info("Screen: RECOVERY CODES");
        // Step1: Click FINISH SETUP button
        extentTest.info("Step1: Click FINISH SETUP button");
        By byFinishSetupBtn = By.xpath("//button[span[text()='FINISH SETUP']]");
        click(webDriver, byFinishSetupBtn, this._timeOut);
    }

    private void verifyAccessBackOffice() {
        WebDriver webDriver = getDriver();
        ExtentTest extentTest = getExtentTest();

        // Screen: BACK OFFICE
        extentTest.info("Screen: BACK OFFICE");
        // VP1: Verify accessing BACK OFFICE screen successfully
        extentTest.info("VP1: Verify accessing BACK OFFICE screen successfully");
        By byLogoutBtn = By.xpath("//button[span[text()='Logout']]");
        if (isElementVisibility(webDriver, byLogoutBtn, this._timeOut)) {
            extentTest.pass("Verify accessing BACK OFFICE screen successfully");
        } else {
            extentTest.fail("Verify accessing BACK OFFICE screen unsuccessfully");
        }
    }

    private void loginTwoFaAuthentApp(String secretKey) throws CodeGenerationException {
        WebDriver webDriver = getDriver();
        ExtentTest extentTest = getExtentTest();

        // Screen: Login with authent app
        extentTest.info("Screen: Login with authent app");
        // Step1: Input authentication code
        extentTest.info("Step1: Input authentication code");
        By byAuthenticationCodeTxt = By.xpath("//input[@name='code']");
        String otpCode = getOtpCode(secretKey);
        getExtentTest().info("OTP Code: " + otpCode);
        sendKeys(webDriver, byAuthenticationCodeTxt, this._timeOut, otpCode);
        // Step2: Click VERIFY button
        extentTest.info("Step2: Click VERIFY button");
        By byVerifyBtn = By.xpath("//button[span[text()='VERIFY']]");
        click(webDriver, byVerifyBtn, this._timeOut);
    }

}
