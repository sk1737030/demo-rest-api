package me.dongguri.demorestapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.dongguri.demorestapi.events.EventRepository;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc// mock mvc를 사용할 수 있게된다.
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)// 다른 스프링 Bean설정 파일을 읽어오는 방법
@ActiveProfiles("test") // aplication-test를 사용하여 씀 (applcation + application-test) 를 같이쓴다 override하게 됨
@Ignore // 테스틀 안함을 선언
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 자바Spec에 맞게 등록된 Bean Serialize로  변환해준다.

    @Autowired
    protected ModelMapper modelMapper;
}
