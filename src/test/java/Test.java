import com.aix.JsonPlus;

public class Test {

    public static void main(String[] args) {
        Object value = JsonPlus.parse("""
        [1,2.57,3,"wwqq1321","大概的",true,false,null]
        """, null);
        System.out.println(value);
    }

}
