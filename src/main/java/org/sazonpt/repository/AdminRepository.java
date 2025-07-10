package org.sazonpt.repository;

import org.sazonpt.model.Administrador;

public interface AdminRepository {
    void AddAdmin(Administrador ad);
    boolean FindAdmin(int id);
    boolean DeleteAdmin(int id);
    Administrador UpdateAdmin(Administrador ad);
    void ListAllAdmins();
}
