package WebAplicacionesDesarrollo.demo.controladores;

import WebAplicacionesDesarrollo.demo.dtos.ConvocatoriaDTO;
import WebAplicacionesDesarrollo.demo.dtos.ConvocatoriaNuevaDTO;
import WebAplicacionesDesarrollo.demo.servicios.ConvocatoriaServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/convocatorias")
public class ConvocatoriaControlador {

    private ConvocatoriaServicio servicio;

    public ConvocatoriaControlador(ConvocatoriaServicio servicio) { this.servicio = servicio;}

    @GetMapping("")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public List<ConvocatoriaDTO> obtenerTodasConvocatorias(){
        return servicio.obtenerTodasConvocatorias();
    }

    @PostMapping("")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public ResponseEntity<ConvocatoriaDTO> crearConvocatoria(@RequestBody ConvocatoriaNuevaDTO dto, UriComponentsBuilder uriBuilder){
        ConvocatoriaDTO creada = servicio.crearConvocatoria(dto);
        URI location = uriBuilder.path("/convocatorias/{id}").buildAndExpand(creada.getIdConvocatoria()).toUri();
        return ResponseEntity.created(location).body(creada);

    }

    @GetMapping("/actual")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public ResponseEntity<ConvocatoriaDTO> obtenerConvocatoriaActual(){
        ConvocatoriaDTO actual = servicio.obtenerConvocatoriaActual();
        return ResponseEntity.ofNullable(actual);
    }
}
