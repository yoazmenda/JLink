import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class JLinkGenerator extends AnAction {


    private static final NotificationGroup BALLOON_NOTIFIER =
            new NotificationGroup("JLink", NotificationDisplayType.BALLOON, false);


    @Override
    public void actionPerformed(AnActionEvent e) {
        String relativePathToFile = getRelativePathToFile(e);
        if (relativePathToFile == null) {
            return;
        }
        CaretCoordinates caretCoordinates = getFileCoordinates(e);

        String jLink = generateJLink(relativePathToFile, caretCoordinates);
        StringSelection selection = new StringSelection(jLink);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    private String generateJLink(String relativePathToFile, CaretCoordinates caretCoordinates) {
        return "http://localhost:63342/api/file/" + relativePathToFile + ":" + caretCoordinates;
    }

    private CaretCoordinates getFileCoordinates(AnActionEvent e) {
        CaretModel caret;
        try {
            caret = e.getData(PlatformDataKeys.CARET).getCaretModel();
        } catch (Exception exception) {
            return null;
        }

        int line = caret.getPrimaryCaret().getLogicalPosition().line + 1;
        int column = caret.getPrimaryCaret().getLogicalPosition().column + 1;
        return new CaretCoordinates(line, column);
    }

    private String getRelativePathToFile(AnActionEvent e) {
        VirtualFile virtualFile = e.getData(LangDataKeys.PSI_FILE).getVirtualFile();
        VirtualFile contentRootForFile = ProjectFileIndex.SERVICE.getInstance(e.getProject()).getContentRootForFile(virtualFile);
        if (contentRootForFile == null) {
            Notification notification = BALLOON_NOTIFIER.createNotification("JLink: Not supported operation", "Only Project Files Are Supported", NotificationType.WARNING, null);
            Notifications.Bus.notify(notification);
            return null;
        }
        return VfsUtilCore.getRelativePath(virtualFile, contentRootForFile);
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }
}
