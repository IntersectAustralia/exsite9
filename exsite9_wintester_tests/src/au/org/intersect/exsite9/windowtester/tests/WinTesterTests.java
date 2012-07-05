package au.org.intersect.exsite9.windowtester.tests;

import java.io.File;
import java.util.Calendar;

import org.junit.Test;

import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.eclipse.WorkbenchLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.eclipse.ContributedToolItemLocator;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.ListItemLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.WT;


public class WinTesterTests extends UITestCaseSWT
{

    private static String testDirName;
    private static File testDirFile = null;
    
    private static String currentProjectName;
    private static long now;
    
    @Override
    public void oneTimeSetup() throws Exception
    {
        super.oneTimeSetup();
        
        IUIContext ui = getUI();
        ui.ensureThat(new WorkbenchLocator().hasFocus());
        ui.ensureThat(ViewLocator.forName("Welcome").isClosed());
        
        now = Calendar.getInstance().getTimeInMillis();
        
        testDirName = System.getProperty("java.io.tmpdir") + File.separator + "windowtester-" + now;
        testDirFile = new File(testDirName);
        testDirFile.mkdir();
        
        File file1 = new File(testDirName + File.separator + "vid1.mpg");
        file1.createNewFile();
        
        File file2 = new File(testDirName + File.separator + "vid2.mpg");
        file2.createNewFile();
        
        File file3 = new File(testDirName + File.separator + "vid3.mpg");
        file3.createNewFile();
        
        File file4 = new File(testDirName + File.separator + "vid4.mpg");
        file4.createNewFile();
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        now = Calendar.getInstance().getTimeInMillis();
        currentProjectName = "Test Project - " + now;
    }

    @Override
    public void oneTimeTearDown() throws Exception
    {
        super.oneTimeTearDown();
        File testDirFile = new File(testDirName);
        File[] files = testDirFile.listFiles();
        for(File file : files)
        {
            file.delete();
        }
        testDirFile.delete();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    //
    // Tests
    //
    
    @Test
    public void testNewProjectFromToolBar()
    {
        try
        {
            toolbarNewProject(currentProjectName, "Owner Name", "This is a test project created by Window Tester using the toolbar button.");
        }
        catch(Exception e)
        {
            fail("Unexpected exception when creating project via the toolbar: " + e.getMessage());
        }
    }
    
    @Test
    public void testNewProjectFromProjectMenu()
    {
        try
        {
            menuProjectNew(currentProjectName, "Owner Name","This is a test project created by Window Tester using the Project| New menu");
        }
        catch(Exception e)
        {
            fail("Unexpected exception when creating project via the Project|New... menu: " + e.getMessage());
        }
    }
    
    @Test
    public void testOpenProjectFromProjectMenu()
    {
        try
        {
            menuProjectNew(currentProjectName, "Owner Name","This is a test project created by Window Tester using the Project| New menu");
            menuProjectNew(currentProjectName + " 2", "Owner Name","This is a test project created by Window Tester using the Project| New menu");
            menuProjectOpen(currentProjectName);
        }
        catch(Exception e)
        {
            fail("Unexpected exception when opening a project via the Project|Open... menu: " + e.getMessage());
        }
    }
    
    @Test
    public void testNewGroupFromProjectContextMenu()
    {
        try
        {
            menuProjectNew(currentProjectName, "Owner Name","This is a test project created by Window Tester using the Project| New menu");
            
            String groupName = "My Group";
            contextMenuNewGroup(currentProjectName, groupName);
        }
        catch(Exception e)
        {
            fail("Unexpected exception when creating a group via the Project context menu: " + e.getMessage());
        }
    }
    
    @Test
    public void testNewFolderFromProjectContextMenu()
    {
        try
        {
            menuProjectNew(currentProjectName, "Owner Name","This is a test project created by Window Tester using the Project| New menu");
            
            projectContextMenuNewFolder(currentProjectName, testDirName);
        }
        catch(Exception e)
        {
            fail("Unexpected exception when adding a folder to a project via the Project context menu: " + e.getMessage());
        }
    }

    @Test
    public void testDragNewFilesToGroup() throws Exception
    {
        try
        {
            toolbarNewProject(currentProjectName,"Owner","This is a test project created by Window Tester.");
            String groupName = "My Group";
            contextMenuNewGroup(currentProjectName, groupName);
            projectContextMenuNewFolder(currentProjectName,testDirName);
            
            String[] itemArray = new String[2];
            itemArray[0] = currentProjectName +"/New Files (4)/vid1.mpg";
            itemArray[1] = currentProjectName +"/New Files (4)/vid2.mpg";
            dragTreeItemsToGroup(itemArray, currentProjectName + "/" + groupName + " (0)");
            
            sleep(2000);
        }
        catch(Exception e)
        {
            fail("Exception: " + e.getMessage());
        }
    }
    
    /*@Test
    public void testDragGroupWithFilesToGroup() throws Exception
    {
        try
        {
            toolbarNewProject(currentProjectName,"Owner","This is a test project created by Window Tester.");
            String groupName1 = "My Group One";
            contextMenuNewGroup(currentProjectName, groupName1);
            String groupName2 = "My Group Two";
            contextMenuNewGroup(currentProjectName, groupName2);
            projectContextMenuNewFolder(currentProjectName,testDirName);
            
            
            // This drag isn't working when run after another test
            String[] itemArray1 = new String[2];
            itemArray1[0] = currentProjectName + "/New Files (4)/vid1.mpg";
            itemArray1[1] = currentProjectName + "/New Files (4)/vid2.mpg";
            dragTreeItemsToGroup(itemArray1, currentProjectName + "/" + groupName1 + " (0)");
            
            getUI().click(1, new TreeItemLocator(currentProjectName + "/" + groupName1 + " (2)/vid1.mpg"));
                    
            sleep(2000);
            
            String[] itemArray2 = new String[1];
            itemArray2[0] = currentProjectName + "/My Group One (2)";
            dragTreeItemsToGroup(itemArray2, currentProjectName + "/" + groupName2 + " (0)");
            
            getUI().click(1, new TreeItemLocator(currentProjectName + "/" + groupName2 + " (1)/" + groupName1 + " (2)/vid1.mpg"));
            
            sleep(2000);
        }
        catch(Exception e)
        {
            fail("Exception: " + e.getMessage());
        }
    }
*/
    
    //
    // Helper methods
    //
    
    private void menuProjectNew(String name, String owner, String description) throws Exception
    {
        IUIContext ui = getUI();
        ui.click(new MenuItemLocator("Project/New..."));
        ui.wait(new ShellShowingCondition(""));
            
        newProjectDialog(name, owner,description);
    }
    
    private void toolbarNewProject(String name, String owner, String description) throws Exception
    {
        IUIContext ui = getUI();
        ui.click(new ContributedToolItemLocator("au.org.intersect.exsite9.commands.NewProjectCommand"));
        ui.wait(new ShellShowingCondition(""));
            
        newProjectDialog(name, owner,description);
    }
    
    private void newProjectDialog(String name, String owner, String description) throws Exception
    {
        IUIContext ui = getUI();
        ui.enterText(name);
        ui.keyClick(WT.TAB);
        ui.enterText(owner);
        ui.keyClick(WT.TAB);
        ui.enterText(description);
        ui.click(new ButtonLocator("&Finish"));
        ui.wait(new ShellDisposedCondition("Progress Information"));
    }
    
    private void menuProjectOpen(String name) throws Exception
    {
        IUIContext ui = getUI();
        ui.click(new MenuItemLocator("Project/Open..."));
        ui.wait(new ShellShowingCondition(""));
        ui.click(new ListItemLocator(name));
        ui.click(new ButtonLocator("&Finish"));
        ui.wait(new ShellDisposedCondition("Progress Information"));
    }
    
    private void projectContextMenuNewFolder(String projectName, String folderPath) throws Exception
    {
        IUIContext ui = getUI();
        ui.click(new TreeItemLocator(projectName, new ViewLocator("au.org.intersect.exsite9.view.ProjectExplorerView")));
        ui.contextClick(new TreeItemLocator(projectName, new ViewLocator(
                "au.org.intersect.exsite9.view.ProjectExplorerView")), "New/Folder...");
        
        // Need to select a folder
        ui.enterText(folderPath);
        ui.keyClick(WT.CR);
    }
    
    private void contextMenuNewGroup(String parent, String groupName) throws Exception
    {
        IUIContext ui = getUI();
        ui.contextClick(new TreeItemLocator(parent, new ViewLocator(
                "au.org.intersect.exsite9.view.ProjectExplorerView")), "New/Group...");
        ui.wait(new ShellShowingCondition(""));
        ui.enterText(groupName);
        ui.click(new ButtonLocator("&Finish"));
    }
    
    private void dragTreeItemsToGroup(String[] treeItems, String newParent) throws Exception
    {
        IUIContext ui = getUI();
        
        ui.click(1, new TreeItemLocator(treeItems[0], new ViewLocator("au.org.intersect.exsite9.view.ProjectExplorerView")));
        
        for(int i = 1; i < treeItems.length; ++i)
        {
            ui.click(1,new TreeItemLocator(treeItems[i], new ViewLocator("au.org.intersect.exsite9.view.ProjectExplorerView")), WT.CTRL);
        }
        sleep(1000);
        //ui.dragTo(new TreeItemLocator(newParent, new ViewLocator("au.org.intersect.exsite9.view.ProjectExplorerView")));
        ui.dragTo(new TreeItemLocator(newParent));
    }

    private void sleep(long millis)
    {
        try{Thread.sleep(millis);}catch(Exception e){};
    }

}