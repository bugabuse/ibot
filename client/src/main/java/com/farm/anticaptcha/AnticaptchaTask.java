package com.farm.anticaptcha;

public class AnticaptchaTask {
    private Integer errorId;
    private Integer taskId;
    private String errorCode;
    private String errorDescription;
    private AnticaptchaResult result;

    AnticaptchaTask(Integer taskId, Integer errorId, String errorCode, String errorDescription) {
        this.errorId = errorId;
        this.taskId = taskId;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }

    public Integer getTaskId() {
        return this.taskId;
    }

    public Integer getErrorId() {
        return this.errorId;
    }

    public String toString() {
        return "AnticaptchaTask{errorId=" + this.errorId + ", taskId=" + this.taskId + ", errorCode='" + this.errorCode + '\'' + ", errorDescription='" + this.errorDescription + '\'' + '}';
    }
}
