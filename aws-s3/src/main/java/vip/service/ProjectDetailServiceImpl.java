package vip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.modal.ProjectDetail;
import vip.repository.ProjectDetailsRepository;

import java.util.Objects;

@Service
public class ProjectDetailServiceImpl implements ProjectDetailService {

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Override
    public String saveProjectDetailsWithUser(ProjectDetail projectDetail, String userId) {

        ProjectDetail existingProjectDetail = projectDetailsRepository.findByUserId(userId);

        if(Objects.nonNull(existingProjectDetail)){
            return "Project already exist for this user";
        }
        projectDetail.setUserId(userId);
        projectDetailsRepository.save(projectDetail);
        return "Project detail for user is saved ";

    }
}
