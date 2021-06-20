package com.farm.ibot.api.util;

import org.apache.commons.text.StrBuilder;
import org.incava.diff.Diff;
import org.incava.diff.Difference;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class StringUtils {
    public static String format(String str) {
        try {
            if (str == null) {
                return null;
            } else {
                str = str.replace(' ', ' ');
                return new String(str.getBytes(), "UTF-8");
            }
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return str;
        }
    }

    public static boolean containsEqual(String str, String[] array) {
        if (str != null && array != null) {
            String[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String s = var2[var4];
                if (Objects.equals(s, str)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsEqualIgnoreCase(String str, String[] array) {
        if (str != null && array != null) {
            String[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String s = var2[var4];
                if (equalsIgnoreCase(s, str)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean equalsIgnoreCase(String s1, String s2) {
        s1 = "" + s1.toLowerCase();
        s2 = "" + s2.toLowerCase();
        return s1.equalsIgnoreCase(s2);
    }

    public static String formatColorsString(String str) {
        return str.replaceAll("<(.*?)>", "");
    }

    public static boolean containsAny(String str, String[] array) {
        if (str != null && array != null) {
            String[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String s = var2[var4];
                if (s != null && str.toLowerCase().contains(s.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String substring(String str, int minLen, int maxLen) {
        maxLen = MathUtils.clamp(str.length(), minLen, maxLen);
        return str.substring(minLen, maxLen);
    }

    public static String substring(String str, int maxLen) {
        maxLen = MathUtils.clamp(str.length(), 0, maxLen);
        return str.substring(0, maxLen);
    }

    public static String diffSideBySide(String fromStr, String toStr) {
        String[] fromLines = fromStr.split("\n");
        String[] toLines = toStr.split("\n");
        List<Difference> diffs = (new Diff(fromLines, toLines)).diff();
        int padding = 3;
        int maxStrWidth = Math.max(maxLength(fromLines), maxLength(toLines)) + padding;
        StrBuilder diffOut = new StrBuilder();
        diffOut.setNewLineText("\n");
        int fromLineNum = 0;
        int toLineNum = 0;
        Iterator var10 = diffs.iterator();

        while (true) {
            while (var10.hasNext()) {
                Difference diff = (Difference) var10.next();
                int delStart = diff.getDeletedStart();
                int delEnd = diff.getDeletedEnd();
                int addStart = diff.getAddedStart();
                int addEnd = diff.getAddedEnd();
                boolean isAdd = delEnd == Difference.NONE && addEnd != Difference.NONE;
                boolean isDel = addEnd == Difference.NONE && delEnd != Difference.NONE;
                boolean isMod = delEnd != Difference.NONE && addEnd != Difference.NONE;

                String left;
                String right;
                do {
                    left = "";
                    right = "";
                    if (fromLineNum < delStart) {
                        left = fromLines[fromLineNum];
                        ++fromLineNum;
                    }

                    if (toLineNum < addStart) {
                        right = toLines[toLineNum];
                        ++toLineNum;
                    }

                    diffOut.append(org.apache.commons.lang3.StringUtils.rightPad(left, maxStrWidth));
                    diffOut.append("  ");
                    diffOut.appendln(right);
                } while (fromLineNum != delStart || toLineNum != addStart);

                int i;
                if (isDel) {
                    for (i = delStart; i <= delEnd; ++i) {
                        diffOut.append(org.apache.commons.lang3.StringUtils.rightPad(fromLines[i], maxStrWidth));
                        diffOut.appendln("<");
                    }

                    fromLineNum = delEnd + 1;
                } else if (isAdd) {
                    for (i = addStart; i <= addEnd; ++i) {
                        diffOut.append(org.apache.commons.lang3.StringUtils.rightPad("", maxStrWidth));
                        diffOut.append("> ");
                        diffOut.appendln(toLines[i]);
                    }

                    toLineNum = addEnd + 1;
                } else if (isMod) {
                    while (true) {
                        left = "";
                        right = "";
                        if (fromLineNum <= delEnd) {
                            left = fromLines[fromLineNum];
                            ++fromLineNum;
                        }

                        if (toLineNum <= addEnd) {
                            right = toLines[toLineNum];
                            ++toLineNum;
                        }

                        diffOut.append(org.apache.commons.lang3.StringUtils.rightPad(left, maxStrWidth));
                        diffOut.append("| ");
                        diffOut.appendln(right);
                        if (fromLineNum > delEnd && toLineNum > addEnd) {
                            break;
                        }
                    }
                }
            }

            do {
                String left = "";
                String right = "";
                if (fromLineNum < fromLines.length) {
                    left = fromLines[fromLineNum];
                    ++fromLineNum;
                }

                if (toLineNum < toLines.length) {
                    right = toLines[toLineNum];
                    ++toLineNum;
                }

                diffOut.append(org.apache.commons.lang3.StringUtils.rightPad(left, maxStrWidth));
                diffOut.append("  ");
                diffOut.appendln(right);
            } while (fromLineNum != fromLines.length || toLineNum != toLines.length);

            return diffOut.toString();
        }
    }

    private static int maxLength(String[] fromLines) {
        int maxLength = 0;

        for (int i = 0; i < fromLines.length; ++i) {
            if (fromLines[i].length() > maxLength) {
                maxLength = fromLines[i].length();
            }
        }

        return maxLength;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < (long) unit) {
            return bytes + " B";
        } else {
            int exp = (int) (Math.log((double) bytes) / Math.log((double) unit));
            String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
            return String.format("%.1f %sB", (double) bytes / Math.pow((double) unit, (double) exp), pre);
        }
    }

    public static String numberToString(Number number) {
        if (number == null) {
            return "NONE";
        } else {
            return number.getClass().getSimpleName().contains("Long") ? number.longValue() + "L" : number.toString();
        }
    }

    public static String firstLetterCapitalized(String name) {
        if (name != null) {
            return name.length() > 1 ? name.substring(0, 1).toUpperCase() + name.substring(1, name.length()).toLowerCase() : name.toUpperCase();
        } else {
            return "";
        }
    }
}
