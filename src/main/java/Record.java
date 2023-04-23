import javax.sound.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Record {

    public static void getAudio() {

        try {

        AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
        System.out.println(audioFormat.toString());
        DataLine.Info.class.toString();
        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        System.out.println(dataInfo.toString());

        if (!AudioSystem.isLineSupported(dataInfo)) {
            System.out.println("not supported");
        }


            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            targetLine.open();
            targetLine.start();

        // create audioRecorderThread

        Thread audioRecorderThread = new Thread() {

          @Override public void run() {

              AudioInputStream recordingStream = new AudioInputStream(targetLine);
              File outputFile = new File("src/data/recording.wav");

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
        sleep(10000);
        System.out.println("Stopping recording...");
        targetLine.stop();
        targetLine.close();

    } catch (LineUnavailableException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
