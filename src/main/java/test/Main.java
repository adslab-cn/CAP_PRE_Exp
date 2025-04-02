package test;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        testForL tl = new testForL();
        testForN tn = new testForN();
        try {
            tl.testAll();
            tn.testAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
