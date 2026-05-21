package WebAplicacionesDesarrollo.demo;

import WebAplicacionesDesarrollo.demo.repositorios.SlotRepository;
import WebAplicacionesDesarrollo.demo.repositorios.ConvocatoriaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestRestTemplate
@DisplayName("En el servicio de slots")
class SlotsTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository; // Util si el slot depende de una convocatoria

    private String url(String rutaYConsulta) {
        return "http://localhost:" + port + rutaYConsulta;
    }

    @Test
    @DisplayName("El servicio de slots debería arrancar y responder")
    void contextLoads() {
        // Tu primer test de integración para Slots aquí
    }
}