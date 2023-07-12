package org.nav.springcloud.msvc.cursos.services;

import org.nav.springcloud.msvc.cursos.models.Usuario;
import org.nav.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
  // === logica de persistencia
  List<Curso> listar();
  Optional<Curso> porId(Long id);
  Optional<Curso> porIdConUsuarios(Long id);
  Curso guardar(Curso curso);
  void eliminar(Long id);

  // === logica de negocio
  void eliminarCursoUsuarioPorId(Long id);

  // Metodos de API externa para usar en web client
  Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);
  Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);
  Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);
}
