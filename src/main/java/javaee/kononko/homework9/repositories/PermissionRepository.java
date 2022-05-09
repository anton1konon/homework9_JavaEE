package javaee.kononko.homework9.repositories;

import javaee.kononko.homework9.models.Permission;
import javaee.kononko.homework9.models.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {
    @Query("select p from PermissionEntity p where p.permission = :permission")
    Optional<PermissionEntity> findByPermission(@Param("permission") Permission permission);
}
