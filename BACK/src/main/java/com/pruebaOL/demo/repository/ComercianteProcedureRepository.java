package com.pruebaOL.demo.repository;

import com.pruebaOL.demo.dto.ComercianteDTO;
import com.pruebaOL.demo.dto.ComercianteReporteDTO;
import com.pruebaOL.demo.model.Comerciante;
import com.pruebaOL.demo.utils.ComercianteException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import oracle.jdbc.OracleTypes;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

@Repository
public class ComercianteProcedureRepository {

    private final EntityManager entityManager;
    private final DataSource dataSource;

    public ComercianteProcedureRepository(EntityManager entityManager,DataSource dataSource) {
        this.entityManager = entityManager;
        this.dataSource = dataSource;
    }

    // Consulta paginada
    public List<ComercianteDTO> consultarComerciantes(String nombre, Long municipio, String fechaRegistro, Long estado, int pagina, int tamanoPagina) {
        List<ComercianteDTO> comerciantes = new ArrayList<>();

        String callableQuery = "{ ? = call COMERCIO.consultar(?, ?, ?, ?, ?, ?) }";

        try (Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(callableQuery)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setString(2, nombre);
            callableStatement.setObject(3, municipio);
            callableStatement.setObject(4, fechaRegistro); // Fecha de registro
            callableStatement.setObject(5, estado);
            callableStatement.setInt(6, pagina);
            callableStatement.setInt(7, tamanoPagina);

            callableStatement.execute();

            try (ResultSet rs = (ResultSet) callableStatement.getObject(1)) {
                while (rs.next()) {
                    ComercianteDTO dto = new ComercianteDTO();
                    dto.setIdComerciante(rs.getLong("ID_COMERCIANTE")); // Mapear ID
                    dto.setNombreRazonSocial(rs.getString("nombre_razon_social"));
                    dto.setDepartamento(rs.getString("DEPARTAMENTO"));
                    dto.setMunicipio(rs.getString("MUNICIPIO"));
                    dto.setTelefono(rs.getString("TELEFONO"));
                    dto.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
                    dto.setFechaRegistro(rs.getString("FECHA_REGISTRO")); // Convertir a String si es necesario
                    dto.setEstado(rs.getLong("ID_ESTADO")); // Mapear estado
                    comerciantes.add(dto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función CONSULTAR en Oracle", e);
        }

        return comerciantes;
    }


    public ComercianteDTO consultarPorId(Long id) {
        try {
            // Consulta para ejecutar la función
            String query = "SELECT COMERCIO.consultar_por_id_comerciante(:p_id) FROM DUAL";

            // Ejecutar la consulta
            Object result = entityManager.createNativeQuery(query)
                    .setParameter("p_id", id)
                    .getSingleResult();
            // Mapear el resultado al DTO
            if (result != null && result != "No se encontró un comerciante con el ID proporcionado.") {
                return mapearResultado(result.toString());
            } else {
                return null; // No hay resultados
            }
        } catch (NoResultException e) {
            return null; // No se encontraron resultados
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar Comerciante por ID", e);
        }
    }

    // Método auxiliar para mapear el resultado al DTO
    private ComercianteDTO mapearResultado(String resultado) {
        String[] values = resultado.split(","); // Separar los valores por coma
        ComercianteDTO dto = new ComercianteDTO();

        // Mapear cada campo al DTO según el orden esperado
        dto.setNombreRazonSocial(values[0].trim());
        dto.setDepartamento(values[1].trim());
        dto.setMunicipio(values[2].trim());
        dto.setTelefono(values[3].trim());
        dto.setCorreoElectronico(values[4].trim());
        dto.setFechaRegistro(values[5].trim());
        dto.setTotalActivos(values[7].trim());
        dto.setEmpleados(values[8].trim());

        return dto;
    }

    // Crear
    public void crearComerciante(
            String nombreRazonSocial,
            Long departamentoId,
            Long municipioId,
            String telefono,
            String correoElectronico,
            Long estadoId
    ) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall("{call COMERCIO.crear(?, ?, ?, ?, ?, SYSDATE, ?, ?, ?)}")) {

            // Configurar parámetros de entrada
            callableStatement.setString(1, nombreRazonSocial);
            callableStatement.setLong(2, departamentoId);
            callableStatement.setLong(3, municipioId);
            callableStatement.setString(4, telefono);
            callableStatement.setString(5, correoElectronico);
            callableStatement.setLong(6, estadoId);

            // Configurar parámetros de salida
            callableStatement.registerOutParameter(7, Types.NUMERIC); // Código de error
            callableStatement.registerOutParameter(8, Types.VARCHAR); // Mensaje de error

            // Ejecutar el procedimiento
            callableStatement.execute();

            // Obtener los valores de salida
            int codigoError = callableStatement.getInt(7);
            String mensajeError = callableStatement.getString(8);

            // Manejar el resultado
            if (codigoError != 0) {
                throw new ComercianteException(codigoError, mensajeError);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar el procedimiento almacenado", e);
        }
    }

    // Actualizar
    public void actualizar(Long id, String nombre, Long departamentoId, Long municipioId, String telefono, String correo, Long estadoId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("COMERCIO.actualizar");

        query.registerStoredProcedureParameter("p_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_departamento", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_municipio", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_telefono", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_correo", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_estado", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("v_codigo_error", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("v_mensaje_error", String.class, ParameterMode.OUT);

        query.setParameter("p_id", id);
        query.setParameter("p_nombre", nombre);
        query.setParameter("p_departamento", departamentoId);
        query.setParameter("p_municipio", municipioId);
        query.setParameter("p_telefono", telefono);
        query.setParameter("p_correo", correo);
        query.setParameter("p_estado", estadoId);

        query.execute();

        Integer codigoError = (Integer) query.getOutputParameterValue("v_codigo_error");
        String mensajeError = (String) query.getOutputParameterValue("v_mensaje_error");

        if (codigoError != 0) {
            throw new RuntimeException("Error al actualizar comerciante: " + mensajeError);
        }
    }


    // Eliminar
    public void eliminar(Long id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("COMERCIO.eliminar");
        query.registerStoredProcedureParameter("p_id", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("v_codigo_error", Integer.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("v_mensaje_error", String.class, ParameterMode.OUT);

        query.setParameter("p_id", id);

        query.execute();
    }
    
    
    public List<ComercianteReporteDTO> obtenerComerciantesActivos() {
        List<ComercianteReporteDTO> lista = new ArrayList<>();
        String query = "{ ? = call obtener_comerciantes_activos }";

        try (Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            try (ResultSet rs = (ResultSet) callableStatement.getObject(1)) {
                while (rs.next()) {
                    ComercianteReporteDTO dto = new ComercianteReporteDTO();
                    dto.setNombreRazonSocial(rs.getString("nombre_razon_social"));
                    dto.setDepartamento(rs.getString("departamento"));
                    dto.setMunicipio(rs.getString("municipio"));
                    dto.setTelefono(rs.getString("telefono"));
                    dto.setCorreoElectronico(rs.getString("correo_electronico"));
                    dto.setFechaRegistro(rs.getDate("fecha_registro"));
                    dto.setEstado(rs.getString("estado"));
                    dto.setCantidadEstablecimientos(rs.getInt("cantidad_establecimientos"));
                    dto.setTotalActivos(rs.getBigDecimal("total_activos"));
                    dto.setCantidadEmpleados(rs.getInt("cantidad_empleados"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar la función obtener_comerciantes_activos", e);
        }
        return lista;
    }
}
