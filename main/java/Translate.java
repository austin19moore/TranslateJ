import com.google.cloud.translate.v3.*;
import java.io.IOException;

public class Translate {

    public static String translate(String projectId, String targetLanguage, String text) throws Exception {

        String translated = "";

        try (TranslationServiceClient client = TranslationServiceClient.create()) {

            LocationName parent = LocationName.of(projectId, "global");

            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setTargetLanguageCode(targetLanguage)
                    .addContents(text)
                    .build();

            TranslateTextResponse response = client.translateText(request);

            for (Translation translation : response.getTranslationsList()) {
                System.out.println(translation.getTranslatedText());
                translated += translation.getTranslatedText();
            }


        }

        return translated;

    }

}
