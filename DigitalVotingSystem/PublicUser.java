import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class PublicUser {
    static class Candidate {
        private String name;
        private int votes;

        public Candidate(String name) {
            this.name = name;
            this.votes = 0;
        }

        public String getName() {
            return name;
        }

        public int getVotes() {
            return votes;
        }

        public void addVote() {
            votes++;
        }
    }

    private List<Candidate> candidates;
    private HashMap<String, String> users; // username -> password
    private HashMap<String, Boolean> voters; // voter -> has voted

    public PublicUser() {
        candidates = new ArrayList<>();
        voters = new HashMap<>();
        users = new HashMap<>();
        loadCandidates(); // Load candidates
        loadUsers();      // Load registered users
        loadVoters();     // Load voting status of users
    }

    public void registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, password);
            voters.put(username, false); // Set initial vote status as false
            saveUsers();
            saveVoters();
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Username already exists!");
        }
    }

    public boolean login(String username, String password) {
        return password.equals(users.get(username));
    }

    public void vote(String voterName, String candidateName) {
        if (voters.getOrDefault(voterName, false)) {
            System.out.println("You have already voted!");
            return;
        }

        for (Candidate candidate : candidates) {
            if (candidate.getName().equalsIgnoreCase(candidateName)) {
                candidate.addVote();
                voters.put(voterName, true);
                saveVotes();
                saveVoters();
                System.out.println("Vote cast for " + candidateName);
                return;
            }
        }
        System.out.println("Candidate not found!");
    }

    private void saveVotes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("candidates.txt"))) {
            for (Candidate candidate : candidates) {
                writer.write(candidate.getName() + ":" + candidate.getVotes());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving votes: " + e.getMessage());
        }
    }

    private void loadCandidates() {
        try (BufferedReader reader = new BufferedReader(new FileReader("candidates.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                Candidate candidate = new Candidate(parts[0]);
                candidate.votes = Integer.parseInt(parts[1]);
                candidates.add(candidate);
            }
        } catch (IOException e) {
            System.out.println("No candidates found.");
        }
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                users.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            System.out.println("No users found.");
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (String username : users.keySet()) {
                writer.write(username + ":" + users.get(username));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void loadVoters() {
        try (BufferedReader reader = new BufferedReader(new FileReader("voters.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                voters.put(parts[0], Boolean.parseBoolean(parts[1]));
            }
        } catch (IOException e) {
            System.out.println("No voting data found.");
        }
    }

    private void saveVoters() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("voters.txt"))) {
            for (String voter : voters.keySet()) {
                writer.write(voter + ":" + voters.get(voter));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving voters: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PublicUser publicUser = new PublicUser();
        Scanner scanner = new Scanner(System.in);

        // Registration or Login
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) {
            // Registration
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            publicUser.registerUser(username, password);
        }

        // Login process
        System.out.print("Login username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Password: ");
        String loginPassword = scanner.nextLine();

        if (!publicUser.login(loginUsername, loginPassword)) {
            System.out.println("Invalid username or password. Exiting.");
            return;
        }

        // Voting
        System.out.println("Enter the name of the candidate you want to vote for:");
        String candidateName = scanner.nextLine();
        publicUser.vote(loginUsername, candidateName);

        scanner.close();
    }
}
