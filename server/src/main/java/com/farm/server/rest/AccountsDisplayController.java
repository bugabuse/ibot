/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.farm.server.rest;

import com.farm.server.content.trade.AccountData;
import com.farm.server.rest.AccountsController;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsDisplayController {
    @RequestMapping(value={"accounts/view"})
    public String onlineAccounts(@RequestParam(value="isBanned", required=false) Boolean isBanned, @RequestParam(value="proxy", required=false) String proxy, @RequestParam(value="username", required=false) String username, @RequestParam(value="gameUsername", required=false) String gameUsername, @RequestParam(value="autostartScript", required=false) String autostartScript, @RequestParam(value="isFlaggedAsStolen", required=false) Boolean isFlaggedAsStolen, @RequestParam(value="isMembers", required=false) Boolean isMembers) throws IOException {
        AccountData[] accs;
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");
        sb.append("<style>table { border-collapse: collapse;} table, th, td { border: 1px solid black;}</style>");
        sb.append("<a href='../index.html'>OSRSBot Panel</a><br>");
        sb.append("<a href='../webconfig/?get=all'>WebConfig</a><br><br>");
        sb.append("<pre>");
        Predicate<AccountData> proxyFilter = a -> proxy == null || a.proxy.equalsIgnoreCase(proxy);
        Predicate<AccountData> scriptFilder = a -> autostartScript == null || a.autostartScript.equalsIgnoreCase(autostartScript);
        Predicate<AccountData> usernameFilter = a -> username == null || a.username.equalsIgnoreCase(username);
        Predicate<AccountData> gameUsernameFilter = a -> gameUsername == null || a.gameUsername.equalsIgnoreCase(gameUsername);
        Predicate<AccountData> isFlaggedAsStolenFilter = a -> isFlaggedAsStolen == null || a.isFlaggedAsStolen == isFlaggedAsStolen;
        Predicate<AccountData> isBannedFilter = a -> isBanned == null || a.isBanned == isBanned;
        Predicate<AccountData> isMembersFilter = a -> isMembers == null || a.isMembers == isMembers;
        sb.append("<table>");
        for (Field f : AccountData.class.getFields()) {
            if (!this.isViewing(f.getName())) continue;
            f.setAccessible(true);
            sb.append("<th>" + f.getName() + "</th>");
        }
        sb.append("<th>Termination</th>");
        for (AccountData data : accs = (AccountData[])AccountsController.getAccounts().stream().filter(proxyFilter).filter(isMembersFilter).filter(isBannedFilter).filter(scriptFilder).filter(usernameFilter).filter(gameUsernameFilter).filter(isFlaggedAsStolenFilter).toArray(x$0 -> new AccountData[x$0])) {
            sb.append("<tr>");
            for (Field f : data.getClass().getFields()) {
                if (!this.isViewing(f.getName())) continue;
                f.setAccessible(true);
                try {
                    sb.append("<th>" + f.get(data) + "</th>");
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (data.isTerminated()) {
                sb.append("<th>" + (data.terminatedUntil - System.currentTimeMillis()) / 1000L / 60L + "min</th>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("</pre>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    private boolean isViewing(String key) {
        return !key.contains("sessionId") && !key.contains("preferredWorld") && !key.contains("currentSpot") && !key.contains("maxSpotCount") && !key.contains("uniqueScriptId") && !key.contains("note") && !key.contains("world") && !key.contains("lastUpdate") && !key.contains("currentScript") && !key.contains("description") && !key.contains("accountCreationTime");
    }
}

