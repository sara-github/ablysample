import com.example.helloworld.HelloWorld;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MyTest {

    @Test
    public void name() throws  Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String s = HelloWorld.print();
        Assert.assertEquals( "Hello World", s);
    }
}
