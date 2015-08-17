package at.renehollander.schnitzeljagd.objects.station;

import java.util.Arrays;
import java.util.UUID;

public class QuestionStation extends Station {

    private String question;
    private String[] answers;

    public QuestionStation(UUID id, String name, String content, String question, String[] answers) {
        super(Type.QUESTION, id, name, content);
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "QuestionStation [type=" + type + ", id=" + id + ", name=" + name + ", content=" + content + ", question=" + question + ", answers=" + Arrays.toString(answers) + "]";
    }

}
