package utils;

/**
 * Registration data loaded from testdata/user.json. The email is generated per test.
 */
public class User {
    public String name;
    public String email;
    public String password;
    public String title;
    public String day;
    public String month;
    public String year;
    public String firstName;
    public String lastName;
    public String company;
    public String address;
    public String address2;
    public String country;
    public String state;
    public String city;
    public String zipcode;
    public String mobile;

    /** Loads the template user and assigns a unique, timestamp-based email. */
    public static User newUniqueUser() {
        User user = JsonReader.read("user.json", User.class);
        user.email = "qa." + System.currentTimeMillis() + "." + Thread.currentThread().getId() + "@example.com";
        return user;
    }
}
