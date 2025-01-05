package com.pruebaOL.demo.dto;

import java.math.BigDecimal;

public class EstablecimientoRequest {

    private Long comercianteId; // ID del comerciante asociado
    private String nombreEstablecimiento;
    private BigDecimal ingresos;
    private Integer numeroEmpleados;
	public Long getComercianteId() {
		return comercianteId;
	}
	public void setComercianteId(Long comercianteId) {
		this.comercianteId = comercianteId;
	}
	public String getNombreEstablecimiento() {
		return nombreEstablecimiento;
	}
	public void setNombreEstablecimiento(String nombreEstablecimiento) {
		this.nombreEstablecimiento = nombreEstablecimiento;
	}
	public BigDecimal getIngresos() {
		return ingresos;
	}
	public void setIngresos(BigDecimal ingresos) {
		this.ingresos = ingresos;
	}
	public Integer getNumeroEmpleados() {
		return numeroEmpleados;
	}
	public void setNumeroEmpleados(Integer numeroEmpleados) {
		this.numeroEmpleados = numeroEmpleados;
	}

    
}
