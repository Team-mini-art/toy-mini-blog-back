package study.till.back.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import study.till.back.dto.token.JwtStatus;
import study.till.back.dto.token.TokenInfo;
import study.till.back.exception.token.UnauthorizedTokenException;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateToken(String memberPk, List<String> roles) {
        long now = (new Date()).getTime();
//        Date accessTokenExpiresIn = new Date(now + 1_800_000);
        Date accessTokenExpiresIn = new Date(now + 5_000);

        // Access Token 생성
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberPk));
        claims.put("roles", roles);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setClaims(claims)
//                .setExpiration(new Date(now + 604_800_000))
                .setExpiration(new Date(now + 5_000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("roles") == null) {
            throw new UnauthorizedTokenException();
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        }
        catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        }
        catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        }
        catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public JwtStatus getTokenStatus(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return JwtStatus.VALID;
        } catch (ExpiredJwtException e) {
            return JwtStatus.EXPIRED;
        } catch (UnsupportedJwtException e) {
            return JwtStatus.UNSUPPORTED;
        } catch (JwtException | IllegalArgumentException e) {
            return JwtStatus.INVALID;
        }
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String createAccessToken(Claims claims) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 1_800_000);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
