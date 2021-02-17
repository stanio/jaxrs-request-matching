package net.example.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;

class Introspector {

    static ResourceInfo[] infosFor(Class<?>... rootResources) {
        ResourceInfo[] infos = new ResourceInfo[rootResources.length];
        for (int i = 0; i < infos.length; i++) {
            infos[i] = Introspector.infoFor(rootResources[i]);
        }
        return infos;
    }

    static ResourceInfo infoFor(Class<?> klass) {
        Path path = klass.getAnnotation(Path.class);
        return infoFor(klass, path.value());
    }

    private static ResourceInfo infoFor(Class<?> klass, String path) {
        List<MethodInfo> resourceMethods = new ArrayList<>();
        List<MethodInfo> subResourceMethos = new ArrayList<>();
        List<ResourceInfo> subResourceLocators = new ArrayList<>();
        for (Method m : klass.getMethods()) {
            Path subPath = m.getAnnotation(Path.class);
            String resMethod = getResourceMethod(m);
            if (resMethod != null) {
                if (subPath == null || subPath.value().isEmpty() || subPath.value().equals("/")) {
                    resourceMethods.add(new MethodInfo(resMethod, m));
                } else {
                    subResourceMethos.add(new MethodInfo(resMethod, m, subPath.value()));
                }
            } else if (subPath != null) {
                subResourceLocators.add(infoFor(m.getReturnType(), subPath.value()));
            }
        }
        return new ResourceInfo(klass, path, resourceMethods, subResourceMethos, subResourceLocators);
    }

    private static String getResourceMethod(Method m) {
        for (Annotation a : m.getAnnotations()) {
            if (a.annotationType() == HttpMethod.class) {
                return ((HttpMethod) a).value();
            }
            HttpMethod b = a.annotationType().getAnnotation(HttpMethod.class);
            if (b != null) {
                return b.value();
            }
        }
        return null;
    }

}
