package net.example.jaxrs;

import org.junit.BeforeClass;
import org.junit.Test;

import net.example.jaxrs.rest.BarEnhanced;
import net.example.jaxrs.rest.QuxResource;

public class RequestMatchingEnhancedTest extends RequestMatchingBaseTest {

    private static RequestMatching simpleRegistry;

    private static RequestMatching advancedResourceRegistry;

    @BeforeClass
    public static void suiteSetUp() {
        simpleRegistry = RequestMatchingEnhanced.of(simpleResources);
        advancedResourceRegistry = RequestMatchingEnhanced.of(advancedResources);
    }

    @Override
    protected RequestMatching simpleRegistry() {
        return simpleRegistry;
    }

    @Override
    protected RequestMatching advancedResourceRegistry() {
        return advancedResourceRegistry;
    }

    @Test
    public void testPostFoo() throws Exception {
        // Given
        final String requestPath = "/rest/foo";

        // When
        MatchInfo<MethodInfo> method = advancedResourceRegistry().find(requestPath, "POST");

        // Then
        assertMethodInfo("POST", requestPath, method, QuxResource.class, "some");
    }

    @Test
    public void testDeleteFoo() throws Exception {
        // Given
        final String requestPath = "/rest/foo";

        // When
        MatchInfo<MethodInfo> method = advancedResourceRegistry().find(requestPath, "DELETE");

        // Then
        assertMethodInfo("DELETE", requestPath, method, BarEnhanced.class, "fooBar");
    }

    @Test
    public void testPostQux() throws Exception {
        // Given
        final String requestPath = "/rest/foo/qux";

        // When
        MatchInfo<MethodInfo> method = advancedResourceRegistry().find(requestPath, "POST");

        // Then
        assertMethodInfo("POST", requestPath, method, BarEnhanced.class, "ohoo");
    }

}
