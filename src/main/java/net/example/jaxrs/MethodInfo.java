package net.example.jaxrs;

import java.lang.reflect.Method;

public final class MethodInfo extends PathInfo {

    private final String httpMethod;
    private final Method method;

    MethodInfo(String httpMethod, Method method) {
        this(httpMethod, method, "");
    }

    MethodInfo(String httpMethod, Method method, String pathPattern) {
        super(pathPattern, true);
        this.httpMethod = httpMethod;
        this.method = method;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "@" + httpMethod + " " + method;
    }

}
