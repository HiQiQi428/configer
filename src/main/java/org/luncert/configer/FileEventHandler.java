package org.luncert.configer;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.luncert.mullog.Mullog;

public class FileEventHandler implements FileAlterationListener {

    private static Mullog mullog = new Mullog("console");

    private Configure config;

    FileEventHandler(Configure config) {
        this.config = config;
    }

    public void onStart(final FileAlterationObserver observer) {}

    public void onDirectoryCreate(final File directory) {
        mullog.info("configure directory create -", directory.getName());
        config.refresh(directory);
    }

    public void onDirectoryChange(final File directory) {
        mullog.info("configure directory change -", directory.getName());
        config.refresh(directory);
    }

    public void onDirectoryDelete(final File directory) {
        mullog.info("configure directory delete -", directory.getName());
        config.refresh(directory);
    }

    public void onFileCreate(File file) {
        mullog.info("configure file create -", file.getName());
        config.refresh(file);
    }

    public void onFileChange(File file) {
        mullog.info("configure file change -", file.getName());
        config.refresh(file);
    }

    public void onFileDelete(File file) {
        mullog.info("configure file delete -", file.getName());
        config.refresh(file);
    }

    public void onStop(FileAlterationObserver observer) {}

}