package com.flatlogic.app.generator.repository;

import com.flatlogic.app.generator.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {

    @Query("select case when count(f) > 0 then true else false end from File f where exists (select file from File file where file.privateUrl = :privateUrl)")
    boolean existsFileByPrivateUrl(String privateUrl);

    @Query("select f from File f where f.belongsTo = :belongsTo")
    List<File> findFilesByBelongsTo(@Param(value = "belongsTo") String belongsTo);

    @Modifying(clearAutomatically = true)
    @Query("update File f set f.deletedAt = :deletedAt where f.id = :id")
    void updateDeletedAt(@Param(value = "id") UUID id, @Param(value = "deletedAt") Date deletedAt);

}
