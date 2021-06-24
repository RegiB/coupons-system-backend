package app.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import app.core.JWT.JwtUtil;

public class LoginFilter implements Filter {

	private JwtUtil jwtUtil;

	public LoginFilter(JwtUtil jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String method = req.getMethod();
		String token = req.getHeader("token");
		String email = req.getHeader("clientEmail");
		String acrh = req.getHeader("access-control-request-headers");
		String url = req.getRequestURI();
		
//		try {

			if (url.contains("/login") || url.contains("/general") || url.contains("/pics")) {
				System.out.println("LOGIN FILTER PASS-------------");
				chain.doFilter(request, response);
				return;
			}

			if (token != null) {
				if (!jwtUtil.validateToken(token, email)) {
					res.setHeader("Access-Control-Allow-Origin", "*");
					res.setHeader("Access-Control-Allow-Headers", "*");
					res.setHeader("Access-Control-Expose-Headers", "*");
					res.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not authorized");
				} else {
					System.out.println("LOGIN FILTER PASS-------------");
					chain.doFilter(request, response);
				}
			} else {
				if (acrh != null && method.equals("OPTIONS")) {
					System.out.println("PREFLIGHT-------------");
					res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
					res.setHeader("Access-Control-Allow-Origin", "*");
					res.setHeader("Access-Control-Allow-Headers", "*");
					res.sendError(HttpStatus.OK.value(), "preflight");
				} else {
					System.out.println("LOGIN FILTER FAILL-------------");
					res.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not logged in");
				}
			}
//		} catch (Exception e) {
//			res.setHeader("Access-Control-Allow-Origin", "*");
//			res.setHeader("Access-Control-Allow-Headers", "*");
//			res.setHeader("Access-Control-Expose-Headers", "*");
//			res.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not authorized");
//		}
	}

}
