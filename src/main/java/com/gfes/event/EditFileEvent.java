package com.gfes.event;

import com.gfes.entity.FileInfo;
import com.gfes.view.FileManagerJPanel;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.core.ResolvableType;

public class EditFileEvent extends PayloadApplicationEvent<FileInfo> {

    private FileManagerJPanel fileManagerJPanel;

    public EditFileEvent(Object source, FileInfo payload) {
        super(source, payload);
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forRawClass(EditFileEvent.class);
    }

    public FileManagerJPanel getFileManagerJPanel() {
        return fileManagerJPanel;
    }

    public void setFileManagerJPanel(FileManagerJPanel fileManagerJPanel) {
        this.fileManagerJPanel = fileManagerJPanel;
    }
}
