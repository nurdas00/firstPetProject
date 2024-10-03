package kg.mega.petproject1.service;

import jakarta.persistence.EntityNotFoundException;
import kg.mega.petproject1.entity.Role;
import kg.mega.petproject1.entity.User;
import kg.mega.petproject1.repository.RoleRepository;
import kg.mega.petproject1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public void create(User user) {
        userRepository.save(user);
    }

    public void getById(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            System.out.println(user.get());
        }
        throw new EntityNotFoundException("User with id: " + id + " not found");
    }
    public List<User> findUserByRoleId(Integer roleId ) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            return role.getUsers();
        } else {
            throw new EntityNotFoundException("User with roleId: " + roleId + " not found");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.loadByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User: " + username + " not found");
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        return userDetails;
    }
}