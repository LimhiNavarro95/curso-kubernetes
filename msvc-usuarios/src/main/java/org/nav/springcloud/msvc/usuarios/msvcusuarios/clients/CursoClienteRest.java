package org.nav.springcloud.msvc.usuarios.msvcusuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

// msvc.cursos.url -> variable de ambiente que esta en el properties
@FeignClient(name="msvc-cursos", url = "${msvc.cursos.url}")
public interface CursoClienteRest {

  @DeleteMapping("/eliminar-curso-usuario/{id}")
  void eliminarCursoUsuarioPorId(@PathVariable Long id);

}
