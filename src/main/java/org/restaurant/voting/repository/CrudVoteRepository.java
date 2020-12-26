package org.restaurant.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.restaurant.voting.model.Vote;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId")
    List<Vote> getAllByUserId(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.id=:voteId AND v.user.id=:userId")
    int delete(@Param("voteId") int voteId, @Param("userId") int userId);
}
