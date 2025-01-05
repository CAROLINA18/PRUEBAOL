package com.pruebaOL.demo.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
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
    

    public ComercianteService(ComercianteProcedureRepository repository , ComercianteRepository comercianteRepository  ) {
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
	
	
	public ComercianteReporteDTO obtenerComercianteReportePorId(Long idComerciante) {
	    Optional<Comerciante> optionalComerciante = comercianteRepository.findByIdComerciante(idComerciante);

	    if (optionalComerciante.isEmpty()) {
	        throw new IllegalArgumentException("El comerciante no existe con ID: " + idComerciante);
	    }

	    Comerciante comerciante = optionalComerciante.get();

	    // Mapear Comerciante a ComercianteReporteDTO
	    return mapearAComercianteReporteDTO(comerciante);
	}
	
	private ComercianteReporteDTO mapearAComercianteReporteDTO(Comerciante comerciante) {
	    ComercianteReporteDTO dto = new ComercianteReporteDTO();
	    dto.setNombreRazonSocial(comerciante.getNombreRazonSocial());
	    dto.setDepartamento(comerciante.getDepartamento().getNombre()); // Suponiendo que tienes relación con Departamento
	    dto.setMunicipio(comerciante.getMunicipio().getNombre());       // Suponiendo que tienes relación con Municipio
	    dto.setTelefono(comerciante.getTelefono());
	    dto.setCorreoElectronico(comerciante.getCorreoElectronico());
	    dto.setFechaRegistro(comerciante.getFechaRegistro());
	    dto.setEstado(comerciante.getEstado().getDescripcion());
	    dto.setCantidadEstablecimientos(comerciante.getEstablecimientos().size());
	    dto.setTotalActivos(comerciante.getEstablecimientos().stream()
	            .map(est -> est.getIngresos())
	            .reduce(BigDecimal.ZERO, BigDecimal::add));
	    dto.setCantidadEmpleados(comerciante.getEstablecimientos().stream()
	            .mapToInt(est -> est.getNumeroEmpleados())
	            .sum());
	    return dto;
	}
	
	
	public String generarPdfComerciante(Long idComerciante) throws IOException, FileNotFoundException, java.io.IOException {
	    // Obtener los detalles del comerciante
	    ComercianteReporteDTO comerciante = obtenerComercianteReportePorId(idComerciante);

	    if (comerciante == null) {
	        throw new IllegalArgumentException("El comerciante no existe");
	    }

	    // Obtener la ruta de la carpeta de Descargas
	    String downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads").toString();

	    // Crear el archivo PDF en la carpeta de Descargas
	    String pdfFilePath = downloadsPath + "/comerciante_detalle_" + idComerciante + ".pdf";

	    try (PdfWriter writer = new PdfWriter(pdfFilePath);
	         PdfDocument pdfDocument = new PdfDocument(writer);
	         Document document = new Document(pdfDocument)) {

	        document.add(new Paragraph("Detalle del Comerciante").setBold().setFontSize(16));
	        document.add(new Paragraph("Nombre o Razón Social: " + comerciante.getNombreRazonSocial()));
	        document.add(new Paragraph("Departamento: " + comerciante.getDepartamento()));
	        document.add(new Paragraph("Municipio: " + comerciante.getMunicipio()));
	        document.add(new Paragraph("Teléfono: " + comerciante.getTelefono()));
	        document.add(new Paragraph("Correo Electrónico: " + comerciante.getCorreoElectronico()));
	        document.add(new Paragraph("Fecha de Registro: " + comerciante.getFechaRegistro()));
	        document.add(new Paragraph("Estado: " + comerciante.getEstado()));
	        document.add(new Paragraph("Cantidad de Establecimientos: " + comerciante.getCantidadEstablecimientos()));
	        document.add(new Paragraph("Total Activos: " + comerciante.getTotalActivos()));
	        document.add(new Paragraph("Cantidad de Empleados: " + comerciante.getCantidadEmpleados()));
	    }

	    return pdfFilePath; // Retornar la ruta del archivo PDF generado
	}
}
