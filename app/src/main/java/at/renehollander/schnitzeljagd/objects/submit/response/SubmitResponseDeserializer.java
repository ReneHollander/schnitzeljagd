package at.renehollander.schnitzeljagd.objects.submit.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class SubmitResponseDeserializer implements JsonDeserializer<SubmitResponse> {

    @Override
    public SubmitResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject submitResponseObject = json.getAsJsonObject();
        if (submitResponseObject.has("success")) {
            return new Success(submitResponseObject.get("success").getAsBoolean());
        } else if (submitResponseObject.has("won")) {
            return new Won();
        }
        return null;
    }

}
