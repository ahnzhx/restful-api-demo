package me.sonnie.demorestapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Spring REST API")
                .description("RESTful APIs")
                .build();
        assertThat(event).isNotNull();
    }

}