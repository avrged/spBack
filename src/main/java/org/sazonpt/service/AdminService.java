package org.sazonpt.service;

import org.sazonpt.model.Administrador;
import org.sazonpt.model.Usuario;
import org.sazonpt.repository.AdminRepository;
import org.sazonpt.repository.UsuarioRepository;

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
        if(admin.getNombre() == null || admin.getCorreo() == null || admin.getContrasena() == null) {
            throw new IllegalArgumentException("Nombre, correo y contraseña son obligatorios");
        }

        if(!admin.getCorreo().contains("@")){
            throw new IllegalArgumentException("El correo debe contener un '@'");
        }
        UsuarioRepository userRepo = new UsuarioRepository();
        if(userRepo.findByCorreo(admin.getCorreo()) != null){
            throw new IllegalArgumentException("Ya existe un usuario con este correo");
        }

        Usuario user = new Usuario(
            admin.getNombre(),
            admin.getCorreo(),
            admin.getContrasena(),
            "administrador"
        );
        int id_usuario = userRepo.save(user);
        adminRepo.createAdmin(new Administrador(id_usuario));
    }
    
    public void updateAdmin(Administrador admin) throws SQLException {
        adminRepo.updateAdmin(admin);
    }
    
    public void deleteAdmin(int idAdmin) throws SQLException {
        adminRepo.deleteAdmin(idAdmin);
    }
}
