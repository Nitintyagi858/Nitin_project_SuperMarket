import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

     class SuperMarket {
    private static final String FILENAME = "items.txt";
    private List<Item> items = new ArrayList<>();

    public static void main(String[] args) {
        SuperMarket market = new SuperMarket();
        market.loadItems();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("App Menu:");
            System.out.println("1. List All Items");
            System.out.println("2. Add New Item");
            System.out.println("3. Remove Existing Item");
            System.out.println("0. Exit");
            System.out.print("Please Enter Your Choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    market.listAllItems();
                    break;
                case 2:
                    market.addItem(scanner);
                    break;
                case 3:
                    market.removeItem(scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice, please try again!");
            }
        } while (choice != 0);

        market.saveItems();
    }

    private void loadItems() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int quantity = Integer.parseInt(parts[1]);
                LocalDate date = LocalDate.parse(parts[2], DateTimeFormatter.ISO_DATE);
                Item item = new Item(name, quantity, date);
                items.add(item);
            }
        } catch (IOException e) {
            // Ignore if file does not exist yet
        }
    }

    private void saveItems() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (Item item : items) {
                String line = String.format("%s,%d,%s", item.getName(), item.getQuantity(),
                        item.getDate().format(DateTimeFormatter.ISO_DATE));
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving items to file: " + e.getMessage());
        }
    }

    private void listAllItems() {
        if (items.isEmpty()) {
            System.out.println("No items found!");
        } else {
            System.out.println("List of Items:");
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }

    private void addItem(Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        System.out.print("Enter item quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        LocalDate date = LocalDate.now();
        Item item = new Item(name, quantity, date);
        items.add(item);

        System.out.println("Item added successfully!");
    }

    private void removeItem(Scanner scanner) {
        System.out.print("Enter item name to remove: ");
        String name = scanner.nextLine();

        boolean found = false;
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                items.remove(item);
                System.out.println("Item removed successfully!");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Item not found!");
        }
    }

    private static class Item {
        private String name;
        private int quantity;
                private LocalDate date;

        public Item(String name, int quantity, LocalDate date) {
            this.name = name;
            this.quantity = quantity;
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public LocalDate getDate() {
            return date;
        }

        @Override
        public String toString() {
            return String.format("%s (Quantity: %d, Date: %s)", name, quantity, date);
        }
    }
}

