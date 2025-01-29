package com.example.serviciodisponibilidad.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "municipio")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmunicipio")
    private Integer idMunicipio;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo", nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "iddepartamento", nullable = false)
    private Departamento departamento;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
