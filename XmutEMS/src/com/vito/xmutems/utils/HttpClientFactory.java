package com.vito.xmutems.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;

public class HttpClientFactory {
	private static DefaultHttpClient customerHttpClient;
	public static CookieStore cookieStore;
	public static Context context;
	private HttpClientFactory() {
	}
	static HttpParams httpParameters;
	static {

		httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 30 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 30 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParameters, 1024 * 512);
		HttpConnectionParams.setTcpNoDelay(httpParameters, false);
		// cacheStorage = new MyHttpCacheStorage();

	}
   
	public static DefaultHttpClient getHttpClient() {
		if(null == customerHttpClient) {
			customerHttpClient = new DefaultHttpClient();
		}
		return customerHttpClient;
	}
	
	/**
	 * 是否存在活跃网络
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static boolean hasAvailableNetwork(Context context) {
		boolean flag = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager.getActiveNetworkInfo() != null)
			flag = cwjManager.getActiveNetworkInfo().isAvailable();

		return flag;
	}
	
	public static String get(String url) {
		String response = "";
		try {
			HttpParams params = new BasicHttpParams();
			params.setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.FALSE);
			url = url.startsWith("/") ? url.substring(1) : url;
			url = url.replaceAll(" ", "%20");
			HttpGet request = new HttpGet(Constant.BASE_DOMAIN + url);
			request.setParams(params);
	        L.v("httpclientRequest",Constant.BASE_DOMAIN + url);
			response = getHttpClient().execute(request, new BasicResponseHandler());
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static String normalGet(String url) {
		String response = "";
		try {
			URLConnection connection = new URL(url).openConnection();
			
			L.d("normalGet",url);
			InputStream stream = connection.getInputStream();
			response = convertStreamToString(stream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	
	public static String specGet(String url) {
        String response = "";
		L.v("httpclientRequest",url);
		try {
			
			HttpParams params = new BasicHttpParams();
			params.setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.FALSE);
			params.setParameter("Referer", Constant.BASE_DOMAIN);
			
			HttpGet request = new HttpGet(url);
			request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.97 Safari/537.22");
			request.addHeader("Referer", Constant.BASE_DOMAIN);
			request.addHeader("Host", "jxgl.xmut.edu.cn");
			request.addHeader("Cookie", getHttpClient().getCookieStore().getCookies().get(0).getName()+"="+getHttpClient().getCookieStore().getCookies().get(0).getValue());
			request.setParams(params);
			Header[] requestHeaders = request.getAllHeaders();
			for (Header h : requestHeaders) {
	        	System.out.println("requestHeader-- " + h.getName() + ": " + h.getValue());
	        }
			
	        HttpResponse httpResponse = getHttpClient().execute(request);
	        
	        System.out.println("------------------------");
	        Header[] responseHeaders = httpResponse.getAllHeaders();
	        for (Header h : responseHeaders) {
	        	System.out.println("responseHeader-- " + h.getName() + ": " + h.getValue());
	        }

	        int status = httpResponse.getStatusLine().getStatusCode();
	        if (status ==HttpStatus.SC_MOVED_TEMPORARILY || status==HttpStatus.SC_MOVED_PERMANENTLY) {
	        	// obtain redirect target
		        Header locationHeader = httpResponse.getFirstHeader("location");
		        if (locationHeader != null) {
		            String redirectLocation = locationHeader.getValue();
		            System.out.println("loaction: " + redirectLocation);
		            response = specGet(redirectLocation);
		        } else {
		          // The response is invalid and did not provide the new location for
		          // the resource.  Report an error or possibly handle the response
		          // like a 404 Not Found error.
		        }
	        } else {
	        	HttpEntity entity = httpResponse.getEntity();
				InputStream is = entity.getContent();
				response = convertStreamToString(is);
				System.out.println("response = "+response);
	        }
	        
			//response = getHttpClient().execute(request, new BasicResponseHandler());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static String post(String url) {
		url = url.startsWith("/") ? url.substring(1) : url;
		url = url.replaceAll(" ", "%20");
		HttpPost request = new HttpPost(Constant.BASE_DOMAIN + url);
		String stuNo = CacheProvider.get(context, "stuNo").toString();
		request.addHeader("Referer", Constant.BASE_DOMAIN + "xh=" + stuNo + "&gnmkdm=N121502");
        String response = "";
        L.v("httpclientRequest", url);
		try {
			response = getHttpClient().execute(request, new BasicResponseHandler());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static String post(String url, Map<String, String> params,
			Context context) {
		String httpResponse = "";

		try {
			HttpClient client = new DefaultHttpClient(httpParameters);

			if (hasAvailableNetwork(context)) {
				url = url.startsWith("/") ? url.substring(1) : url;

				String requestURL = Constant.BASE_DOMAIN + url;
				System.out.println(requestURL);
				requestURL = requestURL.replaceAll(" ", "%20");

				HttpPost http = new HttpPost(requestURL);
				if (params != null) {
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					for (Entry<String, String> param : params.entrySet()) {
						nvps.add(new BasicNameValuePair(param.getKey(), param
								.getValue()));
					}
					http.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				}

				if (!"".equals(params) && null != params)
					requestURL += ("?" + params);

				HttpResponse response = null;

				response = client.execute(http);

				HttpEntity entity = response.getEntity();

				httpResponse = EntityUtils.toString(entity);
			}
		} catch (Exception e) {

		}

		return httpResponse;
	}
	
	
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append((line + "\n"));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
}
