package com.pruebaOL.demo.repository;

import com.pruebaOL.demo.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
}
