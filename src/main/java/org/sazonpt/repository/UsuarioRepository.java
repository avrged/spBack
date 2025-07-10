package org.sazonpt.repository;

import org.sazonpt.model.Usuario;

public interface UsuarioRepository {
    void AddUser(Usuario u);
    boolean FindUser(int id);
    boolean DeleteUser(int id);
    Usuario UpdateUser(Usuario u);
    void ListAllUsers();
}
