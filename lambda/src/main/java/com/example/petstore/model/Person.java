package com.example.petstore.model;

import com.example.petstore.model.mapper.AttributeMappers;
import com.example.petstore.model.mapper.AttributesMapper;
import io.quarkus.runtime.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jbosslog.JBossLog;

/**
 * A Person represented by a rich-domain object.
 * Does not exist as a separate entity.
 */
@Data
@JBossLog
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private static final AttributesMapper<Person> PERSON_MAPPER = AttributeMappers.builder(Person::new)
            .attribute("name", Person::getName, Person::setName, AttributeMappers.STRING)
            .attribute("address", Person::getAddress, Person::setAddress, AttributeMappers.STRING)
            .attribute("phoneNumber", Person::getPhoneNumber, Person::setPhoneNumber, AttributeMappers.STRING)
            .build();

    @Setter
    private static Context context;

    private String name;
    private String phoneNumber;
    private String address;

    public static AttributesMapper<Person> mapper() {
        return PERSON_MAPPER;
    }

    /**
     * Contains any managed beans that this rich domain object requires.
     * Separate to ensures that managed objects are separate from short-lived objects.
     */
    @Startup
    @Singleton
    public static class Context {

        @Inject
        public Context() {
            Person.setContext(this);
        }

    }

}
