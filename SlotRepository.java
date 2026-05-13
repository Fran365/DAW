package WebAplicacionesDesarrollo.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotDTO {
    private Long id;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private boolean eliminado;
    private ConvocatoriaDTO convocatoria;
}