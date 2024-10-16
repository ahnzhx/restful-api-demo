package me.sonnie.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.sonnie.demorestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @Test
    @TestDescription("Creating events")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("startup factory")
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;

    }

    @Test
    @TestDescription("Invalid input data - return bad request")
    public void createEvent_bad_request() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API development with spring")
                .beginEnrollmentDateTime(LocalDateTime.now())
                .closeEnrollmentDateTime(LocalDateTime.now())
                .beginEventDateTime(LocalDateTime.now())
                .endEventDateTime(LocalDateTime.now())
                .free(true)
                .offline(false)
                .location("Gangnam")
                .build();

        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());


    }

    @Test
    @TestDescription("Empty input data - return bad request")
    public void createEvent_bad_request_empty_input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("Wrong input data - return bad request")
    public void createEvent_bad_request_wrong_input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API development with spring")
                .beginEnrollmentDateTime(LocalDateTime.now())
                .closeEnrollmentDateTime(LocalDateTime.of(2018,10,1,1,1))
                .beginEventDateTime(LocalDateTime.now())
                .endEventDateTime(LocalDateTime.now())
                .build();

        this.mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().is5xxServerError());
//                .andExpect(jsonPath("content[0].objectName").exists())
//                .andExpect(jsonPath("content[0].defaultMessage").exists())
//                .andExpect(jsonPath("content[0].code").exists())
//                .andExpect(jsonPath("_links.index").exists());
    }


}
