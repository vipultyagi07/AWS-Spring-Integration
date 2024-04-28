package vip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vip.modal.ProjectDetail;

public interface ProjectDetailsRepository extends JpaRepository<ProjectDetail,Long> {
    ProjectDetail findByUserId(String userId);
}
