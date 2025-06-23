package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDAOMemoria implements UsuarioDAO {

    private final List<Usuario> usuarios = new ArrayList<>();

    public UsuarioDAOMemoria() {
        usuarios.add(new Usuario("admin", "12345", Rol.Administrador));
        usuarios.add(new Usuario("user",  "12345", Rol.Cliente));
    }

    @Override
    public Usuario autenticar(String username, String contrasenia) {
        for (Usuario u : usuarios) {
            if (u.getNombreDeUsuario().equals(username)
                    && u.getContraseña().equals(contrasenia)) {
                return u;
            }
        }
        throw new RuntimeException("Usuario o contraseña inválidos");
    }

    @Override
    public void crear(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return usuarios.stream()
                .filter(u -> u.getNombreDeUsuario().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void eliminar(String username) {
        Iterator<Usuario> it = usuarios.iterator();
        while (it.hasNext()) {
            if (it.next().getNombreDeUsuario().equals(username)) {
                it.remove();
                return;
            }
        }
    }

    @Override
    public void actualizar(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNombreDeUsuario()
                    .equals(usuario.getNombreDeUsuario())) {
                usuarios.set(i, usuario);
                return;
            }
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public List<Usuario> listarAdministradores() {
        return usuarios.stream()
                .filter(u -> u.getRol() == Rol.Administrador)
                .collect(Collectors.toList());
    }
}
