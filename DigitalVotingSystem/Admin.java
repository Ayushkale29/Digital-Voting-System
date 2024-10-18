import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Admin {
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
    private HashMap<String, String> adminUsers; // Admin username -> password

    public Admin() {
        candidates = new ArrayList<>();
        adminUsers = new HashMap<>();
        loadCandidates();  // Load candidates from file
        loadAdminUsers();  // Load admin users from file

        // If no admin users found, prompt for admin creation
        if (adminUsers.isEmpty()) {
            createInitialAdmin();
        }
    }

    // Method to create the first admin user when no admin exists
    private void createInitialAdmin() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("No existing admin users found. Please create the first admin user.");
            System.out.print("Enter admin username: ");
            String username = br.readLine();
            System.out.print("Enter admin password: ");
            String password = br.readLine();

            // Store the new admin user
            adminUsers.put(username, password);
            saveAdminUsers();  // Save new admin user to file
            System.out.println("Admin user " + username + " created successfully.");
        } catch (IOException e) {
            System.out.println("Error creating initial admin: " + e.getMessage());
        }
    }

    // Method to add additional admin users
    public void registerAdminUser(String username, String password) {
        if (!adminUsers.containsKey(username)) {
            adminUsers.put(username, password);
            saveAdminUsers();
            System.out.println("Admin user " + username + " registered successfully.");
        } else {
            System.out.println("Admin username already exists!");
        }
    }

    // Method to display registered admin users
    public void displayAdminUsers() {
        System.out.println("List of Admin Users:");
        for (String username : adminUsers.keySet()) {
            System.out.println(username);
        }
    }

    // Method for admin login
    public boolean login(String username, String password) {
        return password.equals(adminUsers.get(username));
    }

    // Method to add a candidate
    public void addCandidate(String name) {
        candidates.add(new Candidate(name));
        saveCandidates();  // Save to file
        System.out.println("Candidate " + name + " added.");
    }

    // Method to display voting results
    public void displayResults() {
        System.out.println("Voting Results:");
        for (Candidate candidate : candidates) {
            System.out.println(candidate.getName() + ": " + candidate.getVotes() + " votes");
        }
    }

    // New method to display all candidates added
    public void displayCandidates() {
        System.out.println("List of Candidates:");
        for (Candidate candidate : candidates) {
            System.out.println(candidate.getName());
        }
    }

    // Save admin users to file
    private void saveAdminUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("admin_users.txt"))) {
            for (String username : adminUsers.keySet()) {
                writer.write(username + ":" + adminUsers.get(username));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving admin users: " + e.getMessage());
        }
    }

    // Load admin users from file
    private void loadAdminUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("admin_users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    adminUsers.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing admin users found, starting fresh.");
        }
    }

    // Save candidates to file
    private void saveCandidates() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("candidates.txt"))) {
            for (Candidate candidate : candidates) {
                writer.write(candidate.getName() + ":" + candidate.getVotes());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving candidates: " + e.getMessage());
        }
    }

    // Load candidates from file
    private void loadCandidates() {
        try (BufferedReader reader = new BufferedReader(new FileReader("candidates.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                Candidate candidate = new Candidate(parts[0]);
                candidates.add(candidate);  // Load candidates (votes are loaded by public user part)
            }
        } catch (IOException e) {
            System.out.println("No existing candidates found, starting fresh.");
        }
    }

    public static void main(String[] args) {
        Admin admin = new Admin();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("1. Register New Admin User");
            System.out.println("2. Login as Admin");
            System.out.print("Enter your choice: ");
            int initialChoice = Integer.parseInt(br.readLine());

            if (initialChoice == 1) {
                // Create new admin user
                System.out.print("Enter new admin username: ");
                String newAdminUsername = br.readLine();
                System.out.print("Enter new admin password: ");
                String newAdminPassword = br.readLine();
                admin.registerAdminUser(newAdminUsername, newAdminPassword);
            }

            // Admin login
            System.out.print("Admin username: ");
            String username = br.readLine();
            System.out.print("Admin password: ");
            String password = br.readLine();

            // Check login credentials
            if (!admin.login(username, password)) {
                System.out.println("Invalid admin username or password.");
                return;
            }

            // Admin logged in successfully
            System.out.println("Welcome, Admin " + username);

            while (true) {
                System.out.println("1. Add Candidate");
                System.out.println("2. View Results");
                System.out.println("3. Register New Admin User");
                System.out.println("4. Display Admin Users");
                System.out.println("5. Display Candidates"); // New option to display candidates
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                int choice = Integer.parseInt(br.readLine());

                if (choice == 1) {
                    System.out.print("Enter candidate name: ");
                    String candidateName = br.readLine();
                    admin.addCandidate(candidateName);
                } else if (choice == 2) {
                    admin.displayResults();
                } else if (choice == 3) {
                    System.out.print("Enter new admin username: ");
                    String newAdminUsername = br.readLine();
                    System.out.print("Enter new admin password: ");
                    String newAdminPassword = br.readLine();
                    admin.registerAdminUser(newAdminUsername, newAdminPassword);
                } else if (choice == 4) {
                    admin.displayAdminUsers();
                } else if (choice == 5) {  // New case to display candidates
                    admin.displayCandidates();
                } else if (choice == 6) {
                    break;
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}