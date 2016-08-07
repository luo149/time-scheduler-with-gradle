package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import java.util.ArrayList;

public class MeetingAnalyzer{

    private static int maxNumberOfPeopleCanAttend;
    private static int totalNumberOfParticipants;
    // Basic data
    private static ArrayList<String> timeSlots;
    private static ArrayList<ArrayList<String>> data;

    /*********************************************************************
     *                          Setters
     *********************************************************************/

    public void setTimeSlots(ArrayList<String> timeSlots){
        MeetingAnalyzer.timeSlots = timeSlots;
    }

    public void setData(ArrayList<ArrayList<String>> data){
        MeetingAnalyzer.data = data;
    }

    public void setTotalNumberOfParticipants(int totalNumberOfParticipants){
        MeetingAnalyzer.totalNumberOfParticipants = totalNumberOfParticipants;
    }

    /*********************************************************************
     *                          Operations
     *********************************************************************/

    /**
     * Get possible time slots for meeting
     * @return the possible time slots in format of arraylist
     */
    public ArrayList<Integer> getPossibleFinalDates(){
        int numberOfTimeSlots = getNumberOfTimeSlots();
        int[] stats = summarizeData(numberOfTimeSlots);
        return findMostPopularTime(stats);
    }

    /*********************************************************************
     *                          Get Info
     *********************************************************************/

    /**
     * Get number of time slots
     * @return the number
     */
    private static int getNumberOfTimeSlots(){
        // Minus the extra first empty cell
        return timeSlots.size()-1;
    }

    /**
     * Count attendance of each time slot and save it to an int array
     * @param numberOfTimeSlots: total number of time slots
     * @return the attendance in the format of int array
     */
    private static int[] summarizeData(int numberOfTimeSlots){
        int index;
        int[] times = new int[numberOfTimeSlots];
        for (ArrayList<String> row: data){
            index = 0;
            for (int i = 1; i < row.size(); i++){
                if (row.get(i).equals("y")){
                    times[index]++;
                }
                index++;
            }
        }
        return times;
    }

    /*********************************************************************
     *                Find Most Proper Time For Meeting
     *********************************************************************/

    /**
     * Find the time slots with highest attendance. It could be multiple.
     * In case several time slots have the same attendance number.
     * @param stats: record the attendance of each time slot
     * @return time slots with highest attendance in the format of arraylist
     */
    private static ArrayList<Integer> findMostPopularTime(int[] stats){
        // Find the max number of people could attend the meeting on one day
        findMax(stats);
        // In case there're days have the same attendance number
        return hasAlternativeTime(stats, maxNumberOfPeopleCanAttend);
    }

    /**
     * Find the highest attendance number from stats
     * Set maxNumberOfPeopleCanAttend
     * @param stats: record the attendance of each time slot
     */
    private static void findMax(int[] stats){
        // The first number is always 0 because it's the empty cell
        int max = stats[0];
        for (int item: stats) {
            if (item > max){
                max = item;
            }
        }
        maxNumberOfPeopleCanAttend = max;
    }

    /*********************************************************************
     *                      Get More Details
     *********************************************************************/

    /**
     * Find time slots with highest attendance number
     * @param stats: record the attendance of each time slot
     * @param max: the highest attendance number
     * @return times slots with attendance number as max
     */
    private static ArrayList<Integer> hasAlternativeTime(int[] stats, int max){
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < stats.length; i++) {
            if (max == stats[i]){
                indexes.add(i);
            }
        }
        return indexes;
    }

    /*
     * Get list of people able to attend the meeting on certain day
     * @param index: mark the position of chosen time slot
     */
    private static ArrayList<String> getParticipants(int index){
        ArrayList<String> participants = new ArrayList<>();
        for (ArrayList<String> row : data){
            if (row.get(index).equals("y")){
                participants.add(row.get(0));
            }
        }
        return participants;
    }

    /*********************************************************************
     *                          Print
     *********************************************************************/
    /**
     * Print the final decided dates for a meeting
     * @param indexes: possible time slots for a meeting
     */
    public void printFinalResults(ArrayList<Integer> indexes){
        int size = indexes.size();
        int index;
        if (size == 1){
            index = indexes.get(0);
            // indexes is 0 based, + 1 because there's extra space in the beginning
            System.out.println("\n**** The Final Date Would be " + timeSlots.get(index + 1) + " ****");
            printNames(getParticipants(index+1));
        }
        else {
            System.out.println("\n**** The Possible Final Dates Could be: ");
            for (int i = 0; i < indexes.size(); i++) {
                index = indexes.get(i);
                // indexes is 0 based, + 1 because there's extra space in the beginning
                System.out.print(timeSlots.get(index + 1) + ": ");
                printNames(getParticipants(i+1));
                System.out.println();
            }
        }
        System.out.println("\n======== Attendance ========");
        System.out.println(maxNumberOfPeopleCanAttend + " Out of " + totalNumberOfParticipants);
    }

    /**
     * Print participants' names
     * @param names: arraylist of participants' names
     */
    private static void printNames(ArrayList<String> names){
        for (int i = 0; i < names.size(); i++) {
            System.out.print(names.get(i));
            if (i != names.size()-1){
                System.out.print(", ");
            }
        }
    }
}
