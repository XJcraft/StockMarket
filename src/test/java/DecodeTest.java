import java.io.FileNotFoundException;

public class DecodeTest {
    public static void main(String[] args) throws FileNotFoundException {
        String aaa = "交易货币[GOV]";
        System.out.println(aaa.substring(aaa.indexOf("[") + 1, aaa.indexOf("]")));
        String bbb = "          @list@123";
        String[] split = bbb.trim().split("@");
    }


}
