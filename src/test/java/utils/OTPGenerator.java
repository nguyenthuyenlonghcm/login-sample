package utils;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.CodeGenerationException;

public class OTPGenerator {
    private final CodeGenerator codeGenerator;

    public OTPGenerator() {
        this.codeGenerator = new DefaultCodeGenerator();
    }

    public String generateOTP(String secretKey) throws CodeGenerationException {
        long currentTime = System.currentTimeMillis() / 1000 / 30;
        return codeGenerator.generate(secretKey, currentTime);
    }
}
