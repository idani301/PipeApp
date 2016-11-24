package logs;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class responsiable for all logs the pipe will create.
 * Flight debug - will collect all relevant data that will help to understand what exactly
 *                happened during the flight inside the proper timestamp.
 * Program debug - will collect all relevant data related to the progrem run inside the proper
 *                 timestamps.
 */
public class MainLogger {

    HashMap<LoggerTypes,GeneralLogger> loggers_map;

    File root_directory;

    private class WritingLogThread extends Thread {

        LoggerTypes log_type;
        String message;

        public WritingLogThread(LoggerTypes logType,String msg){
            log_type = logType;
            message = msg;
        }

        public void run(){
            boolean is_opened = false;

            try{
                reopen_log(log_type);
                is_opened = true;

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

                if (loggers_map.get(log_type) == null){
                    System.out.println("Logger " + log_type + " is not exists\n");
                }
                else{
                    String currentTime = sdf.format(cal.getTime());
                    loggers_map.get(log_type).write_message(currentTime +  ":" + message);
                }
                close_log(log_type);
            } catch (IOException e) {
                System.out.println("Can't print :" + message);
                e.printStackTrace();
                if(is_opened) {
                    close_log(log_type);
                }
            }
            catch(Exception e){

            }
        }
    }

    public MainLogger(String run_area_directory){

        loggers_map = new HashMap<LoggerTypes,GeneralLogger>();

        int counter = 0;
        boolean is_created = false;

        while(is_created == false){

            counter++;

            root_directory = new File(Environment.getExternalStorageDirectory(),run_area_directory + "/Run_" + counter);

            if(!root_directory.exists()){
                root_directory.mkdirs();
                is_created = true;
            }
        }
    }

    public void add_logger(LoggerTypes log_type,String log_file_name) throws IOException {
        loggers_map.put(log_type,new GeneralLogger(root_directory.getAbsolutePath(),log_file_name));
    }


    public void close_log(LoggerTypes logType){
        loggers_map.get(logType).close_log();
    }

    public void reopen_log(LoggerTypes logType) throws IOException {
        loggers_map.get(logType).reopen_log();
    }

    public String get_log_path(LoggerTypes log_type){

        if (loggers_map.get(log_type) == null){
            return null;
        }
        else{
            return loggers_map.get(log_type).get_log_path();
        }
    }

    public boolean is_log_exists(LoggerTypes log_type){

        if(loggers_map == null){
            return false;
        }

        if (loggers_map.get(log_type) == null){
            return false;
        }
        else{
            return true;
        }
    }

    public void write_message(LoggerTypes logType, String message){

        if(!is_log_exists(logType)){
            return;
        }

        try {
            WritingLogThread logThread = new WritingLogThread(logType, message);
            logThread.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
