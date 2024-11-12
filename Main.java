import java.io.*;
import java.util.*;

class Employee implements Serializable {
    int id;
    String name;
    int salary;

    Employee(int id, String name, int salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return String.format("ID: %-5d | Name: %-15s | Salary: %-10d", id, name, salary);
    }
}

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static Scanner sc2 = new Scanner(System.in);
    private static List<Employee> cl = new ArrayList<>();
    private static File file = new File("Employee.txt");

    public static void main(String[] args) throws Exception {
        if (file.isFile()) {
            loadEmployeesFromFile();
        }

        int choice;
        do {
            displayMenu();
            choice = sc.nextInt();
            handleUserChoice(choice);
        } while (choice != 0);

        sc.close();
        sc2.close();
    }

    // Function to display the menu
    public static void displayMenu() {
        System.out.println("\n========== Employee Database Menu ==========");
        System.out.print(
                "\n1. INSERT\t2. Display\t3. SEARCH\n" +
                        "4. UPDATE\t5. DELETE\t6. SORT - On screen\n" +
                        "7. SORT - In file\t0. EXIT\n\n");
        System.out.print("---> Enter choice: ");
    }

    // Function to handle user choice
    public static void handleUserChoice(int choice) throws IOException {
        switch (choice) {
            case 1:
                insertEmployee();
                break;
            case 2:
                displayEmployees();
                break;
            case 3:
                searchEmployee();
                break;
            case 4:
                updateEmployee();
                break;
            case 5:
                deleteEmployee();
                break;
            case 6:
                sortOnScreen();
                break;
            case 7:
                sortInFile();
                break;
            case 0:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
                break;
        }
    }

    // Function to insert employees
    public static void insertEmployee() throws IOException {
        System.out.print("--> Enter how many employees you want: ");
        int n = sc.nextInt();
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.println("For employee " + (i + 1));

            boolean unique = false;
            int id = 0;

            // Ensure data with only unique ID is inserted
            while (!unique) {
                System.out.print("-> Enter Employee ID: ");
                id = sc.nextInt();
                unique = true;

                for (Employee emp : cl) {
                    if (emp.id == id) {
                        System.out.println("Employee ID already exists! Please enter a unique ID.");
                        unique = false;
                        break;
                    }
                }
            }

            System.out.print("-> Enter Employee Name: ");
            String name = sc2.nextLine();

            System.out.print("-> Enter Employee Salary: ");
            int salary = sc.nextInt();
            System.out.println();

            cl.add(new Employee(id, name, salary));
        }
        saveEmployeesToFile();
    }

    // Function to display employees
    public static void displayEmployees() {
        if (cl.size() != 0) {
            System.out.println("----------------------------------------");
            System.out.println(String.format("%-5s | %-15s | %-10s", "ID", "Name", "Salary"));
            System.out.println("----------------------------------------");
            for (int i = 0; i < cl.size(); i++) {
                System.out
                        .println(String.format("%-5d | %-15s | %-10d", cl.get(i).id, cl.get(i).name, cl.get(i).salary));
            }
            System.out.println("----------------------------------------");
        } else {
            System.out.println("No employees registered...!");
        }
    }

    // Function to search for an employee
    public static void searchEmployee() {
        System.out.print("-> Enter the Employee ID: ");
        int id = sc.nextInt();
        boolean found = false;

        System.out.println("------------------------------------------------------");
        for (Employee e : cl) {
            if (e.id == id) {
                System.out.println(e);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No Employee found with ID: " + id);
        }
        System.out.println("------------------------------------------------------\n");
    }

    // Function to update an employee
    public static void updateEmployee() throws IOException {
        System.out.print("-> Enter the Employee ID: ");
        int id = sc.nextInt();
        boolean updated = false;

        for (ListIterator<Employee> it = cl.listIterator(); it.hasNext();) {
            Employee e = it.next();
            if (e.id == id) {
                System.out.print("-> Enter new name: ");
                String name = sc2.nextLine();
                System.out.print("-> Enter new salary: ");
                int salary = sc.nextInt();
                it.set(new Employee(id, name, salary));
                updated = true;
                break;
            }
        }
        if (updated) {
            saveEmployeesToFile();
            System.out.println("Employee updated successfully with ID: " + id);
        } else {
            System.out.println("No Employee found with ID: " + id);
        }
    }

    // Function to delete an employee
    public static void deleteEmployee() throws IOException {
        System.out.print("-> Enter the Employee ID: ");
        int id = sc.nextInt();
        boolean found = false;

        for (ListIterator<Employee> it = cl.listIterator(); it.hasNext();) {
            Employee e = it.next();
            if (e.id == id) {
                it.remove();
                found = true;
                break;
            }
        }
        if (found) {
            saveEmployeesToFile();
            System.out.println("Employee deleted successfully.");
        } else {
            System.out.println("No Employee found with ID: " + id);
        }
    }

    // Function to sort employees on screen
    public static void sortOnScreen() {
        System.out.println("1. Sort by ID\n2. Sort by Name\n3. Sort by Salary\n");
        System.out.println("---> Enter your choice : ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                cl.sort(Comparator.comparingInt(e -> e.id));
                break;
            case 2:
                cl.sort(Comparator.comparing(e -> e.name));
                break;
            case 3:
                cl.sort(Comparator.comparingInt(e -> e.salary));
                break;
            default:
                System.out.println("Invalid choice!");
        }
        displayEmployees();
    }

    // Function to sort employees in file
    public static void sortInFile() throws IOException {
        cl.sort(Comparator.comparingInt(e -> e.id));
        saveEmployeesToFile();
        displayEmployees();
    }

    // Function to load employees from file
    public static void loadEmployeesFromFile() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        cl = (ArrayList<Employee>) ois.readObject();
        ois.close();
    }

    // Function to save employees to file
    public static void saveEmployeesToFile() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(cl);
        oos.close();
    }
}
