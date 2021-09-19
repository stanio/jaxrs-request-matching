package net.example.jaxrs;

import java.util.Comparator;
import java.util.regex.MatchResult;

/*
 * 3.7.2 Request Matching
 *
 * 1.e, 2.f
 */
public final class MatchInfo<P extends PathInfo> implements Comparable<MatchInfo<?>> {

    public static final Comparator<MatchInfo<?>> FULL_PATH = (o1, o2) -> {
        int num1 = o2.getDepth();
        int num2 = o1.getDepth();
        if (num1 == num2) {
            num1 = o1.getLiteralCharacterCount();
            num2 = o2.getLiteralCharacterCount();
            if (num1 == num2) {
                num1 = o1.getCapturingGroupCount();
                num2 = o2.getCapturingGroupCount();
                if (num1 == num2) {
                    num1 = o1.getNonDefaultGroupCount();
                    num2 = o2.getNonDefaultGroupCount();
                }
            }
        }
        return num2 - num1; // descending order
    };

    // Use pathInfo.parameterNames to query the matchResult

    public final P pathInfo;
    public final MatchResult matchResult;
    public final MatchInfo<ResourceInfo> parent;

    private MatchInfo(P info, MatchResult matchResult, MatchInfo<ResourceInfo> parent) {
        this.pathInfo = info;
        this.matchResult = matchResult;
        this.parent = parent;
    }

    static <T extends PathInfo> MatchInfo<T> of(T info, MatchResult matchResult, MatchInfo<ResourceInfo> parent) {
        return new MatchInfo<>(info, matchResult, parent);
    }

    static <T extends PathInfo> MatchInfo<T> of(T info, MatchInfo<ResourceInfo> parent) {
        return new MatchInfo<>(info, info.emptyMatch, parent);
    }

    static <T extends PathInfo> MatchInfo<T> of(T info, MatchResult matchResult) {
        return of(info, matchResult, null);
    }

    int getDepth() {
        int depth = 0;
        MatchInfo<?> current = parent;
        while (current != null) {
            depth += 1;
            current = current.parent;
        }
        return depth;
    }

    int getLiteralCharacterCount() {
        int count = pathInfo.getLiteralCharacterCount();
        if (parent != null) {
            count += parent.getLiteralCharacterCount();
        }
        return count;
    }

    int getCapturingGroupCount() {
        int count = pathInfo.getCapturingGroupCount();
        if (parent != null) {
            count += parent.getCapturingGroupCount();
        }
        return count;
    }

    int getNonDefaultGroupCount() {
        int count = pathInfo.getNonDefaultGroupCount();
        if (parent != null) {
            count += parent.getNonDefaultGroupCount();
        }
        return count;
    }

    /*
     * 3.7.3 Converting URI Templates to Regular Expressions
     *
     *   5. Append ‘(/.*)?’ to the result.
     */
    String finalCapture() {
        return finalCapture(matchResult);
    }

    static String finalCapture(MatchResult matchResult) {
        String str = matchResult.group(matchResult.groupCount());
        return str == null ? "" : str;
    }

    /*
     * 3.7.2 Request Matching
     *
     *   1.e, 2.f
     */
    @Override
    public int compareTo(MatchInfo<?> other) {
        int num1 = pathInfo.getLiteralCharacterCount();
        int num2 = other.pathInfo.getLiteralCharacterCount();
        if (num1 == num2) {
            num1 = pathInfo.getCapturingGroupCount();
            num2 = other.pathInfo.getCapturingGroupCount();
            if (num1 == num2) {
                num1 = pathInfo.getNonDefaultGroupCount();
                num2 = other.pathInfo.getNonDefaultGroupCount();
                if (num1 == num2) {
                    num1 = (pathInfo instanceof MethodInfo) ? 1 : 0;
                    num2 = (other.pathInfo instanceof MethodInfo) ? 1 : 0;
                }
            }
        }
        return num2 - num1; // descending order
    }

    @Override
    public String toString() {
        return "pathInfo=" + pathInfo;
    }

}
