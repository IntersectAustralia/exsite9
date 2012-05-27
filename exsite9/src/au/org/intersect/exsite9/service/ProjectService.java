package au.org.intersect.exsite9.service;

import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.domain.Project;

public class ProjectService
{
    private ProjectDAO projectDAO = null;
    
    public ProjectService(ProjectDAO projectDAO)
    {
        this.projectDAO = projectDAO;
    }
    
    public void createProject(String name, String description)
    {
        Project project = new Project(name, description);
        projectDAO.createProject(project);
    }
}
