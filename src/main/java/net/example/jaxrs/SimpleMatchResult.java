/*
 * This module, both source code and documentation,
 * is in the Public Domain, and comes with NO WARRANTY.
 */
package net.example.jaxrs;

import java.util.regex.MatchResult;

class SimpleMatchResult implements MatchResult {

    private final String full;
    private final int finalIndex;

    public SimpleMatchResult(String prefix, String full) {
        this.full = full;
        this.finalIndex = prefix.length();
    }

    private void ensureValidGroup(int group) {
        if (group > 1) {
            throw new IndexOutOfBoundsException("No group " + group);
        }
    }

    @Override
    public int start() {
        return 0;
    }

    @Override
    public int start(int group) {
        ensureValidGroup(group);
        return (group == 0) ? 0 : finalIndex;
    }

    @Override
    public int end() {
        return full.length();
    }

    @Override
    public int end(int group) {
        ensureValidGroup(group);
        return end();
    }

    @Override
    public String group() {
        return full;
    }

    @Override
    public String group(int group) {
        ensureValidGroup(group);
        return (group == 0) ? full : full.substring(finalIndex);
    }

    @Override
    public int groupCount() {
        return 1;
    }

}
