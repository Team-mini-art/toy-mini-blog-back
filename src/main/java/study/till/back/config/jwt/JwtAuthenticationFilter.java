package study.till.back.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.token.JwtStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (token != null) {
            JwtStatus status = jwtTokenProvider.getTokenStatus(token);

            switch (status) {
                case VALID:
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    break;
                case EXPIRED:
                    String jsonExpiredErrorResponse = createJsonErrorResponse("401", "access_token_expired");
                    sendJsonErrorResponse(httpResponse, jsonExpiredErrorResponse);
                    return;
                case UNSUPPORTED:
                    String jsonUnsupportedErrorResponse = createJsonErrorResponse("401", "Unsupported JWT token");
                    sendJsonErrorResponse(httpResponse, jsonUnsupportedErrorResponse);
                    return;
                case INVALID:
                default:
                    String jsonInvalidErrorResponse = createJsonErrorResponse("401", "Invalid JWT token");
                    sendJsonErrorResponse(httpResponse, jsonInvalidErrorResponse);
                    return;
            }
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearToken) && bearToken.startsWith("Bearer")) {
            return bearToken.substring(7);
        }
        return null;
    }

    private String createJsonErrorResponse(String status, String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommonResponse errorResponse = CommonResponse.builder()
                .status(status)
                .message(message)
                .build();
        return objectMapper.writeValueAsString(errorResponse);
    }

    private void sendJsonErrorResponse(HttpServletResponse httpResponse, String jsonErrorResponse) throws IOException {
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.getWriter().write(jsonErrorResponse);
    }
}
