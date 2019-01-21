package com.pl.bas.repository;

import com.pl.bas.domain.Drug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {

    @Query(value = "select distinct drug from Drug drug left join fetch drug.producers left join fetch drug.diseases",
        countQuery = "select count(distinct drug) from Drug drug")
    Page<Drug> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct drug from Drug drug left join fetch drug.producers left join fetch drug.diseases")
    List<Drug> findAllWithEagerRelationships();

    @Query("select drug from Drug drug left join fetch drug.producers left join fetch drug.diseases where drug.id =:id")
    Optional<Drug> findOneWithEagerRelationships(@Param("id") Long id);

}
