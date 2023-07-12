package org.nav.springcloud.msvc.usuarios.msvcusuarios.services;

import org.nav.springcloud.msvc.usuarios.msvcusuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

  List<Usuario> listar();
  Optional<Usuario> porId(Long id);
  Usuario guardar(Usuario usuario);
  void eliminar(Long id);

  Iterable<Usuario> listarPorIds(Iterable<Long> ids);

  Optional<Usuario> buscarPorEmail(String email);
  boolean existePorEmail(String email);

}
