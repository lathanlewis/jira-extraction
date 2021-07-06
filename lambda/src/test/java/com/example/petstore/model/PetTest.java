package com.example.petstore.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.example.petstore.model.mapper.DynamoDbMapper;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class PetTest {

    @Mock private DynamoDbMapper dynamoDbMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Pet.setContext(new Pet.Context(dynamoDbMapper));
    }

    @Test
    public void testGetOne() {
        // given
        Pet simplePet = new Pet("1", "test1", LocalDate.now(),
                new Person("test2", "test3", "test4"));

        given(dynamoDbMapper.get(Pet.TABLE_NAME, Pet.mapper(), "id", AttributeValue.builder().s("1").build()))
                .willReturn(Optional.of(simplePet));
        // when
        Optional<Pet> actualPet = Pet.get("1");
        // then
        assertThat(actualPet).isPresent().contains(simplePet);
    }

    @Test
    public void testFindAll() {
        Pet.findAll();
        verify(dynamoDbMapper).scan(Pet.TABLE_NAME, Pet.mapper());
    }

}
