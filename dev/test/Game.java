import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

/**
 * Loosely based on: https://github.com/rotwaang/gamES/blob/master/Game.java
 */
public class Game {

    interface Place {
        void showDescription();

        Place makeChoice(Place previousPlace) throws IOException;
    }

    static final StringBuilder sb = new StringBuilder();
    static final Random random = new Random((int)currentTimeMillis());

    static final String MSG_LINE_SEPARATOR = "\n----------------------";
    static final String MSG_PRESS_RETURN = "Press Return";
    static InputStreamReader inputStream;
    static int playerHP;
    static String playerName;
    static String playerWeapon;
    static int choice;
    static int monsterHP;
    static int silverRing;

    public static String nextLine(String message) throws IOException {
        if (null != message) {
            out.println(message);
        }
        sb.setLength(0);
        char c;
        out.print(">");
        do {
            c = (char) inputStream.read();
            if (c == 0x0D || c == '\n' || c == 155)
                break;
            sb.append(c);
        } while (c != -1);
        return sb.toString();
    }

    public static String nextLine() throws IOException {
        return nextLine(null);
    }

    public static int nextInt() throws IOException {
        while (true) {
            try {
                return Integer.parseInt(nextLine());
            } catch (java.lang.NumberFormatException ex) {
                out.println("Wrong Number");
            }
        }
    }

    private static Place deadPlace = new Place() {

        @Override
        public void showDescription() {
            out.println(MSG_LINE_SEPARATOR);
            out.println("You are dead!!");
            out.println("\nGAME OVER");
            out.println(MSG_LINE_SEPARATOR);
        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {
            nextLine();
            return null;
        }
    };
    private static Place winPlace = new Place() {

        @Override
        public void showDescription() {
            out.println(MSG_LINE_SEPARATOR);
            out.println("You killed the monster!");
            out.println("The monster dropped a ring!");
            out.println("You get a silver ring!\n\n");
            out.println("1: Go east");
            out.println(MSG_LINE_SEPARATOR);

            silverRing = 1;

        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {
            choice = nextInt();
            if (choice == 1) {
                return crossRoad;
            }
            return null;
        }
    };
    public static Place attackPlace = new Place() {
        @Override
        public void showDescription() {
        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {
            int playerDamage = 0;

            if (playerWeapon.equals("Knife")) {
                playerDamage = random.nextInt(5);
            } else if (playerWeapon.equals("Long Sword")) {
                playerDamage = random.nextInt(8);
            }

            out.print("You attacked the monster and gave ");
            out.print(playerDamage);
            out.println(" damage!");

            monsterHP = monsterHP - playerDamage;

            out.print("Monster HP: ");
            out.println(monsterHP);

            if (monsterHP < 1) {
                return winPlace;
            } else if (monsterHP > 0) {

                int monsterDamage = 0;

                monsterDamage = new java.util.Random().nextInt(4);

                out.print("The monster attacked ");
                out.print("you and gave ");
                out.print(monsterDamage);
                out.println(" damage!");

                playerHP = playerHP - monsterDamage;

                out.println("Player HP: ");
                out.println(playerHP);

                if (playerHP < 1) {
                    return deadPlace;
                } else if (playerHP <5) {
                    return fightPlace;
                } else {
                    return attackPlace;
                }
            }
            return this;
        }
    };
    private static Place endingPlace = new Place() {

        @Override
        public void showDescription() {
            out.println(MSG_LINE_SEPARATOR);
            out.println("Guard: You killed that goblin!?? Great!");
            out.println("Seems you are a trustworthy.");
            out.println("Welcome to our town!");
            out.println("\n\n       THE END        ");
            out.println(MSG_LINE_SEPARATOR);
        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {
            nextLine();
            return null;
        }

    };
    // Build the places
    private static Place townGate = new Place() {

        @Override
        public void showDescription() {
            out.println(MSG_LINE_SEPARATOR);
            out.println("You are at the gate of the town.");
            out.println("A guard is standing in front of you.");
            out.println(MSG_LINE_SEPARATOR);
        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {
            out.println("");
            out.println("What do you want to do?");
            out.println("");
            out.println("1: Talk to the guard");
            out.println("2: Attack the guard");
            out.println("3: Leave");

            choice = nextInt();

            if (choice == 1) {
                if (silverRing == 1) {
                    return endingPlace;
                } else {
                    out.print("Guard: Hello there, stranger.\n So your name is ");
                    out.print(playerName);
                    out.println("? \nSorry but we cannot let\n stranger enter our town.");
                    nextLine(MSG_PRESS_RETURN);
                    return this;
                }

            } else if (choice == 2) {
                playerHP = playerHP - 1;
                out.println("Guard: Hey don't be stupid.\n\nThe guard hit you very hard.\n(You receive 1 damage)\n");
                out.println("Your HP: " + playerHP);
                nextLine(MSG_PRESS_RETURN);
                return this;
            } else if (choice == 3) {
                return crossRoad;
            } else {
                return this;
            }
        }

    };
    private static Place crossRoad = new Place() {

        @Override
        public void showDescription() {
            out.println(MSG_LINE_SEPARATOR);
            out.println("You are at a crossroad.\n\n");
        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {

            out.println("1: north");
            out.println("2: east");
            out.println("3: south");
            out.println("4: west");

            choice = nextInt();

            if (choice == 1) {
                return northPlace;
            } else if (choice == 2) {
                return eastPlace;
            } else if (choice == 3) {
                out.println('t');
                return townGate;
            } else if (choice == 4) {
                return westPlace;
            }
            return null;
        }
    };
    private static Place northPlace = new Place() {
        @Override
        public void showDescription() {
            out.println(MSG_LINE_SEPARATOR);
            out.println("There is a river.");
        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {

            if(playerHP < 16 && crossRoad.hashCode()==previousPlace.hashCode()) {
                out.println("You drink the water and rest.");
                out.println("Your HP is recovered.");
                playerHP = playerHP + 1;
                out.print("Your HP: ");
                out.println(playerHP);
            }
            out.println("\n\n1: Go back to the crossroad");

            choice = nextInt();

            if (choice == 1) {
                return crossRoad;
            }
            return northPlace;
        }
    };
    private static Place eastPlace = new Place() {

        @Override
        public void showDescription() {
            out.println(MSG_LINE_SEPARATOR);
            out.println("You walked into a forest\n and found a Long Sword!");
            playerWeapon = "Long Sword";
            out.println("Your Weapon: " + playerWeapon);
        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {
            out.println("\n\n1: Go back to the crossroad");

            choice = nextInt();

            if (choice == 1) {
                return crossRoad;
            }
            return null;
        }
    };
    public static Place fightPlace = new Place() {

        @Override
        public void showDescription() {

        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {
            out.println(MSG_LINE_SEPARATOR);
            out.print("Your HP: ");
            out.println(playerHP);
            out.print("Monster HP: ");
            out.println(monsterHP);
            out.println("\n1: Attack");
            out.println("2: Run");

            choice = nextInt();

            if (choice == 1) {
                return attackPlace;
            } else if (choice == 2) {
                return crossRoad;
            }
            return null;
        }
    };
    public static Place westPlace = new Place() {

        @Override
        public void showDescription() {

        }

        @Override
        public Place makeChoice(Place previousPlace) throws IOException {
            out.println(MSG_LINE_SEPARATOR);
            out.println("You encounter a goblin!\n");
            out.println("1: Fight");
            out.println("2: Run");

            choice = nextInt();

            if (choice == 1) {
                return fightPlace;
            } else if (choice == 2) {
                return crossRoad;
            }
            return null;
        }
    };

    public static void playerSetUp() throws IOException {


        playerHP = 10;
        monsterHP = 15;

        playerWeapon = "Knife";

        out.println("Your HP: " + playerHP);
        out.println("Your Weapon: " + playerWeapon);

        out.println("Please enter your name:");

        playerName = nextLine();

        out.print("Hello ");
        out.print(playerName);
        out.println(",let's start the game");

    }

    public static void gameLoop() throws IOException {
        Place currentPlace=townGate;
        Place previousPlace=townGate;
        Place newPlace;

        while (true) {
            currentPlace.showDescription();
            newPlace=currentPlace.makeChoice(previousPlace);
            previousPlace = currentPlace;
            if(newPlace!=null) {
                currentPlace = newPlace;
            }
        }
    }

    public static void main(String[] args) throws IOException {

        inputStream = new InputStreamReader(System.in);

        out.println((int)System.getRuntime().freeMemory());

        playerSetUp();
        gameLoop();

    }

}
