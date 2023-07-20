package br.com.market.place.domain.customer.value;

import am.ik.yavi.core.CustomConstraint;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.util.Objects;

import static am.ik.yavi.builder.StringValidatorBuilder.of;

@Embeddable
public class Document {
    private String document;
    private DocumentType documentType;

    protected Document() {
    }

    public Document(String document, DocumentType type) {
        if (document == null || document.isEmpty() || type == null) {
            throw new InvalidDataException("Attribute document and document type is required!");
        }

        document = document.replaceAll("[/.-]", "");

        final var validator = new ValidateDocumentPredicate(type);
        var documentVal = of("document", s -> s.predicate(validator)).build().validate(document);

        this.document = documentVal.orElseThrow(InvalidDataException::new);
        this.documentType = type;

    }

    public DocumentType documentType() {
        return documentType;
    }

    public String document() {
        return document;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Document that) {
            return Objects.equals(document, that.document()) &&
                    Objects.equals(documentType, that.documentType());
        }
        return false;

    }

    private class ValidateDocumentPredicate implements CustomConstraint<String> {
        private final DocumentType type;

        private ValidateDocumentPredicate(DocumentType type) {
            this.type = type;
        }

        @Override
        public String defaultMessageFormat() {
            return String.format("Attribute document of type %s is invalid!", type.name());
        }

        @Override
        public String messageKey() {
            return "Document.document";
        }

        @Override
        public boolean test(String value) {
            if (type == DocumentType.CPF) {
                return value.matches("(?!.*(\\d)\\1{6,}).{11}$");
            } else if (type == DocumentType.CNPJ) {
                return value.matches("(?!.*(\\d)\\1{7,}).{14}");
            }
            return value.matches("(?!.*(\\d)\\1{5,}).{9}$");
        }
    }

}