package com.example.lab8_gtics_20204205.Controller;

import lombok.RequiredArgsConstructor;
import  com.example.lab8_gtics_20204205.repository.MonitoreoClimaticoRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/clima")
@RequiredArgsConstructor
public class ClimaController {

    private final RestTemplate restTemplate;
    private final MonitoreoClimaticoRepository MonitoreoClimaticoRepository;

    private final String API_KEY = "88e12060abad41ab97212738250906";
    private final String BASE_URL = "https://api.weatherapi.com/v1";

    @GetMapping("/actual/{ciudad}")
    public ResponseEntity<?> obtenerClimaActual(@PathVariable String ciudad) {
        String url = BASE_URL + "/current.json?key=" + API_KEY + "&q=" + ciudad;
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode data = response.getBody();
            Map<String, Object> resultado = Map.of(
                    "temperatura", data.at("/current/temp_c").asDouble(),
                    "condicion", data.at("/current/condition/text").asText(),
                    "sensacion", data.at("/current/feelslike_c").asDouble(),
                    "humedad", data.at("/current/humidity").asInt()
            );
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al consultar API");
        }
    }

    @GetMapping("/pronostico/{ciudad}")
    public ResponseEntity<?> obtenerPronosticoPorHora(@PathVariable String ciudad) {
        String url = BASE_URL + "/forecast.json?key=" + API_KEY + "&q=" + ciudad + "&days=1";
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Map<String, Object>> lista = new ArrayList<>();
            JsonNode horas = response.getBody().at("/forecast/forecastday/0/hour");

            for (JsonNode hora : horas) {
                lista.add(Map.of(
                        "hora", hora.get("time").asText(),
                        "temperatura", hora.get("temp_c").asDouble(),
                        "condicion", hora.at("/condition/text").asText()
                ));
            }
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al consultar API");
        }
    }

    @PostMapping("/monitoreo")
    public ResponseEntity<?> registrarMonitoreo(@RequestBody MonitoreoClimatico monitoreo) {
        repository.save(monitoreo);
        return ResponseEntity.ok("Monitoreo guardado");
    }
}
