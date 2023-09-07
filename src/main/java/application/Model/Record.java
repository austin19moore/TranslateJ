package application.Model;

import application.Controller.MainController;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class Record {

    public static void getAudio(TargetDataLine targetLine) {

        try {

            if (targetLine == null) {
                System.out.println("Line is invalid, stopping...");
                return;
            }

            targetLine.open();
            targetLine.start();

        // create audioRecorderThread

        Thread audioRecorderThread = new Thread() {

          @Override public void run() {

              AudioInputStream recordingStream = new AudioInputStream(targetLine);
              File outputFile = new File("src/main/resources/audio/recording.wav");

              try {
                  AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
              } catch (IOException ex) {
                  ex.printStackTrace();
              }


          }

        };

        // starts recording for 10 seconds
        System.out.println("Starting recording...");
        audioRecorderThread.start();


    } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public static void stop(TargetDataLine targetLine) {
        targetLine.stop();
        targetLine.close();
        System.out.println("Stopping recording");
    }


}
