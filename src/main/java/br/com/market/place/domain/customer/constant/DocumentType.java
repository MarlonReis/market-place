package br.com.market.place.domain.customer.constant;

import br.com.market.place.domain.shared.exception.InvalidDataException;


public enum DocumentType {
    CPF,
    CNPJ,
    RG;

    public static DocumentType physicalType(String type) {
        try {
            DocumentType documentType = valueOf(type);
            if (documentType == CNPJ) {
                throw new InvalidDataException("Expected type is RG or CPF!");
            }
            return documentType;
        } catch (IllegalArgumentException ex) {
            throw new InvalidDataException(String.format("Type %s is invalid!", type));
        }
    }
}
