package com.example.serviciodisponibilidad.listener;

import com.example.serviciodisponibilidad.entity.Disponibilidad;
import com.example.serviciodisponibilidad.service.DisponibilidadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQListener.class);

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private ObjectMapper objectMapper; // Para deserializar los mensajes JSON

    @RabbitListener(queues = "disponibilidadQueue")
    public void handleMessage(String message) {
        log.info("Mensaje recibido desde RabbitMQ: {}", message);

        try {
            // Convertir el mensaje JSON en un objeto Disponibilidad
            Disponibilidad disponibilidad = objectMapper.readValue(message, Disponibilidad.class);

            // Registrar nueva disponibilidad
            disponibilidadService.registrarDisponibilidad(disponibilidad);
            log.info("Nueva disponibilidad registrada: {}", disponibilidad);
        } catch (Exception e) {
            log.error("Error al procesar el mensaje de disponibilidad: {}", message, e);
        }
    }
}
