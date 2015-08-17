package at.renehollander.schnitzeljagd.objects.submit.response;

public class Won extends SubmitResponse {

    public Won() {
        super(Type.WON);
    }

    @Override
    public String toString() {
        return "Won [type=" + type + "]";
    }

}
