package application.Controller;



import application.Main;
import application.Model.Record;
import application.Model.Translate;
import application.Model.syncSpeech;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

public class MainController {
    @FXML
    TextArea transcriptionArea;

    @FXML
    TextArea translationArea;

    private String tLanguage = "en-US";
    private String sLanguage = "";
    private String projectID = "";
    private static TargetDataLine targetLine;


    public void startButton(ActionEvent arg0) throws Exception {

        System.out.println("Starting...");

        if (targetLine == null) {
            // create default line
        }
        System.out.println("Starting record...");
        Record recAudio = new Record();
        Record.getAudio(targetLine);

        System.out.println("Starting transcription...");
        syncSpeech syncT = new syncSpeech();
        String transcription = syncT.transcribe("src/main/resources/audio/recording.wav", sLanguage);


        System.out.println("Starting translation...");
        Translate t = new Translate();
        String translation = t.translate(projectID, tLanguage, "おはようございます！");

        transcriptionArea.setText(transcription);
        translationArea.setText(translation);

    }

    public void settingsButton(ActionEvent arg0) throws Exception {

        // move the user to the Decks view
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Settings.fxml"));
            System.out.println("Switching user to Settings view");
            Main.stage.setScene( new Scene(root, 800, 400) );
            Main.stage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public void settLanguage(String tLanguage) {
        this.tLanguage = tLanguage;
    }

    public static void setTargetLine(TargetDataLine tLine) {
        targetLine = tLine;
    }


    public void handle(ActionEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("Switching user to ??? view");
    }

}
