package machine;

import java.util.Scanner;

// Coffee class to represent each coffee type
class Coffee {
    private final String name;
    private final int water;
    private final int milk;
    private final int beans;
    private final int cost;

    public Coffee(String name, int water, int milk, int beans, int cost) {
        this.name = name;
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.cost = cost;
    }

    public String getName() { return name; }
    public int getWater() { return water; }
    public int getMilk() { return milk; }
    public int getBeans() { return beans; }
    public int getCost() { return cost; }
}

// Coffee Machine class
class CoffeeMachine {
    // Resources
    private int water;
    private int milk;
    private int beans;
    private int cups;
    private int money;

    // Cleaning tracking
    private int coffeeCount;
    private boolean needsCleaning;

    // Coffee types
    private final Coffee[] coffeeTypes = {
            new Coffee("espresso", 250, 0, 16, 4),
            new Coffee("latte", 350, 75, 20, 7),
            new Coffee("cappuccino", 200, 100, 12, 6)
    };

    // Constructor
    public CoffeeMachine() {
        this.water = 400;
        this.milk = 540;
        this.beans = 120;
        this.cups = 9;
        this.money = 550;
        this.coffeeCount = 0;
        this.needsCleaning = false;
    }

    // Display current state
    public void displayState() {
        System.out.println("\nThe coffee machine has:");
        System.out.println(water + " ml of water");
        System.out.println(milk + " ml of milk");
        System.out.println(beans + " g of coffee beans");
        System.out.println(cups + " disposable cups");
        System.out.println("$" + money + " of money");
        if (needsCleaning) {
            System.out.println("⚠️  Machine needs cleaning! (Made " + coffeeCount + " coffees)");
        }
    }

    // Buy coffee
    public void buyCoffee(Scanner scanner) {
        // Check if machine needs cleaning
        if (needsCleaning) {
            System.out.println("I need cleaning!");
            return;
        }

        System.out.println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
        String input = scanner.nextLine();

        if (input.equals("back")) {
            return;
        }

        try {
            int choice = Integer.parseInt(input) - 1; // Convert to 0-based index

            if (choice < 0 || choice >= coffeeTypes.length) {
                System.out.println("Invalid choice!");
                return;
            }

            Coffee selectedCoffee = coffeeTypes[choice];

            if (canMakeCoffee(selectedCoffee)) {
                makeCoffee(selectedCoffee);
                System.out.println("I have enough resources, making you a coffee!");

                // Increment coffee count and check if cleaning is needed
                coffeeCount++;
                if (coffeeCount >= 10) {
                    needsCleaning = true;
                    System.out.println("⚠️  Machine has made 10 coffees and now needs cleaning!");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number (1, 2, 3) or 'back'.");
        }
    }

    // Check if we can make the coffee
    private boolean canMakeCoffee(Coffee coffee) {
        if (water < coffee.getWater()) {
            System.out.println("Sorry, not enough water!");
            return false;
        }
        if (milk < coffee.getMilk()) {
            System.out.println("Sorry, not enough milk!");
            return false;
        }
        if (beans < coffee.getBeans()) {
            System.out.println("Sorry, not enough coffee beans!");
            return false;
        }
        if (cups < 1) {
            System.out.println("Sorry, not enough disposable cups!");
            return false;
        }
        return true;
    }

    // Actually make the coffee
    private void makeCoffee(Coffee coffee) {
        water -= coffee.getWater();
        milk -= coffee.getMilk();
        beans -= coffee.getBeans();
        cups--;
        money += coffee.getCost();
    }

    // Fill supplies
    public void fillSupplies(Scanner scanner) {
        System.out.println("\nWrite how many ml of water you want to add: ");
        water += getPositiveInt(scanner);

        System.out.println("Write how many ml of milk you want to add: ");
        milk += getPositiveInt(scanner);

        System.out.println("Write how many grams of coffee beans you want to add: ");
        beans += getPositiveInt(scanner);

        System.out.println("Write how many disposable cups you want to add: ");
        cups += getPositiveInt(scanner);
    }

    // Take money
    public void takeMoney() {
        System.out.println("\nI gave you $" + money);
        money = 0;
    }

    // Clean the machine
    public void clean() {
        if (needsCleaning) {
            coffeeCount = 0;
            needsCleaning = false;
            System.out.println("I have been cleaned!");
        } else {
            System.out.println("Machine doesn't need cleaning yet. Made " + coffeeCount + "/10 coffees.");
        }
    }

    // Check if machine needs cleaning
    public boolean needsCleaning() {
        return needsCleaning;
    }

    // Helper method to get positive integer
    private int getPositiveInt(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (value >= 0) {
                    return value;
                }
                System.out.println("Please enter a non-negative number: ");
            } else {
                System.out.println("Please enter a valid number: ");
                scanner.next(); // Clear invalid input
            }
        }
    }


    // Main class to run the program
    public static class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            CoffeeMachine machine = new CoffeeMachine();

            System.out.println("Welcome to the Coffee Machine!");

            while (true) {
                System.out.print("\nWrite action (buy, fill, take, clean, remaining, exit): ");
                String action = scanner.nextLine().toLowerCase();

                switch (action) {
                    case "buy":
                        machine.buyCoffee(scanner);
                        break;
                    case "fill":
                        machine.fillSupplies(scanner);
                        break;
                    case "take":
                        machine.takeMoney();
                        break;
                    case "clean":
                        machine.clean();
                        break;
                    case "remaining":
                        machine.displayState();
                        break;
                    case "exit":
                        System.out.println("Shutting down coffee machine. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid action! Please choose: buy, fill, take, clean, remaining, or exit.");
                }
            }
        }
    }
}