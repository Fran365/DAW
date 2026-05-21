package WebAplicacionesDesarrollo.demo.servicios;

import WebAplicacionesDesarrollo.demo.dtos.SlotDTO;
import WebAplicacionesDesarrollo.demo.dtos.SlotMapper;
import WebAplicacionesDesarrollo.demo.dtos.SlotNuevoDTO;
import WebAplicacionesDesarrollo.demo.entidades.Convocatoria;
import WebAplicacionesDesarrollo.demo.entidades.Slot;
import WebAplicacionesDesarrollo.demo.excepcion.NoEncontradaException;
import WebAplicacionesDesarrollo.demo.repositorios.ConvocatoriaRepository;
import WebAplicacionesDesarrollo.demo.repositorios.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SlotServicio {

    private final SlotRepository slotRepository;
    private final ConvocatoriaRepository convocatoriaRepository;

    public SlotServicio(SlotRepository slotRepository, ConvocatoriaRepository convocatoriaRepository) {
        this.slotRepository = slotRepository;
        this.convocatoriaRepository = convocatoriaRepository;
    }

    public List<SlotDTO> obtenerSlots(Long idConvocatoria) {
        List<Slot> slots;
        if (idConvocatoria != null) {
            slots = slotRepository.findByConvocatoria_IdConvocatoria(idConvocatoria);
        } else {
            slots = slotRepository.findAll();
        }

        return slots.stream()
                .filter(slot -> !slot.isEliminado())
                .map(SlotMapper::toDTO)
                .toList();
    }

    public SlotDTO obtenerPorId(Long id) {
        return slotRepository.findById(id)
                .map(SlotMapper::toDTO)
                .orElseThrow(() -> new NoEncontradaException("Slot no encontrado"));
    }

    public SlotDTO crearSlot(SlotNuevoDTO dto) {
        if (dto.getConvocatoria() == null || dto.getConvocatoria().getIdConvocatoria() == null) {
            throw new NoEncontradaException("La convocatoria indicada no existe (faltan datos)");
        }

        Long idConv = dto.getConvocatoria().getIdConvocatoria().longValue();

        Convocatoria convocatoria = convocatoriaRepository.findById(idConv)
                .orElseThrow(() -> new NoEncontradaException("La convocatoria indicada no existe"));

        Slot slot = new Slot();
        slot.setInicio(dto.getInicio());
        slot.setFin(dto.getFin());
        slot.setEliminado(dto.isEliminado());
        slot.setConvocatoria(convocatoria);

        return SlotMapper.toDTO(slotRepository.save(slot));
    }

    public SlotDTO actualizarSlot(Long id, SlotNuevoDTO dto) {
        Slot slot = slotRepository.findById(id)
                .filter(s -> !s.isEliminado())
                .orElseThrow(() -> new NoEncontradaException("El slot no existe"));

        if (dto.getConvocatoria() == null || dto.getConvocatoria().getIdConvocatoria() == null) {
            throw new NoEncontradaException("La convocatoria indicada no existe (faltan datos)");
        }

        Long idConv = dto.getConvocatoria().getIdConvocatoria().longValue();

        Convocatoria convocatoria = convocatoriaRepository.findById(idConv)
                .orElseThrow(() -> new NoEncontradaException("La convocatoria indicada no existe"));

        slot.setInicio(dto.getInicio());
        slot.setFin(dto.getFin());
        //slot.setEliminado(dto.isEliminado());
        slot.setConvocatoria(convocatoria);

        return SlotMapper.toDTO(slotRepository.save(slot));
    }

    public boolean borrarSlot(Long id) {
        return slotRepository.findById(id).map(slot -> {
            List<Convocatoria> vigentes = convocatoriaRepository.findTopByOrderByFechaInicioDesc();
            if (!vigentes.isEmpty()) {
                Convocatoria actual = vigentes.get(0);
                if (!slot.getConvocatoria().getIdConvocatoria().equals(actual.getIdConvocatoria())) {
                    return false;
                }
            }
            slot.setEliminado(true);
            slotRepository.save(slot);
            return true;
        }).orElse(false);
    }
}