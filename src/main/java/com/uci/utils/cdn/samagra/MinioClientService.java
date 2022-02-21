package com.uci.utils.cdn.samagra;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inversoft.error.Errors;
import com.inversoft.rest.ClientResponse;

import io.fusionauth.client.FusionAuthClient;
import io.fusionauth.domain.api.LoginRequest;
import io.fusionauth.domain.api.LoginResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.credentials.StaticProvider;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SuppressWarnings("ALL")
@Service
@Slf4j
@AllArgsConstructor
@Getter
@Setter
public class MinioClientService {
	private FusionAuthClient fusionAuth;
	private MinioClientProp minioClientProp;
	
	/**
	 * Get Signed URL of CDN for given media file name
	 * @param mediaName
	 * @return
	 */
	public String getCdnSignedUrl(String mediaName) {
		String url = "";
		MinioClient minioClient = getMinioClient();
        if(minioClient != null) {
			try {
				url = minioClient.getPresignedObjectUrl(
	                GetPresignedObjectUrlArgs.builder()
	                    .method(Method.GET)
	                    .bucket(minioClientProp.bucketId)
	                    .object(mediaName)
	                    .expiry(1, TimeUnit.DAYS)
	                    .build());
			} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
					| InvalidResponseException | NoSuchAlgorithmException | XmlParserException | ServerException
					| IllegalArgumentException | IOException e) {
				// TODO Auto-generated catch block
				log.error("Exception in getCdnSignedUrl: "+e.getMessage());
			}
        }
        log.info("minioClient url: "+url);
        return url;
    }
	
	/**
	 * Get Minio Client
	 * @return
	 */
	private MinioClient getMinioClient() {
		try {
			StaticProvider provider = getMinioCredentialsProvider(); 
			if(provider != null) {
				return MinioClient.builder()
						.endpoint(minioClientProp.cdnBaseUrl)
						.credentialsProvider(provider)
						.build();
			}
		} catch(Exception e) {
			log.error("Exception in getMinioClient: "+e.getMessage());
		}
		return null;
	}
	
	/**
	 * Get Credentials Provider for Minio Client
	 * @return
	 */
	private StaticProvider getMinioCredentialsProvider() {
		try {
			String token = getFusionAuthToken();
			log.info("token: "+token);
			if(!token.isEmpty()) {
				WebClient client = WebClient.builder()
						.baseUrl(minioClientProp.cdnBaseUrl)
		                .build();
				String response = client.post().uri(builder -> builder.path(minioClientProp.bucketId)
											.queryParam("Action", "AssumeRoleWithWebIdentity")
											.queryParam("DurationSeconds", "36000")
											.queryParam("WebIdentityToken", token)
											.queryParam("Version", "2011-06-15")
											.build())
					.exchange()
                    .block()
                    .bodyToMono(String.class)
                    .block();
				
				JSONObject xmlJSONObj = XML.toJSONObject(response);
		        String jsonPrettyPrintString = xmlJSONObj.toString(4);
		        
		        ObjectMapper mapper = new ObjectMapper();
		        JsonNode node = mapper.readTree(jsonPrettyPrintString);
		        JsonNode credentials = node.path("AssumeRoleWithWebIdentityResponse").path("AssumeRoleWithWebIdentityResult").path("Credentials");
				String sessionToken = credentials.get("SessionToken").asText();
				String accessKey = credentials.get("AccessKeyId").asText();
				String secretAccessKey = credentials.get("SecretAccessKey").asText();
				
				log.info("sessionToken: "+sessionToken+", accessKey: "+accessKey+",secretAccessKey: "+secretAccessKey);
				
				if(!accessKey.isEmpty() && !secretAccessKey.isEmpty() && !sessionToken.isEmpty()) {
					return new StaticProvider(accessKey, secretAccessKey, sessionToken);
				}
			}  
		} catch (Exception e) {
			log.error("Exception in getMinioCredentialsProvider: "+e.getMessage());
		}
		return null;
	}
	
	/**
	 * Get Fustion Auth Token
	 * @return
	 */
	private String getFusionAuthToken() {
		String token = "";		
		try {
			ClientResponse<LoginResponse, Errors> clientResponse = fusionAuth.login(minioClientProp.loginRequest);
			if(clientResponse.wasSuccessful()) {
				token = clientResponse.successResponse.token;
			}
		} catch (Exception e) {
			log.error("Exception in getFusionAuthToken: "+e.getMessage());
		}
		return token;
	}
}
