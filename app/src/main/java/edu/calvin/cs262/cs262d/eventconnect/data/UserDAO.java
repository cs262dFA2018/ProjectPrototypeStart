package edu.calvin.cs262.cs262d.eventconnect.data;

/**
 * Copied directly from server side
 *
 * This class implements an User Data-Access Object (DAO) class for the Users relation.
 * This provides an object-oriented way to represent and manipulate User "objects" from
 * the traditional (non-object-oriented) EventConnect database.
 *
 */
public class UserDAO {
    private int id;
    private String username, password;


    public UserDAO() {
        // The JSON marshaller used by Endpoints requires this default constructor.
    }

    public UserDAO(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
