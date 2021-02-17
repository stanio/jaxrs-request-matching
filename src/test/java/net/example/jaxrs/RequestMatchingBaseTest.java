package net.example.jaxrs;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;

import java.lang.reflect.Method;

import org.junit.Test;

import net.example.jaxrs.rest.BarEnhanced;
import net.example.jaxrs.rest.BarResource;
import net.example.jaxrs.rest.FooResource;
import net.example.jaxrs.rest.QuxResource;
import net.example.jaxrs.rest.UnmatchedResource;

abstract class RequestMatchingBaseTest {

    static Class<?>[] simpleResources = {
        FooResource.class, BarResource.class, UnmatchedResource.class
    };

    static Class<?>[] advancedResources = {
        FooResource.class, BarEnhanced.class, UnmatchedResource.class
    };

    protected boolean skipAdvancedResources;

    protected abstract RequestMatching simpleRegistry();

    protected abstract RequestMatching advancedResourceRegistry();

    @Test
    public void testFoo() {
        // Given
        final String requestPath = "/rest/foo";

        // When
        MatchInfo<MethodInfo> method = simpleRegistry().find(requestPath);

        // Then
        assertMethodInfo(requestPath, method, FooResource.class, "foo");
    }

    @Test
    public void testBar() {
        // Given
        final String requestPath = "/rest/foo/bar";

        // When
        MatchInfo<MethodInfo> method = simpleRegistry().find(requestPath);

        // Then
        assertMethodInfo(requestPath, method, BarResource.class, "bar");
    }

    @Test
    public void testFooAdvanced() {
        // Given
        final String requestPath = "/rest/foo";

        // When
        MatchInfo<MethodInfo> method = advancedResourceRegistry().find(requestPath);

        // Then
        assumeMethodInfo(requestPath, method, FooResource.class, "foo");
    }

    @Test
    public void testBarAdvanced() {
        // Given
        final String requestPath = "/rest/foo/bar";

        // When
        MatchInfo<MethodInfo> method = advancedResourceRegistry().find(requestPath);

        // Then
        assertMethodInfo(requestPath, method, BarEnhanced.class, "bar");
    }

    @Test
    public void testQuxAdvanced() {
        // Given
        final String requestPath = "/rest/foo/qux";

        // When
        MatchInfo<MethodInfo> method = advancedResourceRegistry().find(requestPath);

        // Then
        assumeMethodInfo(requestPath, method, QuxResource.class, "quux");
    }

    protected void assumeMethodInfo(String requestPath, MatchInfo<MethodInfo> method,
                                    Class<?> resourceClass, String methodName) {
        if (skipAdvancedResources) {
            assumeThat("GET " + requestPath + " not matched", method, notNullValue());
        }
        assertMethodInfo(requestPath, method, resourceClass, methodName);
    }

    protected static void assertMethodInfo(String requestPath,
                                           MatchInfo<MethodInfo> method,
                                           Class<?> resourceClass, String methodName) {
        assertMethodInfo("GET", requestPath, method, resourceClass, methodName);
    }

    protected static void assertMethodInfo(String restMethod, String requestPath,
                                           MatchInfo<MethodInfo> method,
                                           Class<?> resourceClass, String methodName) {
        assertThat(restMethod + " " + requestPath + " not found", method, notNullValue());

        Method javaMethod = method.pathInfo.getMethod();
        assertThat("resource class",
                javaMethod.getDeclaringClass(), sameInstance(resourceClass));
        assertThat("resource method", javaMethod.getName(), equalTo(methodName));
        assertThat("rest method", method.pathInfo.getHttpMethod(), equalTo(restMethod));
    }

}
