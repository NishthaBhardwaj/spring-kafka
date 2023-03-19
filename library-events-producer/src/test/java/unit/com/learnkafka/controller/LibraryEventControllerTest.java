package com.learnkafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.controller.LibraryEventController;
import com.learnkafka.domain.Book;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.domain.LibraryEventType;
import com.learnkafka.producer.LibraryEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LibraryEventProducer libraryEventProducer;

    
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void postLibraryEvent() throws Exception {
        //given
        Book book = Book.builder()
                .bookId(456)
                .bookName("Kafka Using Spring Boot")
                .bookAuthor("Dilip")
                .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .libraryEventType(LibraryEventType.NEW)
                .book(book)
                .build();

        //when
        String json = objectMapper.writeValueAsString(libraryEvent);
        when(libraryEventProducer.sendLibraryEventApproach2(isA(LibraryEvent.class)))
                .thenReturn(null);

        mockMvc.perform(post("/v1/libraryevent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void postLibraryEvent_4XX() throws Exception {
        //given

        Book book = Book.builder()
                .bookId(null)
                .bookName("Kafka Using Spring Boot")
                .bookAuthor("Dilip")
                .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(null)
                .libraryEventType(LibraryEventType.NEW)
                .book(book)
                .build();

        //when
        String json = objectMapper.writeValueAsString(libraryEvent);
        when(libraryEventProducer.sendLibraryEventApproach2(isA(LibraryEvent.class)))
                .thenReturn(null);
        String expectedErrorMessage = "book.bookId-must not be null";
        mockMvc.perform(post("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(expectedErrorMessage));

    }

    @Test
    void putLibraryEvent() throws Exception {
        //given
        Book book = Book.builder()
                .bookId(12)
                .bookName("Kafka Using Spring Boot")
                .bookAuthor("Dilip")
                .build();

        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(123)
                .libraryEventType(LibraryEventType.UPDATE)
                .book(book)
                .build();

        String json = objectMapper.writeValueAsString(libraryEvent);
        mockMvc.perform(put("/v1/libraryevent")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



        @Test
        void putLibraryEvent_withoutEvent_id() throws Exception {
            //given
            Book book = Book.builder()
                    .bookId(12)
                    .bookName("Kafka Using Spring Boot")
                    .bookAuthor("Dilip")
                    .build();

            LibraryEvent libraryEvent = LibraryEvent.builder()
                    .libraryEventId(null)
                    .libraryEventType(LibraryEventType.UPDATE)
                    .book(book)
                    .build();

            String json = objectMapper.writeValueAsString(libraryEvent);
            String expectedErrorMessage = "Please pass the LibraryEventId";
            //when
            mockMvc.perform(put("/v1/libraryevent")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(expectedErrorMessage));

    }
}
