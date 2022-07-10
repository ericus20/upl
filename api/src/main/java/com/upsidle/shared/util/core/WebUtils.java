package com.upsidle.shared.util.core;

import com.upsidle.constant.EmailConstants;
import com.upsidle.constant.ErrorConstants;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * This utility class holds all common methods used in the web layer.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class WebUtils {

  public static final String TOKEN = "token";

  private WebUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Generates a uri dynamically by constructing url.
   *
   * @param path the custom path
   * @param publicUserId the publicUserId
   * @return a dynamically formulated uri
   */
  public static String getGenericUri(String path, String publicUserId) {

    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(path)
        .queryParam(TOKEN, publicUserId)
        .build()
        .toUriString();
  }

  /**
   * Generates a uri dynamically by constructing url.
   *
   * @param path the custom path
   * @return a dynamically formulated uri
   */
  public static String getGenericUri(String path) {
    return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUriString();
  }

  /**
   * Generates a URI dynamically by constructing url from the request.
   *
   * <p>For instance a request from origin <a href="http://localhost:30000">...</a> will be
   * retrieved this way.
   *
   * @param request the request
   * @param path the custom path
   * @param publicUserId the publicUserId
   * @return a dynamically formulated uri
   */
  public static String getUri(HttpServletRequest request, String path, String publicUserId) {
    return ServletUriComponentsBuilder.fromOriginHeader(request.getHeader(HttpHeaders.ORIGIN))
        .path(path)
        .queryParam(TOKEN, publicUserId)
        .build()
        .toUriString();
  }

  /**
   * Generates a URI dynamically by constructing url from the request.
   *
   * <p>For instance a request from origin <a href="http://localhost:30000">...</a> will be
   * retrieved this way.
   *
   * @param request the request
   * @return a dynamically formulated uri
   */
  public static String getUri(HttpServletRequest request) {
    var origin = request.getHeader(HttpHeaders.ORIGIN);
    if (StringUtils.isEmpty(origin)) {
      return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    return ServletUriComponentsBuilder.fromOriginHeader(origin).toUriString();
  }

  /**
   * Generate a URI to the password reset page based on the path.
   *
   * @param url the url
   * @param path the path to the password reset page.
   * @return the URI to the password reset page.
   */
  public static URI getUri(String url, String path) {
    return getUri(url, path, StringUtils.EMPTY);
  }

  /**
   * Generate a URI to the password reset page based on the path.
   *
   * @param url the url
   * @param path the path to the password reset page.
   * @return the URI to the password reset page.
   */
  public static URI getUri(String url, String path, String token) {
    if (StringUtils.isNotBlank(token)) {
      return ServletUriComponentsBuilder.fromHttpUrl(url)
          .path(path)
          .queryParam(WebUtils.TOKEN, token)
          .build()
          .toUri();
    }

    return ServletUriComponentsBuilder.fromHttpUrl(url).path(path).build().toUri();
  }

  /**
   * Generate a URI to the password reset page based on the path.
   *
   * @param url the url
   * @param path the path to the password reset page.
   * @return the URI to the password reset page.
   */
  public static URI getUri(String url, String path, MultiValueMap<String, String> params) {
    return ServletUriComponentsBuilder.fromHttpUrl(url)
        .path(path)
        .queryParams(params)
        .build()
        .toUri();
  }

  /**
   * Get general links used in email definitions.
   *
   * @return default links
   */
  public static Map<String, String> getDefaultEmailUrls() {
    Map<String, String> links = new ConcurrentHashMap<>();
    links.put(EmailConstants.ABOUT_US_LINK, getGenericUri(EmailConstants.COPY_ABOUT_US));

    return links;
  }
}
