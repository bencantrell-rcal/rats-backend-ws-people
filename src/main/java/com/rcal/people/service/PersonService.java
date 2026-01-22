package com.rcal.people.service;

import com.rcal.people.entity.Person;
import com.rcal.people.repository.read.ReadPersonRepository;
import com.rcal.people.repository.write.WritePersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService{

  private final ReadPersonRepository readPersonRepository;
  private final WritePersonRepository writePersonRepository;

  public PersonService(ReadPersonRepository readPersonRepository,
      WritePersonRepository writePersonRepository) {
    this.readPersonRepository = readPersonRepository;
    this.writePersonRepository = writePersonRepository;
  }

  // ---------------------------------------------------------------------------
  // Get all people
  // ---------------------------------------------------------------------------
  public List<Person> getAllPeople(){
    return readPersonRepository.findAll();
  }

  // ---------------------------------------------------------------------------
  // Get a specific person by ID
  // ---------------------------------------------------------------------------
  public Person getPersonById(Integer personId){
    return readPersonRepository.findById(personId).orElseThrow(
        () -> new RuntimeException("Person not found with id: " + personId));
  }

  // ---------------------------------------------------------------------------
  // Create a new person
  // ---------------------------------------------------------------------------
  public Person createPerson(String name){

    if (name == null){
      throw new IllegalArgumentException("Person name must not be null");
    }

    // Keep only alphabetic characters and spaces
    String sanitizedName = name.replaceAll("[^A-Za-z ]","").trim()
        .replaceAll("\\s+"," ");

    if (sanitizedName.isEmpty()){
      throw new IllegalArgumentException(
          "Person name must contain alphabetic characters");
    }

    Person person = new Person();
    person.setName(sanitizedName);

    return writePersonRepository.save(person);
  }

  // ---------------------------------------------------------------------------
  // Delete a person by ID
  // ---------------------------------------------------------------------------
  public void deletePerson(Integer personId){
    writePersonRepository.deleteById(personId);
  }

  // ---------------------------------------------------------------------------
  // Get a specific person by ID (Optional)
  // ---------------------------------------------------------------------------
  public Optional<Person> getPersonByIdOptional(Integer personId){
    return readPersonRepository.findById(personId);
  }

}
