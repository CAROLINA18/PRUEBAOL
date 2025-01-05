package com.pruebaOL.demo.dto;



public class ComercianteDTO {
    private Long idComerciante;
    private String nombreRazonSocial;
    private String departamento;
    private String municipio;
    private String telefono;
    private String correoElectronico;
    private String fechaRegistro;
    private Long estado;
    private String totalActivos;
    private String empleados;

    // Getters y Setters
    public Long getIdComerciante() {
        return idComerciante;
    }

    public void setIdComerciante(Long idComerciante) {
        this.idComerciante = idComerciante;
    }

    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

	public String getEmpleados() {
		return empleados;
	}

	public void setEmpleados(String empleados) {
		this.empleados = empleados;
	}

	public String getTotalActivos() {
		return totalActivos;
	}

	public void setTotalActivos(String totalActivos) {
		this.totalActivos = totalActivos;
	}
		

}

