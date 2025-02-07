package com.example.serviciodisponibilidad.controller;

import com.example.serviciodisponibilidad.dto.DisponibilidadRequest;
import com.example.serviciodisponibilidad.service.DisponibilidadService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/disponibilidad/rabbitmq")
public class RabbitMQController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DisponibilidadService disponibilidadService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
        @RequestParam(required = false, defaultValue = "disponibilidadExchange") String exchange,
        @RequestParam(required = false, defaultValue = "disponibilidadRoutingKey") String routingKey,
        @RequestBody String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return ResponseEntity.ok("Mensaje enviado a RabbitMQ");
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
}