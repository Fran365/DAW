package WebAplicacionesDesarrollo.demo.dtos;

import WebAplicacionesDesarrollo.demo.entidades.Slot;

public class SlotMapper {

    public static SlotDTO toDTO(Slot entidad) {
        if (entidad == null) return null;

        return new SlotDTO(
                entidad.getId(),
                entidad.getInicio(),
                entidad.getFin(),
                entidad.isEliminado(),
                ConvocatoriaMapper.toDTO(entidad.getConvocatoria())
        );
    }

    public static Slot toEntity(SlotNuevoDTO dto) {
        if (dto == null) return null;

        Slot entidad = new Slot();
        entidad.setInicio(dto.getInicio());
        entidad.setFin(dto.getFin());
        entidad.setEliminado(dto.isEliminado());
        return entidad;
    }
}