package WebAplicacionesDesarrollo.demo.controladores;

import WebAplicacionesDesarrollo.demo.dtos.SlotDTO;
import WebAplicacionesDesarrollo.demo.dtos.SlotNuevoDTO;
import WebAplicacionesDesarrollo.demo.excepcion.noEncontrada;
import WebAplicacionesDesarrollo.demo.servicios.SlotServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/slots")
public class SlotsControlador {

    private final SlotServicio servicio;

    public SlotsControlador(SlotServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public List<SlotDTO> getSlots(@RequestParam(required = false) Long idConvocatoria) {
        return servicio.listarSlots(idConvocatoria);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public ResponseEntity<SlotDTO> getSlot(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.obtenerPorId(id));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public ResponseEntity<SlotDTO> postSlot(@RequestBody SlotNuevoDTO dto, UriComponentsBuilder uriBuilder) {
        SlotDTO creado = servicio.crearSlot(dto);
        URI location = uriBuilder.path("/slots/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSlot(@PathVariable Long id) {
        servicio.eliminarSlot(id);
    }

    @ExceptionHandler(noEncontrada.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void manejarNoEncontrada() {}
}