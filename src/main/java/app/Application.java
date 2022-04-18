package app;

import app.entities.User;
import app.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        List<String> list = Arrays.asList(strings);
        if (list.contains("install")) {
            jdbcTemplate.execute("DROP TABLE tasks IF EXISTS");
            jdbcTemplate.execute("DROP TABLE categories IF EXISTS");
            jdbcTemplate.execute("DROP TABLE users IF EXISTS");
            jdbcTemplate.execute("DROP TABLE users_roles IF EXISTS");
            jdbcTemplate.execute("DROP TABLE roles IF EXISTS");

            jdbcTemplate.execute(
                    "CREATE TABLE categories ("+
                            "category_id IDENTITY PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR(20) DEFAULT '' " +
                            ");" );
            log.info("categories TABLE CREATED");

            jdbcTemplate.update("INSERT INTO categories(name) values('todo'); ");
            jdbcTemplate.update("INSERT INTO categories(name) values('WIP');  ");
            jdbcTemplate.update("INSERT INTO categories(name) values('done'); ");
            log.info("categories TABLE POPULATED");

            jdbcTemplate.execute(
                    "CREATE TABLE tasks (" +
                            "   task_id       IDENTITY PRIMARY KEY AUTO_INCREMENT," +
                            "   category      INTEGER NOT NULL," +
                            "   content       VARCHAR(500) NOT NULL," +
                            "   creation_date DATE DEFAULT TODAY(), " +
                            "   end_date      DATE DEFAULT NULL, " +
                            "   FOREIGN KEY(category) REFERENCES categories(category_id)"+
                            ");");
            log.info("tasks TABLE CREATED");

            jdbcTemplate.update("INSERT INTO tasks (category, content) values(3, 'finir le tp 1'); ");
            jdbcTemplate.update("INSERT INTO tasks (category, content) values(2, 'finir le tp 2'); ");
            jdbcTemplate.update("INSERT INTO tasks (category, content) values(1, 'finir le tp 3'); ");
            jdbcTemplate.update("INSERT INTO tasks (category, content) values(1, 'finir le tp 3'); ");
            log.info("tasks TABLE POPULATED");

            jdbcTemplate.execute(
                    "CREATE TABLE users ("+
                            "id IDENTITY PRIMARY KEY AUTO_INCREMENT," +
                            "username VARCHAR(20)," +
                            "password VARCHAR(100)" +
                            ");" );
            log.info("users TABLE CREATED");

            jdbcTemplate.execute(
                    "CREATE TABLE roles ("+
                            "id IDENTITY PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR(20)" +
                            ");" );
            log.info("roles TABLE CREATED");

            jdbcTemplate.execute(
                    "CREATE TABLE users_roles ("+
                            "user_id INTEGER," +
                            "role_id INTEGER," +
                            "CONSTRAINT user_role_pk PRIMARY KEY (user_id, role_id)," +
                            "CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)," +
                            "CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)" +
                            ");" );
            log.info("users_roles TABLE CREATED");
        }

        if (list.contains("testCat")) {
            List<String> categories;
            String sql = "select * from categories";
            categories = jdbcTemplate.query(sql,
                    (rs, rowNum) ->
                    {
                        return new String(rs.getString("name"));
                    }
            );
            log.info(categories.toString());
        }

        if (list.contains("testTask")) {
            List<String> categories;
            String sql = "select * from categories";

            categories = jdbcTemplate.query(sql,
                    (rs, rowNum) ->
                    {
                        return new String(rs.getString("name"));
                    }
            );

            sql = "SELECT * FROM tasks";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            for (Map row : rows) {
                log.info(row.get("content").toString() + " (" + categories.get((Integer)row.get("category")-1) + ")");
            }

        }


    }
}
