package com.example.serviciodisponibilidad.controller;

import com.example.serviciodisponibilidad.dto.DisponibilidadRequest;
import com.example.serviciodisponibilidad.entity.Disponibilidad;
import com.example.serviciodisponibilidad.service.DisponibilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/disponibilidad")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    /**
     * Registrar disponibilidad para una habitación.
     *
     * @param disponibilidad Datos de disponibilidad.
     * @return Disponibilidad registrada.
     */
    @PostMapping
    public ResponseEntity<Disponibilidad> registrarDisponibilidad(@RequestBody Disponibilidad disponibilidad) {
        try {
            Disponibilidad registrada = disponibilidadService.registrarDisponibilidad(disponibilidad);
            return ResponseEntity.ok(registrada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Consultar disponibilidades de una habitación en una fecha específica.
     *
     * @param idHabitacion ID de la habitación.
     * @param fecha Fecha de consulta.
     * @return Lista de disponibilidades.
     */
    @GetMapping("/{idHabitacion}/fecha")
    public ResponseEntity<List<Disponibilidad>> consultarDisponibilidad(
            @PathVariable Integer idHabitacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<Disponibilidad> disponibilidades = disponibilidadService.consultarDisponibilidad(idHabitacion, fecha);
            if (disponibilidades.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 si no hay disponibilidades
            }
            return ResponseEntity.ok(disponibilidades);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Consultar disponibilidad de todas las habitaciones de un hotel en una fecha específica.
     *
     * @param idHotel ID del hotel.
     * @param fecha Fecha de consulta.
     * @return Lista de disponibilidades por hotel.
     */
    @GetMapping("/hotel/{idHotel}/fecha")
    public ResponseEntity<List<Disponibilidad>> consultarDisponibilidadPorHotel(
            @PathVariable Integer idHotel,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<Disponibilidad> disponibilidades = disponibilidadService.consultarDisponibilidadPorHotel(idHotel, fecha);
            if (disponibilidades.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 si no hay disponibilidades
            }
            return ResponseEntity.ok(disponibilidades);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Consultar la cantidad de habitaciones disponibles en un hotel en una fecha específica.
     *
     * @param idHotel ID del hotel.
     * @param fecha Fecha de consulta.
     * @return Cantidad de habitaciones disponibles.
     */
    @GetMapping("/hotel/{idHotel}/disponibles")
    public ResponseEntity<Long> contarHabitacionesDisponibles(
            @PathVariable Integer idHotel,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            long cantidadDisponibles = disponibilidadService.contarHabitacionesDisponiblesPorHotel(idHotel, fecha);
            return ResponseEntity.ok(cantidadDisponibles);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Consultar todas las disponibilidades.
     *
     * @return Lista de todas las disponibilidades.
     */
    @GetMapping
    public ResponseEntity<List<Disponibilidad>> consultarTodasLasDisponibilidades() {
        List<Disponibilidad> disponibilidades = disponibilidadService.consultarTodasLasDisponibilidades();
        if (disponibilidades.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 si no hay disponibilidades
        }
        return ResponseEntity.ok(disponibilidades);
    }

    @PostMapping("/municipio/disponibilidad-por-tipo")
    public ResponseEntity<List<Map<String, Object>>> obtenerHotelesDisponiblesPorTipo(
        @RequestBody DisponibilidadRequest request) {
        try {
            List<Object[]> resultados = disponibilidadService.findHotelesConHabitacionesDisponiblesPorTipo(
                request.getIdMunicipio(), request.getFechaInicio(), request.getFechaFin());

            if (resultados.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content si no hay disponibilidad
            }

            // Convertir resultados a una lista de mapas para una mejor legibilidad en la respuesta
            List<Map<String, Object>> hotelesDisponibles = resultados.stream().map(resultado -> {
                Map<String, Object> mapa = new HashMap<>();
                mapa.put("idHotel", resultado[0]);
                mapa.put("nombre", resultado[1]);
                mapa.put("direccion", resultado[2]);
                mapa.put("telefono", resultado[3]);
                mapa.put("tipo", resultado[4]);
                mapa.put("disponibles", resultado[5]);
                mapa.put("noDisponibles", resultado[6]);
                return mapa;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(hotelesDisponibles);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Eliminar disponibilidad por ID.
     *
     * @param idDisponibilidad ID de la disponibilidad.
     * @return Respuesta de eliminación.
     */
    @DeleteMapping("/{idDisponibilidad}")
    public ResponseEntity<String> eliminarDisponibilidad(@PathVariable Integer idDisponibilidad) {
        try {
            disponibilidadService.eliminarDisponibilidad(idDisponibilidad);
            return ResponseEntity.ok("Disponibilidad eliminada con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/municipio/disponibilidad")
    public ResponseEntity<List<Map<String, Object>>> obtenerHotelesDisponiblesPorMunicipio(
        @RequestBody DisponibilidadRequest request) {
    
        try {
            List<Map<String, Object>> hotelesDisponibles = disponibilidadService.obtenerHotelesConHabitacionesDisponibles(
                request.getIdMunicipio(), request.getFechaInicio(), request.getFechaFin());
        
        if (hotelesDisponibles.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si no hay disponibilidad
        }
        return ResponseEntity.ok(hotelesDisponibles);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
