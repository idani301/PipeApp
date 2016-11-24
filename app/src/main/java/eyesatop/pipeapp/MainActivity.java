package eyesatop.pipeapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import logs.LoggerTypes;
import logs.MainLogger;

/**
 * The start of the pipeApp will be here.
 * We will detect what drone's controllers are connected, and start the right instances for
 * that company.
 */
public class MainActivity extends AppCompatActivity  {

    /**
     * Main logger - responsible for all logs creation and print.
     */
    public static MainLogger logger;

    private int numOfPauses  = 0;
    private int numOfResumes = 0;

    /**
     * All on pause actions.
     * We unregister the receiver and print to the logger which pause number it is.
     */
    protected void onPause() {

        super.onPause();

        numOfPauses++;

//        logger.write_message(LoggerTypes.PROGREM_DBG, "Detected Pause num: " + numOfPauses);
    }

    /**
     * when we back to the program, we want to check if we lost some devices during the
     * break.
     */
    protected void onResume(){

        super.onResume();
        numOfResumes++;

//        logger.write_message(LoggerTypes.PROGREM_DBG,"Resume num: " + numOfResumes);
    }

    /**
     * In case we get to a fatal error we need to print the error reason to the error log
     * (and create it if needed).
     * In addition, we would like to mail the error to the relevant owner if the user will accept it.
     * 31.10.2016 - Owner is Idan Yitzhak(idany@eyesatop.com)
     * Finally, we will make final steps to close the program , and return relevant drones home.
     * Then , exit the progrem.
     */
    public void fatalError(String reason) {

        try {
            if(!logger.is_log_exists(LoggerTypes.ERROR_DBG)) {
                logger.add_logger(LoggerTypes.ERROR_DBG, "fatal_error_dbg");
            }
            logger.write_message(LoggerTypes.ERROR_DBG,"FAIL: " + reason);
//            logger.write_message(LoggerTypes.PROGREM_DBG,"Detected Fatal Error: " + reason);
            sendLogsMail();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * In case we get to a non fatal error we need to print the error reason to the error log
     * (and create it if needed).
     * Also, we will inform the user that we had some errors , so it is very recommended to
     * save the logs in order to show the programmers of Eyesatop.
     * Also, the error log full path will appear on screen(In case the user is not near internet connection)
     */
    public void nonFatalError(String reason){

        try {
            if(!logger.is_log_exists(LoggerTypes.ERROR_DBG)) {
                logger.add_logger(LoggerTypes.ERROR_DBG, "fatal_error_dbg");
            }
            logger.write_message(LoggerTypes.ERROR_DBG,"FAIL: " + reason);
//            logger.write_message(LoggerTypes.PROGREM_DBG,"Detected Non-Fatal Error: " + reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function sends all logs that exists, to Eyesatop programmers.
     */
    private void sendLogsMail(){

        try {

//            logger.write_message(LoggerTypes.PROGREM_DBG, "Sending logs by mail");

            String[] TO = {"idani301@gmail.com"};

            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Logs");

            LoggerTypes[] all_logs = LoggerTypes.values();
            ArrayList<Uri> uris = new ArrayList<>();
            for (int i = 0; i < all_logs.length; i++) {

                if (logger.is_log_exists(all_logs[i])) {

//                    logger.write_message(LoggerTypes.PROGREM_DBG, "Added the log " + all_logs[i] + " to the mail");

                    String filename = logger.get_log_path(all_logs[i]);
                    File fileLocation = new File(filename);
                    Uri path = Uri.fromFile(fileLocation);
                    uris.add(path);
                }
            }
            emailIntent.putExtra(Intent.EXTRA_STREAM, uris);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            nonFatalError("Failed to send logs by mail");
        }
    }

    /**
     * onCreate method.
     * Here we first check which controllers are currently connected.
     * Also, we ask for relevant permissions and loading listeners.
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);

            logger = PipeApp.logger;

            Button mailBtn = (Button) findViewById(R.id.sendMail);
            mailBtn.setOnClickListener(new View.OnClickListener() {


                // Sending all logs by mail.
                public void onClick(View view) {
                    sendLogsMail();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            fatalError("Creation Error: cannot accept error inside the function that responsible for the app initialize");
        }
    }
}
