package br.com.fiap.findwanted.repository;

import br.com.fiap.findwanted.entity.WantedPeopleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterpolRepository extends JpaRepository<WantedPeopleEntity, Long> {
}
