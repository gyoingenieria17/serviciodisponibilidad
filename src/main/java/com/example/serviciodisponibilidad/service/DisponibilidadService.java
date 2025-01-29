package com.example.serviciodisponibilidad.service;

import com.example.serviciodisponibilidad.entity.Disponibilidad;
import com.example.serviciodisponibilidad.entity.Habitacion;
import com.example.serviciodisponibilidad.repository.DisponibilidadRepository;
import com.example.serviciodisponibilidad.repository.HabitacionRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService {

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    /**
     * Registrar disponibilidad para una habitación.
     *
     * @param disponibilidad Datos de disponibilidad.
     * @return Disponibilidad registrada.
     */
    public Disponibilidad registrarDisponibilidad(Disponibilidad disponibilidad) {
        // Validar que la habitación exista y obtener su hotel
        Habitacion habitacion = habitacionRepository.findById(disponibilidad.getHabitacion().getIdHabitacion())
            .orElseThrow(() -> new IllegalArgumentException("La habitación con ID " +
                    disponibilidad.getHabitacion().getIdHabitacion() + " no existe."));

        disponibilidad.setHabitacion(habitacion);

        // Establecer horaInicio y horaFin por defecto (todo el día)
        if (disponibilidad.getHoraInicio() == null) {
            disponibilidad.setHoraInicio(LocalTime.of(0, 0)); // 00:00
        }
        if (disponibilidad.getHoraFin() == null) {
            disponibilidad.setHoraFin(LocalTime.of(23, 59)); // 23:59
        }

        // Invalidar disponibilidades activas existentes que se solapen
        List<Disponibilidad> conflictos = disponibilidadRepository.findByHabitacionAndFecha(habitacion, disponibilidad.getFecha());
        for (Disponibilidad conflicto : conflictos) {
            if (conflicto.getEstadoActivo() && disponibilidad.getHoraInicio().isBefore(conflicto.getHoraFin()) &&
                disponibilidad.getHoraFin().isAfter(conflicto.getHoraInicio())) {
                conflicto.setEstadoActivo(false); // Marcar como inactiva
                disponibilidadRepository.save(conflicto);
            }
        }

        // Guardar la nueva disponibilidad
        return disponibilidadRepository.save(disponibilidad);
    }

    /**
     * Consultar disponibilidades por habitación y fecha.
     *
     * @param idHabitacion ID de la habitación.
     * @param fecha Fecha de consulta.
     * @return Lista de disponibilidades.
     */
    public List<Disponibilidad> consultarDisponibilidad(Integer idHabitacion, LocalDate fecha) {
        Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new IllegalArgumentException("La habitación con ID " + idHabitacion + " no existe."));
    
        // Consultar solo disponibilidades activas
        return disponibilidadRepository.findByHabitacionAndFechaAndEstadoActivo(habitacion, fecha, true);
    }

    /**
     * Consultar disponibilidades de todas las habitaciones de un hotel en una fecha específica.
     *
     * @param idHotel ID del hotel.
     * @param fecha Fecha de consulta.
     * @return Lista de disponibilidades por hotel.
     */
    public List<Disponibilidad> consultarDisponibilidadPorHotel(Integer idHotel, LocalDate fecha) {
        // Obtener habitaciones del hotel
        List<Habitacion> habitaciones = habitacionRepository.findByHotel_IdHotel(idHotel);
        if (habitaciones.isEmpty()) {
            throw new IllegalArgumentException("El hotel con ID " + idHotel + " no tiene habitaciones registradas.");
        }

        // Consultar disponibilidades activas para todas las habitaciones del hotel
        return disponibilidadRepository.findByHabitacionInAndFechaAndEstadoActivo(habitaciones, fecha, true);
    }

    /**
     * Contar habitaciones disponibles en un hotel en una fecha específica.
     *
     * @param idHotel ID del hotel.
     * @param fecha Fecha de consulta.
     * @return Número de habitaciones disponibles.
     */
    public long contarHabitacionesDisponiblesPorHotel(Integer idHotel, LocalDate fecha) {
        return disponibilidadRepository.countByHabitacion_Hotel_IdHotelAndFechaAndEstadoActivo(idHotel, fecha, true);
    }

    /**
     * Consultar todas las disponibilidades.
     *
     * @return Lista de disponibilidades.
     */
    public List<Disponibilidad> consultarTodasLasDisponibilidades() {
        List<Disponibilidad> disponibilidades = disponibilidadRepository.findAll();
        for (Disponibilidad d : disponibilidades) {
            Hibernate.initialize(d.getHabitacion().getHotel()); // Asegura que la relación Hotel se inicialice
        }
        return disponibilidades;
    }

    /**
     * Eliminar disponibilidad por ID.
     *
     * @param idDisponibilidad ID de la disponibilidad a eliminar.
     */
    public void eliminarDisponibilidad(Integer idDisponibilidad) {
        if (!disponibilidadRepository.existsById(idDisponibilidad)) {
            throw new IllegalArgumentException("Disponibilidad con ID " + idDisponibilidad + " no encontrada.");
        }
        disponibilidadRepository.deleteById(idDisponibilidad);
    }

    public List<Map<String, Object>> obtenerHotelesConHabitacionesDisponibles(Integer idMunicipio, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Object[]> resultados = disponibilidadRepository.findHotelesConHabitacionesDisponibles(idMunicipio, fechaInicio, fechaFin);
    
        return resultados.stream().map(obj -> {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("idHotel", obj[0]);
            mapa.put("nombreHotel", obj[1]);
            mapa.put("direccion", obj[2]);
            mapa.put("telefono", obj[3]);
            mapa.put("habitacionesDisponibles", obj[4]);
            return mapa;
        }).collect(Collectors.toList());
    }
}
