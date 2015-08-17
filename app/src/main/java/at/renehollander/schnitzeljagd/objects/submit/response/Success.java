package at.renehollander.schnitzeljagd.objects.submit.response;

public class Success extends SubmitResponse {

    private boolean success;

    public Success(boolean success) {
        super(Type.SUCCESS);
        this.success = success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    @Override
    public String toString() {
        return "Success [type=" + type + ", success=" + success + "]";
    }

}
