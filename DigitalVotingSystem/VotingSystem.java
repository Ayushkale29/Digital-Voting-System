import java.io.BufferedReader;
import java.io.InputStreamReader;

public class VotingSystem {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Welcome to the Voting System");
            System.out.println("1. Admin Login");
            System.out.println("2. Public User Login");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(br.readLine());

            if (choice == 1) {
                // Admin functionality
                Admin admin = new Admin();
                System.out.print("Admin username: ");
                String username = br.readLine();
                System.out.print("Admin password: ");
                String password = br.readLine();

                if (admin.login(username, password)) {
                    System.out.println("Welcome, Admin " + username);
                    while (true) {
                        System.out.println("1. Add Candidate");
                        System.out.println("2. View Results");
                        System.out.println("3. Register New Admin User");
                        System.out.println("4. Display Admin Users");
                        System.out.println("5. Display Candidates");
                        System.out.println("6. Exit");
                        System.out.print("Enter your choice: ");
                        int adminChoice = Integer.parseInt(br.readLine());

                        if (adminChoice == 1) {
                            System.out.print("Enter candidate name: ");
                            String candidateName = br.readLine();
                            admin.addCandidate(candidateName);
                        } else if (adminChoice == 2) {
                            admin.displayResults();
                        } else if (adminChoice == 3) {
                            System.out.print("Enter new admin username: ");
                            String newAdminUsername = br.readLine();
                            System.out.print("Enter new admin password: ");
                            String newAdminPassword = br.readLine();
                            admin.registerAdminUser(newAdminUsername, newAdminPassword);
                        } else if (adminChoice == 4) {
                            admin.displayAdminUsers();
                        } else if (adminChoice == 5) {
                            admin.displayCandidates();
                        } else if (adminChoice == 6) {
                            break;
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    }
                } else {
                    System.out.println("Invalid admin credentials.");
                }
            } else if (choice == 2) {
                // Public user functionality
                PublicUser publicUser = new PublicUser();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.print("Enter your choice: ");
                int userChoice = Integer.parseInt(br.readLine());

                if (userChoice == 1) {
                    System.out.print("Enter new username: ");
                    String username = br.readLine();
                    System.out.print("Enter new password: ");
                    String password = br.readLine();
                    publicUser.registerUser(username, password);
                }

                // Login process
                System.out.print("Login username: ");
                String loginUsername = br.readLine();
                System.out.print("Password: ");
                String loginPassword = br.readLine();

                if (publicUser.login(loginUsername, loginPassword)) {
                    System.out.println("Logged in as " + loginUsername);
                    System.out.print("Enter candidate name to vote: ");
                    String candidateName = br.readLine();
                    publicUser.vote(loginUsername, candidateName);
                } else {
                    System.out.println("Invalid user credentials.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
