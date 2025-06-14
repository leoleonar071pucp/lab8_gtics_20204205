package com.example.lab8_gtics_20204205.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoreoClimatico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ciudad;
    private LocalDate fecha;
    private Double tempPromedio;
    private String condicionFrecuente;
    private Double tempMax;
    private Double tempMin;
}
