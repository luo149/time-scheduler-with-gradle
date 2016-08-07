package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/*********************************************************************
 *                          Access Data
 *********************************************************************/

public class CsvFileReader{
    private static final String COMMA_DELIMITER = ",";
    private String fileHeader;

    /**
     * Read and access data from the file
     * @param filePath: file path of the file
     * @return the voted table excluding the file header
     */
    public ArrayList<ArrayList<String>> readCsvFile(String filePath){
        BufferedReader fileReader = null;
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            String line;

            // Create the file reader
            fileReader = new BufferedReader(new FileReader(filePath));

            // Save the header
            fileHeader = fileReader.readLine();

            // Save the rest of rows to the list, each row is a participant
            while((line = fileReader.readLine()) != null) {
                list.add(stringToList(line));
            }
            //System.out.println("Get the data from file successfully!");
        }
        catch(Exception e){
            System.out.println("Error in CsvFileReader!!");
            e.printStackTrace();
        }
        finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            }
            catch (IOException e){
                System.out.println("Error in closing fileReader!!");
                e.printStackTrace();
            }
        }
        return list;
    }

    public ArrayList<String> getTimeSlots(){
        return stringToList(fileHeader);
    }

    /*********************************************************************
     *                          Modify Data
     *********************************************************************/

    private ArrayList<String> stringToList(String longString){
        String[] items = longString.split(COMMA_DELIMITER);
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(items));
        return list;
    }

    /*********************************************************************
     *                          Test Only
     *********************************************************************/
    public String getFileHeader(){
        return fileHeader;
    }
}
