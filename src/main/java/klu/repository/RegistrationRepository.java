package klu.repository;


//import com.klu.Jfsdbackend.model.Registration1;

import klu.model.Registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}

