package ru.homerep.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.homerep.userservice.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
