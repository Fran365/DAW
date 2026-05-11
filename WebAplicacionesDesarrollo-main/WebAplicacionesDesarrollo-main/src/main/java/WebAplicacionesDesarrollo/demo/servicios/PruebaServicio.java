package WebAplicacionesDesarrollo.demo.servicios;

import WebAplicacionesDesarrollo.demo.dtos.PruebaDTO;
import WebAplicacionesDesarrollo.demo.entidades.Prueba;
import WebAplicacionesDesarrollo.demo.repositorios.PruebaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PruebaServicio {

    private final PruebaRepository pruebaRepository;

    @Autowired
    public PruebaServicio(PruebaRepository pruebaRepository) {
        this.pruebaRepository = pruebaRepository;
    }

    // GET: Devuelve una lista de DTOs
    public List<PruebaDTO> obtenerTodas() {
        List<Prueba> pruebas = pruebaRepository.findAll();
        return pruebas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // GET by ID: Devuelve un DTO si existe
    public Optional<PruebaDTO> obtenerPorId(Long id) {
        return pruebaRepository.findById(id)
                .map(this::convertirADTO);
    }

    // POST/PUT: Recibe un DTO, lo convierte a Entidad, lo guarda, y devuelve el nuevo DTO
    public PruebaDTO guardarPrueba(PruebaDTO pruebaDTO) {
        Prueba prueba = convertirAEntidad(pruebaDTO);
        Prueba pruebaGuardada = pruebaRepository.save(prueba);
        return convertirADTO(pruebaGuardada);
    }

    // DELETE
    public void eliminarPrueba(Long id) {
        pruebaRepository.deleteById(id);
    }

    // --- MÉTODOS PRIVADOS DE MAPEO ---
    // (Asegúrate de mapear aquí todos los atributos que tenga tu clase Prueba)

    private PruebaDTO convertirADTO(Prueba prueba) {
        PruebaDTO dto = new PruebaDTO();
        dto.setId(prueba.getId());
        // dto.setNombre(prueba.getNombre()); // Mapea el resto de atributos
        return dto;
    }

    private Prueba convertirAEntidad(PruebaDTO dto) {
        Prueba prueba = new Prueba();
        prueba.setId(dto.getId());
        // prueba.setNombre(dto.getNombre()); // Mapea el resto de atributos
        return prueba;
    }
}