package homedoctor.medicine.common;

/**
 * REF - https://documentation.commvault.com/commvault/v11/article?p=45599.htm
 */
public class StatusCode {

    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;

    // The HTTP method in the request was not supported by the resource. For example, the DELETE method cannot be used with the Agent API.
    public static final int METHOD_NOT_ALLOWED = 404;
    public static final int CONFLICT = 409;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int DB_ERROR = 600;
}
