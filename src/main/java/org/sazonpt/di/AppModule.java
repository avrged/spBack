package org.sazonpt.di;

import org.sazonpt.controller.*;
import org.sazonpt.repository.*;
import org.sazonpt.routes.*;
import org.sazonpt.service.*;

public class AppModule {


    public static UsuarioRoutes initUser() {
        UsuarioRepository userRepo = new UsuarioRepository();
        UsuarioService userService = new UsuarioService(userRepo);
        UsuarioController userController = new UsuarioController(userService);
        return new UsuarioRoutes(userController);
    }
    
    /*public static AdministradorRoutes initAdmin() {
        AdminRepository adminRepo = new AdminRepository();
        AdminService adminService = new AdminService(adminRepo);
        AdministradorController adminController = new AdministradorController(adminService);
        return new AdministradorRoutes(adminController);
    }*/
}
