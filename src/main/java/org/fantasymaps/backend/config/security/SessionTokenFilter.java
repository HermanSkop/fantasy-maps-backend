package org.fantasymaps.backend.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fantasymaps.backend.dtos.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class SessionTokenFilter extends OncePerRequestFilter {

    private final SessionRepository<? extends Session> sessionRepository;
    private static final Logger logger = Logger.getLogger(SessionTokenFilter.class.getName());

    public SessionTokenFilter(SessionRepository<? extends Session> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String sessionId = request.getHeader("X-Auth-Token");
        if (sessionId != null) {
            Session session = sessionRepository.findById(sessionId);
            if (session != null) {
                UserDto userDto = session.getAttribute("user");
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userDto.getRole().name()));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDto, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
