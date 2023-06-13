package com.xiaodao.dao.jpa.entity.convert;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        System.out.println("execute convert");
        if ("1".equals(attribute)) {
            return "男";
        } else if ("2".equals(attribute)) {
            return "女";
        }
        return null;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        System.out.println("execute convert");
        if ("男".equals(dbData)) {
            return "1";
        } else if ("女".equals(dbData)) {
            return "2";
        }
        return null;
    }
}
