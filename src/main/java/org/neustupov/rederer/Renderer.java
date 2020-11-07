package org.neustupov.rederer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Renderer {

    private final ExecutorService exec;

    public Renderer(ExecutorService exec) {
        this.exec = exec;
    }

    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(exec);
        for (final ImageInfo imageInfo : imageInfos) {
            completionService.submit(imageInfo::downloadImage);
        }
        renderText(source);
        try {
            for (int t = 0, n = imageInfos.size(); t < n; t++) {
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println(e.getCause().toString());
        }
    }

    private List<ImageInfo> scanForImageInfo(CharSequence source) {
        return new ArrayList<>();
    }

    private void renderText(CharSequence source) {

    }

    private void renderImage(ImageData data) {

    }
}
