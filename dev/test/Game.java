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

    static final Random random = new Random((int)currentTimeMillis());
    static final byte[] buffer= new byte[11];

    static final String MSG_LINE_SEPARATOR = "\n----------------------";
    static final String MSG_PRESS_RETURN = "Press Return";

    static final String[] theWeapons= {"knife","sword"};

    private static final int KNIFE = 0;
    private static final int SWORD = 1;

    private static Place townGatePlace;
    private static Place crossRoadPlace;
    private static Place northPlace;
    private static Place eastPlace;
    private static Place westPlace;
    private static Place attackPlace;
    private static Place fightPlace;
    private static Place deadPlace;
    private static Place winPlace;
    private static Place endingPlace;

    static final byte[] playerName= new byte[11];

    static int playerHP;
    static int playerWeapon;
    static int choice;
    static int monsterHP;
    static int silverRing;

    static void print(byte[] chars) {
        for(byte c:chars) {
            if(c==0) break;
            out.print((char)c);
        }
    }

    static int arrayToInt(byte[] data,int start,int end) throws NumberFormatException
    {
        int result = 0;
        for (int i = start; i < end; i++)
        {
            int digit = (int)data[i] - (int)'0';
            if ((digit < 0) || (digit > 9)) return -1;
            result *= 10;
            result += digit;
        }
        return result;
    }

    public static byte[] nextLine(String message, byte[] buffer) throws IOException {
        if (null != message) {
            out.println(message);
        }
        int i=0;
        char c;
        out.print(">");
        do {
            c = (char)System.in.read();
            if (c == 0x0D || c == '\n' || c == 0x9B)
                break;
            buffer[i++]=(byte)c;
        } while (c != -1 && i<buffer.length-1);
        buffer[i]=0x00;
        return buffer;
    }

    public static byte[] nextLine(byte[] buffer) throws IOException {
        return nextLine(null,buffer);
    }

    public static byte[] nextLine() throws IOException {
        return nextLine(null,buffer);
    }

    public static int nextInt() throws IOException {
        int value;
        while (true) {
            value = arrayToInt(nextLine(buffer),0,1);
            if(value<0) {
                out.println("Wrong.");
            } else {
                return value;
            }
        }
    }

    public static void gameSetup() {
        deadPlace = new Place() {

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

        winPlace = new Place() {

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
                    return crossRoadPlace;
                }
                return null;
            }
        };

        attackPlace = new Place() {
            @Override
            public void showDescription() {
            }

            @Override
            public Place makeChoice(Place previousPlace) throws IOException {
                int playerDamage = 0;

                if (playerWeapon==KNIFE) {
                    playerDamage = random.nextInt(5);
                } else if (playerWeapon==SWORD) {
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

                    monsterDamage = random.nextInt(4);

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
                return null;
            }
        };

        // Build the places
        townGatePlace = new Place() {

            @Override
            public void showDescription() {
                out.println(MSG_LINE_SEPARATOR);
                out.println("You are at the gate of the town.");
                out.println("A guard is standing in front of you.");
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
                        print(playerName);
                        out.println("? \nSorry but we cannot let\n stranger enter our town.");
                        nextLine(MSG_PRESS_RETURN,buffer);
                        return this;
                    }

                } else if (choice == 2) {
                    playerHP = playerHP - 1;
                    out.println("Guard: Hey don't be stupid.\n\nThe guard hit you very hard.\n(You get 1 damage)\n");
                    out.print("Your HP: ");
                    out.println(playerHP);
                    nextLine(MSG_PRESS_RETURN,buffer);
                    return this;
                } else if (choice == 3) {
                    return crossRoadPlace;
                } else {
                    return this;
                }
            }

        };

        crossRoadPlace = new Place() {

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
                    return townGatePlace;
                } else if (choice == 4) {
                    return westPlace;
                }
                return null;
            }
        };

        northPlace = new Place() {
            @Override
            public void showDescription() {
                out.println(MSG_LINE_SEPARATOR);
                out.println("There is a river.");
            }

            @Override
            public Place makeChoice(Place previousPlace) throws IOException {

                if(playerHP < 16 && crossRoadPlace==previousPlace) {
                    out.println("You drink the water and rest.");
                    out.println("Your HP is recovered.");
                    playerHP++;
                    out.print("Your HP: ");
                    out.println(playerHP);
                }
                out.println("\n\n1: Go back to the crossroad");

                choice = nextInt();

                if (choice == 1) {
                    return crossRoadPlace;
                }
                return northPlace;
            }
        };

        eastPlace = new Place() {

            @Override
            public void showDescription() {
                out.println(MSG_LINE_SEPARATOR);
                out.println("You walked into a forest\n and found a Long Sword!");
                playerWeapon = SWORD;
                out.println("Your Weapon: ");
                out.println(theWeapons[playerWeapon]);
            }

            @Override
            public Place makeChoice(Place previousPlace) throws IOException {
                out.println("\n\n1: Go back to the crossroad");

                choice = nextInt();

                if (choice == 1) {
                    return crossRoadPlace;
                }
                return null;
            }
        };

        fightPlace = new Place() {

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
                    return crossRoadPlace;
                }
                return null;
            }
        };

        westPlace = new Place() {

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
                    return crossRoadPlace;
                }
                return null;
            }
        };
        endingPlace = new Place() {

            @Override
            public void showDescription() {
                out.println(MSG_LINE_SEPARATOR);
                out.println("Guard: You killed that goblin!?? Great!\nSeems you are a trustworthy.\nWelcome to our town!");
                out.println("\n\n       THE END        ");
            }

            @Override
            public Place makeChoice(Place previousPlace) throws IOException {
                nextLine();
                return null;
            }

        };
    }

    public static void playerSetUp() throws IOException {


        playerHP = 10;
        monsterHP = 15;

        playerWeapon = KNIFE;

        out.print("Your HP: ");
        out.println(playerHP);
        out.print("Your Weapon: ");
        out.println(theWeapons[playerWeapon]);

        out.println("Please enter your name:");

        System.arraycopy(nextLine(),0,playerName,0,playerName.length);

        out.print("Hello ");
        print(playerName);
        out.println(",let's start the game");

    }

    public static void gameLoop() throws IOException {
        Place currentPlace=townGatePlace;
        Place previousPlace=townGatePlace;
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

        // inputStream = new InputStreamReader(System.in);
        gameSetup();
        playerSetUp();
        gameLoop();

    }

}
