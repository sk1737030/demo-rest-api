package me.dongguri.demorestapi.events;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring Rest API")
                .description("REST API development")
                .build();
        assertThat(event).isNotNull();
    }


    @Test
    public void javaBean() {
        // given
        String name = "Event";
        String spring = "Spring";

        // when
        Event event = new Event();
        event.setName(name);
        event.setDescription("Spring");

        // that
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(spring);
    }

    @Test
    public void testFree() {
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isTrue();

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // when
        event.update();

        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void trestOffline() {
        // Given
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타텁 팩토리")
                .build();

        // when
        event.update();

        // then
        assertThat(event.isOffline()).isTrue();

        // Given
        event = Event.builder()
                .build();

        // when
        event.update();

        // then
        assertThat(event.isOffline()).isFalse();


    }
}