package br.com.fiap.findwanted.repository;

import br.com.fiap.findwanted.entity.WantedPeopleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterpolRepository extends JpaRepository<WantedPeopleEntity, Long> {

    Optional<WantedPeopleEntity> findByName(String name);
}
