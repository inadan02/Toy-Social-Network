package com.example.guiex1.repository.dbrepo;

import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.Validator;
import com.example.guiex1.repository.Repository;


import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UtilizatorDbRepository implements Repository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;

    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Utilizator findOne(Long id) {
        String sql = "select first_name, last_name, email, parola from users where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id.intValue());
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) return null;
            return new Utilizator(resultSet.getString("first_name"), resultSet.getString("last_name"),
                    resultSet.getString("email"), resultSet.getString("parola"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Utilizator findByName(String firstName,String lastName) {
        String sql = "select id,first_name, last_name, email, parola from users where first_name=? and last_name=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) return null;
            var usr= new Utilizator(resultSet.getString("first_name"), resultSet.getString("last_name"),
                    resultSet.getString("email"), resultSet.getString("parola"));
            usr.setId(resultSet.getLong("id"));
            return usr;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Utilizator findByEmailPass(String email,String passw) {
        String sql = "select id,first_name, last_name, email, parola from users where email=? or parola=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, passw);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) return null;
            var usr= new Utilizator(resultSet.getString("first_name"), resultSet.getString("last_name"),
                    resultSet.getString("email"), resultSet.getString("parola"));
            usr.setId(resultSet.getLong("id"));
            return usr;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");
                Utilizator utilizator = new Utilizator(firstName, lastName,email,parola);
                utilizator.setId(id);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Utilizator save(Utilizator entity) {
        String sql = "insert into users (first_name, last_name,email,parola) values (?, ?,?,?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return entity;
        }
        return entity;
    }
    public Long login(Utilizator entity) {
        String sql = "select id from users where email=? and parola=?";
//        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getPassword());

            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()) throw new Exception("User not found!");
            return resultSet.getLong("id");
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }
    @Override
    public Utilizator delete(Long aLong) {
        String sql = "delete from users where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        return Optional.empty();
    }
}
