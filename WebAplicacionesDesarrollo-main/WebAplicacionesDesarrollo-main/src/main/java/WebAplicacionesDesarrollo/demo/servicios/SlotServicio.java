package WebAplicacionesDesarrollo.demo.servicios;

import WebAplicacionesDesarrollo.demo.dtos.SlotDTO;
import WebAplicacionesDesarrollo.demo.dtos.SlotNuevoDTO;
import WebAplicacionesDesarrollo.demo.dtos.SlotMapper;
import WebAplicacionesDesarrollo.demo.entidades.Convocatoria;
import WebAplicacionesDesarrollo.demo.entidades.Slot;
import WebAplicacionesDesarrollo.demo.excepcion.noEncontrada;
import WebAplicacionesDesarrollo.demo.repositorios.ConvocatoriaRepository;
import WebAplicacionesDesarrollo.demo.repositorios.SlotRepository;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SlotServicio {

    private final SlotRepository slotRepo;
    private final ConvocatoriaRepository convRepo;
    private final RestTemplate restTemplate;

    public SlotServicio(SlotRepository slotRepo, ConvocatoriaRepository convRepo, RestTemplate restTemplate) {
        this.slotRepo = slotRepo;
        this.convRepo = convRepo;
        this.restTemplate = restTemplate;
    }

    public List<SlotDTO> listarSlots(Long idConvocatoria) {
        List<Slot> slots = (idConvocatoria != null) ?
                slotRepo.findByConvocatoria_IdConvocatoria(idConvocatoria) : slotRepo.findAll();
        return slots.stream().map(SlotMapper::toDTO).toList();
    }

    public SlotDTO obtenerPorId(Long id) {
        return slotRepo.findById(id).map(SlotMapper::toDTO).orElseThrow(noEncontrada::new);
    }

    public SlotDTO crearSlot(SlotNuevoDTO dto) {
        Slot slot = SlotMapper.toEntity(dto);

        Convocatoria conv = convRepo.findById(dto.getConvocatoria().getIdConvocatoria().longValue())
                .orElseThrow(noEncontrada::new);
        slot.setConvocatoria(conv);

        Slot guardado = slotRepo.save(slot);

        notificarEvento(guardado);

        return SlotMapper.toDTO(guardado);
    }

    public void eliminarSlot(Long id) {
        Slot slot = slotRepo.findById(id).orElseThrow(noEncontrada::new);
        slot.setEliminado(true); // Borrado lógico
        slotRepo.save(slot);
    }

    private void notificarEvento(Slot slot) {
        try {
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIkFETUlOSVNUUkFET1IiXSwic3ViIjoiMSIsImlhdCI6MTc3Nzc4ODc3NSwiZXhwIjoxODQwODYwNzc1fQ.QItS3nUduiC52ty2wx4MlZzTHq_N6t-QkFOg78sSdrFeWjhktrB6NCxq-CEuxIzRO9t8LItJMBK4bBXfrGzGfg";
            Map<String, String> body = Map.of("mensaje", "Nuevo slot creado para la fecha " + slot.getInicio());

            RequestEntity<Map<String, String>> request = RequestEntity
                    .post(new URI("https://mallba3.lcc.uma.es/notificaciones"))
                    .header("Authorization", "Bearer " + token)
                    .body(body);

            restTemplate.exchange(request, String.class);
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo contactar con el servicio de notificaciones.");
        }
    }
}