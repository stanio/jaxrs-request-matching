/*
 * This module, both source code and documentation,
 * is in the Public Domain, and comes with NO WARRANTY.
 */
package net.example.jaxrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.regex.MatchResult;

import org.junit.Test;

public class PathInfoTest {

    private PathInfo pathInfo;

    private void setUpPathInfo(String template, boolean terminal) {
        pathInfo = new PathInfo(template, terminal) { /* no extensions */ };
    }

    @Test
    public void withTemplateParameters() throws Exception {
        setUpPathInfo("/foo/{bar:.*?/.*?}", false);

        MatchResult pathMatch = pathInfo.match("/foo/bar/baz/qux");

        assertNotSame("NO_MATCH", PathInfo.NO_MATCH, pathMatch);
        assertEquals("match groupCount", 2, pathMatch.groupCount());
        assertEquals("match group[0]", "/foo/bar/baz/qux", pathMatch.group());
        assertEquals("match group[1]", "bar/baz", pathMatch.group(1));
        assertEquals("match group[2]", "/qux", pathMatch.group(2));
    }

    @Test
    public void simpleTemplate() throws Exception {
        setUpPathInfo("/foo/bar/baz", false);

        MatchResult pathMatch = pathInfo.match("/foo/bar/baz/qux");

        assertNotSame("NO_MATCH", PathInfo.NO_MATCH, pathMatch);
        assertEquals("match groupCount", 1, pathMatch.groupCount());
        assertEquals("match group[0]", "/foo/bar/baz/qux", pathMatch.group());
        assertEquals("match group[1]", "/qux", pathMatch.group(1));
    }

}
