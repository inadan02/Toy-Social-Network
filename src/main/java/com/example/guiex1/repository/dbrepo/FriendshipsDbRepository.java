package com.example.guiex1.repository.dbrepo;

import com.example.guiex1.domain.Entity;
import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Tuple;
import com.example.guiex1.domain.Validator;
import com.example.guiex1.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class FriendshipsDbRepository implements Repository<Tuple<Long,Long>, Prietenie> {
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;
    private Iterable<Prietenie> friends;

    public FriendshipsDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Prietenie findOne(Tuple<Long, Long> t) {
//        Prietenie pr=new Prietenie();
        Prietenie pValidate = new Prietenie(LocalDateTime.now());

        pValidate.setId(t);
        validator.validate(pValidate);
        String sql = "select * from friends where id1=? and id2=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, t.getLeft().intValue());
            ps.setInt(2, t.getRight().intValue());
            ResultSet resultSet = ps.executeQuery();
            LocalDateTime ld = null;
            if(resultSet.next()) {
                if (resultSet.getDate("date") != null &&
                        resultSet.getTime("time") != null)
                    ld = LocalDateTime.of(resultSet.getDate("date").toLocalDate(),
                            resultSet.getTime("time").toLocalTime());

                Prietenie p = new Prietenie(ld);

                p.setId(new Tuple(t.getLeft(), t.getRight()));
                p.setType(resultSet.getString("type"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> pritenii = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friends");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");

                LocalDateTime ld = null;
                if(resultSet.getDate("date") != null &&
                        resultSet.getTime("time") != null )
                    ld = LocalDateTime.of(resultSet.getDate("date").toLocalDate(),
                            resultSet.getTime("time").toLocalTime());

                Prietenie p = new Prietenie(ld);
                p.setId(new Tuple<>(id1,id2));
                p.setType(resultSet.getString("type"));
                pritenii.add(p);
            }
            return pritenii;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pritenii;
    }

    @Override
    public Prietenie save(Prietenie prietenie) {
        String sql1 = "select * from friends where id1=? and id2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql1)) {
            ps.setInt(1, prietenie.getId().getLeft().intValue());
            ps.setInt(2, prietenie.getId().getRight().intValue());

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next() || Objects.equals(prietenie.getId1(), prietenie.getId2()))
                return null;
//                throw new Exception("Prietenie existenta!");

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        String sql = "insert into friends (id1, id2, date, time ,type) values (?, ?, ?, ?,?)";


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, prietenie.getId().getLeft().intValue());
            ps.setInt(2, prietenie.getId().getRight().intValue());
            ps.setDate(3, Date.valueOf(prietenie.getDate().toLocalDate()));
            ps.setTime(4, Time.valueOf(prietenie.getDate().toLocalTime()));
            ps.setString(5, prietenie.getType());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenie;
    }

    @Override
    public Prietenie delete(Tuple<Long, Long> t) {
        Prietenie pValidate = new Prietenie(LocalDateTime.now());
        pValidate.setId(t);
        String sql = "delete from friends where id1=? and id2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, t.getLeft().intValue());
            ps.setInt(2, t.getRight().intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(pValidate);
        return pValidate;
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        return Optional.empty();
    }
}
