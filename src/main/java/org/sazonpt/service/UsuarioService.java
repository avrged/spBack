package org.sazonpt.service;

import org.sazonpt.model.Usuario;
import org.sazonpt.repository.UsuarioRepository;
import org.sazonpt.repository.AdministradorRepository;
import org.sazonpt.repository.RestauranteroRepository;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final RestauranteroRepository restauranteroRepository;
    
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = new AdministradorRepository();
        this.restauranteroRepository = new RestauranteroRepository();
    }
    
    public List<Usuario> getAllUsuarios() throws SQLException {
        return usuarioRepository.findAll();
    }

    public Usuario getByIdUsuario(int idUsuario) throws SQLException {
        return usuarioRepository.findByIdUsuario(idUsuario);
    }

    public Usuario getByCorreo(String correo) throws SQLException {
        return usuarioRepository.findByCorreo(correo);
    }

    public Usuario createUsuario(Usuario usuario) throws SQLException {
        // Validaciones de negocio
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        
        // Verificar si el correo ya existe
        Usuario usuarioExistente = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (usuarioExistente != null) {
            throw new IllegalArgumentException("Ya existe un usuario con este correo");
        }
        
        // Establecer tipo por defecto si no se especifica
        if (usuario.getTipo() == null || usuario.getTipo().trim().isEmpty()) {
            usuario.setTipo("usuario");
        }
        
        // Normalizar el tipo
        String tipo = usuario.getTipo().toLowerCase();
        if (tipo.equals("administrador")) {
            usuario.setTipo("admin");
        }
        
        // Crear el usuario en la tabla usuario
        Usuario usuarioCreado = usuarioRepository.save(usuario);
        
        // Insertar en tabla específica según el tipo
        try {
            switch (usuarioCreado.getTipo().toLowerCase()) {
                case "admin":
                case "administrador":
                    // Insertar en tabla administrador
                    administradorRepository.insertAdministrador(usuarioCreado.getId_usuario());
                    break;
                case "restaurantero":
                    // Insertar en tabla restaurantero
                    restauranteroRepository.insertRestaurantero(usuarioCreado.getId_usuario());
                    break;
                case "usuario":
                case "cliente":
                    // Para usuarios normales no necesitamos insertar en otra tabla
                    break;
                default:
                    // Si es un tipo no reconocido, lo dejamos como usuario normal
                    usuarioCreado.setTipo("usuario");
                    usuarioRepository.update(usuarioCreado);
                    break;
            }
        } catch (SQLException e) {
            // Si hay error al insertar en la tabla específica, eliminamos el usuario creado
            usuarioRepository.delete(usuarioCreado.getId_usuario());
            throw new RuntimeException("Error al crear el registro específico del tipo de usuario: " + e.getMessage(), e);
        }
        
        return usuarioCreado;
    }

    public void updateUsuario(Usuario usuario) throws SQLException {
        if (usuario.getId_usuario() == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio para actualizar");
        }
        
        // Verificar que el usuario existe
        Usuario usuarioExistente = usuarioRepository.findByIdUsuario(usuario.getId_usuario());
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("No se encontró el usuario con ID: " + usuario.getId_usuario());
        }
        
        // Verificar si el correo ya existe en otro usuario
        if (usuario.getCorreo() != null) {
            Usuario usuarioConCorreo = usuarioRepository.findByCorreo(usuario.getCorreo());
            if (usuarioConCorreo != null && !usuarioConCorreo.getId_usuario().equals(usuario.getId_usuario())) {
                throw new IllegalArgumentException("Ya existe otro usuario con este correo");
            }
        }
        
        usuarioRepository.update(usuario);
    }

    public boolean deleteUsuario(int idUsuario) throws SQLException {
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("No se encontró el usuario con ID: " + idUsuario);
        }
        
        return usuarioRepository.delete(idUsuario);
    }

    public Usuario autenticarUsuario(String correo, String contrasena) throws SQLException {
        if (correo == null || correo.trim().isEmpty() || 
            contrasena == null || contrasena.trim().isEmpty()) {
            return null;
        }
        
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            return usuario;
        }
        
        return null;
    }
}
