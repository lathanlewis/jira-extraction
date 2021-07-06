package com.example.petstore.model.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class AttributeMappers {

    public static final AttributeMapper<String> STRING = new SimpleAttributeMapper<>(
                AttributeValue::s,
                value -> AttributeValue.builder().s(value).build()
    );

    public static final AttributeMapper<Long> NUMBER = new SimpleAttributeMapper<>(
                attributeValue -> (attributeValue.n() == null) ? null : Long.parseLong(attributeValue.n()),
                value -> (value == null) ? AttributeValue.builder().nul(true).build() :
                        AttributeValue.builder().n(Long.toString(value)).build()
    );

    public static final AttributeMapper<Double> DECIMAL = new SimpleAttributeMapper<>(
                attributeValue -> (attributeValue.n() == null) ? null : Double.parseDouble(attributeValue.n()),
                value -> (value == null) ? AttributeValue.builder().nul(true).build() :
                        AttributeValue.builder().n(Double.toString(value)).build()
    );

    public static <T> AttributeMapper<T> object(AttributesMapper<T> mapper) {
        return new SimpleAttributeMapper<>(
                attributeValue -> attributeValue.hasM() ? mapper.map(attributeValue.m()) : null,
                value -> (value == null) ? AttributeValue.builder().nul(true).build() :
                        AttributeValue.builder().m(mapper.map(value)).build());
    }

    public static <T> AttributeMapper<List<T>> list(AttributeMapper<T> mapper) {
        return new SimpleAttributeMapper<>(
                attributeValue -> attributeValue.hasL()
                        ? attributeValue.l().stream().map(mapper::map).collect(Collectors.toList()) : null,
                value -> (value == null) ? AttributeValue.builder().nul(true).build() : AttributeValue.builder()
                        .l(value.stream().map(mapper::map).collect(Collectors.toList())).build());
    }

    public static <T> AttributesBuilder<T> builder(Supplier<T> constructor) {
        return new AttributesBuilder<>(constructor);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class AttributesBuilder<R> {

        private final Supplier<R> constructor;
        private final List<Attribute<R, ?>> attributes = new ArrayList<>();

        public <T> AttributesBuilder<R> attribute(Attribute<R, T> attribute) {
            attributes.add(attribute);
            return this;
        }

        public <T> AttributesBuilder<R> attribute(
                String name,
                Function<R, T> getter,
                BiConsumer<R, T> setter,
                AttributeMapper<T> mapper) {
            return attribute(new Property<>(name, getter, setter), mapper);
        }

        public <T> AttributesBuilder<R> attribute(Property<R, T> property, AttributeMapper<T> mapper) {
            return attribute(new Attribute<>(property, mapper));
        }

        public AttributesMapper<R> build() {
            return new SimpleAttributesMapper<>(constructor, attributes);
        }
    }

}
