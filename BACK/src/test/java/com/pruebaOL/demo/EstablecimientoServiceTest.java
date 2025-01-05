package com.pruebaOL.demo;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pruebaOL.demo.dto.EstablecimientoRequest;
import com.pruebaOL.demo.dto.EstablecimientoResponse;
import com.pruebaOL.demo.model.Comerciante;
import com.pruebaOL.demo.model.Establecimiento;
import com.pruebaOL.demo.repository.EstablecimientoRepository;
import com.pruebaOL.demo.services.ComercianteService;
import com.pruebaOL.demo.services.EstablecimientoService;

@ExtendWith(MockitoExtension.class)
class EstablecimientoServiceTest {

    @Mock
    private EstablecimientoRepository establecimientoRepository;

    @Mock
    private ComercianteService comercianteService;

    @InjectMocks
    private EstablecimientoService establecimientoService;

    @Test
    void testCrearEstablecimiento() {
        // Datos de prueba
        EstablecimientoRequest request = new EstablecimientoRequest();
        request.setComercianteId(1L);
        request.setNombreEstablecimiento("Establecimiento A");
        request.setIngresos(BigDecimal.valueOf(10000));
        request.setNumeroEmpleados(10);

        Comerciante comercianteMock = new Comerciante();
        comercianteMock.setIdComerciante(1L);

        Establecimiento establecimientoMock = new Establecimiento();
        establecimientoMock.setIdEstablecimiento(1L);
        establecimientoMock.setNombreEstablecimiento("Establecimiento A");
        establecimientoMock.setIngresos(BigDecimal.valueOf(10000));
        establecimientoMock.setNumeroEmpleados(10);

        // Mock del Comerciante
        Mockito.when(comercianteService.consultarComerciantePorId(1L)).thenReturn(comercianteMock);

        // Mock del repositorio
        Mockito.when(establecimientoRepository.save(Mockito.any(Establecimiento.class))).thenReturn(establecimientoMock);

        // Ejecución del método
        EstablecimientoResponse response = establecimientoService.crearEstablecimiento(request);

        // Validación
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Establecimiento A", response.getNombreEstablecimiento());
        Assertions.assertEquals(BigDecimal.valueOf(10000), response.getIngresos());
        Mockito.verify(establecimientoRepository, Mockito.times(1)).save(Mockito.any(Establecimiento.class));
    }
    
    
    @Test
    void testConsultarPorComerciante() {
        // Datos de prueba
        Long comercianteId = 1L;

        Establecimiento establecimientoMock = new Establecimiento();
        establecimientoMock.setIdEstablecimiento(1L);
        establecimientoMock.setNombreEstablecimiento("Establecimiento A");
        establecimientoMock.setIngresos(BigDecimal.valueOf(10000));
        establecimientoMock.setNumeroEmpleados(10);

        List<Establecimiento> mockEstablecimientos = List.of(establecimientoMock);

        // Mock del repositorio
        Mockito.when(establecimientoRepository.findByComerciante_IdComerciante(comercianteId))
                .thenReturn(mockEstablecimientos);

        // Ejecución del método
        List<EstablecimientoResponse> responses = establecimientoService.consultarPorComerciante(comercianteId);

        // Validación
        Assertions.assertNotNull(responses);
        Assertions.assertEquals(1, responses.size());
        Assertions.assertEquals("Establecimiento A", responses.get(0).getNombreEstablecimiento());
        Mockito.verify(establecimientoRepository, Mockito.times(1))
                .findByComerciante_IdComerciante(comercianteId);
    }
    
    @Test
    void testEliminarEstablecimiento() {
        // Datos de prueba
        Long establecimientoId = 1L;

        // Mock del repositorio
        Mockito.when(establecimientoRepository.existsById(establecimientoId)).thenReturn(true);

        // Ejecución del método
        establecimientoService.eliminarEstablecimiento(establecimientoId);

        // Validación
        Mockito.verify(establecimientoRepository, Mockito.times(1)).existsById(establecimientoId);
        Mockito.verify(establecimientoRepository, Mockito.times(1)).deleteById(establecimientoId);
    }
}
