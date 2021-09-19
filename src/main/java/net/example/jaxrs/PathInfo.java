package net.example.jaxrs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PathInfo {

    public static final MatchResult NO_MATCH = Pattern.compile("").matcher("").toMatchResult();

    private static final Pattern PATH_VARIABLE = Pattern.compile("(?x) \\{ (.+?) (?: : (.*?) )? \\}");

    final MatchResult emptyMatch;

    private final Pattern pathPattern;
    private final ThreadLocal<Matcher> pathMatcher = new ThreadLocal<>();
    private final boolean emptyPath;
    private final String rmatchPattern;
    private final List<String> parameterNames;
    private final int literalCharacterCount;
    private final int capturingGroupCount;
    private final int nonDefaultGroupCount;
    private final boolean terminal;

    private ResourceInfo parent;

    protected PathInfo(String pathTemplate, boolean terminal) {
        this.terminal = terminal;

        // REVISIT: We may actually try to match "" and "/" against the pathTemplate,
        // after escaping {...} placeholders.  It is unclear whether a placeholder
        // {foo:(|/)} (matches "" or "/") should be considered an "empty" path.
        this.emptyPath = pathTemplate.isEmpty() || pathTemplate.equals("/");

        // 3.4    URI Templates
        // 3.7.3    Converting URI Templates to Regular Expressions
        StringBuilder regex = new StringBuilder();
        StringBuilder rmatch = new StringBuilder();
        int literalCount = 0;
        int groupCount = 0;
        int nonDefaultCount = 0;
        List<String> variables = new ArrayList<>();
        {
            if (pathTemplate.length() > 0 && !pathTemplate.startsWith("/")) {
                regex.append('/');
                rmatch.append('/');
            }

            Matcher m = PATH_VARIABLE.matcher(pathTemplate);
            int lastAppendPosition = 0;
            while (m.find()) {
                if (lastAppendPosition < m.start()) {
                    String literal = pathTemplate.substring(lastAppendPosition, m.start());
                    literalCount += literal.length();
                    regex.append(Pattern.quote(literal));
                    rmatch.append(literal);
                }

                String paramRegex = m.group(2) == null ? "[^/]+?" : m.group(2);
                regex.append("(?<").append(m.group(1)).append(">")
                                   .append(paramRegex).append(')');
                rmatch.append('(').append(paramRegex).append(')');
                groupCount += 1;
                if (m.group(2) != null) {
                    nonDefaultCount += 1;
                }
                variables.add(m.group(1));
                lastAppendPosition = m.end();
            }

            String tail = pathTemplate.substring(lastAppendPosition);
            if (tail.endsWith("/")) {
                tail = tail.substring(0, tail.length() - 1);
            }
            if (!tail.isEmpty()) {
                literalCount += tail.length();
                regex.append(Pattern.quote(tail));
                rmatch.append(tail);
            }
        }
        if (terminal) {
            regex.append("(/)?");
        } else {
            regex.append("(/.*)?");
        }

        this.pathPattern = Pattern.compile(regex.toString());
        this.emptyMatch = emptyPath ? createEmptyResult(pathPattern) : null;
        this.rmatchPattern = rmatch.toString();
        this.literalCharacterCount = literalCount;
        this.capturingGroupCount = groupCount;
        this.nonDefaultGroupCount = nonDefaultCount;
        this.parameterNames = Collections.unmodifiableList(variables);
    }

    private static MatchResult createEmptyResult(Pattern p) {
        Matcher m = p.matcher("");
        if (!m.matches()) {
            throw new IllegalStateException("Doesn't match empty string: " + p.pattern());
        }
        return m.toMatchResult();
    }

    public boolean isEmpty() {
        return emptyPath;
    }

    /*
     * REVISIT: Do we need this?  MatchInfo already links the parents.
     */
    public ResourceInfo getParent() {
        return parent;
    }

    void setParent(ResourceInfo parent) {
        if (this.parent == null) {
            this.parent = parent;
        } else {
            throw new IllegalStateException("parent already set");
        }
    }

    /**
     * @return  Compiled regular expression pattern for matching paths.
     */
    public Pattern getPattern() {
        return pathPattern;
    }

    /*
     * IMPORTANT: Do not modify Matcher's pattern.
     */
    private Matcher getMatcher(String input) {
        Matcher matcher = pathMatcher.get();
        if (matcher == null) {
            matcher = pathPattern.matcher(input);
            pathMatcher.set(matcher);
            return matcher;
        }
        assert matcher.pattern() == pathPattern;
        return matcher.reset(input);
    }

    public MatchResult match(String segment) {
        if (capturingGroupCount > 0) {
            Matcher m = getMatcher(segment);
            return m.matches() ? m.toMatchResult() : NO_MATCH;
        }
        return simpleMatch(segment) ? new SimpleMatchResult(rmatchPattern, segment)
                                    : NO_MATCH;
    }

    private boolean simpleMatch(String segment) {
        return segment.startsWith(rmatchPattern)
                && (segment.length() == rmatchPattern.length()
                        || segment.charAt(rmatchPattern.length()) == '/'
                        && (!terminal || segment.length() == rmatchPattern.length() + 1));
    }

    /**
     * @return  The regular expression modulo variable names.
     */
    public String getRmatch() {
        return rmatchPattern;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public int getLiteralCharacterCount() {
        return literalCharacterCount;
    }

    public int getCapturingGroupCount() {
        return capturingGroupCount;
    }

    public int getNonDefaultGroupCount() {
        return nonDefaultGroupCount;
    }

    public static boolean isEmpty(String path) {
        return path == null || path.isEmpty() || path.equals("/");
    }

}
