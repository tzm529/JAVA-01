import java.io.*;
import java.net.URL;

public class HelloClassLoader extends ClassLoader{

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        URL url = this.getClass().getResource("/");
        String path = url.getPath();
        File file = new File(path + name + ".xlass");
        Class<?> c = null;
        try{
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] bytes = bufferedInputStream.readAllBytes();
            for (int i = 0; i < bytes.length; i++){
                bytes[i] = (byte) (255 - (int) bytes[i]);
            }
            c = defineClass(name, bytes, 0, bytes.length);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            return c;
        }
    }
}
