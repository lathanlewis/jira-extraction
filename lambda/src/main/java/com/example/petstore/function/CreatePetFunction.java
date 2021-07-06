package com.example.petstore.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.petstore.model.Pet;
import javax.inject.Named;

@Named("create-pet")
public class CreatePetFunction implements RequestHandler<Pet, Pet> {

    @Override
    public Pet handleRequest(Pet pet, Context context) {
        return Pet.save(pet);
    }
}
