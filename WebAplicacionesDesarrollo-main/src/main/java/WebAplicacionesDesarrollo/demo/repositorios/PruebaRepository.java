package WebAplicacionesDesarrollo.demo.repositorios;

import WebAplicacionesDesarrollo.demo.entidades.Prueba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PruebaRepository extends JpaRepository<Prueba, Long> {
    // Método para buscar pruebas por el ID del slot (para el GET /pruebas?idSlot=X)
    List<Prueba> findBySlot_Id(Long idSlot);

    // Método para buscar pruebas por el ID de la convocatoria (para el GET /pruebas?idConvocatoria=Y)
    List<Prueba> findBySlot_Convocatoria_IdConvocatoria(Long idConvocatoria);

    // Método para buscar pruebas que no hayan sido eliminadas (para el borrado lógico)
    List<Prueba> findByEliminadaFalse();
}
