package com.example.serviciodisponibilidad.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tipo_habitacion")
public class TipoHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_habitacion") // Asegúrate de que coincida con el nombre exacto de la columna en la base de datos
    private Integer idTipoHabitacion;

    private String nombre;

    private String descripcion;

    private Boolean estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
