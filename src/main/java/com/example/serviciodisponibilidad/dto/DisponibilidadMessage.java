package com.example.serviciodisponibilidad.dto;

import lombok.Data;

@Data
public class DisponibilidadMessage {
    private String operation; // "INSERT" o "UPDATE"
    private Integer idHabitacion;
    private String fechaInicio; // Fecha desde la que estará disponible
    private String fechaFin; // Fecha hasta la que estará disponible
    private Boolean estadoDisponible; // true o false
}
