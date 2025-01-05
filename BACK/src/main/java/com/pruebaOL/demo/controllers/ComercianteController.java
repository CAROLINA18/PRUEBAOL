package com.pruebaOL.demo.controllers;

import com.pruebaOL.demo.dto.ComercianteDTO;
import com.pruebaOL.demo.dto.ComercianteRequest;
import com.pruebaOL.demo.model.Comerciante;
import com.pruebaOL.demo.services.ComercianteService;
import com.pruebaOL.demo.utils.ComercianteException;
import com.pruebaOL.demo.utils.Response;

import jakarta.annotation.Resource;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/comerciantes")
public class ComercianteController {

    private final ComercianteService comercianteService;

    public ComercianteController(ComercianteService comercianteService) {
        this.comercianteService = comercianteService;
    }

    /**
     * Consulta paginada con filtros opcionales
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'AUXILIAR')") // Requiere roles específicos
    public ResponseEntity<?> getComerciantes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long municipio,
            @RequestParam(required = false) String fechaRegistro,
            @RequestParam(required = false) Long estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<ComercianteDTO> comerciantes = comercianteService.consultarPaginado(nombre, municipio, fechaRegistro, estado, page, size);
        return ResponseEntity.ok(comerciantes);
    }

    /**
     * Consultar comerciante por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'AUXILIAR')")
    public ResponseEntity<?> getComercianteById(@PathVariable Long id) {
        ComercianteDTO comerciante = comercianteService.consultarPorId(id);
        if (comerciante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comerciante);
    }

    /**
     * Crear comerciante
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crearComerciante(@RequestBody ComercianteRequest request) {
        try {
            comercianteService.crearComerciante(
                request.getNombreRazonSocial(),
                request.getDepartamentoId(),
                request.getMunicipioId(),
                request.getTelefono(),
                request.getCorreoElectronico(),
                request.getEstadoId()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response(true, "Comerciante creado exitosamente", null));
        } catch (ComercianteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response(false, "Error interno al crear el comerciante: " + e.getMessage(), null));
        }
    }

    /**
     * Actualizar comerciante
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizarComerciante(@PathVariable Long id, @RequestBody ComercianteRequest request) {
        comercianteService.actualizarComerciante(
            id,
            request.getNombreRazonSocial(),
            request.getDepartamentoId(),
            request.getMunicipioId(),
            request.getTelefono(),
            request.getCorreoElectronico(),
            request.getEstadoId()
        );
        return ResponseEntity.ok("Comerciante actualizado con éxito");
    }

    /**
     * Eliminar comerciante
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarComerciante(@PathVariable Long id) {
        comercianteService.eliminarComerciante(id);
        return ResponseEntity.ok("Comerciante eliminado con éxito");
    }

    /**
     * Modificar estado (Activar/Inactivar)
     */
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> modificarEstado(@PathVariable Long id, @RequestParam("estado") Long nuevoEstado) {
        comercianteService.modificarEstado(id, nuevoEstado);
        return ResponseEntity.ok("Estado del comerciante actualizado con éxito");
    }
    
    
    @GetMapping("/reporte-activos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> generarReporteComerciantesActivos() {
        final String nombreArchivo = "reporte_comerciantes_activos.csv";
        final String rutaDescargas = System.getProperty("user.home") + "\\Downloads\\" + nombreArchivo;

        try {
            // Generar el archivo en formato CSV
            byte[] archivo = comercianteService.generarArchivoComerciantesActivos();

            // Guardar el archivo en la carpeta Descargas de Windows
            Path rutaArchivo = Paths.get(rutaDescargas);
            Files.write(rutaArchivo, archivo);

            // Retornar una respuesta exitosa con la ubicación del archivo guardado
            return ResponseEntity.ok("Archivo generado y guardado en: " + rutaArchivo.toString());

        } catch (IOException e) {
            // Manejo de excepciones
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar o guardar el archivo: " + e.getMessage());
        }
    }
}
