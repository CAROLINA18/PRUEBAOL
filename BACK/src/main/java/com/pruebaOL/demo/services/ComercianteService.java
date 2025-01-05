package com.pruebaOL.demo.services;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import com.pruebaOL.demo.dto.ComercianteDTO;
import com.pruebaOL.demo.dto.ComercianteReporteDTO;
import com.pruebaOL.demo.model.Comerciante;
import com.pruebaOL.demo.repository.ComercianteProcedureRepository;
import com.pruebaOL.demo.repository.ComercianteRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class ComercianteService {
    private final ComercianteProcedureRepository repository;
    
    private final ComercianteRepository comercianteRepository;

    public ComercianteService(ComercianteProcedureRepository repository , ComercianteRepository comercianteRepository ) {
    	this.comercianteRepository = comercianteRepository;
        this.repository = repository;
    }
    

    public Comerciante consultarComerciantePorId(Long id) {
        return comercianteRepository.findByIdComerciante(id)
            .orElseThrow(() -> new IllegalArgumentException("Comerciante no encontrado con el ID especificado"));
    }

    public List<ComercianteDTO> consultarPaginado(String nombre, Long municipio, String fechaRegistro, Long estado, int pagina, int tamanoPagina) {
        return repository.consultarComerciantes(nombre, municipio, fechaRegistro, estado, pagina, tamanoPagina);
    }

    public ComercianteDTO consultarPorId(Long id) {
        return repository.consultarPorId(id);
    }

    public void crearComerciante(String nombre, Long departamento, Long municipio, String telefono, String correo, Long estado) {
        repository.crearComerciante(nombre, departamento, municipio, telefono, correo, estado);
    }

    public void actualizarComerciante(Long id, String nombre, Long departamento, Long municipio, String telefono, String correo, Long estado) {
        repository.actualizar(id, nombre, departamento, municipio, telefono, correo, estado);
    }

    public void eliminarComerciante(Long id) {
        repository.eliminar(id);
    }

	public void modificarEstado(Long id, Long nuevoEstado) {
		// TODO Auto-generated method stub
		
	}
	
	public byte[] generarArchivoComerciantesActivos() throws IOException, java.io.IOException {
	    List<ComercianteReporteDTO> comerciantes = repository.obtenerComerciantesActivos();

	    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
	         OutputStreamWriter writer = new OutputStreamWriter(out)) {

	        // Configurar CSVFormat usando CSVFormat.Builder
	        CSVFormat csvFormat = CSVFormat.Builder.create()
	                .setDelimiter('|')
	                .setHeader(
	                        "Nombre o Razón Social", 
	                        "Departamento", 
	                        "Municipio", 
	                        "Teléfono", 
	                        "Correo Electrónico", 
	                        "Fecha de Registro", 
	                        "Estado", 
	                        "Cantidad de Establecimientos", 
	                        "Total Activos", 
	                        "Cantidad de Empleados"
	                )
	                .build();

	        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
	            // Iterar sobre la lista de comerciantes y escribir en el archivo CSV
	            for (ComercianteReporteDTO comerciante : comerciantes) {
	                csvPrinter.printRecord(
	                        comerciante.getNombreRazonSocial(),
	                        comerciante.getDepartamento(),
	                        comerciante.getMunicipio(),
	                        comerciante.getTelefono(),
	                        comerciante.getCorreoElectronico(),
	                        comerciante.getFechaRegistro(),
	                        comerciante.getEstado(),
	                        comerciante.getCantidadEstablecimientos(),
	                        comerciante.getTotalActivos(),
	                        comerciante.getCantidadEmpleados()
	                );
	            }
	            csvPrinter.flush();
	        }

	        return out.toByteArray(); // Retornar el contenido del archivo CSV como un array de bytes
	    }
	}
}
