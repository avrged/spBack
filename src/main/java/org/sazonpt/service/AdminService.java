package org.sazonpt.service;

import org.sazonpt.model.Administrador;
import org.sazonpt.repository.AdminRepository;

import java.sql.SQLException;
import java.util.List;

public class AdminService {
    private final AdminRepository adminRepo;
    
    public AdminService(AdminRepository adminRepo) {
        this.adminRepo = adminRepo;
    }
    
    public List<Administrador> getAllAdmins() throws SQLException {
        return adminRepo.getAllAdmins();
    }
    
    public Administrador getByIdAdmin(int idAdmin) throws SQLException {
        return adminRepo.findAdminById(idAdmin);
    }
    
    public void createAdmin(Administrador admin) throws SQLException {
        if(admin.getNombreU() == null || admin.getCorreo() == null || admin.getContrasena() == null) {
            throw new IllegalArgumentException("Nombre, correo y contraseña son obligatorios");
        }

        if(adminRepo.findAdminById(admin.getId_usuario()) != null){
            throw new IllegalArgumentException("Ya existe un administrador con este ID");
        }

        if(!admin.getCorreo().contains("@")){
            throw new IllegalArgumentException("El correo debe contener un '@'");
        }
        adminRepo.createAdmin(admin);
    }
    
    public void updateAdmin(Administrador admin) throws SQLException {
        adminRepo.updateAdmin(admin);
    }
    
    public void deleteAdmin(int idAdmin) throws SQLException {
        adminRepo.deleteAdmin(idAdmin);
    }
}
