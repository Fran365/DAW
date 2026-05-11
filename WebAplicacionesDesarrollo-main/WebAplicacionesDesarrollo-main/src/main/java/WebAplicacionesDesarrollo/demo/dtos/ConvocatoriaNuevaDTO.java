package WebAplicacionesDesarrollo.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ConvocatoriaNuevaDTO {
    private String nombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
