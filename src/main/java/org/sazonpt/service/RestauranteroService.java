package org.sazonpt.service;

import org.sazonpt.model.Restaurantero;
import org.sazonpt.model.Usuario;
import org.sazonpt.repository.RestauranteroRepository;
import org.sazonpt.repository.UsuarioRepository;

import java.sql.SQLException;
import java.util.List;

public class RestauranteroService {
    private final RestauranteroRepository restauranteroRepository;
    private final UsuarioRepository usuarioRepository;
    
    public RestauranteroService(RestauranteroRepository restauranteroRepository, UsuarioRepository usuarioRepository) {
        this.restauranteroRepository = restauranteroRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    public List<Restaurantero> getAllRestauranteros() throws SQLException {
        return restauranteroRepository.findAllWithFullUserData();
    }

    public Restaurantero getByIdUsuario(int idUsuario) throws SQLException {
        return restauranteroRepository.findByIdUsuario(idUsuario);
    }

    public Restaurantero getByCorreo(String correo) throws SQLException {
        return restauranteroRepository.findByCorreo(correo);
    }

    public void promoverUsuarioARestaurantero(int idUsuario) throws SQLException {
        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("No se encontró el usuario con ID: " + idUsuario);
        }
        
        // Verificar que no sea ya restaurantero
        if (restauranteroRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("El usuario ya es restaurantero");
        }
        
        // Actualizar el tipo de usuario a 'restaurantero' si no lo es ya
        if (!"restaurantero".equals(usuario.getTipo())) {
            usuario.setTipo("restaurantero");
            usuarioRepository.update(usuario);
        }
        
        // Crear el registro de restaurantero
        Restaurantero restaurantero = new Restaurantero(idUsuario);
        restauranteroRepository.save(restaurantero);
    }

    public void crearRestaurantero(Usuario usuarioData) throws SQLException {
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
        
        // Establecer tipo como restaurantero
        usuarioData.setTipo("restaurantero");
        
        // Crear el usuario primero
        usuarioRepository.save(usuarioData);
        
        // Obtener el usuario recién creado para obtener su ID
        Usuario usuarioCreado = usuarioRepository.findByCorreo(usuarioData.getCorreo());
        if (usuarioCreado == null) {
            throw new SQLException("Error al crear el usuario restaurantero");
        }
        
        // Crear el registro de restaurantero
        Restaurantero restaurantero = new Restaurantero(usuarioCreado.getId_usuario());
        restauranteroRepository.save(restaurantero);
    }

    public void revocarRestaurantero(int idUsuario) throws SQLException {
        // Verificar que el restaurantero existe
        Restaurantero restaurantero = restauranteroRepository.findByIdUsuario(idUsuario);
        if (restaurantero == null) {
            throw new IllegalArgumentException("No se encontró el restaurantero con ID: " + idUsuario);
        }
        
        // Eliminar el registro de restaurantero
        boolean deleted = restauranteroRepository.delete(idUsuario);
        if (!deleted) {
            throw new SQLException("Error al revocar permisos de restaurantero");
        }
        
        // Cambiar el tipo de usuario a 'usuario' regular
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario);
        if (usuario != null) {
            usuario.setTipo("usuario");
            usuarioRepository.update(usuario);
        }
    }

    public Restaurantero autenticarRestaurantero(String correo, String contrasena) throws SQLException {
        if (correo == null || correo.trim().isEmpty() || 
            contrasena == null || contrasena.trim().isEmpty()) {
            return null;
        }
        
        Restaurantero restaurantero = restauranteroRepository.findByCorreo(correo);
        if (restaurantero != null && 
            restaurantero.getUsuario() != null && 
            restaurantero.getUsuario().getContrasena().equals(contrasena)) {
            return restaurantero;
        }
        
        return null;
    }

    public boolean esRestaurantero(int idUsuario) throws SQLException {
        return restauranteroRepository.existsById(idUsuario);
    }

    public void actualizarDatosRestaurantero(int idUsuario, Usuario usuarioData) throws SQLException {
        // Verificar que es restaurantero
        Restaurantero restaurantero = restauranteroRepository.findByIdUsuario(idUsuario);
        if (restaurantero == null) {
            throw new IllegalArgumentException("No se encontró el restaurantero con ID: " + idUsuario);
        }
        
        // Verificar si el correo ya existe en otro usuario
        if (usuarioData.getCorreo() != null) {
            Usuario usuarioConCorreo = usuarioRepository.findByCorreo(usuarioData.getCorreo());
            if (usuarioConCorreo != null && !usuarioConCorreo.getId_usuario().equals(idUsuario)) {
                throw new IllegalArgumentException("Ya existe otro usuario con este correo");
            }
        }
        
        // Asegurar que mantenga el tipo 'restaurantero'
        usuarioData.setId_usuario(idUsuario);
        usuarioData.setTipo("restaurantero");
        
        // Actualizar los datos del usuario
        usuarioRepository.update(usuarioData);
    }
}
