package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Usuario;
import org.sazonpt.repository.UsuarioRepository;

public class UsuarioService {
    private final UsuarioRepository userRepo;

    public UsuarioService(UsuarioRepository userRepo){
        this.userRepo = userRepo;
    }

    public List<Usuario> getAllUsers() throws SQLException {
        return userRepo.findAll();
    }

    public Usuario getByIdUser(int idUser) throws SQLException {
        return userRepo.findByIdUser(idUser);
    }

    public void createUser(Usuario user) throws SQLException {
        if (user.getNombreU() == null || user.getCorreo() == null || user.getContrasena() == null) {
            throw new IllegalArgumentException("Nombre, correo y contraseña obligatorios");
        }

        if (userRepo.findByIdUser(user.getId_usuario()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con este id");
        }

        if (!user.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo debe contener un '@'");
        }
        
        userRepo.save(user);
    }

    public void updateUser(Usuario user) throws SQLException {
        userRepo.UpdateUser(user);
    }

    public void deleteUser(int idUser) throws SQLException {
        userRepo.deleteUser(idUser);
    }

    public Usuario login(String correo, String contrasena, String rol) throws SQLException {
        return userRepo.findByCorreoAndContrasenaAndRol(correo, contrasena, rol);
    }

}
