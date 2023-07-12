package org.nav.springcloud.msvc.cursos.controller;

import feign.FeignException;
import jakarta.validation.Valid;
import org.nav.springcloud.msvc.cursos.models.Usuario;
import org.nav.springcloud.msvc.cursos.models.entity.Curso;
import org.nav.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
//@RequestMapping("/v1/cursos")
public class CursoController {

  @Autowired
  private CursoService cursoService;

  @GetMapping("/listar")
  public ResponseEntity<?> listar() {
    return ResponseEntity.ok(cursoService.listar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> detalle(@PathVariable Long id) {

    Optional<Curso> optionalCurso =  cursoService.porIdConUsuarios(id); //cursoService.porId(id);

    if (optionalCurso.isPresent()){
      return ResponseEntity.ok(optionalCurso.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/")
  public ResponseEntity<?> registrarCurso(@Valid @RequestBody Curso curso, BindingResult bindingResult){

    if (bindingResult.hasErrors()){
      return validateBody(bindingResult);
    }

    Curso cursoDb = cursoService.guardar(curso);

    return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> editarCurso(@Valid @RequestBody Curso curso, BindingResult bindingResult ,@PathVariable Long id){

    if (bindingResult.hasErrors()){
      return validateBody(bindingResult);
    }

    Optional<Curso> optionalCurso = cursoService.porId(id);

    if (optionalCurso.isPresent()) {
      Curso cursoDb = optionalCurso.get();
      cursoDb.setNombre(curso.getNombre());
      //se hace el guardado
      return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoDb));
    } else {
      return ResponseEntity.notFound().build();
    }

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> borrarCurso(@PathVariable Long id){

    Optional<Curso> optionalCurso = cursoService.porId(id);
    if (optionalCurso.isPresent()) {
      cursoService.eliminar(id);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.noContent().build();
    }

  }

  @PutMapping("/asignar-usuario/{cursoId}")
  public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
    Optional<Usuario> optionalUsuario;
    try {
      optionalUsuario = cursoService.asignarUsuario(usuario, cursoId);
    } catch (FeignException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          //.body(Collections.singletonMap("Mensaje", "Status " + e.status() + " No existe el usuario por el id o error en la comunicacion: " + e.getMessage()));
          .body(handleFeignException(e, "No existe el usuario por el id o error en la comunicacion"));
    }
    if (optionalUsuario.isPresent()){
      return ResponseEntity.status(HttpStatus.CREATED).body(optionalUsuario.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/crear-usuario/{cursoId}")
  public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
    Optional<Usuario> optionalUsuario;
    try {
      optionalUsuario = cursoService.crearUsuario(usuario, cursoId);
    } catch (FeignException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          //.body(Collections.singletonMap("Mensaje", "Status " + e.status() + " No se pudo crear el usuario o error en la comunicacion: " + e.getMessage()));
          .body(handleFeignException(e, "No se pudo crear el usuario o error en la comunicacion"));
    }
    if (optionalUsuario.isPresent()){
      return ResponseEntity.status(HttpStatus.CREATED).body(optionalUsuario.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/eliminar-usuario/{cursoId}")
  public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
    Optional<Usuario> optionalUsuario;
    try {
      optionalUsuario = cursoService.eliminarUsuario(usuario, cursoId);
    } catch (FeignException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          //.body(Collections.singletonMap("Mensaje", "Status " + e.status() + " No existe el usuario por el id o error en la comunicacion: " + e.getMessage()));
          .body(handleFeignException(e, "No existe el usuario por el id o error en la comunicacion"));
    }
    if (optionalUsuario.isPresent()){
      return ResponseEntity.status(HttpStatus.OK).body(optionalUsuario.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/eliminar-curso-usuario/{id}")
  public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id){
    cursoService.eliminarCursoUsuarioPorId(id);
    return ResponseEntity.noContent().build();
  }

  // === utileria
  private static ResponseEntity<Map<String, String>> validateBody(BindingResult bindingResult) {
    Map<String, String> errors = new HashMap<>();
    bindingResult.getFieldErrors().forEach(err -> {
      errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
    });
    //se responde el mapa en un pojo
    return ResponseEntity.badRequest().body(errors);
  }

  private static Map<String, String> handleFeignException(FeignException e, String cause){
    return Map.of(
        "Status", String.valueOf(e.status()),
        "Cause", cause,
        "Message", e.getMessage()
    );
  }

}
