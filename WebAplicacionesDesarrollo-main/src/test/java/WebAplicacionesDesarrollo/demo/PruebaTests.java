package WebAplicacionesDesarrollo.demo;

import WebAplicacionesDesarrollo.demo.dtos.*;
import WebAplicacionesDesarrollo.demo.entidades.Convocatoria;
import WebAplicacionesDesarrollo.demo.entidades.Materia;
import WebAplicacionesDesarrollo.demo.entidades.Prueba;
import WebAplicacionesDesarrollo.demo.entidades.Slot;
import WebAplicacionesDesarrollo.demo.repositorios.ConvocatoriaRepository;
import WebAplicacionesDesarrollo.demo.repositorios.PruebaRepository;
import WebAplicacionesDesarrollo.demo.repositorios.MateriaRepository;
import WebAplicacionesDesarrollo.demo.repositorios.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DisplayName("En el servicio de pruebas")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PruebaTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private PruebaRepository pruebaRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @Autowired
    private SlotRepository slotRepository;

    @BeforeEach
    public void limpiarBaseDeDatosGlobal() {
        pruebaRepository.deleteAll();
        slotRepository.deleteAll();
        materiaRepository.deleteAll();
        convocatoriaRepository.deleteAll();
    }

    private URI uri(String scheme, String host, int port, String ...paths) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host).port(port);
        for (String path: paths) {
            ub = ub.path(path);
        }
        return ub.build();
    }

    private RequestEntity<Void> get(String scheme, String host, int port, String path, String token) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .build();
        return peticion;
    }

    private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.delete(uri)
                .build();
        return peticion;
    }

    private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object, String token) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(object);
        return peticion;
    }

    private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    private void compruebaCampos(Prueba expected, Prueba actual) {
        assertThat(actual.getSlot().getId()).isEqualTo(expected.getSlot().getId());
        assertThat(actual.getMateria().getId()).isEqualTo(expected.getMateria().getId());
        assertThat(actual.isEliminada()).isEqualTo(expected.isEliminada());
    }

    @Nested
    @DisplayName("cuando la base de datos está vacía")
    public class BaseDatosVacia{
        //Esta es de CrearPrueba
        @Test
        @DisplayName("inserta correctamente una prueba")
        public void insertaPrueba() {

            var convocatoriaDTO = ConvocatoriaNuevaDTO.builder().nombre("Convocatoria Jubio 2026")
                    .fechaInicio(java.time.LocalDateTime.of(2026, 6, 1, 9, 0))
                    .fechaFin(java.time.LocalDateTime.of(2026, 6, 30, 21, 0))
                    .build();
            var convocatoriaBD = convocatoriaRepository.save(ConvocatoriaMapper.toEntity(convocatoriaDTO));
            Integer idConvInteger = convocatoriaBD.getIdConvocatoria().intValue();

            var slotNuevoDTO = SlotNuevoDTO.builder()
                    .inicio(java.time.LocalDateTime.of(2026, 6, 20, 10, 0))
                    .fin(java.time.LocalDateTime.of(2026, 6, 20, 12, 0))
                    .eliminado(false)
                    .build();
            Slot slotEntidad = SlotMapper.toEntity(slotNuevoDTO);
            slotEntidad.setConvocatoria(convocatoriaBD);
            var slotBD = slotRepository.save(slotEntidad);

            var materiaNuevaDTO = MateriaNuevaDTO.builder()
                    .nombre("Desarrollo de Aplicaciones Web")
                    .build();
            var materiaBD = materiaRepository.save(MateriaMapper.toEntity(materiaNuevaDTO));

            //Construir PruebaDTO
            var pruebaNuevaDTO = PruebaNuevaDTO.builder()
                    .slot(PruebaNuevaDTO.ReferenciaDTO.builder().id(slotBD.getId()).build())
                    .materia(PruebaNuevaDTO.ReferenciaDTO.builder().id(materiaBD.getId()).build())
                    .build();

            String jwtAntonio = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9"
                    + ".eyJyb2xlIjpbIlZJQ0VSUkVDVE9SQURPIl0sInN1YiI6IjIiLCJpYXQiOjE3Nzc4MzQ2MjcsImV4cCI6MTg0MDkwNjYyN30"
                    + ".r0TI0RGEIbvXkMS5NZBOmDyKveb1Pbt9DATV14xS3TUiiFNvgpI9nCPVT-J5LnQoTb00OzuZcp2UslZCkh78Eg";

            var peticion = post("http", "localhost", port, "/pruebas", pruebaNuevaDTO, jwtAntonio);
            var respuesta = restTemplate.exchange(peticion, Void.class);

            //Responde 201
            assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

            // Cabecera aputna bien
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .startsWith("http://localhost:" + port + "/pruebas");

            //Guardado correcto
            List<Prueba> pruebasBD = pruebaRepository.findAll();
            assertThat(pruebasBD).hasSize(1);
            //ID de la URL coincida con el ID en BD
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .endsWith("/" + pruebasBD.get(0).getId());
            //Datos en BD consistentes
            compruebaCampos(pruebasBD.get(0), pruebasBD.get(0));

        }

        @Test
        @DisplayName("inserta una prueba con una autoriazcion incorrecta")
        public void insertaPruebaSinAutorizacion() {

            var convocatoriaDTO = ConvocatoriaNuevaDTO.builder().nombre("Convocatoria Julio 2026")
                    .fechaInicio(java.time.LocalDateTime.of(2026, 6, 1, 9, 0))
                    .fechaFin(java.time.LocalDateTime.of(2026, 6, 30, 21, 0))
                    .build();
            var convocatoriaBD = convocatoriaRepository.save(ConvocatoriaMapper.toEntity(convocatoriaDTO));
            Integer idConvInteger = convocatoriaBD.getIdConvocatoria().intValue();

            var slotNuevoDTO = SlotNuevoDTO.builder()
                    .inicio(java.time.LocalDateTime.of(2026, 6, 20, 10, 0))
                    .fin(java.time.LocalDateTime.of(2026, 6, 20, 12, 0))
                    .eliminado(false)
                    .build();
            Slot slotEntidad = SlotMapper.toEntity(slotNuevoDTO);
            slotEntidad.setConvocatoria(convocatoriaBD);
            var slotBD = slotRepository.save(slotEntidad);

            var materiaNuevaDTO = MateriaNuevaDTO.builder()
                    .nombre("Desarrollo de Aplicaciones Web")
                    .build();
            var materiaBD = materiaRepository.save(MateriaMapper.toEntity(materiaNuevaDTO));

            //Construir PruebaDTO
            var pruebaNuevaDTO = PruebaNuevaDTO.builder()
                    .slot(PruebaNuevaDTO.ReferenciaDTO.builder().id(slotBD.getId()).build())
                    .materia(PruebaNuevaDTO.ReferenciaDTO.builder().id(materiaBD.getId()).build())
                    .build();

            String jwtAntonio = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9"+
                    ".eyJyb2xlIjpbXSwic3ViIjoiMyIsImlhdCI6MTc3NzgzNDgzMiwiZXhwIjoxODQwOTA2ODMyfQ"+
                    ".TgwNBRM_P2QLk8YVbiDIb7P0kOY_YHjyU7v9wkLuc_4aBBW7ZCTdkCMHFlN3wxN_x69WF8inKiZipHTVaDzmmg";

            var peticion = post("http", "localhost", port, "/pruebas", pruebaNuevaDTO, jwtAntonio);
            var respuesta = restTemplate.exchange(peticion, Void.class);

            //Responde 201
            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);

            //Guardado correcto
            List<Prueba> pruebasBD = pruebaRepository.findAll();
            assertThat(pruebasBD).isEmpty();
        }
    }

    @Nested
    @DisplayName("cuando la base de datos tiene informacion")
    public class BaseDatosAlgo{

        private Long slotIdPrueba;

        @BeforeEach
        public void llenarBD(){
            var convocatoria = new Convocatoria();
            convocatoria.setNombre("Convocatoria Ordinaria 2026");
            convocatoria.setFechaInicio(java.time.LocalDateTime.now());
            convocatoria.setFechaFin(java.time.LocalDateTime.now().plusMonths(1));
            convocatoria = convocatoriaRepository.save(convocatoria);

            var materia = new Materia();
            materia.setNombre("Matemáticas");
            materia = materiaRepository.save(materia);

            var slot = new Slot();
            slot.setInicio(java.time.LocalDateTime.now());
            slot.setFin(java.time.LocalDateTime.now().plusHours(2));
            slot.setConvocatoria(convocatoria);
            slot = slotRepository.save(slot);

            this.slotIdPrueba = slot.getId();

            var prueba = new Prueba();
            prueba.setMateria(materia);
            prueba.setSlot(slot);
            prueba.setEliminada(false);

            pruebaRepository.save(prueba);
        }

        //Para pruebaSlotConvocatoria
        @Test
        @DisplayName("Devuelve una lista de todas las pruebas")
        public void devuelvePruebasDeUnSlot() {
            String jwtAntonio = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9"
                    + ".eyJyb2xlIjpbIlZJQ0VSUkVDVE9SQURPIl0sInN1YiI6IjIiLCJpYXQiOjE3Nzc4MzQ2MjcsImV4cCI6MTg0MDkwNjYyN30"
                    + ".r0TI0RGEIbvXkMS5NZBOmDyKveb1Pbt9DATV14xS3TUiiFNvgpI9nCPVT-J5LnQoTb00OzuZcp2UslZCkh78Eg";

            var peticion = get("http", "localhost", port, "/pruebas", jwtAntonio);
            var respuesta = restTemplate.exchange(peticion, List.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).isNotEmpty();
        }
    }
}