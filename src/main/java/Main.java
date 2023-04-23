import com.google.cloud.translate.v3.*;

import java.io.IOException;

/*
*
* Austin Moore
*
*/

public class Main {

    public static void main(String[] args) throws Exception {

        // set Google Cloud project variables
        String projectId = "PUT PROJECT ID FROM GCLOUD CONSOLE HERE";
        String targetLanguage = "LANGUAGE CODE HERE";

        // need to fix to record desktop audio
        Record.getAudio();

        String file = "src/data/recording.wav";

        // either async (over 60 seconds), or sync (under 60 seconds), sync is cheaper per/min
        // String script = asyncSpeech.transcribe(file);
        String script = syncSpeech.transcribe(file);

        if (script.isEmpty()) {
            System.out.println("Error: speech to text result was empty");
            return;
        }

        Translate.translate(projectId, targetLanguage, script);

    }

}
