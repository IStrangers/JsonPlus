import com.aix.JsonPlus;

public class Test {

    public static void main(String[] args) {
        Object value = JsonPlus.parse("[1.546,2,3]", null);
        System.out.println(value);
    }

}
