package com.pruebaOL.demo.services;

import com.pruebaOL.demo.model.Municipio;
import com.pruebaOL.demo.repository.MunicipioRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipioService {

    private final MunicipioRepository municipioRepository;

    public MunicipioService(MunicipioRepository municipioRepository) {
        this.municipioRepository = municipioRepository;
    }

    @Cacheable("municipios")
    public List<Municipio> obtenerTodosLosMunicipios() {
        return municipioRepository.findAll();
    }
}
