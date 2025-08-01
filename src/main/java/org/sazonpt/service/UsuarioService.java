package org.sazonpt.service;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Administrador;
import org.sazonpt.model.Usuario;
import org.sazonpt.repository.AdminRepository;
import org.sazonpt.repository.UsuarioRepository;

public class UsuarioService {
    private final UsuarioRepository userRepo;

    public UsuarioService(UsuarioRepository userRepo){
        this.userRepo = userRepo;
    }

    public List<Usuario> getAllUsers() throws SQLException {
        return userRepo.findAll();
    }

}