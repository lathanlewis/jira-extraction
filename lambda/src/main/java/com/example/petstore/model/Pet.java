package com.example.petstore.model;

import com.example.petstore.model.mapper.AttributeMappers;
import com.example.petstore.model.mapper.AttributesMapper;
import com.example.petstore.model.mapper.DynamoDbMapper;
import io.quarkus.runtime.Startup;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jbosslog.JBossLog;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * A Pet represented by a rich-domain object.
 * Persisted to DynamoDB as the primary table key.
 */
@Data
@JBossLog
@NoArgsConstructor
@AllArgsConstructor
// For immutable beans replace @Data and @DynamoDbBean:
//@Value
//@DynamoDbImmutable(builder = Pet.PetBuilder.class)
public class Pet {

    public static final String TABLE_NAME = "PetStore";

    private static final AttributesMapper<Pet> PET_MAPPER = AttributeMappers.builder(Pet::new)
            .attribute("id", Pet::getId, Pet::setId, AttributeMappers.STRING)
            .attribute("name", Pet::getName, Pet::setName, AttributeMappers.STRING)
            .attribute("dateOfBirth", Pet::getDateOfBirth, Pet::setDateOfBirth,
                    AttributeMappers.STRING.map(LocalDate::parse, LocalDate::toString))
            .attribute("owner", Pet::getOwner, Pet::setOwner, AttributeMappers.object(Person.mapper()))
            .build();

    @Setter private static Context context;

    private String id;
    private String name;
    private LocalDate dateOfBirth;
    private Person owner;

    public static AttributesMapper<Pet> mapper() {
        return PET_MAPPER;
    }

    public static Optional<Pet> get(String id) {
        Objects.requireNonNull(id, "Id should de passed");
        return context.dynamoDbMapper.get(TABLE_NAME, mapper(), "id", AttributeValue.builder().s(id).build());
    }

    public static List<Pet> findAll() {
        return context.dynamoDbMapper.scan(TABLE_NAME, mapper()).collect(Collectors.toList());
    }

    public static Pet save(Pet pet) {
        Objects.requireNonNull(pet, "Per should de passed");
        return context.dynamoDbMapper.save(TABLE_NAME, mapper(), pet);
    }

    /**
     * Contains any managed beans that this rich domain object requires.
     * Separate to ensures that managed objects are separate from short-lived objects.
     */
    @Startup
    @Singleton
    public static class Context {

        private final DynamoDbMapper dynamoDbMapper;

        @Inject
        public Context(DynamoDbMapper dynamoDbMapper) {
            this.dynamoDbMapper = dynamoDbMapper;
            Pet.setContext(this);
        }
    }

}
