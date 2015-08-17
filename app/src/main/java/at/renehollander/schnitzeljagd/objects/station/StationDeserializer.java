package at.renehollander.schnitzeljagd.objects.station;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.UUID;

public class StationDeserializer implements JsonDeserializer<Station> {

    public Station deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject stationObject = json.getAsJsonObject();
        UUID id = UUID.fromString(stationObject.get("id").getAsString());
        String name = stationObject.get("name").getAsString();
        String content = stationObject.get("content").getAsString();
        Station.Type type = Station.Type.getFromTypeName(stationObject.get("type").getAsString());

        if (type == Station.Type.QR) {
            return new QrStation(id, name, content);
        } else if (type == Station.Type.QUESTION) {
            String question = stationObject.get("question").getAsString();
            JsonArray answerJsonArray = stationObject.get("answers").getAsJsonArray();
            String[] answers = new String[answerJsonArray.size()];
            for (int i = 0; i < answerJsonArray.size(); i++) {
                answers[i] = answerJsonArray.get(i).getAsString();
            }
            return new QuestionStation(id, name, content, question, answers);
        } else {
            return null;
        }
    }
}
