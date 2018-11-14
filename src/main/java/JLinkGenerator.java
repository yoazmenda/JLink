import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class JLinkGenerator extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon());

        String relativePathToFile = getRelativePathToFile(e);
        CaretCoordinates caretCoordinates = getFileCoordinates(e);

        String jLink = generateJLink(relativePathToFile, caretCoordinates);
        String[] options = new String[0];
        Messages.showDialog(jLink, "JLink", options, 0, null);
    }

    private String generateJLink(String relativePathToFile, CaretCoordinates caretCoordinates) {
        return "Relative path: " + relativePathToFile + " Coordinates: " + caretCoordinates;
    }

    private CaretCoordinates getFileCoordinates(AnActionEvent e) {
        return new CaretCoordinates();
    }

    private String getRelativePathToFile(AnActionEvent e) {
        return "Relative Path";
    }
}
