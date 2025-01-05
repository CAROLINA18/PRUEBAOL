package com.pruebaOL.demo.controllers;

import com.pruebaOL.demo.model.Departamento;
import com.pruebaOL.demo.services.DepartamentoService;
import com.pruebaOL.demo.utils.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<Departamento>>> obtenerDepartamentos() {
        List<Departamento> departamentos = departamentoService.obtenerTodosLosDepartamentos();
        return ResponseEntity.ok(new ResponseWrapper<>(true, "Departamentos obtenidos con éxito", departamentos));
    }
}
