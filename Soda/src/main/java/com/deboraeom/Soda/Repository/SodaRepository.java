package com.deboraeom.Soda.Repository;

import com.deboraeom.Soda.Entity.Soda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SodaRepository extends JpaRepository<Soda, Long> {

    Optional<Soda> findByName(String name);
}
