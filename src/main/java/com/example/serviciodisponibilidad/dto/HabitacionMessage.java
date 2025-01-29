package com.example.serviciodisponibilidad.dto;

import lombok.Data;

@Data
public class HabitacionMessage {
    private Integer idHabitacion;
    private Integer idHotel;
    private String numeroHabitacion;
    private String descripcion;
    private Integer capacidad;
    private Double precioBase;
}
