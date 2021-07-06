package com.example.petstore.api;

import com.example.petstore.model.Pet;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/pets")
public class PetResource {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Pet getPet(@QueryParam("petId") String petId) {
        return Pet.get(petId).orElseThrow(() -> new IllegalArgumentException("Pet " + petId + " not found"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pet> findAll() {
        return Pet.findAll();
    }

}
