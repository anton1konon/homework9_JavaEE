package javaee.kononko.homework9;

import javaee.kononko.homework9.models.Permission;
import javaee.kononko.homework9.models.PermissionEntity;
import javaee.kononko.homework9.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    private final PermissionRepository permissions;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        createPermissionIfNotFound(Permission.WISHLIST);

        alreadySetup = true;
    }


    @Transactional
    PermissionEntity createPermissionIfNotFound(Permission permission) {

        var opt = permissions.findByPermission(permission);
        if (opt.isEmpty()) {
            var perm = new PermissionEntity(null, permission);
            permissions.save(perm);
            return perm;
        }
        return null;
    }
}
