package com.uci.utils.cdn;

import com.uci.utils.azure.AzureBlobService;
import com.uci.utils.cdn.samagra.MinioClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class FileCdnFactory {
    @Autowired
    MinioClientService minioClientService;

    @Autowired
    AzureBlobService azureBlobService;

    public FileCdnProvider getFileCdnProvider() {
        String selected = System.getenv("SELECTED_FILE_CDN");
        if(selected != null && selected.equals("minio")) {
            return minioClientService;
        } else {
            return azureBlobService;
        }
    }
}
