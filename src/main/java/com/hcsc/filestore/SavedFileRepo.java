package com.hcsc.filestore;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedFileRepo extends CrudRepository<SavedFileEntity, Long> {

    SavedFileEntity findByName(String filename);
}
