package br.com.market.place.domain.shared.value;

import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DueDateTest {

    @Test
    void shouldReturnDueDate() {
        LocalDateTime date = LocalDateTime.parse("2023-10-11T22:34:32");
        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(date);
            DomainDate dueDate = new DueDate(3);

            assertThat(dueDate.getDate(), LocalDateTimeMatchers.isDay(2023, Month.OCTOBER, 14));
            assertThat(dueDate.dateFormatted(), Matchers.is("14/10/2023"));
        }
    }

    @Test
    void shouldReturnFalseWhenDueDateIsNotEquals() {
        LocalDateTime date = LocalDateTime.parse("2023-10-11T22:34:32");
        try (MockedStatic<LocalDateTime> mocked = Mockito.mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(date);
            DomainDate dueDate = new DueDate(3);

            assertTrue(dueDate.equals(new DueDate(3)));
        }

        DomainDate dueDate = new DueDate(3);
        assertFalse(dueDate.equals(new DueDate()));
        assertFalse(dueDate.equals(new Object()));
        assertFalse(dueDate.equals(null));

    }

}