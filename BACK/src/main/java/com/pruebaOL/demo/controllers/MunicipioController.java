package com.pruebaOL.demo.controllers;

import com.pruebaOL.demo.model.Municipio;
import com.pruebaOL.demo.services.MunicipioService;
import com.pruebaOL.demo.utils.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/municipios")
public class MunicipioController {

    private final MunicipioService municipioService;

    public MunicipioController(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<Municipio>>> obtenerMunicipios() {
        List<Municipio> municipios = municipioService.obtenerTodosLosMunicipios();
        return ResponseEntity.ok(new ResponseWrapper<>(true, "Municipios obtenidos con éxito", municipios));
    }
}