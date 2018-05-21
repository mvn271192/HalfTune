package Models;

/**
 * Created by manikandanvnair on 21/05/18.
 */

public class DownloadProgress {

    String progressId;
    int progress = 0;

    public DownloadProgress(String progressId, int progress) {
        this.progressId = progressId;
        this.progress = progress;
    }

    public String getProgressId() {
        return progressId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
