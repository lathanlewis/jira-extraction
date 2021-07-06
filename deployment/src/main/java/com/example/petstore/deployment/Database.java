package com.example.petstore.deployment;

import com.devfactory.cdk.base.NameStrategy;
import com.devfactory.cdk.base.stack.PersistentStack;
import com.example.petstore.model.Pet;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

public class Database {

    @Singleton
    @Produces
    public Table createTable(PersistentStack stack, NameStrategy nameStrategy) {
        Attribute id = Attribute.builder()
                .name("id")
                .type(AttributeType.STRING)
                .build();

        return Table.Builder.create(stack, nameStrategy.formatName("database"))
                .tableName(nameStrategy.formatName(Pet.TABLE_NAME))
                .partitionKey(id)
                .build();
    }

}
