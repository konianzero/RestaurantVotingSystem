package org.restaurant.voting.repository;

import org.restaurant.voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId")
    List<Vote> getByUserId(@Param("userId")  int userId);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.votingDate=:votingDate")
    Optional<Vote> getByUserIdAndDate(@Param("votingDate") LocalDate now, @Param("userId")  int userId);
}
