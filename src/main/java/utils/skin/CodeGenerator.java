package utils.skin;

import java.util.Random;

/**
 * This class is used for generating codes.
 * @author Josefinators
 */
public class CodeGenerator {
    /**
     * Generates a 5 number code.
     * @return the generated code
     */
    public static String generate5DigitCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("%05d", number);
    }
}
