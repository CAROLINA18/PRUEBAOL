package com.pruebaOL.demo.model;


import jakarta.persistence.*;

@Entity
@Table(name = "ESTADO")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_estado")
    @SequenceGenerator(name = "seq_estado", sequenceName = "SEQ_ESTADO", allocationSize = 1)
    @Column(name = "ID_ESTADO")
    private Long idEstado;

    @Column(name = "DESCRIPCION", nullable = false, unique = true, length = 20)
    private String descripcion;

    // Getters y Setters

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

