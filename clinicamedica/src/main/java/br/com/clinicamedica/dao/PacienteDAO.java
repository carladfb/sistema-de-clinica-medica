package br.com.clinicamedica.dao;


import br.com.clinicamedica.model.Medico;
import br.com.clinicamedica.model.Paciente;

import java.sql.*;

public class PacienteDAO {

    private final String url = "jdbc:h2:~/test";
    private final String usuario = "sa";
    private final String senha = "sa";
    public Paciente getPacienteByCPF(String cpf) {

        final String sqlSelect = "SELECT * FROM usuario WHERE cpf = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, senha)) {
            PreparedStatement ps = connection.prepareStatement(sqlSelect);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setIdUsuario(rs.getLong("id_usuario"));
                paciente.setNome(rs.getString("nome"));
                paciente.setCpf(rs.getLong("cpf"));
                paciente.setSenha(rs.getString("senha"));
                paciente.setTelefone(rs.getLong("telefone"));

                long enderecoId = rs.getLong("id_endereco");
                Endereco endereco =  new EnderecoDAO().getEnderecoById(enderecoId);
                paciente.setEndereco(endereco);


                // Set other fields as necessary
                return paciente;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao recuperar o paciente pelo CPF: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    public Paciente getidPacienteByCPF(String cpf) {
        Paciente paciente = getPacienteByCPF(cpf);

        if (paciente != null) {
            final String sqlSelect = "SELECT * FROM paciente WHERE id_usuario = ?";

            try (Connection connection = DriverManager.getConnection(url, usuario, senha)) {
                PreparedStatement ps = connection.prepareStatement(sqlSelect);
                ps.setLong(1, paciente.getIdUsuario());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    paciente.setIdUsuario(rs.getLong("id_paciente"));
                    // Set other fields as necessary
                    return paciente;
                }
            } catch (SQLException e) {
                System.out.println("Erro ao recuperar o paciente pelo CPF: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return null;

    public boolean atualizarPaciente(Paciente paciente, String cpf) {
        new PacienteDAO();
        final String sqlUpdateUsuario = "UPDATE usuario SET nome = ?, senha = ?, telefone = ?, id_endereco = ? WHERE cpf = ?";
        final String sqlUpdateEndereco = "UPDATE endereco SET logradouro = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, cep = ? WHERE id_endereco = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, senha)) {

            PreparedStatement psEndereco = connection.prepareStatement(sqlUpdateEndereco);
            psEndereco.setString(1, paciente.getEndereco().getLogradouro());
            psEndereco.setInt(2, paciente.getEndereco().getNumero());
            psEndereco.setString(3, paciente.getEndereco().getBairro());
            psEndereco.setString(4, paciente.getEndereco().getCidade());
            psEndereco.setString(5, paciente.getEndereco().getEstado());
            psEndereco.setString(6, paciente.getEndereco().getCep());
            psEndereco.setLong(7, paciente.getEndereco().getId_endereco());
            psEndereco.executeUpdate();
            System.out.println("Endereço Atualizado");

            PreparedStatement ps = connection.prepareStatement(sqlUpdateUsuario);
            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getSenha());
            ps.setLong(3, paciente.getTelefone());
            ps.setLong(4, paciente.getEndereco().getId_endereco());
            ps.setLong(5, Long.parseLong(cpf));
            ps.executeUpdate();
            System.out.println("Paciente Atualizado");

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
