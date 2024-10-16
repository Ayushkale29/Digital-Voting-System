import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class VotingSystem {
    // Candidate class
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

    public VotingSystem() {
        candidates = new ArrayList<>();
        voters = new HashMap<>();
        users = new HashMap<>();
    }

    public void addCandidate(String name) {
        candidates.add(new Candidate(name));
        System.out.println("Candidate " + name + " added.");
    }

    public void registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, password);
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Username already exists!");
        }
    }

    public boolean login(String username, String password) {
        return password.equals(users.get(username));
    }

    public void vote(String voterName, String candidateName) {
        if (voters.containsKey(voterName)) {
            System.out.println("You have already voted!");
            return;
        }

        for (Candidate candidate : candidates) {
            if (candidate.getName().equalsIgnoreCase(candidateName)) {
                candidate.addVote();
                voters.put(voterName, true);
                System.out.println("Vote cast for " + candidateName);
                return;
            }
        }
        System.out.println("Candidate not found!");
    }

    public void displayResults() {
        System.out.println("Voting Results:");
        for (Candidate candidate : candidates) {
            System.out.println(candidate.getName() + ": " + candidate.getVotes() + " votes");
        }
    }

    public void saveResults() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"))) {
            for (Candidate candidate : candidates) {
                writer.write(candidate.getName() + ":" + candidate.getVotes());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving results: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        VotingSystem votingSystem = new VotingSystem();
        Scanner scanner = new Scanner(System.in);

        // Adding candidates
        System.out.println("Enter candidate names (type 'done' when finished):");
        while (true) {
            String candidateName = scanner.nextLine();
            if (candidateName.equalsIgnoreCase("done")) {
                break;  // Exit the loop when "done" is entered
            }
            votingSystem.addCandidate(candidateName);
        }

        // User registration
        System.out.print("Register a new user (username): ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        votingSystem.registerUser(username, password);

        // User login
        System.out.print("Login (username): ");
        String loginUsername = scanner.nextLine();
        System.out.print("Password: ");
        String loginPassword = scanner.nextLine();

        if (!votingSystem.login(loginUsername, loginPassword)) {
            System.out.println("Invalid username or password. Exiting.");
            return;
        }

        // Voting process
        System.out.print("Enter the name of the candidate you want to vote for: ");
        String candidateName = scanner.nextLine();
        votingSystem.vote(loginUsername, candidateName);
        
        // After voting
        System.out.println("Thank you for voting!");
        votingSystem.saveResults();
        votingSystem.displayResults(); // Optionally show results after voting

        scanner.close();
    }
}
