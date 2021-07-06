package com.example.petstore.model.mapper;

import java.util.Map;
import lombok.Data;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Data
public class Attribute<R, T> {

    private final Property<R, T> property;
    private final AttributeMapper<T> mapper;

    public void writeFromMapToInstance(Map<String, AttributeValue> attributeValues, R instance) {
        AttributeValue attributeValue = attributeValues.get(getProperty().getName());
        getProperty().getSetter().accept(instance, getMapper().map(attributeValue));
    }

    public void writeFromInstanceToMap(R instance, Map<String, AttributeValue> result) {
        T value = getProperty().getGetter().apply(instance);
        result.put(getProperty().getName(), getMapper().map(value));
    }

}
