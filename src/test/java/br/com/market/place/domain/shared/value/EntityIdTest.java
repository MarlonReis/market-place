package br.com.market.place.domain.shared.value;

import br.com.market.place.domain.shared.exception.InvalidIdException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class EntityIdTest {

    static class EntityIdStub extends EntityId {
        public EntityIdStub() {
            super();
        }

        public EntityIdStub(String id) {
            super(id);
        }
    }


    @Test
    void shouldGenerateRandomIdWhenUseDefaultConstructor() {
        EntityId entityId = new EntityIdStub();
        assertThat(entityId.getId(), CoreMatchers.notNullValue());
    }

    @Test
    void shouldInitializeClassWhenIdIsValid() {
        EntityId entityId = new EntityIdStub("badc8643-e58c-4fa6-83d0-dfa95a28626d");
        assertThat(entityId.getId(), CoreMatchers.notNullValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"badc8643e58c4fa683d0dfa95a28626d", "badc8643-e58c-4fa6-83d", "134134"})
    void shouldThrowInvalidIdExceptionWhenSetInvalidId(String value) {
        var exception = assertThrows(InvalidIdException.class, () -> new EntityIdStub(value));
        assertThat(exception.getMessage(), Matchers.containsString(value));
    }

    @Test
    void shouldThrowInvalidIdExceptionWhenSetNull() {
        var exception = assertThrows(InvalidIdException.class, () -> new EntityIdStub(null));
        assertThat(exception.getMessage(), Matchers.is("Id is required, cannot be null!"));
    }

    @Test
    void shouldReturnTrueWhenBothIsEquals() {
        final UUID uuidMocked = UUID.fromString("badc8643-e58c-4fa6-83d0-dfa95a28626d");
        var idOne = new EntityIdStub("badc8643-e58c-4fa6-83d0-dfa95a28626d");
        var idTwo = new EntityIdStub("badc8643-e58c-4fa6-83d0-dfa95a28626d");

        try (MockedStatic<UUID> mocked = Mockito.mockStatic(UUID.class)) {
            mocked.when(UUID::randomUUID).thenReturn(uuidMocked);
            assertTrue(idTwo.equals(new EntityIdStub()));
        }

        assertTrue(idOne.equals(idTwo));
        assertFalse(idOne.equals(null));
        assertFalse(idOne.equals(new EntityIdStub(UUID.randomUUID().toString())));
        assertFalse(idOne.equals(new Object()));
    }

}