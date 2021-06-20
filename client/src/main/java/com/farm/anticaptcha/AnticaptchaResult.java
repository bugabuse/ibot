package com.farm.anticaptcha;

public class AnticaptchaResult {
    private Integer errorId;
    private String errorCode;
    private String errorDescription;
    private AnticaptchaResult.Status status;
    private String solution;
    private Double cost;
    private String ip;
    private Integer createTime;
    private Integer endTime;
    private Integer solveCount;

    public AnticaptchaResult(AnticaptchaResult.Status status, String solution, Integer errorId, String errorCode, String errorDescription, Double cost, String ip, Integer createTime, Integer endTime, Integer solveCount) {
        this.status = AnticaptchaResult.Status.unknown;
        this.errorId = errorId;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.status = status;
        this.solution = solution;
        this.cost = cost;
        this.ip = ip;
        this.createTime = createTime;
        this.endTime = endTime;
        this.solveCount = solveCount;
    }

    public String toString() {
        return "AnticaptchaResult{errorId=" + this.errorId + ", errorCode='" + this.errorCode + '\'' + ", errorDescription='" + this.errorDescription + '\'' + ", status=" + this.status + ", solution='" + this.solution + '\'' + ", cost=" + this.cost + ", ip='" + this.ip + '\'' + ", createTime=" + this.createTime + ", endTime=" + this.endTime + ", solveCount=" + this.solveCount + '}';
    }

    public Integer getErrorId() {
        return this.errorId;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }

    public AnticaptchaResult.Status getStatus() {
        return this.status;
    }

    public String getSolution() {
        return this.solution;
    }

    public Double getCost() {
        return this.cost;
    }

    public String getIp() {
        return this.ip;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }

    public Integer getEndTime() {
        return this.endTime;
    }

    public Integer getSolveCount() {
        return this.solveCount;
    }

    public static enum Status {
        ready,
        unknown,
        processing;
    }
}
