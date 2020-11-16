package me.dongguri.demorestapi.events;

import me.dongguri.demorestapi.accounts.Account;
import me.dongguri.demorestapi.accounts.AccountRepository;
import me.dongguri.demorestapi.accounts.AccountRole;
import me.dongguri.demorestapi.accounts.AccountService;
import me.dongguri.demorestapi.common.AppProperties;
import me.dongguri.demorestapi.common.BaseControllerTest;
import me.dongguri.demorestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTests extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @Before
    public void setUp() {
        this.eventRepository.deleteAll();
        this.accountRepository.deleteAll();
    }

    @Test
    @TestDescription("정상적으로 이벤트가생성 됨")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API dev")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 15, 12))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 15, 11))
                .endEventDateTime(LocalDateTime.of(2019, 11, 1, 10, 12))
                .basePrice(1)
                .maxPrice(10)
                .limitOfEnrollment(100)
                .location("강념역 D2")
                .build();

        mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearereToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(event))) // json으로
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query"),
                                linkWithRel("update-event").description("link to update and existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content Type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("description of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end  of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
                        ),
                        relaxedResponseFields(
                                //relaxedResponseFields( // 문서를 강제로 안 할경우
                                fieldWithPath("id").description("identifier of new Event"),
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("description of new Event"),

                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end  of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("it tells if this event is free event or not"),
                                fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event to Status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("event to Status"),
                                fieldWithPath("_links.update-event.href").description("event to Status"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
        ;

    }

    private String getBearereToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    private String getAccessToken() throws Exception {
        // Given
        Account dongguri = Account.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(dongguri);

        // When & Then
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", appProperties.getUserUsername())
                .param("password", appProperties.getUserPassword())
                .param("grant_type", "password"));

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parse = new Jackson2JsonParser();
        return parse.parseMap(responseBody).get("access_token").toString();

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createBadEvent() throws Exception {
        EventDto event = EventDto.builder()

                .name("Spring")
                .description("REST API dev")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 15, 12))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 12, 15, 11))
                .endEventDateTime(LocalDateTime.of(2019, 11, 1, 15, 12))
                .basePrice(100)
                .maxPrice(50)
                .limitOfEnrollment(100)
                .location("강념역 D2")
                .build();

        mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearereToken())
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(event))) // json으로
                .andDo(print())
                .andExpect(status().isBadRequest())
//                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
        /*.andExpect(jsonPath("id").exists())*/
        //          .andExpect(jsonPath("free").value(false))
        //         .andExpect(jsonPath("offline").value(true))
        //        .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }


    @Test
    @TestDescription("입력 값이 비어있는 경우에 발생하는 에러 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearereToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // 다시 확인해보기
    @Test
    @TestDescription("입력 값이 잘못된 경우에 발생하는 에러 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API dev")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 15, 12))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 12, 15, 11))
                .endEventDateTime(LocalDateTime.of(2018, 11, 1, 15, 12))
                .basePrice(10000)
                .maxPrice(10)
                .limitOfEnrollment(100)
                .location("강념역 D2")
                .build();

        mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearereToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))) // json으로
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회화기")
    public void queryEvents() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // When
        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))

        ;

    }

    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception {
        // Given
        Event event = this.generateEvent(100);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(print())
                .andDo(document("get-and-event"))
        ;
    }

    @Test
    @TestDescription("없는 이벤트는 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception {
        // When & Then
        this.mockMvc.perform(get("/api/events/2222"))
                .andExpect(status().isNotFound())
        ;

    }

    @Test
    @TestDescription("수정하려는  이벤트가 없는 경우 404 응답받기")
    public void updateEvent_get404() throws Exception {
        // Given
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API dev")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 15, 12))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 12, 15, 11))
                .endEventDateTime(LocalDateTime.of(2018, 11, 1, 15, 12))
                .basePrice(10000)
                .maxPrice(10)
                .limitOfEnrollment(100)
                .location("강념역 D2")
                .build();

        // When
        ResultActions perform =
                this.mockMvc.perform(put("/api/events/200")
                        .header(HttpHeaders.AUTHORIZATION, getBearereToken())
                        .contentType(MediaTypes.HAL_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(eventDto)));

        // Then
        perform.andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @TestDescription("입력 데이터가 이상한 경우 400 NOT_FOUND")
    public void updateEvent_wrongInput_get400() throws Exception {
        // Given
        Event generatedEvent = generateEvent(1);

        EventDto eventDto = modelMapper.map(generatedEvent, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        // when
        ResultActions perform = this.mockMvc.perform(put("/api/events/{id}", generatedEvent.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearereToken())
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());


        // then
        perform.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("도메인 로직으로 데이터 검증 실패한 경우 400 NOT_FOUND")
    public void updateEvent_wrongDomain_get400() throws Exception {
        // Given
        Event generateEvent = generateEvent(200);

        EventDto event = EventDto.builder()
//                .name("Spring")
                .description("REST API dev")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 15, 12))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 12, 15, 11))
                .endEventDateTime(LocalDateTime.of(2018, 11, 1, 15, 12))
                .basePrice(10000)
                .maxPrice(10)
                .limitOfEnrollment(100)
                .location("강념역 D2")
                .build();

        // when
        ResultActions perform = this.mockMvc.perform(put("/api/events/{id}", generateEvent.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearereToken())
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(event)));


        // then
        perform.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정했을 경우")
    public void updateEvent() throws Exception {
        // Give
        Event event = generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String updateName = "UpdatedEvent";
        eventDto.setName(updateName);


        // When
        ResultActions resultActions = this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearereToken())
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto)));


        // Then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("description").value(Matchers.is("REST API dev")))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("update-event",
                        links(linkWithRel("self").description("link to Self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content Type")
                        ),
                        requestFields(
                                //fieldWithPath("id").description("id of update Event"),
                                fieldWithPath("name").description("Name of update Event"),
                                fieldWithPath("description").description("description of update Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment of update event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment of update event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of update event"),
                                fieldWithPath("endEventDateTime").description("date time of end  of update event"),
                                fieldWithPath("location").description("location of update event"),
                                fieldWithPath("basePrice").description("basePrice of update event"),
                                fieldWithPath("maxPrice").description("maxPrice of update event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of update event")
                        ),
                        responseHeaders(
                                //headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
                        ),
                        relaxedResponseFields(
                                //relaxedResponseFields( // 문서를 강제로 안 할경우
                                fieldWithPath("id").description("identifier of updated Event"),
                                fieldWithPath("name").description("Name of updated Event"),
                                fieldWithPath("description").description("description of updated Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment of updated event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment of updated event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of updated event"),
                                fieldWithPath("endEventDateTime").description("date time of end  of updated event"),
                                fieldWithPath("location").description("location of updated event"),
                                fieldWithPath("basePrice").description("basePrice of updated event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("it tells if this event is free event or not"),
                                fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event to Status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        ))
                )
        ;
    }

    @Test
    @TestDescription("잘못된 권한으로 업데이트 할 경우 415")
    public void updateEvent_unauthorized_get415() throws Exception {
        // Given
        Event generateEvent = generateEvent(200);

        Event event = Event.builder()
                .name("Spring")
                .description("REST API dev")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 15, 12))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 12, 15, 11))
                .endEventDateTime(LocalDateTime.of(2018, 11, 1, 15, 12))
                .basePrice(10000)
                .maxPrice(10)
                .limitOfEnrollment(100)
                .location("강념역 D2")
                .build();

        // when
        ResultActions resultActions = this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearereToken())
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(event)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isUnauthorized());
    }


    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("event " + i)
                .description("test event")
                .description("REST API dev")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 12, 12))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 15, 12))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 15, 11))
                .endEventDateTime(LocalDateTime.of(2019, 11, 1, 10, 12))
                .basePrice(1)
                .maxPrice(10)
                .limitOfEnrollment(100)
                .location("강념역 D2")
                .free(false)
                .offline(true)
                .build();

        return this.eventRepository.save(event);
    }

}
