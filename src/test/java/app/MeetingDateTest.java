package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by mengxueluo on 8/5/16.
 */
public class MeetingDateTest {

    @Test
    public void FinalDateShouldHaveHighestAttendance(){
        String fileName = "testMeetingDate.csv";
        TimeAnalyzer analyzer = new TimeAnalyzer();
        // get basic data from the test file
        analyzer.formFullyFilled(fileName);
        ArrayList<String> timeSlots = analyzer.getTimeSlots();
        ArrayList<ArrayList<String>> data = analyzer.getData();
        int totalNumberOfParticipants = analyzer.getTotalNumberOfParticipants();

        MeetingAnalyzer meetingAnalyzer = new MeetingAnalyzer();
        meetingAnalyzer.setTotalNumberOfParticipants(totalNumberOfParticipants);
        meetingAnalyzer.setTimeSlots(timeSlots);
        meetingAnalyzer.setData(data);
        ArrayList<Integer> finalDates = meetingAnalyzer.getPossibleFinalDates();

        assertTrue("The final date should have highest attendance", finalDates.get(0) == 1);
    }
}

