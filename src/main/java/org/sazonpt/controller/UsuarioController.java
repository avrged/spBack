package org.sazonpt.controller;

import java.sql.SQLException;
import java.util.List;

import org.sazonpt.model.Usuario;
import org.sazonpt.service.UsuarioService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class UsuarioController {
    private final UsuarioService userService;

    public UsuarioController(UsuarioService userService){
        this.userService = userService;
    }

    public void getAllUsers(Context ctx){
        try{
            List<Usuario> users = userService.getAllUsers();
            ctx.json(users);

        } catch(SQLException e){
            ctx.status(500).result("Error al obtener usuarios");
        }
    }

    public void getUserById(Context ctx){
        try{
            int idUser = Integer.parseInt(ctx.pathParam("id"));
            Usuario user = userService.findUserById(idUser);

            if(user != null){
                ctx.json(user);
            }
            else{
                ctx.status(HttpStatus.NOT_FOUND).result("Usuario no encontrado");
            }
        } catch(SQLException e){
            ctx.status(404).result("Error al obtener los datos del usuario");
        }
    }

    public void CreateUser(Context ctx){
        try{
            Usuario user = ctx.bodyAsClass(Usuario.class);
            userService.CreateUser(user);
            ctx.status(201).result("Usuario creado");
        } catch(SQLException e){
            ctx.status(400).result("Error al crear el usuario");
        }
    }

    public void UpdateUser(Context ctx){
        try{
            int idUser = Integer.parseInt(ctx.pathParam("id"));
            Usuario user = ctx.bodyAsClass(Usuario.class);
            user.setIdUsuario(idUser);
            userService.UpdateUser(user);
            ctx.status(200).result("Usuario actualizado");
        } catch(Exception e){
            ctx.status(400).result("Error al actualizar el usuario");
        }
    }

    public void DeleteUser(Context ctx){
        try{
            int idUser = Integer.parseInt(ctx.pathParam("id"));
            userService.DeleteUser(idUser);
            ctx.status(200).result("Usuario eliminado");
        } catch(Exception e){
            ctx.status(400).result("Error al eliminar el usuario");
        }
    }
}
