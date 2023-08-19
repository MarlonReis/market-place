package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.customer.constant.DocumentType;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Document {
    private String document;
    private DocumentType documentType;

    protected Document() {}

    public Document(String document, DocumentType type) {
        if (document == null || document.isEmpty() || type == null) {
            throw new InvalidDataException("Attribute document and document type is required!");
        }
        document = document.replaceAll("[/.-]", "");
        if (isInvalid(document, type))
            throw new InvalidDataException(String.format("Attribute document of type %s is invalid!", type.name()));

        this.document = document;
        this.documentType = type;
    }

    private boolean isInvalid(String document, DocumentType type) {
        return !switch (type) {
            case CPF -> document.matches("(?!.*(\\d)\\1{6,}).{11}$");
            case CNPJ -> document.matches("(?!.*(\\d)\\1{7,}).{14}");
            default -> document.matches("(?!.*(\\d)\\1{5,}).{9}$");
        };
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
}