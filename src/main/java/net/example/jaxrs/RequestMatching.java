package net.example.jaxrs;

/**
 * @see  <a href="https://download.oracle.com/otn-pub/jcp/jaxrs-2_1-final-eval-spec/jaxrs-2_1-final-spec.pdf"
 *              >Matching Requests to Resource Methods</a>
 */
public interface RequestMatching {

    default MatchInfo<MethodInfo> find(String path) {
        return find(path, "GET");
    }

    MatchInfo<MethodInfo> find(String path, String httpMethod);

}
