package ec.edu.ups.dao.impl.ArchBin;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsuarioDAOArchivoBinario implements UsuarioDAO {

    private String rutaArchivo;

    public UsuarioDAOArchivoBinario(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        inicializarArchivo();
    }

    private void inicializarArchivo() {
        File archivo = new File(rutaArchivo);
        File directorio = archivo.getParentFile();

        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo binario de usuarios: " + e.getMessage());
            }
        }
    }

    private void guardarTodosLosUsuarios(List<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en archivo binario: " + e.getMessage());
        }
    }

    private List<Usuario> cargarTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (archivo.length() == 0) { // Archivo vacío
            return usuarios;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                usuarios = (List<Usuario>) obj;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de usuarios no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios desde archivo binario: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Clase Usuario no encontrada al deserializar: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public Usuario autenticar(String username, String contrasenia) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void crear(Usuario usuario) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        boolean found = false;
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(usuario.getUsername())) {
                found = true;
                break;
            }
        }
        if (!found) {
            usuarios.add(usuario);
            guardarTodosLosUsuarios(usuarios);
        }
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void eliminar(String username) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            if (usuario.getUsername().equals(username)) {
                iterator.remove();
                break;
            }
        }
        guardarTodosLosUsuarios(usuarios);
    }

    @Override
    public void actualizar(Usuario usuario) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuarioAux = usuarios.get(i);
            if (usuarioAux.getUsername().equals(usuario.getUsername())) {
                usuarios.set(i, usuario);
                break;
            }
        }
        guardarTodosLosUsuarios(usuarios);
    }

    @Override
    public List<Usuario> listarTodos() {
        return cargarTodosLosUsuarios();
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getRol().equals(rol)) {
                usuariosEncontrados.add(usuario);
            }
        }
        return usuariosEncontrados;
    }
}