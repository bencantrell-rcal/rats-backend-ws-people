package com.rcal.people.repository.read;

import com.rcal.people.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadPersonRepository extends JpaRepository<Person, Integer>{

}
