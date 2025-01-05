package com.pruebaOL.demo.dto;

import java.math.BigDecimal;

public class EstablecimientoResponse {

    private Long idEstablecimiento;
    private String nombreEstablecimiento;
    private BigDecimal ingresos;
    private Integer numeroEmpleados;
	public Long getIdEstablecimiento() {
		return idEstablecimiento;
	}
	public void setIdEstablecimiento(Long idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
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
