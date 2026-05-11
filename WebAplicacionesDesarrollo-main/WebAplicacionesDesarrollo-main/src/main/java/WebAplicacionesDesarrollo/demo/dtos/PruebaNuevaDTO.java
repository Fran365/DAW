package WebAplicacionesDesarrollo.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import WebAplicacionesDesarrollo.demo.dtos.SlotDTO;
import WebAplicacionesDesarrollo.demo.dtos.MateriaDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PruebaNuevaDTO {
    private SlotDTO idSlot;
    private MateriaDTO idMateria;
}
