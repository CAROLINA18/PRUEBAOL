package com.pruebaOL.demo.services;

import com.pruebaOL.demo.model.Departamento;
import com.pruebaOL.demo.repository.DepartamentoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Cacheable("departamentos")
    public List<Departamento> obtenerTodosLosDepartamentos() {
        return departamentoRepository.findAll();
    }
}
