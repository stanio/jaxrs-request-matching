package net.example.jaxrs;

import org.junit.Before;
import org.junit.BeforeClass;

public class RequestMatchingSpecTest extends RequestMatchingBaseTest {

    private static RequestMatching simpleRegistry;

    private static RequestMatching advancedResourceRegistry;

    @BeforeClass
    public static void suiteSetUp() {
        simpleRegistry = RequestMatchingSpec.of(simpleResources);
        advancedResourceRegistry = RequestMatchingSpec.of(advancedResources);
    }

    @Before
    public void skipAdvancedResources() {
        skipAdvancedResources = true;
    }

    @Override
    protected RequestMatching simpleRegistry() {
        return simpleRegistry;
    }

    @Override
    protected RequestMatching advancedResourceRegistry() {
        return advancedResourceRegistry;
    }

}
