package com.uci.utils.cdn;

import java.io.InputStream;

public interface FileCdnProvider {
    public String getFileSignedUrl(String name);

    public String uploadFile(String urlStr, String mimeType, String name, Double maxSizeForMedia);

    public String uploadFileFromInputStream(InputStream binary, String mimeType, String name);
}
