package algonquin.cst2335.shu00003.data;

public class ChatMessage {

    String message;
    String timeSent;
    boolean isSentButton;

    public  ChatMessage(String message, String timeSent, boolean isSentButton){
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}
