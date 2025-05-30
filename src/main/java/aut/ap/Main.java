package aut.ap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        setUpSessionFactory();

        String command = "";

        while(!command.equals("0")) {
        System.out.println("Sign up / Login:");
        command = input.nextLine();

            try {
                if (command.equals("Sign up") || command.equals("S")) {

                    System.out.println("First Name: ");
                    String firstName = input.nextLine();

                    System.out.println("Last Name: ");
                    String lastName = input.nextLine();

                    System.out.println("Age: ");
                    int age = input.nextInt();

                    if (age < 0)
                        throw new IllegalArgumentException("Age cannot be negative");

                    input.nextLine();

                    System.out.println("Email: ");
                    String email = input.nextLine();

                    if (checkEmail1(email))
                        throw new IllegalArgumentException("An account with this email already exists");

                    System.out.println("Password: ");
                    String password = input.nextLine();

                    if (password.length() < 8)
                        throw new IllegalArgumentException("Weak password");

                    User user = new User(firstName, lastName, age, email, password);
                    addUser(user);
                }


                if (command.equals("Login") || command.equals("L")) {
                    System.out.println("Email:");
                    String email = input.nextLine();

                    System.out.println("Password:");
                    String password = input.nextLine();

                    if (!(checkEmail2(email)))
                        throw new IllegalArgumentException("You have to Sign up");

                    if (password.length() < 8)
                        throw new IllegalArgumentException("Password should be at least 8 characters");

                    User user = getUser(email, password);

                    if (user == null)
                        throw new IllegalArgumentException("User not found");

                    System.out.println("Welcome," + user.getFirstName() + " " + user.getLastName());

                }
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            finally {
                sessionFactory.close();
            }
        }

    }

    private static void setUpSessionFactory() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private static boolean checkEmail1(String email) {
        List<User> allUsers = sessionFactory.fromTransaction(session ->
                session.createNativeQuery("select * from users" , User.class)
                        .getResultList());

        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private static void addUser(User newUser) {
        sessionFactory.inTransaction(session -> {
            session.persist(newUser);
        });
    }

    private static boolean checkEmail2(String email) {
        List<User> allUsers = sessionFactory.fromTransaction(session ->
                session.createNativeQuery("select * from users" , User.class)
                        .getResultList());

        boolean found = false;
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                found = true;
            }
        }
        return found;
    }

    private static User getUser(String email , String password) {
        List<User> allUsers = sessionFactory.fromTransaction(session ->
                session.createNativeQuery("select * from users" , User.class)
                        .getResultList());
        for (User user : allUsers) {
            if(user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}