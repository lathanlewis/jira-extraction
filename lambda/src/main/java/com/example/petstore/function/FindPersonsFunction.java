package com.example.petstore.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.petstore.model.Person;
import com.example.petstore.model.Pet;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Named;

@Named("find-persons")
public class FindPersonsFunction implements RequestHandler<Void, List<Person>> {

    @Override
    public List<Person> handleRequest(Void nothing, Context context) {
        return Pet.findAll().stream()
                .map(Pet::getOwner).distinct()
                .collect(Collectors.toList());
    }
}
