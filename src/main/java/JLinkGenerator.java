import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class JLinkGenerator extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        String relativePathToFile = getRelativePathToFile(e);
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
        CaretModel caretModel;
        try {
            caretModel = e.getData(DataKeys.CARET).getCaretModel();
        } catch (Exception exception) {
            return null;
        }

        int line = caretModel.getPrimaryCaret().getVisualPosition().line;
        int col = caretModel.getPrimaryCaret().getVisualPosition().column;
        return new CaretCoordinates(line, col);
    }

    private String getRelativePathToFile(AnActionEvent e) {
        VirtualFile virtualFile = e.getData(LangDataKeys.PSI_FILE).getVirtualFile();
        VirtualFile contentRootForFile = ProjectFileIndex.SERVICE.getInstance(e.getProject()).getContentRootForFile(virtualFile);
        return VfsUtilCore.getRelativePath(virtualFile, contentRootForFile);
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }
}
