package com.example.serviciodisponibilidad.repository;

import com.example.serviciodisponibilidad.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {
    List<Habitacion> findByHotel_IdHotel(Integer idHotel);
}
