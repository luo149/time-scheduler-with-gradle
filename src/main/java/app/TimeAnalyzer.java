package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import java.util.*;

public class TimeAnalyzer{
    // For check if the form is fully filled
    private static int totalNumberOfParticipants;
    private static ArrayList<String> missedPeople;

    // Save basic data
    private static ArrayList<String> timeSlots;
    private static ArrayList<ArrayList<String>> data;

    private static final int MEETING = 1;
    private static final int LAB_ASSIGNMENT = 2;

    private static final String USAGE = "Usage: java TimeAnalyzer filename";
    private static final String MENU = "*********** Menu ***********\n\n" +
            "1. Decide a time for meeting\n" +
            "2. Decide schedule for lab assignment\n";

    public static void main(String[] args){
        // Check if user has passed enough arguments
        if (args.length < 1){
            System.out.println(USAGE);
            System.exit(1);
        }

        String filename = args[0];

        // Ask user what specifically to analyze
        int option = getOption();
        TimeAnalyzer analyzer = new TimeAnalyzer();

        // Check whether the vote form is filled fully
        if (analyzer.formFullyFilled(filename)){
            // Begin to analyze the time slots


            switch (option){
                case MEETING:
                    analyzer.runMeetingAnalyzer();
                    break;
                case LAB_ASSIGNMENT:
                    analyzer.runLabSectionAnalyzer();
                    break;
            }
        }
        else {
            System.out.println("\n**** Cannot Decide on the Final Date!! ****");
            System.out.println("\nThe following list of people have not voted yet: ");
            printMissedPeople();
        }
    }

    /*********************************************************************
     *                          Getters
     *********************************************************************/

    ArrayList<String> getTimeSlots(){
        return timeSlots;
    }

    ArrayList<ArrayList<String>> getData(){
        return data;
    }

    int getTotalNumberOfParticipants() { return totalNumberOfParticipants; }

    /*********************************************************************
     *                          Menu
     *********************************************************************/

    /**
     * Print options menu for users
     */
    private static void printMenu(){System.out.print(MENU);}

    /**
     * Get option from user
     * @return the option number
     */
    private static int getOption(){
        int option = 0;
        boolean validOption = false;
        Scanner in = new Scanner(System.in);
        do {
            printMenu();
            option = in.nextInt();
            if (option >= 1 && option <= 2){
                validOption = true;
            }
            else {
                System.out.println("Not a valid option!!");
            }
        } while(!validOption);
        return option;
    }

    /*********************************************************************
     *                      Check Filled Form
     *********************************************************************/

    /**
     * Check whether the form is fully filled by participants
     * @param fileName: name of the file to be analyzed
     * @return true if it's fully form, otherwise false
     */
    boolean formFullyFilled(String fileName){
        int count = countVotedParticipants(fileName);
        return (count == totalNumberOfParticipants);
    }

    /**
     * Load data from the file and count number of participants voted
     * @param fileName: name of the file to be analyzed
     * @return the count
     */
    private int countVotedParticipants(String fileName){
        int count = 0;
        missedPeople = new ArrayList<>();
        getData(fileName);
        for (ArrayList<String> row : data){
            if (row.get(row.size()-1).equals("voted")){
                count++;
            }
            else {
                missedPeople.add(row.get(0));
            }
        }
        return count;
    }

    /*********************************************************************
     *                      Get General Info
     *********************************************************************/

    /**
     * Get name of the file
     * @return file name
     */
    private static String getFilePath(String fileName){
        return new FileAccessor().getFilePath(fileName);
    }

    /**
     * Load data from the file
     * Get list of time slots
     * Get number of participants
     */
    void getData(String fileName){
        String filePath = getFilePath(fileName);
        CsvFileReader reader = new CsvFileReader();
        data = reader.readCsvFile(filePath);
        timeSlots = reader.getTimeSlots();
        totalNumberOfParticipants = data.size();
    }

    /*********************************************************************
     *                      Print Out
     *********************************************************************/

    /**
     * Print list of people who have not voted
     */
    private static void printMissedPeople(){
        int i = 1;
        for (String item: missedPeople){
            System.out.println(i +". " + item);
            i++;
        }
    }

    /*********************************************************************
     *                          Run Analyzers
     *********************************************************************/

    /**
     * Run meeting analyzer
     */
    void runMeetingAnalyzer(){
        // init meetingAnalyzer
        MeetingAnalyzer meetingAnalyzer = new MeetingAnalyzer();

        // Set the loaded data
        meetingAnalyzer.setData(data);
        meetingAnalyzer.setTimeSlots(timeSlots);
        meetingAnalyzer.setTotalNumberOfParticipants(totalNumberOfParticipants);

        // Decide a time for meeting
        ArrayList<Integer> finalDates = meetingAnalyzer.getPossibleFinalDates();
        meetingAnalyzer.printFinalResults(finalDates);
    }

    /**
     * Run lab section assignment analyzer
     */
    void runLabSectionAnalyzer(){
        // init labSectionAnalyzer
        LabSectionAnalyzer labSectionAnalyzer = new LabSectionAnalyzer();

        // Set the loaded data
        labSectionAnalyzer.setData(data);
        labSectionAnalyzer.setTimeSlots(timeSlots);

        // Decide schedule arrangement for lab
        labSectionAnalyzer.getFinalLabSchedule();
        labSectionAnalyzer.printFinalLabSchedule();
    }

}
