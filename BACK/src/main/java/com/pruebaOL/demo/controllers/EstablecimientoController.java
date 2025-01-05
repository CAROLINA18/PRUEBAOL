package com.pruebaOL.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pruebaOL.demo.dto.EstablecimientoRequest;
import com.pruebaOL.demo.dto.EstablecimientoResponse;
import com.pruebaOL.demo.services.EstablecimientoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/establecimientos")
@PreAuthorize("hasRole('ADMINISTRADOR')") // Restringir acceso a administradores
public class EstablecimientoController {

    private final EstablecimientoService establecimientoService;

    public EstablecimientoController(EstablecimientoService establecimientoService) {
        this.establecimientoService = establecimientoService;
    }

    @GetMapping("/comerciante/{comercianteId}")
    public ResponseEntity<?> consultarPorComerciante(@PathVariable Long comercianteId) {
        List<EstablecimientoResponse> establecimientos = establecimientoService.consultarPorComerciante(comercianteId);
        return ResponseEntity.ok(establecimientos);
    }

    @PostMapping
    public ResponseEntity<?> crearEstablecimiento(@RequestBody @Valid EstablecimientoRequest request) {
        EstablecimientoResponse response = establecimientoService.crearEstablecimiento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstablecimiento(@PathVariable Long id, @RequestBody @Valid EstablecimientoRequest request) {
        EstablecimientoResponse response = establecimientoService.actualizarEstablecimiento(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstablecimiento(@PathVariable Long id) {
        establecimientoService.eliminarEstablecimiento(id);
        return ResponseEntity.ok("Establecimiento eliminado con éxito");
    }
}