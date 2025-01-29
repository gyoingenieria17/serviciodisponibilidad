package com.example.serviciodisponibilidad.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "habitacion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idhabitacion")
    private Integer idHabitacion;

    @Column(name = "numero_habitacion")
    private String numeroHabitacion;

    private String descripcion;

    private Integer capacidad;

    @Column(name = "precio_base")
    private Double precioBase;

    private Boolean estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "idtipo_habitacion", nullable = false) // Asegúrate de que coincida con la FK en la tabla habitacion
    private TipoHabitacion tipoHabitacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idhotel", nullable = false) // Asegúrate de que coincide con la FK en la base de datos
    @JsonIgnoreProperties({"habitaciones"}) // Evita que se serialice la lista de habitaciones dentro del hotel
    private Hotel hotel;


    // Inicializar fechas antes de insertar
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Actualizar la fecha de actualización antes de actualizar
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
