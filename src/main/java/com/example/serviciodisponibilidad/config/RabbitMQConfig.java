package com.example.serviciodisponibilidad.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    // Nombre del Exchange, la cola y la routing key
    public static final String EXCHANGE_NAME = "disponibilidadExchange";
    public static final String QUEUE_NAME = "disponibilidadQueue";
    public static final String ROUTING_KEY = "disponibilidadRoutingKey";

    @Bean
    public Queue queue() {
        log.info("Configurando el listener para la cola: {}", QUEUE_NAME);
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange exchange() {
        log.info("Configurando el listener para Exchange: {}", EXCHANGE_NAME);
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange){
        log.info("Configurando el listener para Routing Key: {}", ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
