package br.com.market.place.domain.shared.value;


import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;

class UpdateAtTest {
    @Test
    void shouldReturnCurrentDate() {
        LocalDateTime date = LocalDateTime.parse("2023-10-11T22:34:32");
        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(date);
            UpdateAt stub = new UpdateAt();

            assertThat(stub.getDate(), LocalDateTimeMatchers.isDay(2023, Month.OCTOBER, 11));
            assertThat(stub.getDate(), LocalDateTimeMatchers.isHour(22));
            assertThat(stub.getDate(), LocalDateTimeMatchers.isMinute(34));
            assertThat(stub.getDate(), LocalDateTimeMatchers.isSecond(32));
            assertThat(stub.dateFormatted(), Matchers.is("11/10/2023 22:34:32"));
        }
    }
}