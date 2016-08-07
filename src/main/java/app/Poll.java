package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import java.util.ArrayList;
import java.util.Scanner;

public class Poll{
    private static String fileName;
    private static ArrayList<String> names;
    private static ArrayList<String> timeSlots;
    private static ArrayList<ArrayList<String>> data;

    private static final String USAGE = "Usage: java Poll filename";

    public static void main(String[]args){
        String userName;
        int index; // index used to mark the position of participant in data
        ArrayList<String> participant;

        // Check if user has passed enough arguments
        if (args.length < 1){
            System.out.println(USAGE);
            System.exit(1);
        }
        fileName = args[0];

        // Load data from the file
        loadData(fileName);

        // Begin to vote
        System.out.println("\nStart to poll!!");
        System.out.println("Please enter your user name:");
        Scanner in = new Scanner(System.in);
        userName = in.nextLine();

        if ((index = verifyAccess(userName)) >= 0){
            // Get to Vote
            if (verifyVoteStatus(index) == 1){
                // The participant wants to edit the previous vote
                // Clear the participant's previous vote
                data.get(index).subList(1, data.get(index).size()).clear();
            }
            vote(in, index);

            // Print the participant's vote
            printVote(index);

            // Print current table
            printTable();

            // Update the user's responses to the data file
            updateData(fileName);

            System.out.println("Thanks for your time !!");
        }
        else {
            System.out.println("Sorry, you don't have access to poll.");
        }
    }

    /*********************************************************************
     *                              Vote
     *********************************************************************/
    private static void vote(Scanner in, int index){
        String response;
        boolean validResponse = false;

        for (int i = 1; i < timeSlots.size(); i++){
            do {
                System.out.println("Are your available on " + timeSlots.get(i) + "? y/n");
                response = in.nextLine();
                if (response.equals("y") || response.equals("n")){
                    validResponse = true;

                    // Save the user's votes to the data
                    data.get(index).add(i, response);
                    break;
                }
                System.out.println("Not a valid option!!");
            } while(true);
            // Reset the mark
            validResponse = false;
        }
        data.get(index).add("voted");
    }


    /*********************************************************************
     *                          Update Data
     *********************************************************************/

    /**
     * Update participants' vote in file
     * @param fileName: name of the file participant wants to access
     */
    private static void updateData(String fileName){
        String filePath = new FileAccessor().getFilePath(fileName);
        String fileHeader = getFileHeader(timeSlots);
        CsvFileWriter writer = new CsvFileWriter(fileHeader);
        writer.overwriteFile(filePath, data);
    }


    /*********************************************************************
     *                      Verify User ID
     *********************************************************************/

    private static int verifyAccess(String userName){
        return names.indexOf(userName);
    }

    private static int verifyVoteStatus(int index){
        if (data.get(index).size() == 1){
            return 0;
        }
        return 1;
    }

    /*********************************************************************
     *                          Load Data
     *********************************************************************/

    private static void loadData(String fileName){
        System.out.println("Begin to load data\n");
        String filePath;
        CsvFileReader reader = new CsvFileReader();
        System.out.println("Get file path: ");
        filePath = new FileAccessor().getFilePath(fileName);
        System.out.println("Get names: ");
        names = getUserName(filePath);
        System.out.println("Get data: ");
        data = getData(filePath);
        System.out.println("Get time slots: ");
        timeSlots = getTimeSlots(filePath);
        System.out.println("\nFinish loading data...");
    }

    private static ArrayList<String> getTimeSlots(String filePath) {
        CsvFileReader r = new CsvFileReader();
        r.readCsvFile(filePath);
        return r.getTimeSlots();
    }

    private static ArrayList<ArrayList<String>> getData(String filePath){
        return new CsvFileReader().readCsvFile(filePath);
    }

    private static ArrayList<String> getUserName(String filePath){

        ArrayList<ArrayList<String>> rows = getData(filePath);
        ArrayList<String> names = new ArrayList<>();
        for (ArrayList<String> row : rows){
            names.add(row.get(0));
        }
        return names;
    }

    private  static String getFileHeader(ArrayList<String> timeSlots){
        String fileHeader = "";
        for (int i = 0; i < timeSlots.size(); i++){
            fileHeader += timeSlots.get(i);
            if(i != timeSlots.size()-1) {
                fileHeader += ",";
            }
        }
        return fileHeader;
    }


    /*********************************************************************
     *                          Test Only
     *********************************************************************/
    public static void printList(ArrayList<String> list){

        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i != list.size()-1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    private static void print2DList(ArrayList<ArrayList<String>> list){
        for (ArrayList<String> subList: list){
            for (String item : subList){
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }

    private static void printTable(){
        System.out.println("The saved data is: ");
        // Insert extra space to make table look format
        System.out.print(" ");
        printList(timeSlots);
        print2DList(data);
    }

    private static void printVote(int index){
        System.out.println("\nThe new vote: ");
        System.out.print(" ");
        printList(timeSlots);
        printList(data.get(index));
        System.out.println();
    }

}
