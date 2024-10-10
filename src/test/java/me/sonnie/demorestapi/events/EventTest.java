package me.sonnie.demorestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Spring REST API")
                .description("RESTful APIs")
                .build();
        assertThat(event).isNotNull();
    }
    @ParameterizedTest
    @MethodSource
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        event.update();

        assertThat(event.isFree()).isEqualTo(isFree);
    }
    static Object[] testFree(){
        return new Object[]{
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 200, false}
        };
    }
    @ParameterizedTest
    @MethodSource
    public void testOffline(String location, boolean offline){
        Event event = Event.builder()
                .location(location)
                .build();

        event.update();

        assertThat(event.isOffline()).isEqualTo(offline);
    }

    static Object[] testOffline(){
        return new Object[]{
                new Object[]{"ucf library", true},
                new Object[]{null, false}
        };
    }
}