package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class Start {
    // used to save user inputs
    static String title;
    static String location;
    static String timeSlots;
    static String participants;

    // used to write user inputs to file
    static String fileHeader;
    static String fileName;
    static String filePath;

    public static void main(String[] args){


        // get inputs from user
        getInputsFromUser();

        // save inputs to arrayLits
        ArrayList<String> timeList = stringToArrayList(timeSlots);
        ArrayList<ArrayList<String>> participantList = stringTo2DArrayList(participants);

        // Create a new Event
        Event event = new Event(title, location, timeList, participantList);

        // Get the file Header
        fileHeader = getFileHeader(timeList);

        // Create a new csvFileWriter
        CsvFileWriter writer = new CsvFileWriter(fileHeader);

        // Create file Name
        fileName = event.getTitle() +"_At_" + event.getLocation() +  ".csv";

        // Get file Path
        filePath = new FileAccessor().getFilePath(fileName);
        System.out.println("\nSave at " + filePath);

        // Fill out the rest of the table
        System.out.println("Write to Csv file\n");
        writer.writeCsvFile(filePath, participantList);
    }

    /*********************************************************************
     *                          Get Data
     *********************************************************************/

    /**
     * Get inputs from user
     */
    private static void getInputsFromUser(){
        Scanner in = new Scanner(System.in);

        System.out.println("Start an Event!");
        System.out.println("Please enter the title:");
        title = in.nextLine();
        System.out.println("Please enter the location:");
        location = in.nextLine();
        System.out.println("Please enter possible time slots separated by one space:");
        timeSlots = trimSpace(in.nextLine());
        System.out.println("Please enter names of participants separated by one space:");
        participants = trimSpace(in.nextLine());

        in.close();
    }


    private static String getFileHeader(ArrayList<String> timeSlots){
        // First space is empty cell
        String fileHeader = ",";
        for (int i = 0; i < timeSlots.size(); i++){
            fileHeader += timeSlots.get(i);
            if(i != timeSlots.size()-1) {
                fileHeader += ",";
            }
        }
        return fileHeader;
    }

    /*********************************************************************
     *                          Format Data
     *********************************************************************/

    private static String trimSpace(String s){
        return s.trim();
    }

    private static ArrayList<String> stringToArrayList(String longString){
        String[] arr = longString.split(" ");
        return new ArrayList<>(Arrays.asList(arr));
    }

    private static ArrayList<ArrayList<String>> stringTo2DArrayList(String longString){
        String[] arr = longString.split(" ");
        ArrayList<ArrayList<String>> List = new ArrayList<>();
        for (String str : arr){
            ArrayList<String> subList = new ArrayList<>();
            subList.add(str);
            List.add(subList);
        }
        return List;
    }

    /*********************************************************************
     *                          Test Only
     *********************************************************************/

    private static void print2DList(ArrayList<ArrayList<String>> list){
        for (ArrayList<String> subList: list){
            for (String item : subList){
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }
}
