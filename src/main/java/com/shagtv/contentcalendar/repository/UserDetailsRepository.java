package com.shagtv.contentcalendar.repository;

import com.shagtv.contentcalendar.model.User;
import com.shagtv.contentcalendar.model.UserRoles;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

@Repository
public class UserDetailsRepository {
    private final UserRepository repository;

    public UserDetailsRepository(UserRepository repository) {
        this.repository = repository;
    }

    public UserDetails findUserByName(String name) {
        User user = this.repository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("No user was found");
        }
        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (UserRoles role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.toString()));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                roles
        );
    }
}
