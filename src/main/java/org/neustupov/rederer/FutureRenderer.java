package org.neustupov.rederer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FutureRenderer {
    private final ExecutorService exec = Executors.newCachedThreadPool();

    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task = () -> imageInfos
                .stream()
                .map(ImageInfo::downloadImage)
                .collect(Collectors.toList());
        Future<List<ImageData>> future = exec.submit(task);
        renderText(source);
        try {
            List<ImageData> imageData = future.get();
            for (ImageData data : imageData) {
                renderImage(data);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
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
