package com.uci.utils.azure;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

import com.uci.utils.cdn.FileCdnProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobProperties;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.uci.utils.bot.util.FileUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@Service
public class AzureBlobService implements FileCdnProvider {

	private AzureBlobProperties properties;
	private BlobServiceClient serviceClient;
	private BlobContainerClient containerClient;

	public AzureBlobService(AzureBlobProperties properties) {
		this.properties = properties;
		System.out.println("accountName: "+properties.accountName+", key: "+properties.accountKey+", container: "+properties.container);
		if(properties.accountName != null && !properties.accountName.isEmpty()
				&& properties.accountKey != null && !properties.accountKey.isEmpty()
				&& properties.container != null && !properties.container.isEmpty()) {
			String connectionStr = "DefaultEndpointsProtocol=https;AccountName="+properties.accountName+";AccountKey="+properties.accountKey+";EndpointSuffix=core.windows.net";
			this.serviceClient = new BlobServiceClientBuilder().connectionString(connectionStr).buildClient();
			this.containerClient = serviceClient.getBlobContainerClient(properties.container);
		}
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
			if(this.containerClient != null) {
				BlobClient blobClient = containerClient.getBlobClient(name);
				log.info("getBlobUrl: " + blobClient.getBlobUrl());

				if (blobClient != null && blobClient.getBlobUrl() != null) {
					return blobClient.getBlobUrl() + "?" + generateBlobSASToken(blobClient);
				}
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
	/* Not in use */
//	public byte[] getFile(String name) {
//		try {
//			if(this.containerClient != null) {
//				BlobClient blobClient = containerClient.getBlobClient(name);
//
//				File temp = new File(name);
//				BlobProperties properties = blobClient.downloadToFile(temp.getPath());
//				byte[] content = Files.readAllBytes(Paths.get(temp.getPath()));
//				temp.delete();
//				return content;
//			}
//		} catch (Exception e) {
//			log.error("Exception in azure getFile: " + e.getMessage());
//			e.printStackTrace();
//		}
//		return null;
//	}

	/**
	 * Upload File from URL to Azure Blob Storage
	 *
	 * @param urlStr
	 * @param mimeType
	 * @param maxSizeForMedia
	 */
	public String uploadFile(String urlStr, String mimeType, String name, Double maxSizeForMedia) {
		try {
			if(this.containerClient != null) {
				/* Find File Name */
				Path path = new File(urlStr).toPath();
				String ext = FileUtil.getFileTypeByMimeSubTypeString(MimeTypeUtils.parseMimeType(mimeType).getSubtype());

				Random rand = new Random();
				if(name == null || name.isEmpty()) {
					name = UUID.randomUUID().toString();
				}
				name += "." + ext;

				log.info("Azure Blob Storage Container File Name :" + name);

				/* File input stream to copy from */
				URL url = new URL(urlStr);
				byte[] inputBytes = url.openStream().readAllBytes();

				/* Discard if file size is greater than MAX_SIZE_FOR_MEDIA */
				if(maxSizeForMedia != null && inputBytes.length > maxSizeForMedia){
					log.info("file size is greater than limit : " + inputBytes.length);
					return "";
				}

				/* Create temp file to copy to */
				String localPath = "/tmp/";
				String filePath = localPath + name;
				File temp = new File(filePath);
				temp.createNewFile();

				// Copy file from url to temp file
				Files.copy(new ByteArrayInputStream(inputBytes), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

				// Get a reference to a blob
				BlobClient blobClient = containerClient.getBlobClient(name);
				// Upload the blob
				blobClient.uploadFromFile(filePath);
				// Delete temp file
				temp.delete();

				// Return blob name
				return blobClient.getBlobName();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Upload File from URL to Azure Blob Storage
	 *
	 * @param binary
	 * @param mimeType
	 * @param name
	 */
	public String uploadFileFromInputStream(InputStream binary, String mimeType, String name) {
		try {
			if(this.containerClient != null) {
				/* Find File Name */
				String ext = FileUtil.getFileTypeByMimeSubTypeString(MimeTypeUtils.parseMimeType(mimeType).getSubtype());

				Random rand = new Random();
				if(name == null || name.isEmpty()) {
					name = UUID.randomUUID().toString();
				}
				name += "." + ext;

				log.info("Azure Blob Storage Container File Name :" + name);

				/* Create temp file to copy to */
				String localPath = "/tmp/";
				String filePath = localPath + name;
				File temp = new File(filePath);
				temp.createNewFile();

				// Copy file from url to temp file
				Files.copy(binary, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

				// Get a reference to a blob
				BlobClient blobClient = containerClient.getBlobClient(name);
				log.info("yash file path : " + filePath);
				blobClient.uploadFromFile(filePath);

				temp.delete();

				// Return blob name
				return blobClient.getBlobName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Generate SAS token
	 * @param blobClient
	 * @return
	 */
	public String generateBlobSASToken(BlobClient blobClient) {
		// Generate a sas using a blob client
		OffsetDateTime expiryTime = OffsetDateTime.now().plusMonths(1);
		BlobSasPermission blobSasPermission = new BlobSasPermission().setReadPermission(true);
		BlobServiceSasSignatureValues serviceSasValues = new BlobServiceSasSignatureValues(expiryTime,
				blobSasPermission);
		
		return blobClient.generateSas(serviceSasValues);
	}
	
	/**
	 * Generate SAS token
	 * @return
	 */
	public String generateContainerSASToken() {
		// Generate a sas using a blob client
		OffsetDateTime expiryTime = OffsetDateTime.now().plusMonths(3);
		// Generate a sas using a container client
		BlobContainerSasPermission containerSasPermission = new BlobContainerSasPermission()
																	.setCreatePermission(true)
																	.setAddPermission(true)
																	.setListPermission(true)
																	.setWritePermission(true)
																	.setReadPermission(true);
		BlobServiceSasSignatureValues serviceSasValues =
		    new BlobServiceSasSignatureValues(expiryTime, containerSasPermission);
		return containerClient.generateSas(serviceSasValues);
	}
}
