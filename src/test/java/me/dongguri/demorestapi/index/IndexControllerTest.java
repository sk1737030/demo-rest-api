package me.dongguri.demorestapi.index;

import me.dongguri.demorestapi.common.BaseControllerTest;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class IndexControllerTest  extends BaseControllerTest {

    @Test
    public void index() throws  Exception{
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists())
        ;
    }


}
