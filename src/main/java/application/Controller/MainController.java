package application.Controller;



import application.Main;
import application.Model.Record;
import application.Model.syncSpeech;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import javax.sound.sampled.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    TextArea transcriptionArea;

    @FXML
    TextArea translationArea;

    @FXML
    Button startButton;

    @FXML
    Button cancelButton;

    @FXML
    Text currentAudioSource;

    private static TargetDataLine targetLine;
    private static Mixer mixer;
    private static boolean recording = false;
    private Record recAudio;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setTargetLine(getMic());
        cancelButton.setDisable(true);

        transcriptionArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String text = "";
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        text = deeplTranslate(transcriptionArea.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!text.equals("")) {
                        translationArea.setText(text);
                    }
                }
            }
        });

        try {
            SettingsController.getSettings();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(SettingsController.getProjectID());
        System.out.println(SettingsController.getSourceLanguage());
        System.out.println(SettingsController.getTargetLanguage());
    }


    public void startButton(ActionEvent arg0) throws Exception {

        recAudio = new Record();

        if (recording) {
            recAudio.stop(targetLine);
            startButton.setText("Start");
            recording = false;
            cancelButton.setDisable(true);

            System.out.println("Starting transcription...");
            application.Model.syncSpeech syncT = new syncSpeech();
            String transcription = syncT.transcribe("src/main/resources/audio/recording.wav", SettingsController.getSourceLanguage());

            System.out.println("Starting translation...");
            String translation = "";

            if (transcription.equals("") || transcription.isEmpty()) {
                translation = "Error: Transcription was empty.";
            } else {
                translation = deeplTranslate(transcription);
            }

            transcriptionArea.setText(transcription);
            translationArea.setText(translation);
            System.out.println("done");

        } else {
            recording = true;
            cancelButton.setDisable(false);
            Record.getAudio(targetLine);
            System.out.println("Starting record...");
            startButton.setText("Stop");
        }

    }

    // translate string to from source language to target language
    public String deeplTranslate(String transcription) throws Exception {
        Translator translator = new Translator(SettingsController.getAuthKey());
        TextResult result = translator.translateText(transcription, SettingsController.getSourceLanguage(), SettingsController.getTargetLanguage());
        System.out.println("translation: " + result.getText());
        return result.getText();
    }

    // cancel, don't transcribe/translate
    public void cancelButton(ActionEvent arg0) {
        System.out.println("Cancelling...");
        recAudio.stop(targetLine);
        recording = false;
        startButton.setText("Start");
        cancelButton.setDisable(true);
    }

    // switch user to settings view
    public void settingsButton(ActionEvent arg0) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Settings.fxml"));
            System.out.println("Switching user to Settings view");
            Main.stage.setScene( new Scene(root, 800, 400) );
            Main.stage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    // attempt to swap from default mic to stereo mix
    public void swapButton(ActionEvent arg0) throws Exception {
        if (mixer != null && mixer.getMixerInfo().getName().contains("Stereo Mix")) {
            setTargetLine(getMic());
            System.out.println("Switched to: Default Mic");
        } else {
            setTargetLine(getStereo());
            System.out.println("Switched to: " + mixer.getMixerInfo().getName());
        }
    }

    // get default mic
    public TargetDataLine getMic() {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine line = null;

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            currentAudioSource.setText("Current: Default Mic");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        mixer = null;
        return line;
    }

    // attempt to get stereo mixer
    public TargetDataLine getStereo() throws LineUnavailableException {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

        for (Mixer.Info mix : mixers) {
            if (mix.getName().contains("Stereo Mix")) {
                mixer = AudioSystem.getMixer(mix);
                currentAudioSource.setText("Current: Stereo Mix");
            }
        }

        TargetDataLine line = (TargetDataLine) mixer.getLine(dataLineInfo);
        return line;
    }

    // setters/getters
    public static void setTargetLine(TargetDataLine tLine) {
        targetLine = tLine;
    }

    public static Mixer getMixer() {
        return mixer;
    }

    public void handle(ActionEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("Switching user to ??? view");
    }

}
