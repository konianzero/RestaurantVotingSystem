package org.restaurant.voting.repository;

import org.restaurant.voting.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CrudUserRepository extends BaseRepository<User> {

    @Query("SELECT u FROM User u WHERE u.email=:email")
    User getByEmail(@Param("email") String email);
}
