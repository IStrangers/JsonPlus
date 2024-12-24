import com.aix.JsonPlus;

public class Test {

    public static void main(String[] args) {
        Object value = JsonPlus.parse("""
        {
            "key1": 123,
            "key2": "接待加我的",
            "key3": true,
            "key4": false,
            "key5": null,
        }
        """, null);
        System.out.println(value);
    }

}
