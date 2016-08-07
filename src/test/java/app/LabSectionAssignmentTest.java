package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by mengxueluo on 8/4/16.
 */
public class LabSectionAssignmentTest {

    @Test
    public void EachGTAShouldHaveValidDoableSectionList(){
        String fileName = "testLabAssignment_GTA.csv";
        TimeAnalyzer analyzer = new TimeAnalyzer();
        // get basic data from the test file
        analyzer.formFullyFilled(fileName);
        ArrayList<String> timeSlots = analyzer.getTimeSlots();
        ArrayList<ArrayList<String>> data = analyzer.getData();

        // set the basic data for labSectionAnalyzer
        LabSectionAnalyzer labSectionAnalyzer = new LabSectionAnalyzer();
        labSectionAnalyzer.setTimeSlots(timeSlots);
        labSectionAnalyzer.setData(data);

        // set the doable sections for each gta
        labSectionAnalyzer.getFinalLabSchedule();

        GTA[] gtas = labSectionAnalyzer.getGTAList();
        ArrayList<Integer> doableList;

        // check each gta has valid doable section list
        int i = 1;
        for (GTA p : gtas){
            doableList = p.getDoableSectionList();
            for (Integer number : doableList){
                assertTrue("The doable section number in file on the row of the GTA should be equal to 'y'",
                        data.get(i).get(number).equals("y"));
            }
            i++;
        }

    }

    @Test
    public void EachSectionShouldHaveValidCandidatesList(){
        String fileName = "testLabAssignment_GTA.csv";
        TimeAnalyzer analyzer = new TimeAnalyzer();
        // get basic data from the test file
        analyzer.formFullyFilled(fileName);
        ArrayList<String> timeSlots = analyzer.getTimeSlots();
        ArrayList<ArrayList<String>> data = analyzer.getData();

        // set the basic data for labSectionAnalyzer
        LabSectionAnalyzer labSectionAnalyzer = new LabSectionAnalyzer();
        labSectionAnalyzer.setTimeSlots(timeSlots);
        labSectionAnalyzer.setData(data);

        labSectionAnalyzer.getFinalLabSchedule();
        // reset the doable sections for each gta after sorting the GTA list
        labSectionAnalyzer.setInfoForEachGTA();

        LabSection[] labSections = labSectionAnalyzer.getLabSectionList();

        // check every section has valid list of candidates
        ArrayList<String> candidates;
        int sectionNumber;
        int indexOfCandidate;
        for (LabSection section : labSections){
            candidates = section.getCandidates();
            sectionNumber = section.getSectionNumber();
            for (String candidate : candidates){
                indexOfCandidate = labSectionAnalyzer.getIndexOfGTA(candidate);
                //System.out.println("Candi at index of " + indexOfCandidate + " can attend section " + sectionNumber);
                assertTrue("Each section should have valid candidate who can attend the section",
                        data.get(indexOfCandidate).get(sectionNumber).equals("y"));
            }
        }
    }

    @Test
    public void FinalLabScheduleShouldBeValid(){
        String fileName = "testLabAssignment_GTA.csv";
        TimeAnalyzer analyzer = new TimeAnalyzer();
        // get basic data from the test file
        analyzer.formFullyFilled(fileName);
        ArrayList<String> timeSlots = analyzer.getTimeSlots();
        ArrayList<ArrayList<String>> data = analyzer.getData();

        // set the basic data for labSectionAnalyzer
        LabSectionAnalyzer labSectionAnalyzer = new LabSectionAnalyzer();
        labSectionAnalyzer.setTimeSlots(timeSlots);
        labSectionAnalyzer.setData(data);

        // generate the final lab schedule
        labSectionAnalyzer.getFinalLabSchedule();

        // reset the doable sections for each gta
        labSectionAnalyzer.setInfoForEachGTA();

        LabSection[] labSections = labSectionAnalyzer.getLabSectionList();
        GTA[] gtas = labSectionAnalyzer.getGTAList();

        // check if the final lab schedule is valid
        int sectionNumber;
        int indexOfGTA;
        String nominated;
        int indexOfSection;

        for (LabSection section : labSections){
            nominated = section.getNominated();
            sectionNumber = section.getSectionNumber();
            indexOfGTA = labSectionAnalyzer.getIndexOfGTA(nominated);
            indexOfSection = gtas[indexOfGTA].getDoableSectionList().indexOf(sectionNumber);
            assertTrue("The nominated should be able to attend the assigned lab section", indexOfSection != -1);
        }
    }
}

