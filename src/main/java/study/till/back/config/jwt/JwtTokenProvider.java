package study.till.back.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import study.till.back.dto.token.JwtStatus;
import study.till.back.dto.token.TokenInfo;
import study.till.back.exception.token.ExpiredTokenException;
import study.till.back.exception.token.InvalidTokenException;
import study.till.back.exception.token.UnauthorizedTokenException;
import study.till.back.exception.token.UnsupportedTokenException;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    @Value("${jwt.accessExpirationTime}")
    long accessExpirationTime;

    @Value("${jwt.refreshExpirationTime}")
    long refreshExpirationTime;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RedisTemplate<String, String> redisTemplate) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.redisTemplate = redisTemplate;
    }

    public TokenInfo generateToken(String memberPk, List<String> roles) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + accessExpirationTime);

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
                .setExpiration(new Date(now + refreshExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Redis에 토큰 저장
        redisTemplate.opsForValue().set(
                memberPk,
                refreshToken,
                refreshExpirationTime,
                TimeUnit.MILLISECONDS
        );

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
            log.error("invalid_jwt_token", e);
            throw new InvalidTokenException();
        }
        catch (ExpiredJwtException e) {
            log.error("jwt_token_expired", e);
            throw new ExpiredTokenException();
        }
        catch (UnsupportedJwtException e) {
            log.error("unsupported_jwt_token", e);
            throw new UnsupportedTokenException();
        }
        catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
            throw new IllegalArgumentException();
        }
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

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
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

    public TokenInfo createSuperToken() {
        Claims claims = Jwts.claims().setSubject("superAdmin");
        claims.put("roles", "superAdmin");

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 15_768_000_000L);
        Date refreshTokenExpiresIn = new Date(now + 31_536_000_000L);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
