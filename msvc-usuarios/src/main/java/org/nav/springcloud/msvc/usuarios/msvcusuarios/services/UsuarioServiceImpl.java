package org.nav.springcloud.msvc.usuarios.msvcusuarios.services;

import org.nav.springcloud.msvc.usuarios.msvcusuarios.clients.CursoClienteRest;
import org.nav.springcloud.msvc.usuarios.msvcusuarios.models.entity.Usuario;
import org.nav.springcloud.msvc.usuarios.msvcusuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

  @Autowired // -> se inyecta en el constructor
  private UsuarioRepository usuarioRepository;
  @Autowired
  private CursoClienteRest cursoClienteRest;

  @Override
  @Transactional(readOnly = true)
  public List<Usuario> listar() {
    return (List<Usuario>) usuarioRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Usuario> porId(Long id) {
    return usuarioRepository.findById(id);
  }

  @Override
  @Transactional
  public Usuario guardar(Usuario usuario) {
    return usuarioRepository.save(usuario);
  }

  @Override
  @Transactional
  public void eliminar(Long id) {
    usuarioRepository.deleteById(id);
    cursoClienteRest.eliminarCursoUsuarioPorId(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Usuario> listarPorIds(Iterable<Long> ids) {
    return (List<Usuario>) usuarioRepository.findAllById(ids);
  }

  @Override
  public Optional<Usuario> buscarPorEmail(String email) {
    return usuarioRepository.porEmail(email);
  }

  @Override
  public boolean existePorEmail(String email) {
    return usuarioRepository.existsByEmail(email);
  }

}
