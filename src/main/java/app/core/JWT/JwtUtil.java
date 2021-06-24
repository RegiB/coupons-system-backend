package app.core.JWT;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	private String signatureAlgorithm = SignatureAlgorithm.HS256.getJcaName();
	private String encodedSecretKey = "This+is+my+First+Secret+Key+Regina+Brand+26April1988";
	private Key decodedSecretKey = new SecretKeySpec(Base64.getDecoder().decode(encodedSecretKey),
			this.signatureAlgorithm);

	public String generateToken(ClientDetails clientDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("clientId", clientDetails.id);
		claims.put("clientType", clientDetails.clientType);
		return createToken(claims, clientDetails.email);
	}

	private String createToken(Map<String, Object> claims, String subject) {
		Instant now = Instant.now();
		return Jwts.builder().setClaims(claims)

				.setSubject(subject)

				.setIssuedAt(Date.from(now))

				.setExpiration(Date.from(now.plus(10, ChronoUnit.HOURS)))

				.signWith(this.decodedSecretKey)

				.compact();
	}

	private Claims extractAllClaims(String token) throws ExpiredJwtException {
		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(this.decodedSecretKey).build();
		return jwtParser.parseClaimsJws(token).getBody();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public int extractClientId(String token) {
		return extractAllClaims(token).get("clientId", Integer.class);
	}

	public String extractClientType(String token) {
		return extractAllClaims(token).get("clientType", String.class);
	}

	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	public boolean isTokenExpired(String token) {
		try {
			extractAllClaims(token);
			return false;
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	/**
	 * returns true if the user (email) in the specified token equals the one in the
	 * specified user details and the token is not expired
	 */
	public boolean validateToken(String token, String subject) {
		try {
			final String username = extractUsername(token);
			return (username.equals(subject) && !isTokenExpired(token));
		} catch (Throwable e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized!", e);
		}
	}

	public static class ClientDetails {
		public int id;
		public String email;
		public ClientType clientType;
		public String token;

		public ClientDetails(int id, String email, ClientType clientType) {
			this.id = id;
			this.email = email;
			this.clientType = clientType;
		}

		public enum ClientType {
			ADMINISTRATOR, COMPANY, CUSTOMER;
		}

	}

}
