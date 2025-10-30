package test;

import dao.MotorcyclistDao;
import model.Motorcyclist;
import java.util.List;
import java.util.Scanner;
import java.sql.Date;

public class MotorcyclistTester {
    public static void main(String[] args) {
        MotorcyclistDao dao = new MotorcyclistDao();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n1. Add Motorcyclist");
            System.out.println("2. Update Motorcyclist");
            System.out.println("3. Delete Motorcyclist");
            System.out.println("4. Find by ID");
            System.out.println("5. List All");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1: {
                    // Add new rider (only added DOB input)
                    Motorcyclist rider = new Motorcyclist();
                    System.out.print("Enter rider ID: ");
                    rider.setId(scanner.nextInt());
                    scanner.nextLine();
                    System.out.print("Enter name: ");
                    rider.setName(scanner.nextLine());
                    System.out.print("Enter license number: ");
                    rider.setLicenseNumber(scanner.nextLine());
                    System.out.print("Enter phone: ");
                    rider.setPhone(scanner.nextLine());
                    
                    // NEW: Date of birth input
                    System.out.print("Enter date of birth (yyyy-mm-dd): ");
                    String dobString = scanner.nextLine();
                    Date dob = Date.valueOf(dobString);
                    rider.setDateOfBirth(dob);
                    
                    System.out.print("Enter cooperative ID: ");
                    rider.setCooperativeId(scanner.nextInt());
                    
                    dao.add(rider);
                    System.out.println("Added successfully!");
                    break;
                }
                    
                case 2: {
                    // Update rider (only added DOB input)
                    Motorcyclist rider = new Motorcyclist();
                    System.out.print("Enter rider ID to update: ");
                    rider.setId(scanner.nextInt());
                    scanner.nextLine();
                    
                    System.out.print("Enter new name: ");
                    rider.setName(scanner.nextLine());
                    System.out.print("Enter new license number: ");
                    rider.setLicenseNumber(scanner.nextLine());
                    System.out.print("Enter new phone: ");
                    rider.setPhone(scanner.nextLine());
                    
                    // NEW: Date of birth input
                    System.out.print("Enter new date of birth (yyyy-mm-dd): ");
                    String dobString = scanner.nextLine();
                    Date dob = Date.valueOf(dobString);
                    rider.setDateOfBirth(dob);
                    
                    System.out.print("Enter new cooperative ID: ");
                    rider.setCooperativeId(scanner.nextInt());
                    scanner.nextLine();
                    
                    dao.update(rider);
                    System.out.println("Updated successfully!");
                    break;
                }
                    
                case 3: {
    // Delete rider (updated to match DAO)
    System.out.print("Enter rider ID to delete: ");
    int riderId = scanner.nextInt();
    scanner.nextLine(); // Consume newline
    
    // Option 1: If your DAO has delete(int riderId)
    dao.delete(riderId); // This matches your DAO implementation
    
    // Option 2: If you prefer to use Motorcyclist object (less efficient)
    // Motorcyclist rider = new Motorcyclist();
    // rider.setId(riderId);
    // dao.delete(rider); // You would need to add this method to DAO
    
    System.out.println("Deleted successfully!");
    break;
} 
               case 4: {
    System.out.print("Enter rider ID to search: ");
    int riderId = scanner.nextInt();
    scanner.nextLine();
    
    Motorcyclist found = dao.findMotorcyclistById(riderId);
    if (found != null) {
        System.out.println("\nFound Rider:");
        System.out.println("ID: " + found.getId());
        System.out.println("Name: " + found.getName());
        System.out.println("License: " + found.getLicenseNumber());
        System.out.println("Phone: " + found.getPhone());
        System.out.println("Age: " + found.getAge());  // Changed to show age
        System.out.println("Cooperative: " + found.getCooperativeName());
    } else {
        System.out.println("Rider not found!");
    }
    break;
}
                    
                case 5: {
    List<Motorcyclist> riders = dao.findAllMotorcyclist();
    if (riders != null && !riders.isEmpty()) {
        System.out.println("\nAll Motorcyclists:");
        System.out.printf("%-5s %-20s %-15s %-12s %-5s %-20s%n", 
                        "ID", "Name", "License", "Phone", "Age", "Cooperative");
        for (Motorcyclist m : riders) {
            System.out.printf("%-5d %-20s %-15s %-12s %-5d %-20s%n",
                            m.getId(),
                            m.getName(),
                            m.getLicenseNumber(),
                            m.getPhone(),
                            m.getAge(),  // Changed to show age
                            m.getCooperativeName());
        }
    } else {
        System.out.println("No riders found!");
    }
    break;
}
                    
                case 6:
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}