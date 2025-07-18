package ec.edu.ups.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Usuario implements Serializable {
    private String username;
    private String contrasenia;
    private Rol rol;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String correo;
    private String telefono;

    public Usuario() {
    }

    public Usuario(String nombreDeUsuario, String contrasenia, Rol rol) {
        this.username = nombreDeUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public Usuario(String username, String contrasenia, Rol rol,
                   String nombreCompleto, LocalDate fechaNacimiento,
                   String correo, String telefono) {
        this.username = username;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.telefono = telefono;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreDeUsuario='" + username + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", rol=" + rol +
                '}';
    }
}
