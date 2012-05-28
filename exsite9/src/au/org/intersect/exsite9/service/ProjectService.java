package au.org.intersect.exsite9.service;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;

public class ProjectService
{
    private ProjectDAO projectDAO = null;
    private FolderDAO folderDAO = null;
    
    public ProjectService(ProjectDAO projectDAO,
                          FolderDAO folderDAO)
    {
        this.projectDAO = projectDAO;
        this.folderDAO = folderDAO;
    }
    
    public Project createProject(String name, String description)
    {
        Project project = new Project(name, description);
        projectDAO.createProject(project);
        return project;
    }
    
    public void mapFolderToProject(Project project, Folder folder)
    {
        folderDAO.createFolder(folder);
        project.getFolders().add(folder);
        projectDAO.updateProject(project);
    }
}
