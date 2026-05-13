package WebAplicacionesDesarrollo.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MateriaDTO {
    // Añadimos los atributos básicos de una materia
    private Long id;
    private String nombre;
    private boolean eliminada;


}