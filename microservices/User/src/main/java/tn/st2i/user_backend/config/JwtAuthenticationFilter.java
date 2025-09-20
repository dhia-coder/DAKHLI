package tn.st2i.user_backend.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProviders tokenProvider;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtTokenProviders tokenProvider, UserDetailsService uds) {
    this.tokenProvider = tokenProvider;
    this.userDetailsService = uds;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath(); // safer than getRequestURI for matching
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;  // preflight
    return path.startsWith("/api/auth/") || path.equals("/error")
        || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response); // no token => do NOT block
      return;
    }

    String token = header.substring(7);
    if (tokenProvider.validateToken(token)) {
      String email = tokenProvider.getEmailFromJWT(token);
      UserDetails user = userDetailsService.loadUserByUsername(email);
      UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    chain.doFilter(request, response);
  }
}
