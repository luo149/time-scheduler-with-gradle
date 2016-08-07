package app;

/**
 * Created by mengxueluo on 8/7/16.
 */
public class FileAccessor {
    private static final String prefix = System.getProperty("user.home") + "/Desktop/time_scheduler_data/";

    public String getFilePath(String fileName){
        return prefix + fileName;
    }
}
