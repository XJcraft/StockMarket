import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class DecodeTest {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("D:\\OneDrive\\ProjectsPrivate\\StockMarket\\src\\main\\resources\\zh_cn.json");
        Gson gson = new Gson();
        Map map = gson.fromJson(new FileReader(file), Map.class);
        System.out.println(map.size());
    }
}
