package com.example.serviciodisponibilidad.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DisponibilidadRequest {
    private Integer idMunicipio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;    
}
