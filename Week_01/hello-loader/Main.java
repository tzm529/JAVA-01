import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        try {
            Class o = new HelloClassLoader().findClass("Hello");
            Method method = o.getMethod("hello");
            method.invoke(o.newInstance());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
