package org.restaurant.voting.repository;

public interface FindById {
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projection.dynamic
    <T> T findById(Integer integer, Class<T> type);
}
