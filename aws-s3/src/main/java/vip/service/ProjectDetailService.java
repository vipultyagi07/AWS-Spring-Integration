package vip.service;

import org.springframework.stereotype.Service;
import vip.modal.ProjectDetail;

public interface ProjectDetailService {
    String saveProjectDetailsWithUser(ProjectDetail projectDetail, String userId);
}
