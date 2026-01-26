package web.FirstSecurityApp.exceptions;


public class UserIncorrectData extends RuntimeException {
    private String info;

    public UserIncorrectData() {
    }

    public UserIncorrectData(String message) {
        super(message);
        this.info = message;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
