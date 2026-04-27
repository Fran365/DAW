package es.uma.informatica.daw.practicapruebas;

import es.uma.informatica.daw.practicapruebas.dtos.CitaDTO;
import es.uma.informatica.daw.practicapruebas.entidades.Cita;
import es.uma.informatica.daw.practicapruebas.repositorios.RepositorioCitas;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestRestTemplate
@DisplayName("En el servicio de citas")
class PracticaPruebasApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Autowired
    private RepositorioCitas repositorioCitas;

    private String url(String rutaYConsulta) {
        return "http://localhost:" + port + rutaYConsulta;
    }

    @Test
    @DisplayName("al buscar por fecha se encuentran las citas de ese día")
    void buscarPorFecha() {
        // Guarda una cita para el 30 de abril a las 10:00am de 1 hora
        Cita cita = new Cita();
        cita.setCliente("Juan");
        cita.setInicio(LocalDateTime.parse("2026-04-30T10:00:00"));
        cita.setDuracion(60);
        repositorioCitas.save(cita);

        // Consulta las citas del 30 de abril
        ResponseEntity<CitaDTO[]> res = restTemplate.getForEntity(
            url("/citas?fecha=2026-04-30"),
            CitaDTO[].class
        );

        // Comprueba que hay una
        assertThat(res.getBody()).hasSize(1);
    }

}
