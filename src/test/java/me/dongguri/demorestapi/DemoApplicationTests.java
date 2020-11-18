package me.dongguri.demorestapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

//@WebMvcTest
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test") // profiles test로 해야 test-properties 사용한다
public class DemoApplicationTests {
    @Test
    public void contextLoaders() {

    }
}
