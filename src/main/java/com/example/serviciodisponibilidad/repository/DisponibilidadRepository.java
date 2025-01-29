package com.example.serviciodisponibilidad.repository;

import com.example.serviciodisponibilidad.entity.Disponibilidad;
import com.example.serviciodisponibilidad.entity.Habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

import java.util.List;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer> {
    List<Disponibilidad> findByHabitacionAndFecha(Habitacion habitacion, LocalDate fecha);
    List<Disponibilidad> findByHabitacionAndFechaAndEstadoDisponible(Habitacion habitacion, LocalDate fecha, Boolean estadoDisponible);
    List<Disponibilidad> findByHabitacionAndFechaAndEstadoActivo(Habitacion habitacion, LocalDate fecha, boolean estadoActivo);
    List<Disponibilidad> findByHabitacionInAndFechaAndEstadoActivo(List<Habitacion> habitaciones, LocalDate fecha, boolean estadoActivo);
    long countByHabitacion_Hotel_IdHotelAndFechaAndEstadoActivo(Integer idHotel, LocalDate fecha, boolean estadoActivo);

    @Query("SELECT h.idHotel, h.nombre, h.direccion, h.telefono, COUNT(DISTINCT hab) " +
       "FROM Habitacion hab " +
       "LEFT JOIN hab.hotel h " +
       "LEFT JOIN Disponibilidad d ON d.habitacion = hab AND d.fecha BETWEEN :fechaInicio AND :fechaFin " +
       "WHERE h.municipio.idMunicipio = :idMunicipio " +
       "AND (d.idDisponibilidad IS NULL OR (d.estadoActivo = true AND d.estadoDisponible = true)) " +
       "GROUP BY h.idHotel, h.nombre, h.direccion, h.telefono")
    List<Object[]> findHotelesConHabitacionesDisponibles(
        @Param("idMunicipio") Integer idMunicipio,
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin
    );
}
