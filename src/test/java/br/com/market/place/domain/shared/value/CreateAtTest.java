package br.com.market.place.domain.shared.value;

import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateAtTest {

    @Test
    void shouldReturnCurrentDateWhenInvokeDefaultConstructor() {
        LocalDateTime date = LocalDateTime.parse("2023-10-11T22:34:32");
        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(date);
            CreateAt create = new CreateAt();

            assertThat(create.getDate(), LocalDateTimeMatchers.isDay(2023, Month.OCTOBER, 11));
            assertThat(create.getDate(), LocalDateTimeMatchers.isHour(22));
            assertThat(create.getDate(), LocalDateTimeMatchers.isMinute(34));
            assertThat(create.getDate(), LocalDateTimeMatchers.isSecond(32));
            assertThat(create.dateFormatted(), Matchers.is("11/10/2023 22:34:32"));
        }
    }

    @Test
    void shouldReturnTrueWhenBothHaveTheSemeDate(){
        LocalDateTime date = LocalDateTime.parse("2023-10-11T22:34:32");
        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(date);

            assertTrue(new CreateAt().equals(new CreateAt()));
            assertFalse(new CreateAt().equals(new Object()));
        }
    }

    @Test
    void shouldReturnFalseWhenBothNotHaveTheSemeDate(){
        DomainDate domainDate = new CreateAt();
        assertFalse(domainDate.equals(new CreateAt()));
        assertFalse(domainDate.equals(new Object()));
        assertFalse(domainDate.equals(null));
    }

}