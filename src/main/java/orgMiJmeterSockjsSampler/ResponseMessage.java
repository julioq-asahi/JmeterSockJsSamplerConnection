package orgMiJmeterSockjsSampler;

public class ResponseMessage {

    private String message = "";
    private String problems = "";

    public String getMessage() {
        return message;
    }

    public void addMessage(String message) {
        this.message = (this.message.isEmpty() ? "" : this.message + "\n") + message;
    }

    public String getProblems() {
        return problems;
    }

    public void addProblem(String problem) {
        this.problems = (this.problems.isEmpty() ? "" : this.problems + "\n") + problem;
    }
}