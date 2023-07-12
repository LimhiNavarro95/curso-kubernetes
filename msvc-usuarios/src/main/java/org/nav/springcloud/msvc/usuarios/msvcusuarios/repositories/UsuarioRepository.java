package org.nav.springcloud.msvc.usuarios.msvcusuarios.repositories;

import org.nav.springcloud.msvc.usuarios.msvcusuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

  // Lo de encontrar por email se puede hacer mediante metodo abstracto o mediante query

  Optional<Usuario> findByEmail(String email);

  // el valor "?1" se reemplaza por el valor 1 de los parametros que consume
  @Query("select u from Usuario u where u.email = ?1")
  Optional<Usuario> porEmail(String email);

  boolean existsByEmail(String email);

}
