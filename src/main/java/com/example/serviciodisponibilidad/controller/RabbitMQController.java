package com.example.serviciodisponibilidad.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/disponibilidad/rabbitmq")
public class RabbitMQController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
        @RequestParam(required = false, defaultValue = "disponibilidadExchange") String exchange,
        @RequestParam(required = false, defaultValue = "disponibilidadRoutingKey") String routingKey,
        @RequestBody String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return ResponseEntity.ok("Mensaje enviado a RabbitMQ");
    }

}
