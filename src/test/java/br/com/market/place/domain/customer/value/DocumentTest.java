package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.customer.constant.DocumentType;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void shouldReturnValidCPF() {
        Document document = new Document("211.830.995-33", DocumentType.CPF);
        assertThat(document.document(), Matchers.is("21183099533"));
        assertThat(document.documentType(), Matchers.is(DocumentType.CPF));
    }

    @Test
    void shouldReturnValidCNPJ() {
        Document document = new Document("33.747.249/0001-14", DocumentType.CNPJ);
        assertThat(document.document(), Matchers.is("33747249000114"));
        assertThat(document.documentType(), Matchers.is(DocumentType.CNPJ));
    }

    @Test
    void shouldReturnValidRG() {
        Document document = new Document("45.492.691-1", DocumentType.RG);
        assertThat(document.document(), Matchers.is("454926911"));
        assertThat(document.documentType(), Matchers.is(DocumentType.RG));
    }

    @ParameterizedTest
    @ValueSource(strings = {"484.880.470-100", "000.000.070-10", "224903930570", "1111111111"})
    void shouldThrowInvalidDataExceptionWhenCPFIsInvalid(String document) {
        var exception = assertThrows(InvalidDataException.class, () -> new Document(document, DocumentType.CPF));
        assertThat(exception.getMessage(), Matchers.is("Attribute document of type CPF is invalid!"));
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {""})
    void shouldThrowInvalidDataExceptionWhenCPFIsIsNullOrEmpty(String document) {
        var exception = assertThrows(InvalidDataException.class, () -> new Document(document, DocumentType.CPF));
        assertThat(exception.getMessage(), Matchers.is("Attribute document and document type is required!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenDocumentTypeIsNull() {
        var exception = assertThrows(InvalidDataException.class, () -> new Document("224903930570", null));
        assertThat(exception.getMessage(), Matchers.is("Attribute document and document type is required!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"62.400.000/0001-43", "6400000000143", "64000000001439939"})
    void shouldThrowInvalidDataExceptionWhenCNPJIsInvalid(String document) {
        var exception = assertThrows(InvalidDataException.class, () -> new Document(document, DocumentType.CNPJ));
        assertThat(exception.getMessage(), Matchers.is("Attribute document of type CNPJ is invalid!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"15.000.000-0", "15130122", "15.130.122-000"})
    void shouldThrowInvalidDataExceptionWhenRGIsInvalid(String document) {
        var exception = assertThrows(InvalidDataException.class, () -> new Document(document, DocumentType.RG));
        assertThat(exception.getMessage(), Matchers.is("Attribute document of type RG is invalid!"));
    }

    @Test
    void shouldReturnNullWhenUseDefaultConstructor() {
        var document = new Document();
        assertThat(document.documentType(), Matchers.nullValue());
        assertThat(document.document(), Matchers.nullValue());
    }

    @Test
    void shouldReturnFalseWhenDocumentIsNotEquals() {
        Document document = new Document("45.492.691-1", DocumentType.RG);

        assertFalse(document.equals(new Document("211.830.995-33", DocumentType.CPF)));
        assertFalse(document.equals(new Object()));
        assertFalse(document.equals(null));
    }

}