package org.sazonpt.service;

import org.sazonpt.model.Administrador;
import org.sazonpt.model.Usuario;
import org.sazonpt.repository.AdministradorRepository;
import org.sazonpt.repository.UsuarioRepository;

import java.sql.SQLException;
import java.util.List;

public class AdministradorService {
    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;
    
    public AdministradorService(AdministradorRepository administradorRepository, UsuarioRepository usuarioRepository) {
        this.administradorRepository = administradorRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    public List<Administrador> getAllAdministradores() throws SQLException {
        return administradorRepository.findAllWithFullUserData();
    }

    public Administrador getByIdUsuario(int idUsuario) throws SQLException {
        return administradorRepository.findByIdUsuario(idUsuario);
    }

    public Administrador getByCorreo(String correo) throws SQLException {
        return administradorRepository.findByCorreo(correo);
    }

    public void promoverUsuarioAAdmin(int idUsuario) throws SQLException {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("No se encontró el usuario con ID: " + idUsuario);
        }
        
        // Verificar que no sea ya administrador
        if (administradorRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("El usuario ya es administrador");
        }
        
        // Actualizar el tipo de usuario a 'admin' si no lo es ya
        if (!"admin".equals(usuario.getTipo())) {
            usuario.setTipo("admin");
            usuarioRepository.update(usuario);
        }
        
        // Crear el registro de administrador
        Administrador administrador = new Administrador(idUsuario);
        administradorRepository.save(administrador);
    }

    public void crearAdministrador(Usuario usuarioData) throws SQLException {
        // Validaciones de negocio
        if (usuarioData.getCorreo() == null || usuarioData.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        
        if (usuarioData.getNombre() == null || usuarioData.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        if (usuarioData.getContrasena() == null || usuarioData.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        
        // Verificar si el correo ya existe
        Usuario usuarioExistente = usuarioRepository.findByCorreo(usuarioData.getCorreo());
        if (usuarioExistente != null) {
            throw new IllegalArgumentException("Ya existe un usuario con este correo");
        }
        
        // Establecer tipo como admin
        usuarioData.setTipo("admin");
        
        // Crear el usuario primero
        usuarioRepository.save(usuarioData);
        
        // Obtener el usuario recién creado para obtener su ID
        Usuario usuarioCreado = usuarioRepository.findByCorreo(usuarioData.getCorreo());
        if (usuarioCreado == null) {
            throw new SQLException("Error al crear el usuario administrador");
        }
        
        // Crear el registro de administrador
        Administrador administrador = new Administrador(usuarioCreado.getId_usuario());
        administradorRepository.save(administrador);
    }

    public void revocarAdministrador(int idUsuario) throws SQLException {
        // Verificar que el administrador existe
        Administrador administrador = administradorRepository.findByIdUsuario(idUsuario);
        if (administrador == null) {
            throw new IllegalArgumentException("No se encontró el administrador con ID: " + idUsuario);
        }
        
        // Eliminar el registro de administrador
        boolean deleted = administradorRepository.delete(idUsuario);
        if (!deleted) {
            throw new SQLException("Error al revocar permisos de administrador");
        }
        
        // Cambiar el tipo de usuario a 'usuario' regular
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario);
        if (usuario != null) {
            usuario.setTipo("usuario");
            usuarioRepository.update(usuario);
        }
    }

    public Administrador autenticarAdministrador(String correo, String contrasena) throws SQLException {
        if (correo == null || correo.trim().isEmpty() || 
            contrasena == null || contrasena.trim().isEmpty()) {
            return null;
        }
        
        Administrador administrador = administradorRepository.findByCorreo(correo);
        if (administrador != null && 
            administrador.getUsuario() != null && 
            administrador.getUsuario().getContrasena().equals(contrasena)) {
            return administrador;
        }
        
        return null;
    }

    public boolean esAdministrador(int idUsuario) throws SQLException {
        return administradorRepository.existsById(idUsuario);
    }

    public void actualizarDatosAdministrador(int idUsuario, Usuario usuarioData) throws SQLException {
        // Verificar que es administrador
        Administrador administrador = administradorRepository.findByIdUsuario(idUsuario);
        if (administrador == null) {
            throw new IllegalArgumentException("No se encontró el administrador con ID: " + idUsuario);
        }
        
        // Verificar si el correo ya existe en otro usuario
        if (usuarioData.getCorreo() != null) {
            Usuario usuarioConCorreo = usuarioRepository.findByCorreo(usuarioData.getCorreo());
            if (usuarioConCorreo != null && !usuarioConCorreo.getId_usuario().equals(idUsuario)) {
                throw new IllegalArgumentException("Ya existe otro usuario con este correo");
            }
        }
        
        // Asegurar que mantenga el tipo 'admin'
        usuarioData.setId_usuario(idUsuario);
        usuarioData.setTipo("admin");
        
        // Actualizar los datos del usuario
        usuarioRepository.update(usuarioData);
    }
}
