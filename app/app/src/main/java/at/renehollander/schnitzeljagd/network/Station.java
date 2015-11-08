package at.renehollander.schnitzeljagd.network;

import org.json.JSONObject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Station {

    private final String name;
    private final String description;
    private final Navigation navigation;
    private final Answer answer;

    public static void getCurrentStation(Connection.Callback<Station> cb) {
        Connection.instance().getSocket().emit("get_current_station", null, (data) -> {
            try {
                JSONObject obj = Connection.validateData(data);
                if (obj == null) {
                    cb.call(null, null);
                } else {

                    Navigation navigation = null;
                    {
                        JSONObject navObj = obj.getJSONObject("navigation");
                        String type = navObj.getString("type");
                        String text = null;
                        if (navObj.has("text")) {
                            text = navObj.getString("text");
                        }
                        switch (type) {
                            case "text":
                                String content = navObj.getString("content");
                                navigation = new Navigation.Text(text, content);
                                break;
                            default:
                                throw new IllegalStateException("unknown navigation type " + type);
                        }
                    }

                    Answer answer = null;
                    {
                        JSONObject answerObj = obj.getJSONObject("answer");
                        String type = answerObj.getString("type");
                        String text = null;
                        if (answerObj.has("text")) {
                            text = answerObj.getString("text");
                        }
                        switch (type) {
                            case "qr":
                                answer = new Answer.QR(text);
                                break;
                            default:
                                throw new IllegalStateException("unknown answer type " + type);
                        }
                    }

                    Station station = new StationBuilder().name(obj.getString("name")).description(obj.getString("description")).navigation(navigation).answer(answer).build();
                    cb.call(station);
                }
            } catch (Exception e) {
                cb.call(e);
            }
        });
    }

}
