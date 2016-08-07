package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvFileWriter {
    // Delimiters in CSV file
    private static final String DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    // first line of file
    private String fileHeader;

    /****************************************************************
     *                         Constructor
     ****************************************************************/

    public CsvFileWriter(String fileHeader) {
        this.fileHeader = fileHeader;
    }


    /****************************************************************
     *                          Init File
     ****************************************************************/
    public void writeCsvFile(String filePath, ArrayList<ArrayList<String>> participants) {
        FileWriter fileWriter = null;
        // check whether the file with same name exits
        File f = new File(filePath);
        if (f.exists()){
            System.out.println("Creating CSV file  fails!");
            System.out.println("File with same Event title and location already Exits!!");
            return;
        }

        try {
            fileWriter = new FileWriter(filePath);

            // Add the file header
            fileWriter.append(fileHeader);
            fileWriter.append(NEW_LINE_SEPARATOR);

            // Add the status of each participants to the file
            for (ArrayList<String> participant : participants) {
                for (String item : participant) {
                    fileWriter.append(item);
                    if (!item.equals(participant.get(participant.size() - 1))) {
                        fileWriter.append(DELIMITER);
                    }
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }
            }

            System.out.println("Created CSV file  successfully!");
        } catch (Exception e) {
            System.out.println("Error in creating CSV file!");
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null){
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Error in flushing/closing the fileWriter!!");
                e.printStackTrace();
            }
        }
    }

    /*********************************************************************
     *                        Overwrite Data
     *********************************************************************/

    public void overwriteFile(String filePath, ArrayList<ArrayList<String>> rows) {
        File fileNew = new File(filePath);
        FileWriter fileWriter = null;

        try {
            // Set the file to be overwritten
            fileWriter = new FileWriter(fileNew, false);
            fileWriter.write(fileHeader);
            fileWriter.append(NEW_LINE_SEPARATOR);

            // Add the status of each participants to the file
            for (ArrayList<String> row : rows) {
                for (String item : row) {
                    fileWriter.append(item);
                    if (!item.equals(row.get(row.size() - 1))) {
                        fileWriter.append(DELIMITER);
                    }
                }
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("Overwrite CSV file  successfully!");

        } catch (Exception e) {
            System.out.println("Error in creating CSV file!");
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null){
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Error in flushing/closing the fileWriter!!");
                e.printStackTrace();
            }
        }
    }
}
