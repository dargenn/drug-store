package com.pl.bas.repository;

import com.pl.bas.domain.Disease;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Disease entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

}
