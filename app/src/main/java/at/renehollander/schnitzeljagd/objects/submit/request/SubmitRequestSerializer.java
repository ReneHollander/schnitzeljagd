package at.renehollander.schnitzeljagd.objects.submit.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SubmitRequestSerializer implements JsonSerializer<SubmitRequest> {
    @Override
    public JsonElement serialize(SubmitRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retObject = new JsonObject();
        if (src.getType() == SubmitRequest.Type.QR) {
            SubmitQR submitQr = (SubmitQR) src;
            retObject.addProperty("qr", submitQr.getQr().toString());
        } else if (src.getType() == SubmitRequest.Type.QUESTION) {
            SubmitQuestion submitQuestion = (SubmitQuestion) src;
            retObject.addProperty("selectedanswer", submitQuestion.getAnswer());
        }
        return retObject;
    }
}