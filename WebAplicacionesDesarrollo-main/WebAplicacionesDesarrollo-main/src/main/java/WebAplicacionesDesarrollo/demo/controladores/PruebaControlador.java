package WebAplicacionesDesarrollo.demo.controladores;

import WebAplicacionesDesarrollo.demo.dtos.PruebaDTO;
import WebAplicacionesDesarrollo.demo.dtos.PruebaNuevaDTO;
import WebAplicacionesDesarrollo.demo.excepcion.noEncontrada;
import WebAplicacionesDesarrollo.demo.servicios.PruebaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pruebas")

public class PruebaControlador {
    private PruebaServicio servicio;

    public PruebaControlador(PruebaServicio servicio) { this.servicio = servicio;}

    @GetMapping("")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public ResponseEntity<List<PruebaDTO>> pruebaSlotConvocatoria(@RequestParam(required = false) Long idConvocatoria, @RequestParam(required = false) Long idSlot){
        List<PruebaDTO> lista = servicio.listarPruebas(idConvocatoria, idSlot);
        return ResponseEntity.ofNullable(lista);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public ResponseEntity<PruebaDTO> crearPrueba(@RequestBody PruebaNuevaDTO prueba, UriComponentsBuilder uriBuilder){
        PruebaDTO nueva = servicio.crearPrueba(prueba);

        URI location = uriBuilder.path("/pruebas/{id}").buildAndExpand(nueva.getId()).toUri();

        return ResponseEntity.created(location).body(nueva);
    }

    @GetMapping("/{idPrueba}")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public ResponseEntity<PruebaDTO> informacionPrueba(@PathVariable  Long idPrueba){
        PruebaDTO prueba = servicio.obtenerPruebaPorId(idPrueba);
        return ResponseEntity.ofNullable(prueba);
    }

    @PutMapping("/{idPrueba}")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    public ResponseEntity<PruebaDTO> actualizacionPrueba(@PathVariable  Long idPrueba, @RequestBody PruebaNuevaDTO dto){
        PruebaDTO actualizar =  servicio.actualizarPrueba(idPrueba, dto);
        return ResponseEntity.ofNullable(actualizar);
    }

    @DeleteMapping("/{idPrueba}")
    @PreAuthorize("hasRole('VICERRECTORADO')")
    @ResponseStatus(HttpStatus.OK)
    public void borrarPrueba(@PathVariable  Long idPrueba){
        servicio.borradoLogicoPrueba(idPrueba);
    }

    @ExceptionHandler(noEncontrada.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void noEncontrada(){
    }
}
