
package gatech.edu.DeathRecordPuller.util;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.web.client.RestTemplate;

public class StatefulRestTemplate extends RestTemplate
{
    private final HttpClient httpClient;
    private final CookieStore cookieStore;
    private final HttpContext httpContext;
    private final StatefulHttpComponentsClientHttpRequestFactory statefulHttpComponentsClientHttpRequestFactory;

    public StatefulRestTemplate()
    {
        super();
        HttpParams params = new BasicHttpParams();
        HttpClientParams.setRedirecting(params, false);

        httpClient = new DefaultHttpClient(params);
        cookieStore = new BasicCookieStore();
        httpContext = new BasicHttpContext();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, getCookieStore());
        statefulHttpComponentsClientHttpRequestFactory = new StatefulHttpComponentsClientHttpRequestFactory(httpClient, httpContext);
        super.setRequestFactory(statefulHttpComponentsClientHttpRequestFactory);
    }

    public HttpClient getHttpClient()
    {
        return httpClient;
    }

    public CookieStore getCookieStore()
    {
        return cookieStore;
    }

    public HttpContext getHttpContext()
    {
        return httpContext;
    }

    public StatefulHttpComponentsClientHttpRequestFactory getStatefulHttpClientRequestFactory()
    {
        return statefulHttpComponentsClientHttpRequestFactory;
    }
    
    public boolean containsCookieName(String name) {
    	for(Cookie cookie:cookieStore.getCookies()) {
    		if(cookie.getName().equals(name))
    			return true;
    	}
    	return false;
    }

}