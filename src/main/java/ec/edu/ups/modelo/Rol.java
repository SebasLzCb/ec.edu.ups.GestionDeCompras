package ec.edu.ups.modelo;

/**
 * El enum {@code Rol} define los diferentes tipos de roles que un usuario puede tener
 * en el sistema. Estos roles determinan los permisos y el nivel de acceso
 * a las funcionalidades de la aplicación.
 */
public enum Rol  {
    /**
     * Representa a un usuario con privilegios de administrador.
     * Los administradores suelen tener acceso completo a todas las funcionalidades,
     * incluyendo la gestión de otros usuarios, productos y carritos.
     */
    ADMINISTRADOR,
    /**
     * Representa a un usuario estándar.
     * Los usuarios regulares suelen tener acceso limitado a funcionalidades,
     * como la gestión de sus propios carritos y la visualización de productos.
     */
    USUARIO
}