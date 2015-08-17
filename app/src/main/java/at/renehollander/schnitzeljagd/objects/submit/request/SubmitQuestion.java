package at.renehollander.schnitzeljagd.objects.submit.request;

public class SubmitQuestion extends SubmitRequest {

    private int answer;

    public SubmitQuestion(int answer) {
        super(Type.QUESTION);
        this.answer = answer;
    }

    public int getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "SubmitQuestion{" +
                "answer=" + answer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubmitQuestion)) return false;
        if (!super.equals(o)) return false;

        SubmitQuestion that = (SubmitQuestion) o;

        if (answer != that.answer) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + answer;
        return result;
    }
}
