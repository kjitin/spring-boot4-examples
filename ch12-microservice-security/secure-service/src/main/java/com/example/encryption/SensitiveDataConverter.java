package com.example.encryption;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// Custom encryption for sensitive data fields in entities
@Converter
public class SensitiveDataConverter implements AttributeConverter<String, String> {
    private final AesEncryptor encryptor = new AesEncryptor("your-secret-key"); // Key management is crucial!

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryptor.decrypt(dbData);
    }
}
