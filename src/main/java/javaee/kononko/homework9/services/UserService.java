package javaee.kononko.homework9.services;

import javaee.kononko.homework9.models.Permission;
import javaee.kononko.homework9.models.RegisterForm;
import javaee.kononko.homework9.models.User;
import javaee.kononko.homework9.repositories.PermissionRepository;
import javaee.kononko.homework9.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final MyPasswordEncoder encoder;
    private final PermissionRepository permissions;
    private final WishlistService wishlists;

    @Transactional
    public void registerUser(RegisterForm user) {
        var entity = new User();
        entity.setLogin(user.getLogin());
        entity.setPassword(encoder.encode(user.getPassword()));
        var perm = permissions.findByPermission(Permission.WISHLIST).get();
        entity.setPermissions(List.of(perm));
        repository.save(entity);
        wishlists.initWishlist(entity);
    }
}
