package org.sazonpt.model;

public class Administrador extends Usuario {
    
    // Enum para representar los niveles de permiso
    public enum NivelPermiso {
        SUPER("super"),
        MODERADOR("moderador");
        
        private final String valor;
        
        NivelPermiso(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
        
        public static NivelPermiso fromValor(String valor) {
            for (NivelPermiso nivel : NivelPermiso.values()) {
                if (nivel.valor.equals(valor)) {
                    return nivel;
                }
            }
            throw new IllegalArgumentException("Valor de nivel de permiso no válido: " + valor);
        }
        
        @Override
        public String toString() {
            return valor;
        }
    }
    
    private int id_administrador;
    private NivelPermiso nivel_permiso;
    
    public Administrador() {
        super();
        this.nivel_permiso = NivelPermiso.MODERADOR;
    }
    
    public Administrador(String email, String password_hash, String nombre, String telefono) {
        super(email, password_hash, nombre, telefono);
        this.nivel_permiso = NivelPermiso.MODERADOR;
    }
    
    public Administrador(String email, String password_hash, String nombre, String telefono, NivelPermiso nivel_permiso) {
        super(email, password_hash, nombre, telefono);
        this.nivel_permiso = nivel_permiso;
    }
    
    public int getId_administrador() {
        return id_administrador;
    }
    
    public void setId_administrador(int id_administrador) {
        this.id_administrador = id_administrador;
    }
    
    public NivelPermiso getNivel_permiso() {
        return nivel_permiso;
    }
    
    public void setNivel_permiso(NivelPermiso nivel_permiso) {
        this.nivel_permiso = nivel_permiso;
    }
    
    public boolean esSuper() {
        return this.nivel_permiso == NivelPermiso.SUPER;
    }
    
    public boolean esModerador() {
        return this.nivel_permiso == NivelPermiso.MODERADOR;
    }
    
}