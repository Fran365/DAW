package WebAplicacionesDesarrollo.demo.dtos;

import WebAplicacionesDesarrollo.demo.entidades.Slot;

public class SlotMapper {
    public static SlotDTO toDTO(Slot slot) {
        if (slot == null) {
            return null;
        }

        SlotDTO dto = new SlotDTO();
        dto.setId(slot.getId());
        dto.setInicio(slot.getInicio());
        dto.setFin(slot.getFin());
        dto.setEliminado(slot.isEliminado());

        if (slot.getConvocatoria() != null) {
            dto.setConvocatoria(ConvocatoriaMapper.toDTO(slot.getConvocatoria()));
        }

        return dto;
    }
}