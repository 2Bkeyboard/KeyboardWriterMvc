import java.lang.reflect.Field;

/**
 * @Author 2B键盘
 * @Date 2018/12/12 2:56
 * @Description 这只是个测试类
 */
public class Main {

    private String s;

    private Integer i;

    public static void main(String[] args) {
        Class<?> c = Main.class;
        Field[] fields = c.getDeclaredFields();
        for(Field field : fields){
            System.out.println(field.getType());
        }
    }

}
