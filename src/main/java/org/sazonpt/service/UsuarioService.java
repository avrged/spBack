package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Usuario;
import org.sazonpt.model.Restaurantero;
import org.sazonpt.repository.UsuarioRepository;
import org.sazonpt.repository.RestauranteroRepository;

/**
 * Servicio para la gestión de usuarios
 * Siguiendo la arquitectura del template API
 */
public class UsuarioService {
    private final UsuarioRepository userRepo;
    private final RestauranteroRepository restauranteroRepo;

    public UsuarioService(UsuarioRepository userRepo, RestauranteroRepository restauranteroRepo) {
        this.userRepo = userRepo;
        this.restauranteroRepo = restauranteroRepo;
    }
    
    /**
     * Crea un nuevo usuario (siempre será restaurantero por defecto)
     * @param usuario Datos del usuario a crear
     * @return ID del usuario creado
     * @throws SQLException Si hay error en la base de datos
     * @throws IllegalArgumentException Si los datos no son válidos
     */
    public int createUser(Usuario usuario) throws SQLException {
        // Validaciones de negocio
        validateUserData(usuario);
        
        // Validar email único
        if (userRepo.findByEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con este email");
        }
        
        // ✅ Por defecto, todos los usuarios nuevos son restauranteros
        if (usuario.getTipo_usuario() == null || usuario.getTipo_usuario().trim().isEmpty()) {
            usuario.setTipo_usuario("Restaurantero");
        }
        
        // Crear el usuario
        int userId = userRepo.save(usuario);
        
        // ✅ SIEMPRE crear entrada en tabla restaurantero para nuevos usuarios
        try {
            createRestauranteroEntry(userId, usuario);
            System.out.println("✅ Usuario creado con ID: " + userId + " y registrado como restaurantero");
        } catch (Exception e) {
            System.err.println("❌ Error creando entrada de restaurantero para usuario " + userId + ": " + e.getMessage());
            // Si falla la creación del restaurantero, eliminar el usuario para mantener consistencia
            userRepo.delete(userId);
            throw new RuntimeException("Error al crear restaurantero automáticamente: " + e.getMessage(), e);
        }
        
        return userId;
    }
    
    /**
     * Crea una entrada en la tabla restaurantero para un usuario
     * @param userId ID del usuario creado
     * @param usuario Datos del usuario para extraer información del restaurantero
     * @throws SQLException Si hay error en la base de datos
     */
    private void createRestauranteroEntry(int userId, Usuario usuario) throws SQLException {
        Restaurantero restaurantero = new Restaurantero();
        restaurantero.setId_restaurantero(userId);
        
        // ✅ Por defecto, valores iniciales para nuevo restaurantero
        restaurantero.setRfc(null); // Se completará en la solicitud de registro
        restaurantero.setVerificado(false); // Se verificará cuando se apruebe la solicitud
        
        int restauranteroId = restauranteroRepo.save(restaurantero);
        System.out.println("🏪 Entrada de restaurantero creada con ID: " + restauranteroId);
    }
    
    /**
     * Obtiene un usuario por ID
     * @param id ID del usuario
     * @return Usuario encontrado o null si no existe
     * @throws SQLException Si hay error en la base de datos
     */
    public Usuario getUserById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser positivo");
        }
        
        return userRepo.findById(id);
    }

    public Usuario getUserByEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        
        return userRepo.findByEmail(email.trim().toLowerCase());
    }
    
    /**
     * Obtiene todos los usuarios
     * @return Lista de usuarios
     * @throws SQLException Si hay error en la base de datos
     */
    public List<Usuario> getAllUsers() throws SQLException {
        return userRepo.findAll();
    }
    
    /**
     * Actualiza un usuario existente
     * @param usuario Datos actualizados del usuario
     * @return true si se actualizó correctamente
     * @throws SQLException Si hay error en la base de datos
     * @throws IllegalArgumentException Si los datos no son válidos
     */
    public boolean updateUser(Usuario usuario) throws SQLException {
        // Validaciones básicas
        if (usuario.getId_usuario() <= 0) {
            throw new IllegalArgumentException("ID de usuario inválido");
        }
        
        validateUserData(usuario);
        
        // Verificar que el usuario existe
        Usuario existingUser = userRepo.findById(usuario.getId_usuario());
        if (existingUser == null) {
            throw new IllegalArgumentException("No se encontró el usuario con ID: " + usuario.getId_usuario());
        }
        
        // Validar email único (solo si cambió)
        if (!existingUser.getEmail().equals(usuario.getEmail())) {
            Usuario userWithEmail = userRepo.findByEmail(usuario.getEmail());
            if (userWithEmail != null && userWithEmail.getId_usuario() != usuario.getId_usuario()) {
                throw new IllegalArgumentException("Ya existe otro usuario con este email");
            }
        }
        
        return userRepo.update(usuario);
    }
    
    /**
     * Elimina un usuario por ID (hard delete)
     * @param id ID del usuario a eliminar
     * @return true si se eliminó correctamente
     * @throws SQLException Si hay error en la base de datos
     * @throws IllegalArgumentException Si no se puede eliminar
     */
    public boolean deleteUser(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser positivo");
        }
        
        // Verificar que el usuario existe
        Usuario usuario = userRepo.findById(id);
        if (usuario == null) {
            throw new IllegalArgumentException("No se encontró el usuario con ID: " + id);
        }
        
        return userRepo.delete(id);
    }
    
    /**
     * Realiza eliminación suave de un usuario
     * @param id ID del usuario a eliminar
     * @return true si se marcó como eliminado
     * @throws SQLException Si hay error en la base de datos
     * @throws IllegalArgumentException Si no se puede eliminar
     */
    public boolean softDeleteUser(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser positivo");
        }
        
        // Verificar que el usuario existe
        Usuario usuario = userRepo.findById(id);
        if (usuario == null) {
            throw new IllegalArgumentException("No se encontró el usuario con ID: " + id);
        }
        
        return userRepo.softDelete(id);
    }
    
    /**
     * Cuenta el total de usuarios activos
     * @return Número total de usuarios
     * @throws SQLException Si hay error en la base de datos
     */
    public int getTotalUsers() throws SQLException {
        return userRepo.count();
    }
    
    /**
     * Obtiene usuarios con información de roles
     * @return Lista de arrays con información completa de usuarios
     * @throws SQLException Si hay error en la base de datos
     */
    public List<Object[]> getUsersWithRoles() throws SQLException {
        return userRepo.findAllWithRoles();
    }
    
    /**
     * Verifica si un email ya está en uso
     * @param email Email a verificar
     * @return true si el email ya existe
     * @throws SQLException Si hay error en la base de datos
     */
    public boolean emailExists(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return userRepo.findByEmail(email.trim().toLowerCase()) != null;
    }
    
    /**
     * Obtiene estadísticas básicas de usuarios por tipo
     * @return Array con estadísticas [total, administradores, restauranteros, usuarios_base]
     * @throws SQLException Si hay error en la base de datos
     */
    public Object[] getUserStatsByType() throws SQLException {
        List<Object[]> usersWithRoles = userRepo.findAllWithRoles();
        
        int total = usersWithRoles.size();
        int administradores = 0;
        int restauranteros = 0;
        int usuariosBase = 0;
        
        for (Object[] userData : usersWithRoles) {
            String tipoUsuario = (String) userData[8]; // Índice del tipo_usuario
            switch (tipoUsuario) {
                case "Administrador" -> administradores++;
                case "Restaurantero" -> restauranteros++;
                default -> usuariosBase++;
            }
        }
        
        return new Object[]{total, administradores, restauranteros, usuariosBase};
    }
    
    /**
     * Autentica un usuario con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña en texto plano
     * @return Usuario si las credenciales son válidas, null si no
     * @throws SQLException Si hay error en la base de datos
     */
    public Usuario loginUser(String email, String password) throws SQLException {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        Usuario usuario = userRepo.findByEmail(email.trim().toLowerCase());
        
        if (usuario != null && usuario.isActivo()) {
            // Verificar contraseña (por ahora comparación directa, después usar bcrypt)
            if (usuario.getPassword_hash().equals(password)) {
                Object[] userWithRole = userRepo.findUserWithRoleByEmail(email.trim().toLowerCase());
                
                if (userWithRole != null) {
                    return usuario;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Obtiene información completa del usuario para login (incluyendo tipo de usuario)
     * @param email Email del usuario
     * @return Información completa del usuario o null si no existe
     * @throws SQLException Si hay error en la base de datos
     */
    public Object[] getUserLoginInfo(String email) throws SQLException {
        return userRepo.findUserWithRoleByEmail(email);
    }
    
    /**
     * Valida los datos básicos de un usuario
     * Solo validaciones esenciales para el CRUD
     * @param usuario Usuario a validar
     * @throws IllegalArgumentException Si los datos no son válidos
     */
    private void validateUserData(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Los datos del usuario no pueden ser null");
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        if (usuario.getId_usuario() == 0 && 
            (usuario.getPassword_hash() == null || usuario.getPassword_hash().trim().isEmpty())) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
    }
}