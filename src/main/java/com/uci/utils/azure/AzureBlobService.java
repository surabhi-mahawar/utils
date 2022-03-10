package com.uci.utils.azure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobProperties;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.AccountSasPermission;
import com.azure.storage.common.sas.AccountSasResourceType;
import com.azure.storage.common.sas.AccountSasService;
import com.azure.storage.common.sas.AccountSasSignatureValues;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@Service
public class AzureBlobService {

	private AzureBlobProperties properties;
	private BlobContainerClient client;

	public AzureBlobService(AzureBlobProperties properties) {
		this.properties = properties;
		this.client = new BlobContainerClientBuilder().endpoint(properties.url).sasToken(properties.token)
				.containerName(properties.container).buildClient();
	}

	/**
	 * Download File from Azure Blob Storage
	 * 
	 * @return
	 */
//	public ResponseEntity downloadFile(String file) {
//		byte[] data = getFile(file);
//		ByteArrayResource resource = new ByteArrayResource(data);
//
//		return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
//				.header("Content-disposition", "attachment; filename=\"" + file + "\"").body(resource);
//	}

	/**
	 * Get File signed url
	 * 
	 * @param name
	 * @return
	 */
	public String getFileSignedUrl(String name) {
		try {
			BlobClient blobClient = client.getBlobClient(name);
			log.info("getBlobUrl: " + blobClient.getBlobUrl());

			if (blobClient != null && blobClient.getBlobUrl() != null) {
				return blobClient.getBlobUrl() + "?" + properties.token;
			}
		} catch (Exception e) {
			log.error("Exception in azure getFileSignedUrl: " + e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Get File from Azure Blob Storage
	 * 
	 * @param name
	 * @return
	 */
	public byte[] getFile(String name) {
		try {
			BlobClient blobClient = client.getBlobClient(name);

			File temp = new File(name);
			BlobProperties properties = blobClient.downloadToFile(temp.getPath());
			byte[] content = Files.readAllBytes(Paths.get(temp.getPath()));
			temp.delete();
			return content;
		} catch (Exception e) {
			log.error("Exception in azure getFile: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Upload File from URL to Azure Blob Storage
	 * @param urlStr
	 */
	public String uploadFile(String urlStr, String mimeType) {
		try {
			/* Find File Name */
			Path path = new File(urlStr).toPath();
			String ext = MimeTypeUtils.parseMimeType(mimeType).getSubtype();
			Random rand = new Random();
			String name = UUID.randomUUID().toString()+"."+ext;

			log.info("Azure Blob Storage Container File Name :"+name);
			
			/* File input stream to copy from */
			URL url = new URL(urlStr);
			InputStream in = url.openStream();

			/* Create temp file to copy to */
			String localPath = "/tmp/";
			String filePath = localPath + name;
			File temp = new File(filePath);
			temp.createNewFile();
			
			// Copy file from url to temp file
			Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

			// Get a reference to a blob
			BlobClient blobClient = client.getBlobClient(name);
			// Upload the blob
			blobClient.uploadFromFile(filePath);
			// Delete temp file
			temp.delete();
			
			// Return blob name
			return blobClient.getBlobName();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
}
