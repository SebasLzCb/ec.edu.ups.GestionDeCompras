package ec.edu.ups.dao.impl.ArchTxt;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * La clase {@code UsuarioDAOArchivoTexto} es una implementación de la interfaz {@link UsuarioDAO}
 * que persiste los objetos {@link Usuario} en un archivo de texto plano.
 * Cada línea del archivo representa un usuario, y los atributos del usuario
 * se codifican en un formato de texto separado por delimitadores.
 */
public class UsuarioDAOArchivoTexto implements UsuarioDAO {

    private String rutaArchivo;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE; // Formato para LocalDate (YYYY-MM-DD)
    private static final String DELIMITADOR = "\\|"; // Delimitador para separar los campos de un usuario en la línea

    /**
     * Constructor de la clase {@code UsuarioDAOArchivoTexto}.
     *
     * @param rutaArchivo La ruta completa del archivo de texto donde se guardarán/cargarán los usuarios.
     */
    public UsuarioDAOArchivoTexto(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        inicializarArchivo();
    }

    /**
     * Inicializa el archivo de texto: crea el directorio si no existe y luego crea el archivo
     * si aún no existe. Muestra mensajes de error si ocurren problemas durante la creación.
     */
    private void inicializarArchivo() {
        File archivo = new File(rutaArchivo);
        File directorio = archivo.getParentFile();

        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs(); // Crea el directorio y todos sus padres si no existen
        }
        if (!archivo.exists()) {
            try {
                archivo.createNewFile(); // Crea el archivo vacío si no existe
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de usuarios: " + e.getMessage());
            }
        }
    }

    /**
     * Guarda la lista completa de usuarios en el archivo de texto especificado por {@code rutaArchivo}.
     * Cada objeto {@link Usuario} se convierte a una cadena de texto utilizando {@link #usuarioATexto(Usuario)}.
     *
     * @param usuarios La lista de objetos {@link Usuario} a guardar.
     */
    private void guardarUsuariosEnArchivo(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Usuario usuario : usuarios) {
                writer.write(usuarioATexto(usuario));
                writer.newLine(); // Añade un salto de línea después de cada usuario
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en archivo: " + e.getMessage());
        }
    }

    /**
     * Carga todos los usuarios desde el archivo de texto especificado por {@code rutaArchivo}.
     * Cada línea del archivo se lee y se convierte a un objeto {@link Usuario}
     * utilizando {@link #textoAUsuario(String)}.
     * Maneja casos de archivo vacío o errores de lectura/formato.
     *
     * @return Una {@link List} de objetos {@link Usuario} cargados desde el archivo.
     * Retorna una lista vacía si el archivo está vacío o ocurre un error.
     */
    private List<Usuario> cargarUsuariosDesdeArchivo() {
        List<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) { // Comprobar si el archivo está vacío o no existe
            return usuarios;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Usuario usuario = textoAUsuario(line);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer usuarios desde archivo: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Convierte un objeto {@link Usuario} a una cadena de texto para su almacenamiento.
     * El formato de la cadena es: {@code username|contrasenia|rol|nombreCompleto|fechaNacimiento|correo|telefono}.
     * Los caracteres delimitadores dentro de los datos se escapan para evitar conflictos.
     *
     * @param usuario El objeto {@link Usuario} a convertir.
     * @return Una {@link String} que representa el usuario en formato de texto.
     */
    private String usuarioATexto(Usuario usuario) {
        // Formatear la fecha de nacimiento; si es null, usar una cadena vacía
        String fechaNacimientoStr = (usuario.getFechaNacimiento() != null) ? usuario.getFechaNacimiento().format(formatter) : "";
        // Asegurarse de que los campos String no sean null antes de escapar y unir
        String nombreCompleto = (usuario.getNombreCompleto() != null) ? usuario.getNombreCompleto() : "";
        String correo = (usuario.getCorreo() != null) ? usuario.getCorreo() : "";
        String telefono = (usuario.getTelefono() != null) ? usuario.getTelefono() : "";

        // Unir los campos con el delimitador, escapando los campos que podrían contener el delimitador
        return String.join("|",
                escapeString(usuario.getUsername()),
                escapeString(usuario.getContrasenia()),
                usuario.getRol().name(), // El nombre del enum no necesita escapar
                escapeString(nombreCompleto),
                fechaNacimientoStr, // La fecha no debería contener el delimitador
                escapeString(correo),
                escapeString(telefono)
        );
    }

    /**
     * Convierte una cadena de texto leída del archivo a un objeto {@link Usuario}.
     * Descompone la línea en sus partes y parsea los tipos de datos correspondientes.
     * Maneja errores de formato (ej. número de partes incorrecto o parseo de enum/fecha fallido).
     *
     * @param line La cadena de texto que representa un usuario.
     * @return Un objeto {@link Usuario} si la conversión es exitosa, o {@code null} si hay un error de formato.
     */
    private Usuario textoAUsuario(String line) {
        // Usa el delimitador para dividir la línea en partes
        String[] partes = line.split(DELIMITADOR);
        // Se esperan 7 partes para todos los campos de Usuario
        if (partes.length == 7) {
            try {
                Usuario usuario = new Usuario();
                usuario.setUsername(unescapeString(partes[0]));
                usuario.setContrasenia(unescapeString(partes[1]));
                usuario.setRol(Rol.valueOf(partes[2])); // Convierte la cadena a un valor de enum Rol
                usuario.setNombreCompleto(unescapeString(partes[3]));
                // Parsear fecha de nacimiento, si la cadena no está vacía
                usuario.setFechaNacimiento(partes[4].isEmpty() ? null : LocalDate.parse(partes[4], formatter));
                usuario.setCorreo(unescapeString(partes[5]));
                usuario.setTelefono(unescapeString(partes[6]));
                return usuario;
            } catch (IllegalArgumentException | DateTimeParseException e) {
                // Captura errores si el valor del rol no es válido o la fecha no puede ser parseada
                System.err.println("Error en formato de datos de usuario en archivo: " + line + " - " + e.getMessage());
            }
        }
        return null; // Retorna null si la línea no tiene el formato esperado o hay un error de parseo
    }

    /**
     * Escapa el carácter delimitador interno ('|') si aparece en una cadena de texto,
     * para que no interfiera con la estructura de la línea al guardarla.
     * Reemplaza '|' con su entidad HTML (&#124;).
     *
     * @param s La cadena de texto a escapar.
     * @return La cadena de texto con el delimitador escapado.
     */
    private String escapeString(String s) {
        return s.replace("|", "&#124;");
    }

    /**
     * Desescapa el carácter delimitador interno ('|') que fue escapado previamente,
     * restaurando el delimitador original.
     * Reemplaza la entidad HTML (&#124;) con el carácter real '|'.
     *
     * @param s La cadena de texto a desescapar.
     * @return La cadena de texto con el delimitador restaurado.
     */
    private String unescapeString(String s) {
        return s.replace("&#124;", "|");
    }

    /**
     * Autentica un usuario verificando su nombre de usuario y contraseña
     * en la lista de usuarios cargada desde el archivo de texto.
     *
     * @param username El nombre de usuario a autenticar.
     * @param contrasenia La contraseña del usuario.
     * @return El objeto {@link Usuario} si las credenciales coinciden, o {@code null} en caso contrario.
     */
    @Override
    public Usuario autenticar(String username, String contrasenia) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Crea y persiste un nuevo objeto {@link Usuario} en el archivo de texto.
     * Carga todos los usuarios existentes, añade el nuevo si no existe ya un usuario
     * con el mismo nombre de usuario, y luego guarda la lista completa.
     *
     * @param usuario El objeto {@link Usuario} a ser creado.
     */
    @Override
    public void crear(Usuario usuario) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        // Verifica si ya existe un usuario con el mismo username para evitar duplicados
        if (usuarios.stream().noneMatch(u -> u.getUsername().equals(usuario.getUsername()))) {
            usuarios.add(usuario);
            guardarUsuariosEnArchivo(usuarios);
        } else {
            System.err.println("Error: El usuario " + usuario.getUsername() + " ya existe. No se creará.");
        }
    }

    /**
     * Busca un objeto {@link Usuario} por su nombre de usuario en el archivo de texto.
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return El objeto {@link Usuario} si se encuentra, o {@code null} si no existe.
     */
    @Override
    public Usuario buscarPorUsername(String username) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Elimina un usuario del archivo de texto utilizando su nombre de usuario.
     * Carga todos los usuarios, remueve el que coincide con el nombre de usuario
     * y luego guarda la lista actualizada.
     *
     * @param username El nombre de usuario del usuario a eliminar.
     */
    @Override
    public void eliminar(String username) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        // removeIf elimina todos los elementos que cumplen la condición.
        // Si el nombre de usuario es único, eliminará solo uno.
        boolean removido = usuarios.removeIf(u -> u.getUsername().equals(username));
        if (removido) { // Solo guarda si hubo un cambio
            guardarUsuariosEnArchivo(usuarios);
        } else {
            System.err.println("Usuario " + username + " no encontrado para eliminar.");
        }
    }

    /**
     * Actualiza la información de un objeto {@link Usuario} existente en el archivo de texto.
     * Carga todos los usuarios, busca el usuario por su nombre de usuario y lo reemplaza
     * con la versión actualizada. Luego, guarda la lista completa de usuarios.
     *
     * @param usuario El objeto {@link Usuario} con la información actualizada.
     */
    @Override
    public void actualizar(Usuario usuario) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        boolean actualizado = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getUsername().equals(usuario.getUsername())) {
                usuarios.set(i, usuario); // Reemplaza el objeto en la posición
                actualizado = true;
                break;
            }
        }
        if (actualizado) { // Solo guarda si hubo un cambio
            guardarUsuariosEnArchivo(usuarios);
        } else {
            System.err.println("Usuario " + usuario.getUsername() + " no encontrado para actualizar.");
        }
    }

    /**
     * Devuelve una lista de todos los usuarios actualmente almacenados en el archivo de texto.
     *
     * @return Una {@link List} de todos los objetos {@link Usuario} cargados desde el archivo.
     */
    @Override
    public List<Usuario> listarTodos() {
        return cargarUsuariosDesdeArchivo();
    }

    /**
     * Devuelve una lista de usuarios que tienen un rol específico,
     * cargándolos desde el archivo de texto y filtrándolos.
     *
     * @param rol El {@link Rol} por el cual se desea filtrar los usuarios.
     * @return Una {@link List} de objetos {@link Usuario} que coinciden con el rol especificado.
     */
    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        return cargarUsuariosDesdeArchivo().stream()
                .filter(u -> u.getRol().equals(rol))
                .collect(Collectors.toList());
    }
}