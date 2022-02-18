package server.database;

import org.springframework.data.jpa.repository.JpaRepository;

import commons.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Activity findByTitle(String title);
}
