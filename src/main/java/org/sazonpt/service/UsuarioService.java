package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Administrador;
import org.sazonpt.model.Restaurantero;
import org.sazonpt.model.Usuario;
import org.sazonpt.repository.AdminRepository;
import org.sazonpt.repository.RestauranteroRepository;
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

    public int createUser(Usuario user) throws SQLException {
        // Validaciones antes de guardar
        if (user.getNombre() == null || user.getCorreo() == null || user.getContrasena() == null) {
            throw new IllegalArgumentException("Nombre, correo y contraseña obligatorios");
        }

        // Validación de correo
        if (!user.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo debe contener un '@'");
        }

        // Validación de usuario existente por correo
        if (userRepo.findByCorreo(user.getCorreo()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con este correo");
        }

        int id_usuario = userRepo.save(user);

        if("restaurantero".equalsIgnoreCase(user.getTipo())){
            RestauranteroRepository restRepo = new RestauranteroRepository();
            restRepo.CreateRestaurantero(new Restaurantero(id_usuario));
        }
        else if("administrador".equalsIgnoreCase(user.getTipo())){
            AdminRepository adminRepo = new AdminRepository();
            adminRepo.createAdmin(new Administrador(id_usuario));
        }

        return id_usuario;
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
