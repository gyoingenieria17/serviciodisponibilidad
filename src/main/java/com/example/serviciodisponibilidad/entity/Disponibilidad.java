package com.example.serviciodisponibilidad.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "disponibilidad")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDisponibilidad;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(nullable = false)
    private Boolean estadoDisponible;

    @Column(nullable = false)
    private Boolean estadoActivo = true; // Campo para identificar si la disponibilidad está activa

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idhabitacion", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Habitacion habitacion;


    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
