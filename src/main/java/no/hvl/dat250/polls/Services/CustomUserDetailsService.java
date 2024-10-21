package no.hvl.dat250.polls.Services;

import no.hvl.dat250.polls.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        // Implement this method properly
        User.withUsername(username).orElseThrow(() ->
            new UsernameNotFoundException("User not found: " + username)
        );
        // For now, just throw an exception to indicate it's not fully implemented
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
