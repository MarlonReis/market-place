package br.com.market.place.domain.customer.entity;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

class PhysicalTest {

    @Test
    void shouldDefineIdAndDatesAfterCallPrePersist() {
        Customer customer = Physical.Builder.build().now();
        customer.prePersist();

        assertThat(customer.getId(), Matchers.notNullValue());
        assertThat(customer.getCreateAt(), Matchers.notNullValue());
        assertThat(customer.getUpdateAt(), Matchers.nullValue());
    }

    @Test
    void shouldDefineUpdateAtAfterCallPreUpdate() {
        Customer customer = Physical.Builder.build().now();
        customer.preUpdate();

        assertThat(customer.getUpdateAt(), Matchers.notNullValue());
    }

    @Test
    void shouldReturnFalseWhenCustomersIsNotEquals(){
        Customer physicalOne = Physical.Builder.build().now();

        UUID id = UUID.randomUUID();
        try(MockedStatic<UUID> mocked = Mockito.mockStatic(UUID.class)){
            mocked.when(UUID::randomUUID).thenReturn(id);
            physicalOne.prePersist();

            Customer physicalTwo = Physical.Builder.build().now();
            physicalTwo.prePersist();

            assertTrue(physicalTwo.equals(physicalOne));
        }

        assertFalse(physicalOne.equals(Physical.Builder.build().now()));
        assertFalse(physicalOne.equals(new Object()));
        assertFalse(physicalOne.equals(null));

    }


}