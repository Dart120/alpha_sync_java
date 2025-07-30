package org.alpha_sync_java.infra;

import org.alpha_sync_java.application.interfaces.Downloader;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

// TODO
public class NioDownloader implements Downloader {

    @Override
    public CompletableFuture<Void> downloadToFile(URL url, String path, String filename){
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Files.createDirectories(Paths.get(path));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Path finalPath = Paths.get(path, filename);
                try (FileOutputStream fileOutputStream = new FileOutputStream(finalPath.toFile())){

                    ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                    FileChannel fileChannel = fileOutputStream.getChannel();
                    long ret =  fileChannel
                            .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to download file: " + filename + " from: " + url, e);
                }
                return null;
            });
    }

    @Override
    public CompletableFuture<byte[]> downloadToBytes(URL url){



            return CompletableFuture.supplyAsync(() -> {
                try {
                    ReadableByteChannel readable =  Channels.newChannel(url.openStream());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    WritableByteChannel writable = Channels.newChannel(out);

                    ByteBuffer buffer = ByteBuffer.allocate(8192); // 8 KB buffer
                    while (readable.read(buffer) != -1) {
                        buffer.flip();                 // Prepare buffer for reading
                        writable.write(buffer);       // Write buffer to output
                        buffer.clear();               // Prepare buffer for writing
                    }

                    readable.close();
                    return out.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
