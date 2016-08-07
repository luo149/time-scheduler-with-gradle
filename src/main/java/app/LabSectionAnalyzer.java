package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LabSectionAnalyzer{
    private LabSection[] labSectionList;
    private GTA[] GTAList;
    private static ArrayList<String> timeSlots;
    private static ArrayList<ArrayList<String>> data;

    /*********************************************************************
     *                          Setters
     *********************************************************************/

    public void setTimeSlots(ArrayList<String> timeSlots){
        LabSectionAnalyzer.timeSlots = timeSlots;
    }

    public void setData(ArrayList<ArrayList<String>> data){
        LabSectionAnalyzer.data = data;
    }

    /*********************************************************************
     *                          Getters
     *********************************************************************/

    public GTA[] getGTAList(){
        return GTAList;
    }

    public LabSection[] getLabSectionList(){
        return labSectionList;
    }

    public ArrayList<ArrayList<String>> getData() { return data; }

    /*********************************************************************
     *                          Operations
     *********************************************************************/

    /**
     * Get the final assigned lab schedule
     * Note: Suppose number of section is more than number of GTA and less than double the # of GTA
     *  # of GTA < # of sections < # of GTA * 2
     */
    public void getFinalLabSchedule(){
        // init labSectionList
        int numberOfSections = getNumberOfTimeSlots();
        initLabSectionList(numberOfSections);
        // init GTAList
        int numberOfGTAs = data.size();
        initGTAList(numberOfGTAs);

        // Get basic statistics for each section and each GTA
        setInfoForEachSection();
        setInfoForEachGTA();

        // First round
        firstRound();

        // Second round
        secondRound();
    }

    /*********************************************************************
     *                  Load Data For Lab Assignment
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
     * Init labSectionList
     * @param numberOfSections: total number of sections
     */
    private void initLabSectionList(int numberOfSections){
        labSectionList = new LabSection[numberOfSections];
        for(int i = 0; i < numberOfSections; i++){
            labSectionList[i] = new LabSection();
        }
    }

    /**
     * Init GTAList
     * @param numberOfGTAs: total number of GTAs
     */
    private void initGTAList(int numberOfGTAs){
        GTAList = new GTA[numberOfGTAs];
        for(int i = 0; i < numberOfGTAs; i++){
            GTAList[i] = new GTA();
        }
    }

    /**
     * Set section number for each section
     * Set candidates for each section
     * Set nominated temporarily as ""
     * Note: section number is 1 based
     */
    void setInfoForEachSection(){
        int numberOfSections = labSectionList.length;
        ArrayList<String> subList;
        for(int i = 0; i < numberOfSections; i++){
            subList = new ArrayList<>();
            for(ArrayList<String> row : data){
                if(row.get(i+1).equals("y")){
                    subList.add(row.get(0));
                }
            }
            labSectionList[i].setSectionNumber(i+1);
            labSectionList[i].setCandidates(subList);
            labSectionList[i].setNominated("");
        }
    }

    /**
     * Set name for each GTA
     * Set section list doable for each GTA
     * Note the section number is 1 based
     */
    void setInfoForEachGTA(){
        int index = 0;
        ArrayList<Integer> doableList;
        ArrayList<Integer> assignedList;
        for(ArrayList<String> row : data){
            doableList = new ArrayList<>();
            assignedList = new ArrayList<>();
            for(int i = 0; i < row.size(); i++){
                if (row.get(i).equals("y")){
                    doableList.add(i);
                }
            }
            GTAList[index].setDoableSectionList(doableList);
            GTAList[index].setName(row.get(0));
            GTAList[index].setAssignedSectionList(assignedList);
            index++;
        }
    }

    /*********************************************************************
     *                          First Round
     *********************************************************************/

    /**
     * Assign labs to GTA and update the final lab schedule
     * Make sure every GTA is assigned one section, so we could fully utilize the given resources
     * Priority:
     *  1. Section takes precedence over GTA
     *  2. Always assign GTA to section with fewest number of GTA available
     *  3. After one section is assigned, dynamically update the # of sections doable for each GTA
     */
    private void firstRound(){
        int numberOfGTA = GTAList.length;
        // used to mark number of GTA assigned section
        int numberOfGTAAssigned = 0;
        // used to check one-to-one case
        ArrayList<Integer> matched;

        LabSection section;
        String nominated;

        // First check if there's any section has only one candidate
        if ((matched = checkOneToOneCase()) != null){
            // found one to one case
            for (Integer sectionNumber : matched){
                section = labSectionList[sectionNumber-1];
                // get name of the nominated
                nominated = section.getCandidates().get(0);
                // assign the section to the nominated
                assignSection(nominated, section);
                // increase number of GTA assigned
                numberOfGTAAssigned++;
            }
        }

        // Assign sections to the rest of GTA to make sure every one gets a section
        // start from the busiest GTA excluding those who already has lab assigned
        int sectionNumber;
        int indexOfGTA;
        int indexOfSection;
        while(numberOfGTAAssigned < numberOfGTA){
            // sort the GTAList
            sortGTAList();
            // find the next busiest GTA who has not been assigned any section
            indexOfGTA = 0;
            while(GTAList[indexOfGTA].getAssignedSectionList().size() != 0){ indexOfGTA++; }
            sectionNumber = findSectionOfHighestPriority(GTAList[indexOfGTA].getDoableSectionList());
            indexOfSection = getIndexOfSection(sectionNumber);
            assignSection(GTAList[indexOfGTA].getName(), labSectionList[indexOfSection]);
            numberOfGTAAssigned++;
        }
    }

    /**
     * Check if there's any lab section only one GTA is available
     * @return the list of section numbers which is one-one case, otherwise return null
     */
    private ArrayList<Integer> checkOneToOneCase(){
        ArrayList<Integer> matched = new ArrayList<>();
        int index = 1;
        for(LabSection section : labSectionList){
            if (section.getCandidates().size() == 1){
                matched.add(index);
            }
            index++;
        }
        if (matched.size() < 0){
            return null;
        }
        return matched;
    }

    /*********************************************************************
     *                          Second Round
     *********************************************************************/

    /**
     * Second Round
     * Assign the rest of sections to available GTA by choosing the most proper candidate
     * Start from the section with fewest GTA
     * To choose the most proper candidate:
     * 1. make sure that the candidate don't take consecutive sections
     * 2. The longer the gap between his last assigned and the current section, The Better
     */
    private void secondRound(){
        ArrayList<LabSection> remain = getRestUnassignedSections();
        int indexOfSection;
        LabSection section;
        for (int i = 0; i < remain.size(); i++){
            // sort the section list by putting section with fewest GTA available in the front
            remain = sortSectionList(remain);
            // find the index of next section with fewest candidates
            indexOfSection = 0;
            // get to the next section with fewest GTAs and is not assigned
            while(!remain.get(indexOfSection).getNominated().equals("")){ indexOfSection++; }
            section = remain.get(indexOfSection);
            String nominated = findMostProperCandidate(section.getCandidates(), section.getSectionNumber());
            // assign the lab
            assignSection(nominated, section);
        }
    }

    /**
     * Get the rest of unassigned sections
     * @return the list of unassigned sections
     */
    private ArrayList<LabSection> getRestUnassignedSections(){
        ArrayList<LabSection> remain = new ArrayList<>();
        for (LabSection section : labSectionList){
            if (section.getNominated().equals("")){
                remain.add(section);
            }
        }
        return remain;
    }

    /*********************************************************************
     *                   Find Info About Sections and GTA
     *********************************************************************/

    /**
     * Find the section of highest priority.
     * It means section with fewest number of candidates available
     * @param sectionList: a list of doable sections
     * @return the section number of most proper section
     */
    private int findSectionOfHighestPriority(ArrayList<Integer> sectionList){
        int res = 0;
        int min = Integer.MAX_VALUE;
        int indexOfSection;
        int numberOfCandidates;
        for (Integer sectionNumber : sectionList){
            indexOfSection = getIndexOfSection(sectionNumber);
            numberOfCandidates = labSectionList[indexOfSection].getCandidates().size();
            if (numberOfCandidates < min){
                min = numberOfCandidates;
                res = sectionNumber;
            }
        }

        return res;
    }

    /**
     * Find the most proper candidate who has been assigned a section that has longest gap from the current one
     * @param candidates: list of candidates for the current section
     * @param sectionNumber: the current section to be assigned
     * @return the most proper candidate's name
     */
    private String findMostProperCandidate(ArrayList<String> candidates, int sectionNumber){
        int maxGap = Integer.MIN_VALUE;
        String nominated = "";
        int indexOfGTA;
        int prevSectionNumber;
        int gap;
        for(String candidate : candidates){
            indexOfGTA = getIndexOfGTA(candidate);
            // ** now only consider each GTA at most take 2 sections
            prevSectionNumber = GTAList[indexOfGTA].getAssignedSectionList().get(0);
            // calculate the gap(Absolute val) between the prev assigned section and current section
            gap = Math.abs((prevSectionNumber-sectionNumber));
            if ( gap > maxGap){
                maxGap = gap;
                nominated = candidate;
            }
        }
        return nominated;
    }

    /**
     * get index of a GTA in the GTA list
     * @return the index if found otherwise return -1
     */
    int getIndexOfGTA(String nameOfGTA){
        for (int i = 0; i < GTAList.length; i++){
            if (GTAList[i].getName().equals(nameOfGTA)){
                return i;
            }
        }
        // did not find the GTA
        return -1;
    }

    /**
     * get index of a section in the lab section list
     * @return the index if found otherwise return -1
     */
    int getIndexOfSection(int sectionNumber){
        for (int i = 0; i < labSectionList.length; i++){
            if (labSectionList[i].getSectionNumber() == sectionNumber){
                return i;
            }
        }
        // no such section
        return -1;
    }

    /*********************************************************************
     *                        Updates Data
     *********************************************************************/

    /**
     * Assign the section to the nominated GTA
     * Increase number of section assigned to the nominated
     * Remove the assigned section from all its candidates' section list
     * @param nominated name of the nominated GTA
     * @param section the section to be assigned
     */
    private void assignSection(String nominated, LabSection section){
        // updated the nominated for the section
        section.setNominated(nominated);

        // find the nominated from the GTA list
        int indexOfGTA = getIndexOfGTA(nominated);

        // increase the number of section assigned for the nominated
        ArrayList<Integer> assignedList = GTAList[indexOfGTA].getAssignedSectionList();
        assignedList.add(section.getSectionNumber());
        GTAList[indexOfGTA].setAssignedSectionList(assignedList);

        // find the section from lab section list
        int indexOfSection = getIndexOfSection(section.getSectionNumber());

        // remove the assigned section from all its candidates' section list
        ArrayList<String> candidates = labSectionList[indexOfSection].getCandidates();
        for(String candidate : candidates){
            indexOfGTA = getIndexOfGTA(candidate);
            GTAList[indexOfGTA].getDoableSectionList().remove(new Integer(section.getSectionNumber()));
        }
    }

    /**
     * Sort the GTA list by putting busiest GTA in the beginning
     * Ascending order by number of doable sections
     * By insertion sort
     */
    private void sortGTAList(){
        int numberOfSections;
        GTA tmpGTA;
        for(int i = 1; i < GTAList.length; i++){
            numberOfSections = GTAList[i].getDoableSectionList().size();
            for (int j = i-1; j >= 0 && GTAList[j].getDoableSectionList().size() > numberOfSections; j--){
                // swap the two GTA
                tmpGTA = GTAList[j+1];
                GTAList[j+1] = GTAList[j];
                GTAList[j] = tmpGTA;
            }
        }
    }

    /**
     * Sort the section list by Ascending Order based on number of each section's candidates
     * @param sectionList: list of sections
     * @return the sorted section list
     */
    private ArrayList<LabSection> sortSectionList(ArrayList<LabSection> sectionList){
        Collections.sort(sectionList, new Comparator<LabSection>() {
            @Override
            public int compare(LabSection section1, LabSection section2) {
                return section1.getCandidates().size()-section2.getCandidates().size();
            }
        });
        return sectionList;
    }

    /*********************************************************************
     *                      Print Out
     *********************************************************************/

    /**
     * Print final lab schedule while each lab is assigned to participants properly
     */
    public void printFinalLabSchedule(){
        System.out.println("\n****  The Final Lab Schedule is ****");
        String nominated;
        for(LabSection section : labSectionList){
            nominated = section.getNominated();
            if (nominated.equals("")){
                nominated = "?";
            }
            System.out.println(section.getSectionNumber() + ": " + nominated);
        }
    }

    /**
     * Test use
     * Print the GTAList
     */
    private void printGTAList(){
        for(int i = 0; i < GTAList.length; i++){
            System.out.print(i + ". "
                    + GTAList[i].getName() + " "
                    + GTAList[i].getDoableSectionList().size() + ": ");
            for(Integer section : GTAList[i].getDoableSectionList()){
                System.out.print(section + " ");
            }
            System.out.println();
        }
    }

    /**
     * Test use
     * Print SectionsList
     */
    private void printSectionList(ArrayList<LabSection> list){
        for (LabSection section : list){
            System.out.println(section.getSectionNumber() + " : "  + section.getCandidates().size());
        }
    }
}
