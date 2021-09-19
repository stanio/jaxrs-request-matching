package net.example.jaxrs;

import static net.example.jaxrs.MatchInfo.finalCapture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;

public class RequestMatchingEnhanced implements RequestMatching {

    private final List<ResourceInfo> rootResources;

    private final List<MatchInfo<ResourceInfo>> candidateRoots = new ArrayList<>();
    private final List<MatchInfo<MethodInfo>> candidateMethods = new ArrayList<>();
    private final List<MatchInfo<ResourceInfo>> subLocatorList = new ArrayList<>();

    RequestMatchingEnhanced(ResourceInfo... rootResources) {
        this.rootResources = new ArrayList<>(Arrays.asList(rootResources));
    }

    public static RequestMatching of(Class<?>... rootResources) {
        return new RequestMatchingEnhanced(Introspector.infosFor(rootResources));
    }

    private List<MatchInfo<ResourceInfo>> resetCandidateRoots() {
        List<MatchInfo<ResourceInfo>> list = candidateRoots;
        list.clear();
        return list;
    }

    private List<MatchInfo<MethodInfo>> resetCandidateMethods() {
        List<MatchInfo<MethodInfo>> list = candidateMethods;
        list.clear();
        return list;
    }

    private List<MatchInfo<ResourceInfo>> resetSubResourceLocators() {
        List<MatchInfo<ResourceInfo>> list = subLocatorList;
        list.clear();
        return list;
    }

    @Override
    public MatchInfo<MethodInfo> find(String path, String httMethod) {
        // 1. Identify a set of candidate root resource classes matching the request

        // 1.a
        List<MatchInfo<ResourceInfo>> matchedRoots = resetCandidateRoots();

        // 1.b, 1.c
        rootResources.forEach(resourceInfo -> {
            MatchResult pathMatch = resourceInfo.match(path);
            if (pathMatch == PathInfo.NO_MATCH)
                return;

            String finalCapture = finalCapture(pathMatch);
            if (PathInfo.isEmpty(finalCapture)
                    || resourceInfo.hasSubResourceMethodsOrLocators()) {
                matchedRoots.add(MatchInfo.of(resourceInfo, pathMatch));
            }
        });

        // 1.d
        if (matchedRoots.isEmpty()) {
            return null; // not found
        }

        // 1.e
        // ~~~~~~

        // 1.f
        // ~~~~~~

        List<MatchInfo<MethodInfo>> resourceMethods = findStep2(matchedRoots);
        // 2.e
        if (resourceMethods.isEmpty()) {
            return null; // not found
        }

        return findStep3(resourceMethods, httMethod);
    }

    private List<MatchInfo<MethodInfo>>
            findStep2(List<MatchInfo<ResourceInfo>> matchedResources) {
        // 2. Obtain a set of candidate resource methods for the request:

        // 2.b
        List<MatchInfo<MethodInfo>> resourceMethods = resetCandidateMethods();
        findStep2x(matchedResources, resetSubResourceLocators());
        return resourceMethods;
    }

    /*
     * Find all matching methods in depth
     */
    private void findStep2x(List<MatchInfo<ResourceInfo>> matchedResources,
                                   List<MatchInfo<ResourceInfo>> subResourceLocators) {
        // 2.a, 2.c, 2.d, 2.h
        matchedResources.forEach(parentMatch -> {
            String subPath = parentMatch.finalCapture();

            if (PathInfo.isEmpty(subPath)) {
                parentMatch.pathInfo.getResourceMethodsAndEmptyPathLocators().forEach(subPathInfo -> {
                    if (subPathInfo instanceof MethodInfo) {
                        candidateMethods.add(MatchInfo
                                .of((MethodInfo) subPathInfo, parentMatch));
                    } else {
                        subResourceLocators.add(MatchInfo
                                .of((ResourceInfo) subPathInfo, parentMatch));
                    }
                });
                return;
            }

            parentMatch.pathInfo.getSubResourceMethodsAndLocators().forEach(subPathInfo -> {
                MatchResult pathMatcher = subPathInfo.match(subPath);
                if (pathMatcher != PathInfo.NO_MATCH) {
                    if (subPathInfo instanceof MethodInfo) {
                        candidateMethods.add(MatchInfo
                                .of((MethodInfo) subPathInfo, pathMatcher, parentMatch));
                    } else {
                        subResourceLocators.add(MatchInfo
                                .of((ResourceInfo) subPathInfo, pathMatcher, parentMatch));
                    }
                }
            });
        });

        // 2.f
        // ~~~~~~

        if (subResourceLocators.isEmpty())
            return;

        // 2.j
        matchedResources.clear(); // reuse previous matches for next level matches
        findStep2x(subResourceLocators, matchedResources);
    }

    private MatchInfo<MethodInfo> findStep3(List<MatchInfo<MethodInfo>> candidateMethods, String httpMethod) {
        // XXX: 3.a - filter by request method, media type
        for (int i = candidateMethods.size() - 1; i >= 0; i--) {
            MatchInfo<MethodInfo> methodInfo = candidateMethods.get(i);
            if (!httpMethod.equals(methodInfo.pathInfo.getHttpMethod())) {
                candidateMethods.remove(i);
            }
        }

        if (candidateMethods.isEmpty()) {
            return null; // Method not allowed
        }

        // 2.f
        Collections.sort(candidateMethods, MatchInfo.FULL_PATH);

        // XXX: 3.b - sort and filter by accept/response type

        // 3.c
        if (candidateMethods.size() > 1) {
            warn("Multiple methods match: " + candidateMethods);
        }
        return candidateMethods.get(0);
    }

    private static void warn(String message) {
        System.err.println(message);
    }

}
