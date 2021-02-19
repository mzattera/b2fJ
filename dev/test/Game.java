package b2fJ.test.atari;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.out;

/*
    Based on: https://ryisnow.net/2017/04/30/a-beginners-text-adventure-game-creation-in-java/
 */
public class Game {

    static final String LINE = "\n----------------------\n";

    int playerHP;
    String playerName;
    String playerWeapon;
    int choice;
    int monsterHP;

    int silverRing;

    public String nextLine() throws IOException {
        final InputStreamReader stream = new InputStreamReader(System.in);
        char c;
        out.println(">");
        String s = "";
        do {
            c = (char)stream.read();
            if (c == 0x0D || c=='\n' || c==155 )
                break;
            s += c + "";
        } while (c != -1);
        out.println("Your Input:"+s);
        return s;
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(nextLine());
    }

    public static void main(String[] args) throws IOException {

        Game dublin;
        dublin = new Game();

        dublin.playerSetUp();
        dublin.townGate();
    }

    public void playerSetUp() throws IOException {


        playerHP = 10;
        monsterHP = 15;

        playerWeapon = "Knife";

        out.println("Your HP: "+ playerHP);
        out.println("Your Weapon: "+ playerWeapon);

        out.println("Please enter your name:");

        playerName = nextLine();

        out.println("Hello " + playerName + ", let's start the game!");


    }

    public void townGate() throws IOException {

        out.println(LINE);
        out.println("You are at the gate of the town.");
        out.println("A guard is standing in front of you.");
        out.println("");
        out.println("What do you want to do?");
        out.println("");
        out.println("1: Talk to the guard");
        out.println("2: Attack the guard");
        out.println("3: Leave");
        out.println(LINE);

        choice = nextInt();

        if(choice==1){
            if(silverRing==1){
                ending();
            }
            else{
                out.println("Guard: Hello there, stranger. So your name is " + playerName + "? \nSorry but we cannot let stranger enter our town.");
                nextLine();
                townGate();
            }

        }
        else if(choice==2){
            playerHP = playerHP-1;
            out.println("Guard: Hey don't be stupid.\n\nThe guard hit you so hard and you gave up.\n(You receive 1 damage)\n");
            out.println("Your HP: " + playerHP);
            nextLine();
            townGate();
        }
        else if(choice==3){
            crossRoad();
        }
        else{
            townGate();
        }
    }

    public void crossRoad() throws IOException {
        out.println(LINE);
        out.println("You are at a crossroad. If you go south, you will go back to the town.\n\n");
        out.println("1: Go north");
        out.println("2: Go east");
        out.println("3: Go south");
        out.println("4: Go west");

        choice = nextInt();

        if(choice==1){
            north();
        }
        else if(choice==2){
            east();
        }
        else if(choice==3){
            townGate();
        }
        else if(choice==4){
            west();
        }
        else{
            crossRoad();
        }
    }

    public void north() throws IOException {
        out.println(LINE);
        out.println("There is a river. You drink the water and rest at the riverside.");
        out.println("Your HP is recovered.");
        playerHP = playerHP + 1;
        out.println("Your HP: " + playerHP);
        out.println("\n\n1: Go back to the crossroad");

        choice = nextInt();

        if(choice==1){
            crossRoad();
        }
        else{
            north();
        }
    }

    public void east() throws IOException {
        out.println(LINE);
        out.println("You walked into a forest and found a Long Sword!");
        playerWeapon = "Long Sword";
        out.println("Your Weapon: "+ playerWeapon);
        out.println("\n\n1: Go back to the crossroad");

        choice = nextInt();

        if(choice==1){
            crossRoad();
        }
        else{
            east();
        }
    }

    public void west() throws IOException {
        out.println(LINE);
        out.println("You encounter a goblin!\n");
        out.println("1: Fight");
        out.println("2: Run");

        choice = nextInt();

        if(choice==1){
            fight();
        }
        else if(choice==2){
            crossRoad();
        }
        else{
            west();
        }
    }

    public void fight() throws IOException {
        out.println(LINE);
        out.println("Your HP: "+ playerHP);
        out.println("Monster HP: " + monsterHP);
        out.println("\n1: Attack");
        out.println("2: Run");

        choice = nextInt();

        if(choice==1){
            attack();
        }
        else if(choice==2){
            crossRoad();
        }
        else{
            fight();
        }
    }

    public void attack() throws IOException {
        int playerDamage =0;

        if(playerWeapon.equals("Knife")){
            playerDamage = new java.util.Random().nextInt(5);
        }
        else if(playerWeapon.equals("Long Sword")){
            playerDamage = new java.util.Random().nextInt(8);
        }

        out.println("You attacked the monster and gave " + playerDamage + " damage!");

        monsterHP = monsterHP - playerDamage;

        out.println("Monster HP: " + monsterHP);

        if(monsterHP<1){ win(); } else if(monsterHP>0){
            int monsterDamage =0;

            monsterDamage = new java.util.Random().nextInt(4);

            out.println("The monster attacked you and gave " + monsterDamage + " damage!");

            playerHP = playerHP - monsterDamage;

            out.println("Player HP: " + playerHP);

            if(playerHP<1){ dead(); } else if(playerHP>0){
                fight();
            }
        }


    }

    public void dead(){
        out.println(LINE);
        out.println("You are dead!!");
        out.println("\nGAME OVER");
        out.println(LINE);

    }

    public void win() throws IOException {
        out.println(LINE);
        out.println("You killed the monster!");
        out.println("The monster dropped a ring!");
        out.println("You get a silver ring!\n\n");
        out.println("1: Go east");
        out.println(LINE);

        silverRing = 1;

        choice = nextInt();
        if(choice==1){
            crossRoad();
        }
        else{
            win();
        }

    }

    public void ending(){
        out.println(LINE);
        out.println("Guard: You killed that goblin!?? Great!");
        out.println("Seems you are a trustworthy. Welcome to our town!");
        out.println("\n\n           THE END                    ");
        out.println(LINE);
    }
}
