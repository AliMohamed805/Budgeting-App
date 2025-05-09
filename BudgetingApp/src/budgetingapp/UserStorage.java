package budgetingapp;

import java.io.*;
import java.util.*;
import budgetingapp.BudgetingDataHandling.User;

/**
 * Utility class for saving and loading users using serialization.
 */
public class UserStorage {
    private static final String FILENAME = "users_list.ser";

    /**
     * Saves the users' map to a file.
     * @param users the map of users to save
     */
    public static void saveUsers(Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Loads the users' map from a file.
     * @return the loaded map of users, or an empty map if not found
     */
    @SuppressWarnings("unchecked")
    public static Map<String, User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            return (Map<String, User>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}