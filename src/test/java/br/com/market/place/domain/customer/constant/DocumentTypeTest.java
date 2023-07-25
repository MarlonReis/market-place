package br.com.market.place.domain.customer.constant;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DocumentTypeTest {
    @ParameterizedTest
    @ValueSource(strings = {"CPF", "RG"})
    void shouldReturnPhysicalCustomerDocumentType(String type) {
        DocumentType documentType = DocumentType.physicalType(type);
        assertThat(documentType, Matchers.is(DocumentType.valueOf(type)));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenTypeIsLegal() {
        var exception = assertThrows(InvalidDataException.class, () -> DocumentType.physicalType("CNPJ"));
        assertThat(exception.getMessage(),Matchers.is("Expected type is RG or CPF!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenReceiveInvalidType() {
        var exception = assertThrows(InvalidDataException.class, () -> DocumentType.physicalType("CNH"));
        assertThat(exception.getMessage(),Matchers.is("Type CNH is invalid!"));
    }

}