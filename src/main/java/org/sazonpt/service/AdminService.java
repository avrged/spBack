package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Administrador;
import org.sazonpt.repository.AdminRepository;

/**
 * Servicio para la gestión de administradores
 * Siguiendo la arquitectura del template API
 */
public class AdminService {
    private final AdminRepository adminRepo;

    public AdminService(AdminRepository adminRepo) {
        this.adminRepo = adminRepo;
    }
    
    /**
     * Crea un nuevo administrador
     * @param administrador Datos del administrador a crear
     * @return ID del administrador creado
     * @throws SQLException Si hay error en la base de datos
     * @throws IllegalArgumentException Si los datos no son válidos
     */
    public int createAdmin(Administrador administrador) throws SQLException {
        // Validaciones de negocio
        validateAdminData(administrador);
        
        // Validar email único
        if (adminRepo.findByEmail(administrador.getEmail()) != null) {
            throw new IllegalArgumentException("Ya existe un administrador con este email");
        }
        
        // Crear el administrador
        return adminRepo.save(administrador);
    }
    
    /**
     * Obtiene un administrador por ID
     * @param id ID del administrador
     * @return Administrador encontrado o null si no existe
     * @throws SQLException Si hay error en la base de datos
     */
    public Administrador getAdminById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del administrador debe ser positivo");
        }
        
        return adminRepo.findById(id);
    }
    
    /**
     * Obtiene un administrador por email
     * @param email Email del administrador
     * @return Administrador encontrado o null si no existe
     * @throws SQLException Si hay error en la base de datos
     */
    public Administrador getAdminByEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        
        return adminRepo.findByEmail(email.trim().toLowerCase());
    }
    
    /**
     * Obtiene todos los administradores
     * @return Lista de administradores
     * @throws SQLException Si hay error en la base de datos
     */
    public List<Administrador> getAllAdmins() throws SQLException {
        return adminRepo.findAll();
    }
    
    /**
     * Actualiza un administrador existente
     * @param administrador Datos actualizados del administrador
     * @return true si se actualizó correctamente
     * @throws SQLException Si hay error en la base de datos
     * @throws IllegalArgumentException Si los datos no son válidos
     */
    public boolean updateAdmin(Administrador administrador) throws SQLException {
        // Validaciones
        if (administrador.getId_usuario() <= 0) {
            throw new IllegalArgumentException("ID de administrador inválido");
        }
        
        validateAdminData(administrador);
        
        // Verificar que el administrador existe
        Administrador existingAdmin = adminRepo.findById(administrador.getId_usuario());
        if (existingAdmin == null) {
            throw new IllegalArgumentException("No se encontró el administrador con ID: " + administrador.getId_usuario());
        }
        
        // Validar email único (solo si cambió)
        if (!existingAdmin.getEmail().equals(administrador.getEmail())) {
            Administrador adminWithEmail = adminRepo.findByEmail(administrador.getEmail());
            if (adminWithEmail != null && adminWithEmail.getId_usuario() != administrador.getId_usuario()) {
                throw new IllegalArgumentException("Ya existe otro administrador con este email");
            }
        }
        
        return adminRepo.update(administrador);
    }
    
    /**
     * Elimina un administrador por ID
     * @param id ID del administrador a eliminar
     * @return true si se eliminó correctamente
     * @throws SQLException Si hay error en la base de datos
     * @throws IllegalArgumentException Si no se puede eliminar
     */
    public boolean deleteAdmin(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del administrador debe ser positivo");
        }
        
        // Verificar que el administrador existe
        Administrador admin = adminRepo.findById(id);
        if (admin == null) {
            throw new IllegalArgumentException("No se encontró el administrador con ID: " + id);
        }
        
        return adminRepo.delete(id);
    }
    
    /**
     * Cuenta el total de administradores activos
     * @return Número total de administradores
     * @throws SQLException Si hay error en la base de datos
     */
    public int getTotalAdmins() throws SQLException {
        return adminRepo.count();
    }
    
    /**
     * Verifica si existe un administrador SUPER
     * @return true si existe un administrador SUPER
     * @throws SQLException Si hay error en la base de datos
     */
    public boolean hasSuperAdmin() throws SQLException {
        return adminRepo.existeAdminSuper();
    }
    
    /**
     * Cambia el nivel de permiso de un administrador
     * @param adminId ID del administrador
     * @param nuevoNivel Nuevo nivel de permiso
     * @return true si se actualizó correctamente
     * @throws SQLException Si hay error en la base de datos
     * @throws IllegalArgumentException Si la operación no es válida
     */
    public boolean changeAdminLevel(int adminId, Administrador.NivelPermiso nuevoNivel) throws SQLException {
        if (adminId <= 0) {
            throw new IllegalArgumentException("ID de administrador inválido");
        }
        
        if (nuevoNivel == null) {
            throw new IllegalArgumentException("El nivel de permiso no puede ser null");
        }
        
        // Obtener el administrador actual
        Administrador admin = adminRepo.findById(adminId);
        if (admin == null) {
            throw new IllegalArgumentException("No se encontró el administrador con ID: " + adminId);
        }
        
        // Actualizar solo el nivel de permiso
        admin.setNivel_permiso(nuevoNivel);
        return adminRepo.update(admin);
    }
    
    /**
     * Valida los datos básicos de un administrador
     * Solo validaciones esenciales para el CRUD
     * @param administrador Administrador a validar
     * @throws IllegalArgumentException Si los datos no son válidos
     */
    private void validateAdminData(Administrador administrador) {
        if (administrador == null) {
            throw new IllegalArgumentException("Los datos del administrador no pueden ser null");
        }
        
        // Validaciones básicas - solo campos obligatorios
        if (administrador.getEmail() == null || administrador.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        
        if (administrador.getNombre() == null || administrador.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        // Password solo para nuevos administradores
        if (administrador.getId_usuario() == 0 && 
            (administrador.getPassword_hash() == null || administrador.getPassword_hash().trim().isEmpty())) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        
        // Nivel de permiso es obligatorio para administradores
        if (administrador.getNivel_permiso() == null) {
            throw new IllegalArgumentException("El nivel de permiso es obligatorio");
        }
    }
}