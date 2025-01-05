package com.pruebaOL.demo.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pruebaOL.demo.dto.ComercianteDTO;
import com.pruebaOL.demo.dto.EstablecimientoRequest;
import com.pruebaOL.demo.dto.EstablecimientoResponse;
import com.pruebaOL.demo.model.Establecimiento;
import com.pruebaOL.demo.model.Comerciante;
import com.pruebaOL.demo.repository.EstablecimientoRepository;

@Service
public class EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;
    private final ComercianteService comercianteService; // Para validar la existencia del comerciante

    public EstablecimientoService(EstablecimientoRepository establecimientoRepository, ComercianteService comercianteService) {
        this.establecimientoRepository = establecimientoRepository;
        this.comercianteService = comercianteService;
    }

    public EstablecimientoResponse crearEstablecimiento(EstablecimientoRequest request) {
    	
    	Comerciante comerciante = comercianteService.consultarComerciantePorId(request.getComercianteId());
        if (comerciante == null) {
            throw new IllegalArgumentException("El comerciante especificado no existe");
        }
    	
    	
        Establecimiento establecimiento = new Establecimiento();
        establecimiento.setComerciante(comerciante);
        establecimiento.setNombreEstablecimiento(request.getNombreEstablecimiento());
        establecimiento.setIngresos(request.getIngresos());
        establecimiento.setNumeroEmpleados(request.getNumeroEmpleados());

        establecimientoRepository.save(establecimiento);

        return mapearAResponse(establecimiento);
    }

    public List<EstablecimientoResponse> consultarPorComerciante(Long comercianteId) {
        List<Establecimiento> establecimientos = establecimientoRepository.findByComerciante_IdComerciante(comercianteId);
        return establecimientos.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    public EstablecimientoResponse actualizarEstablecimiento(Long id, EstablecimientoRequest request) {
        Establecimiento establecimiento = establecimientoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Establecimiento no encontrado"));
        
    	Comerciante comerciante = comercianteService.consultarComerciantePorId(request.getComercianteId());
        if (comerciante == null) {
            throw new IllegalArgumentException("El comerciante especificado no existe");
        }
        
        establecimiento.setComerciante(comerciante);
        establecimiento.setNombreEstablecimiento(request.getNombreEstablecimiento());
        establecimiento.setIngresos(request.getIngresos());
        establecimiento.setNumeroEmpleados(request.getNumeroEmpleados());
        establecimiento.setFechaActualizacion(new Date());
        establecimiento.setUsuarioActualizacion("admin"); // Sustituye por el usuario autenticado

        establecimientoRepository.save(establecimiento);

        return mapearAResponse(establecimiento);
    }

    public void eliminarEstablecimiento(Long id) {
        if (!establecimientoRepository.existsById(id)) {
            throw new IllegalArgumentException("Establecimiento no encontrado");
        }
        establecimientoRepository.deleteById(id);
    }

    private EstablecimientoResponse mapearAResponse(Establecimiento establecimiento) {
        EstablecimientoResponse response = new EstablecimientoResponse();
        response.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        response.setNombreEstablecimiento(establecimiento.getNombreEstablecimiento());
        response.setIngresos(establecimiento.getIngresos());
        response.setNumeroEmpleados(establecimiento.getNumeroEmpleados());
        return response;
    }
}
