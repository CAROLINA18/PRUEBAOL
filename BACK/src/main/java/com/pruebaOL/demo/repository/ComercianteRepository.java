package com.pruebaOL.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.pruebaOL.demo.model.Comerciante;

@Repository
public interface ComercianteRepository extends JpaRepository<Comerciante, Long> {
    Optional<Comerciante> findByIdComerciante(Long idComerciante);
}