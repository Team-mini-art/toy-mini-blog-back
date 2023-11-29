package study.till.back.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import study.till.back.dto.token.JwtStatus;
import study.till.back.dto.token.TokenErrorResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private static final List<String> EXEMPTED_URIS = Arrays.asList(
            "/swagger-ui/index.html",
            "/api/signup",
            "/api/login",
            "/api/refresh",
            "oauth2/authorization/google",
            "/login/oauth2/code/google"
    );

    private static final List<String> EXEMPTED_REGEX_URIS = Arrays.asList(
            "^/swagger-ui.*$",
            "^/v3/api-docs(/.*)?$",
            "^/api/posts(/.*)?$",
            "^/api/comments(/.*)?$"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isExemptedFromTokenValidation(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }
        String token = resolveToken(httpRequest);
        if (token != null) {
            JwtStatus status = jwtTokenProvider.getTokenStatus(token);

            switch (status) {
                case VALID:
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    break;
                case EXPIRED:
                    String jsonExpiredErrorResponse = createJsonErrorResponse("access_token_expired");
                    sendJsonErrorResponse(httpResponse, 401, jsonExpiredErrorResponse);
                    break;
                case UNSUPPORTED:
                    String jsonUnsupportedErrorResponse = createJsonErrorResponse("unsupported_jwt_token");
                    sendJsonErrorResponse(httpResponse, 401, jsonUnsupportedErrorResponse);
                    break;
                case INVALID:
                default:
                    String jsonInvalidErrorResponse = createJsonErrorResponse("invalid_jwt_token");
                    sendJsonErrorResponse(httpResponse, 401, jsonInvalidErrorResponse);
                    break;
            }
        }
        else {
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                if (e instanceof IOException || e instanceof ServletException) {
                    throw e;
                } else {
                    String jsonInvalidErrorResponse = createJsonErrorResponse("empty token in header");
                    sendJsonErrorResponse(httpResponse, 403, jsonInvalidErrorResponse);
                }
            }
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearToken) && bearToken.startsWith("Bearer")) {
            return bearToken.substring(7);
        }
        return null;
    }

    private String createJsonErrorResponse(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TokenErrorResponse errorResponse = TokenErrorResponse.builder()
                .status("FAIL")
                .message(message)
                .build();
        return objectMapper.writeValueAsString(errorResponse);
    }

    private void sendJsonErrorResponse(HttpServletResponse httpResponse, int status, String jsonErrorResponse) throws IOException {
        httpResponse.setStatus(status);
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.getWriter().write(jsonErrorResponse);
    }

    private boolean isExemptedFromTokenValidation(HttpServletRequest httpRequest) {
        String httpMethod = httpRequest.getMethod();
        String requestURI = httpRequest.getRequestURI();

        boolean isExemptedByUri = EXEMPTED_URIS.stream().anyMatch(uri -> requestURI.equals(uri));
        boolean isExemptedByRegex = EXEMPTED_REGEX_URIS.stream().anyMatch(pattern -> requestURI.matches(pattern));

        if(HttpMethod.GET.name().equalsIgnoreCase(httpMethod)) {
            return isExemptedByRegex;
        } else {
            return isExemptedByUri;
        }
    }
}
