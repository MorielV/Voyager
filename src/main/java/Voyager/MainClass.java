package Voyager;

public class MainClass {

  //  private static final String ILLEGAL_INPUT = "Illegal Input!";
    //private static final Client client = new Client();


// voyager put www.ynet.co.il
// voyager get www.ynet.co.il
//voyager get www.ynet.co.il 0


//
//        while(true)
//
//    {
//        System.out.println("\nWelcome!\n" +
//                " What would you wish to do? (Enter 1,2,3 or 4)\n" +
//                " 1. Store a web page.\n" +
//                " 2. Get all stored slices by URL.\n" +
//                " 3. Get a stored slice by URL and slice.\n" +
//                " 4. Exit.");
//        System.out.print("Your choice : ");
//        reader = new Scanner(System.in);
//        requestedNum = reader.nextLine();
//        String urlAsString;
//        int SliceNum;
//        reader = new Scanner(System.in);
//        switch (requestedNum) {
//            case "1":
//                System.out.println("Please Enter URL to store slices");
//                System.out.print("URL : ");
//                urlAsString = reader.nextLine();
//                client.storeWebPage(urlAsString);
//                System.out.println("web page stored!");
//                break;
//            case "2":
//                System.out.println("Please Enter URL");
//                System.out.print("URL : ");
//                urlAsString = reader.nextLine();
//                client.printAllSlicesByUrl(urlAsString);
//                break;
//            case "3":
//                System.out.println("Please Enter URL");
//                System.out.print("URL : ");
//                urlAsString = reader.nextLine();
//                while (true) {
//                    reader = new Scanner(System.in);
//                    System.out.println("Please Enter Slice Number");
//                    System.out.print("Slice Number : ");
//                    try {
//                        SliceNum = reader.nextInt();
//                        break;
//                    } catch (Exception e) {
//                        System.out.println("Error : did not recognize integer, try again");
//                    }
//                }
//                client.printSliceByUrlAndSliceNum(urlAsString, SliceNum);
//                break;
//            case "4":
//                client.exit();
//                System.out.println("Thank you, goodbye!");
//                return;
//            default:
//                System.out.println(ILLEGAL_INPUT + "\ntry again please");
//        }
//    }
//}
}
