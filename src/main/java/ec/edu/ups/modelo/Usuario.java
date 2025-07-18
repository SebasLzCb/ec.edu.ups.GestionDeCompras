package ec.edu.ups.modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * La clase {@code Usuario} representa un usuario registrado en el sistema.
 * Contiene información de autenticación como nombre de usuario y contraseña,
 * un rol asignado, y datos personales adicionales.
 * Implementa {@link Serializable} para permitir su persistencia en archivos binarios.
 */
public class Usuario implements Serializable {
    // Nombre de usuario único para el login
    private String username;
    // Contraseña del usuario
    private String contrasenia;
    // Rol del usuario (ADMINISTRADOR o USUARIO)
    private Rol rol;
    // Nombre completo del usuario
    private String nombreCompleto;
    // Fecha de nacimiento del usuario
    private LocalDate fechaNacimiento;
    // Dirección de correo electrónico del usuario
    private String correo;
    // Número de teléfono del usuario
    private String telefono;

    /**
     * Constructor predeterminado de la clase {@code Usuario}.
     * Inicializa todos los campos con valores predeterminados (null o 0).
     */
    public Usuario() {
    }

    /**
     * Constructor para crear una instancia de {@code Usuario} con los datos mínimos
     * para la autenticación y asignación de rol.
     *
     * @param nombreDeUsuario El nombre de usuario.
     * @param contrasenia La contraseña.
     * @param rol El {@link Rol} del usuario.
     */
    public Usuario(String nombreDeUsuario, String contrasenia, Rol rol) {
        this.username = nombreDeUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    /**
     * Constructor completo para crear una instancia de {@code Usuario} con todos sus atributos.
     *
     * @param username El nombre de usuario.
     * @param contrasenia La contraseña.
     * @param rol El {@link Rol} del usuario.
     * @param nombreCompleto El nombre completo del usuario.
     * @param fechaNacimiento La fecha de nacimiento del usuario.
     * @param correo La dirección de correo electrónico del usuario.
     * @param telefono El número de teléfono del usuario.
     */
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

    /**
     * Obtiene el nombre de usuario del usuario.
     *
     * @return El nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario del usuario.
     *
     * @param username El nuevo nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return La contraseña.
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contrasenia La nueva contraseña.
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return El {@link Rol} del usuario.
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol El nuevo {@link Rol} del usuario.
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el nombre completo del usuario.
     *
     * @return El nombre completo.
     */
    public String getNombreCompleto() { return nombreCompleto; }

    /**
     * Establece el nombre completo del usuario.
     *
     * @param nombreCompleto El nuevo nombre completo.
     */
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    /**
     * Obtiene la fecha de nacimiento del usuario.
     *
     * @return La fecha de nacimiento.
     */
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }

    /**
     * Establece la fecha de nacimiento del usuario.
     *
     * @param fechaNacimiento La nueva fecha de nacimiento.
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    /**
     * Obtiene la dirección de correo electrónico del usuario.
     *
     * @return La dirección de correo electrónico.
     */
    public String getCorreo() { return correo; }

    /**
     * Establece la dirección de correo electrónico del usuario.
     *
     * @param correo La nueva dirección de correo electrónico.
     */
    public void setCorreo(String correo) { this.correo = correo; }

    /**
     * Obtiene el número de teléfono del usuario.
     *
     * @return El número de teléfono.
     */
    public String getTelefono() { return telefono; }

    /**
     * Establece el número de teléfono del usuario.
     *
     * @param telefono El nuevo número de teléfono.
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /**
     * Devuelve una representación en cadena de este objeto {@code Usuario}.
     * Incluye el nombre de usuario, la contraseña (sin ocultar por simplicidad) y el rol.
     *
     * @return Una cadena que representa el objeto Usuario.
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "nombreDeUsuario='" + username + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", rol=" + rol +
                '}';
    }
}