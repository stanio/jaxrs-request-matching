package net.example.jaxrs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ResourceInfo extends PathInfo {

    private final Class<?> klass;
    private final List<MethodInfo> resourceMethods;
    private final List<MethodInfo> subResourceMethods;
    private final List<ResourceInfo> subResourceLocators;

    private final List<PathInfo> subResourceMethodsAndLocators;
    private final List<PathInfo> resourceMethodsAndEmptyLocators;

    ResourceInfo(Class<?> klass,
                 String pathPattern,
                 List<MethodInfo> resourceMethods,
                 List<MethodInfo> subResourceMethods,
                 List<ResourceInfo> subResourceLocators) {
        super(pathPattern, false);
        this.klass = klass;
        this.resourceMethods = Collections.unmodifiableList(initParent(resourceMethods, this));
        this.subResourceMethods = Collections.unmodifiableList(initParent(subResourceMethods, this));
        this.subResourceLocators = Collections.unmodifiableList(initParent(subResourceLocators, this));

        List<PathInfo> subMethodsAndLocators = new ArrayList<>(subResourceMethods.size() + subResourceLocators.size());
        subMethodsAndLocators.addAll(subResourceMethods);
        subMethodsAndLocators.addAll(subResourceLocators);
        this.subResourceMethodsAndLocators = Collections.unmodifiableList(subMethodsAndLocators);

        List<PathInfo> emptyPathResources = new ArrayList<>(resourceMethods.size() + 1);
        emptyPathResources.addAll(resourceMethods);
        subResourceLocators.stream().filter(it -> it.isEmpty())
                                    .forEach(it -> emptyPathResources.add(it));
        this.resourceMethodsAndEmptyLocators = Collections.unmodifiableList(emptyPathResources);
    }

    private static <T extends PathInfo> List<T> initParent(List<T> resources, ResourceInfo parent) {
        resources.forEach(item -> item.setParent(parent));
        return resources;
    }

    public static ResourceInfo of(Class<?> klass) {
        return Introspector.infoFor(klass);
    }

    public Class<?> getResourceClass() {
        return klass;
    }

    public List<MethodInfo> getResourceMethods() {
        return resourceMethods;
    }

    public List<MethodInfo> getSubResourceMethods() {
        return subResourceMethods;
    }

    public List<ResourceInfo> getSubResourceLocators() {
        return subResourceLocators;
    }

    public boolean hasSubResourceMethodsOrLocators() {
        return !getSubResourceMethods().isEmpty()
                || !getSubResourceLocators().isEmpty();
    }

    public List<PathInfo> getSubResourceMethodsAndLocators() {
        return subResourceMethodsAndLocators;
    }

    public List<PathInfo> getResourceMethodsAndEmptyPathLocators() {
        return resourceMethodsAndEmptyLocators;
    }

    @Override
    public String toString() {
        return String.valueOf(klass);
    }

}
