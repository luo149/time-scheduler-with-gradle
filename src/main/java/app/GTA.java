package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import java.util.ArrayList;

public class GTA{

    private String name;
    private ArrayList<Integer> doableSectionList;
    private ArrayList<Integer> assignedSectionList;

    /*********************************************************************
     *                              Setter
     *********************************************************************/

    public void setName(String name){
        this.name = name;
    }

    public void setDoableSectionList(ArrayList<Integer> doableSectionList){
        this.doableSectionList = doableSectionList;
    }

    public void setAssignedSectionList(ArrayList<Integer> assignedSectionList){
        this.assignedSectionList = assignedSectionList;
    }

    /*********************************************************************
     *                              Getter
     *********************************************************************/
    public String getName(){
        return name;
    }

    public ArrayList<Integer> getDoableSectionList(){
        return doableSectionList;
    }

    public ArrayList<Integer> getAssignedSectionList(){
        return assignedSectionList;
    }
}