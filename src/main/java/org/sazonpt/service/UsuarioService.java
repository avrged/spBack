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

    public List<Usuario> getAllUsers() throws SQLException{
        return userRepo.getAllUsers();
    }

    public Usuario findUserById(int idUser) throws SQLException{
        return userRepo.findUserById(idUser);
    }

    public void CreateUser(Usuario user) throws SQLException {

        if(user.getNombreU() == null || user.getCorreo() == null || user.getContrasena() == null){
            throw new IllegalArgumentException("Nombre, correo y contraseña obligatorios");
        }

        if(userRepo.findUserById(user.getId_usuario()) != null){
            throw new IllegalArgumentException("Ya existe un usuario con este id");
        }

        if(!user.getCorreo().contains("@")){
            throw new IllegalArgumentException("El correo debe contener un '@'");
        }
        userRepo.CreateUser(user);
    }

    public void UpdateUser(Usuario user) throws SQLException{
        userRepo.UpdateUser(user);
    }

    public void DeleteUser(int idUser) throws SQLException{
        userRepo.deleteUser(idUser);
    }
}
