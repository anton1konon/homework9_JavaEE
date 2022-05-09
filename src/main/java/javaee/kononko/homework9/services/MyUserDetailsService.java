package javaee.kononko.homework9.services;

import javaee.kononko.homework9.models.PermissionEntity;
import javaee.kononko.homework9.models.User;
import javaee.kononko.homework9.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("No user with login: " + username));
        return org.springframework.security.core.userdetails.User.builder().username(username).password(user.getPassword()).authorities(mapAuthorities(user.getPermissions())).build();
    }

    private static List<GrantedAuthority> mapAuthorities(final List<PermissionEntity> permissions) {
        return permissions.stream().map(PermissionEntity::getPermission).map(Enum::name).map(SimpleGrantedAuthority::new).collect(Collectors.toUnmodifiableList());
    }
}
