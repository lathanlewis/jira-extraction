package com.example.petstore.model.mapper;

import com.devfactory.base.NamingStrategy;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@Singleton
@AllArgsConstructor
public class DynamoDbMapper {

    private final DynamoDbClient client;
    private final NamingStrategy namingStrategy;

    public <T> Stream<T> scan(String tableName, AttributesMapper<T> mapper) {
        return scan(tableName, mapper, builder -> {});
    }

    public <T> Stream<T> scan(String tableName,
            AttributesMapper<T> mapper,
            Consumer<ScanRequest.Builder> requestBuilder) {

        ScanResponse response = client.scan(requestBuilder
                .andThen(builder -> builder.tableName(namingStrategy.name(tableName))));

        return response.items().stream().map(mapper::map);
    }

    public <T> Optional<T> get(String tableName, AttributesMapper<T> mapper, String key, AttributeValue value) {
        GetItemResponse response = client.getItem(builder -> builder
                .tableName(namingStrategy.name(tableName))
                .key(Map.of(key, value)));

        return response.hasItem() ? Optional.of(mapper.map(response.item())) : Optional.empty();
    }

    public <T> T save(String tableName, AttributesMapper<T> mapper, T value) {
        client.putItem(builder -> builder.tableName(namingStrategy.name(tableName)).item(mapper.map(value)));
        // todo: return mapper.map(response.attributes())
        return value;
    }
}
