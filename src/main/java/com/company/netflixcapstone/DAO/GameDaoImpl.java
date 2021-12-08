package com.company.netflixcapstone.DAO;

import com.company.netflixcapstone.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GameDaoImpl implements GameDAO {

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_GAME_SQL =
            "insert into game (game_id, title , esrb_rating , description , price, studio, quantity) values (?, ?, ?, ?, ?,?,?)";

    private static final String SELECT_GAME_SQL =
            "select * from game where game_id = ?";

    private static final String SELECT_ALL_GAMES_SQL =
            "select * from game";

    private static final String UPDATE_GAME_SQL =
            "update game set title = ?, esrb_rating = ?, description  = ?, price = ?, studio = ?, quantity = ?, where game_id = ?";

    private static final String DELETE_GAME =
            "delete from game where game_id = ?";


    @Autowired
    public GameDaoImpl(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game create(Game game) {
        jdbcTemplate.update(
                INSERT_GAME_SQL,
                game.getTitle(),
                game.getEsrb_rating(),
                game.getDescription(),
                game.getPrice(),
                game.getStudio(),
                game.getQuantity());

        int id = jdbcTemplate.queryForObject("select LAST_GAME_ID()", Integer.class);

        game.setGame_id(id);

        return game;
    }

    @Override
    public Game read(int id) {

        try {
            return jdbcTemplate.queryForObject(SELECT_GAME_SQL, this::mapRowToGame, id);
        } catch (EmptyResultDataAccessException e) {
            // if there is no match for this game id, return null
            return null;
        }
    }

    @Override
    public List<Game> readAll() {
        return jdbcTemplate.query(SELECT_ALL_GAMES_SQL, this::mapRowToGame);
    }

    @Override
    public void update(Game game) {
        jdbcTemplate.update(
                UPDATE_GAME_SQL,
                game.getTitle(),
                game.getEsrb_rating(),
                game.getDescription(),
                game.getPrice(),
                game.getStudio(),
                game.getQuantity());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_GAME, id);
    }
    private Game mapRowToGame(ResultSet rs, int rowNum) throws SQLException {
        Game game = new Game();
        game.setGame_id(rs.getInt("game_id"));
        game.setTitle(rs.getString("title"));
        game.setEsrb_rating(rs.getString("esrb_rating"));
        game.setDescription(rs.getString("description "));
        game.setPrice(rs.getDouble("price "));
        game.setStudio(rs.getString("studio"));
        game.setQuantity(rs.getInt("quantity"));

        return game;
    }
}
