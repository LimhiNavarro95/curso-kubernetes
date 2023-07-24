package org.nav.springcloud.msvc.usuarios.msvcusuarios.controllers;

import jakarta.validation.Valid;
import org.nav.springcloud.msvc.usuarios.msvcusuarios.models.entity.Usuario;
import org.nav.springcloud.msvc.usuarios.msvcusuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
//@RequestMapping("/v1/usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  //simular un punto de quiebre
  @Autowired
  private ApplicationContext context;
  @GetMapping("/crash")
  public void crash(){
    ((ConfigurableApplicationContext) context).close();
  }

  @GetMapping("/listar")
  public List<Usuario> listar(){
    return usuarioService.listar();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> detalle(@PathVariable Long id){
    Optional<Usuario> usuarioOptional = usuarioService.porId(id);

    if (usuarioOptional.isPresent()){
      return ResponseEntity.ok(usuarioOptional.get());
    }

    return ResponseEntity.notFound().build();

  }

  @PostMapping("/")
  public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult bindingResult){

    if (bindingResult.hasErrors()){
      return validateBody(bindingResult);
    }

    if (!usuario.getEmail().isEmpty() && usuarioService.existePorEmail(usuario.getEmail())){
      return ResponseEntity.badRequest()
          .body(Collections
              .singletonMap("Message", "Ya existe un usuario con ese correo electronico!"));
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));

  }

  @PutMapping("/{id}")
  public ResponseEntity<?> editar(@Valid @PathVariable Long id, @RequestBody Usuario usuario, BindingResult bindingResult) {

    if (bindingResult.hasErrors()){
      return validateBody(bindingResult);
    }

    Optional<Usuario> optionalUsuario = usuarioService.porId(id);

    if (optionalUsuario.isPresent()){
      Usuario usuarioBD = optionalUsuario.get();

      if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioBD.getEmail()) && usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()){
        return ResponseEntity.badRequest()
            .body(Collections
                .singletonMap("Message", "Ya existe un usuario con ese correo electronico!"));
      }

      usuarioBD.setNombre(usuario.getNombre());
      usuarioBD.setEmail(usuario.getEmail());
      usuarioBD.setPassword(usuario.getPassword());
      return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioBD));
    } else {
      return ResponseEntity.notFound().build();
    }

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> borrar(@PathVariable Long id){
    Optional<Usuario> optionalUsuario = usuarioService.porId(id);
    if (optionalUsuario.isPresent()){
      usuarioService.eliminar(id);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/usuarios-por-curso")
  public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
    return ResponseEntity.ok(usuarioService.listarPorIds(ids));
  }

  // utileria
  private static ResponseEntity<Map<String, String>> validateBody(BindingResult bindingResult) {
    Map<String, String> errors = new HashMap<>();
    bindingResult.getFieldErrors().forEach(err -> {
      errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
    });
    //se responde el mapa en un pojo
    return ResponseEntity.badRequest().body(errors);
  }

}
