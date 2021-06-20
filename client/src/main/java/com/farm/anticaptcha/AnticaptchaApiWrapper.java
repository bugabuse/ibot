package com.farm.anticaptcha;

import com.farm.ibot.proxy.Proxy;
import org.json.JSONException;
import org.json.JSONObject;

public class AnticaptchaApiWrapper {
    private static JSONObject jsonPostRequest(String host, String methodName, String postData) {
        AntigateHttpRequest antigateHttpRequest = new AntigateHttpRequest("http://" + host + "/" + methodName);
        antigateHttpRequest.setRawPost(postData);
        antigateHttpRequest.addHeader("Content-Type", "application/json; charset=utf-8");
        antigateHttpRequest.addHeader("Accept", "application/json");
        antigateHttpRequest.setTimeout(30000);

        try {
            return new JSONObject(AntigateHttpHelper.download(antigateHttpRequest).getBody());
        } catch (Exception var5) {
            return null;
        }
    }

    public static AnticaptchaTask createNoCaptchaTask(Proxy proxy, String host, String clientKey, String websiteUrl, String websiteKey, String userAgent) {
        String json = "";

        JSONObject resultJson;
        try {
            resultJson = new JSONObject();
            JSONObject obj = new JSONObject();
            obj.put("clientKey", clientKey);
            resultJson.put("websiteURL", websiteUrl);
            resultJson.put("websiteKey", websiteKey);
            resultJson.put("userAgent", userAgent);
            resultJson.put("type", "NoCaptchaTaskProxyless");
            obj.put("task", resultJson);
            json = obj.toString();
            System.out.println(json);
        } catch (JSONException var10) {
            var10.printStackTrace();
        }

        try {
            resultJson = jsonPostRequest(host, "createTask", json);
            return new AnticaptchaTask(resultJson.has("taskId") ? resultJson.getInt("taskId") : null, resultJson.has("errorId") ? resultJson.getInt("errorId") : null, resultJson.has("errorCode") ? resultJson.getString("errorCode") : null, resultJson.has("errorDescription") ? resultJson.getString("errorDescription") : null);
        } catch (Exception var9) {
            return null;
        }
    }

    public static AnticaptchaTask createImageToTextTask(String host, String clientKey, String body) {
        return createImageToTextTask(host, clientKey, body, (Boolean) null, (Boolean) null, (Integer) null, (Boolean) null, (Integer) null, (Integer) null);
    }

    public static AnticaptchaTask createImageToTextTask(String host, String clientKey, String body, Boolean phrase, Boolean _case, Integer numeric, Boolean math, Integer minLength, Integer maxLength) {
        String json = "{\n  \"clientKey\": \"" + clientKey + "\",\n  \"task\": {\n    \"type\": \"ImageToTextTask\",\n" + (phrase != null ? "    \"phrase\": \"" + phrase + "\",\n" : "") + (_case != null ? "    \"case\": \"" + _case + "\",\n" : "") + (numeric != null ? "    \"numeric\": \"" + numeric + "\",\n" : "") + (math != null ? "    \"math\": \"" + math + "\",\n" : "") + (minLength != null ? "    \"minLength\": " + minLength + ",\n" : "") + (maxLength != null ? "    \"maxLength\": \"" + maxLength + "\",\n" : "") + "    \"body\": \"" + body + "\"\n  }\n}";

        try {
            JSONObject resultJson = jsonPostRequest(host, "createTask", json);
            return new AnticaptchaTask(resultJson.has("taskId") ? resultJson.getInt("taskId") : null, resultJson.has("errorId") ? resultJson.getInt("errorId") : null, resultJson.has("errorCode") ? resultJson.getString("errorCode") : null, resultJson.has("errorDescription") ? resultJson.getString("errorDescription") : null);
        } catch (Exception var11) {
            return null;
        }
    }

    public static AnticaptchaResult getTaskResult(String host, String clientKey, AnticaptchaTask task) {
        String json = "{\n  \"clientKey\": \"" + clientKey + "\",\n  \"taskId\": " + task.getTaskId() + "\n}";

        try {
            JSONObject resultJson = jsonPostRequest(host, "getTaskResult", json);
            AnticaptchaResult.Status status = resultJson.has("status") ? (resultJson.getString("status").equals("ready") ? AnticaptchaResult.Status.ready : AnticaptchaResult.Status.processing) : AnticaptchaResult.Status.unknown;
            String solution = null;
            Integer errorId = null;
            String errorCode = null;
            String errorDescription = null;
            Double cost = null;
            String ip = null;
            Integer createTime = null;
            Integer endTime = null;
            Integer solveCount = null;
            if (resultJson.has("solution")) {
                if (resultJson.getJSONObject("solution").has("gRecaptchaResponse")) {
                    solution = resultJson.getJSONObject("solution").getString("gRecaptchaResponse");
                } else if (resultJson.getJSONObject("solution").has("text")) {
                    solution = resultJson.getJSONObject("solution").getString("text");
                }
            }

            if (resultJson.has("errorId")) {
                errorId = resultJson.getInt("errorId");
            }

            if (resultJson.has("errorCode")) {
                errorCode = resultJson.getString("errorCode");
            }

            if (resultJson.has("errorDescription")) {
                errorDescription = resultJson.getString("errorDescription");
            }

            if (resultJson.has("cost")) {
                cost = resultJson.getDouble("cost");
            }

            if (resultJson.has("createTime")) {
                createTime = resultJson.getInt("createTime");
            }

            if (resultJson.has("endTime")) {
                endTime = resultJson.getInt("endTime");
            }

            if (resultJson.has("solveCount")) {
                solveCount = resultJson.getInt("solveCount");
            }

            return new AnticaptchaResult(status, solution, errorId, errorCode, errorDescription, cost, (String) ip, createTime, endTime, solveCount);
        } catch (Exception var15) {
            return null;
        }
    }

    static enum ProxyType {
        http;
    }
}
