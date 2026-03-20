package com.team05.studycafe.user.repository;

import com.team05.studycafe.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByLoginId(String loginId);
	Optional<User> findByLoginId(String loginId);
}
