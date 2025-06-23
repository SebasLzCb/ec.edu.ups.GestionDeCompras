package ec.edu.ups.modelo;

public class Usuario {
    private String nombreDeUsuario;
    private String Contraseña;
    private Rol rol;

    public Usuario(String nombreDeUsuario, String Contraseña, Rol rol) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.Contraseña = Contraseña;
        this.rol = rol;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreDeUsuario='" + nombreDeUsuario + '\'' +
                ", Contraseña='" + Contraseña + '\'' +
                ", rol=" + rol +
                '}';
    }
}
