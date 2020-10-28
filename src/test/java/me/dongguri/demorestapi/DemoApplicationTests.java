package me.dongguri.demorestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

//@WebMvcTest
public class DemoApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    //@TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent()  throws Exception{
      /*  Event event = Event.builder()
                .name("Spring")
                .description("Rest API Develo")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,12,12,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,14,12,10))
                .beginEventDateTime(LocalDateTime.of(2018,11,15,12,10))
                .endEventDateTime(LocalDateTime.of(2018,11,16,12,10))
                .basePrice(10)
                .maxPrice(20)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());*/
    }

}
