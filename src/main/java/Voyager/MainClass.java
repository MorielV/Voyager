package Voyager;

import java.util.Scanner;

public class MainClass {

    private static final String ILLEGAL_INPUT = "Illegal Input!";
    private static final Client client = new Client();
    public static void main(String[] args) {
        String requestedNum;
        //can make as private func to get argument , should make a test for it
        Scanner reader = new Scanner(System.in);
        boolean isGoodInput = false;
        while (!isGoodInput) {
            System.out.println("Welcome!\n What would you to do? (Enter 1 or 2)\n 1. Store a web page.\n 2. Get stored web page");
            requestedNum = reader.nextLine();
            switch (requestedNum) {
                case "1":
                    isGoodInput = true;
                    client.storeWebPage();
                    System.out.println("web page stored!");
                    break;
                case "2":
                    isGoodInput = true;
                    //client.getWebPage();
                    break;
                default:
                    System.out.println(ILLEGAL_INPUT +"\ntry again please");
            }
        }
    }
}
