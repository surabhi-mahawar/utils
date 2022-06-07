package com.uci.utils.cdn.samagra;

import com.uci.utils.cache.service.RedisCacheService;
import io.fusionauth.client.FusionAuthClient;
import io.fusionauth.domain.api.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

class MinioClientServiceTest {
    @Mock
    MinioClientService minioClientService;

    RedisCacheService redisCacheService;

    @Mock
    LoginRequest loginRequest;

    @Mock
    FusionAuthClient fusionAuthClient;

    private static final String CONTENT_LENGTH = "Content-Length";

    InputStream inputStream;

    private final String imageUrl = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg";

    private final String minioUrl = "http://play.min.io/mybucket";

    private final String bucketId = "bucketId";

    @BeforeEach
    void init() throws IOException {
        MinioClientProp minioClientProp = MinioClientProp.builder()
                .loginRequest(loginRequest)
                .cdnBaseUrl(minioUrl)
                .bucketId(bucketId)
                .fusionAuth(fusionAuthClient).build();
        RedisTemplate<String, Object> objRedisTemplate = new RedisTemplate<>();
        redisCacheService = Mockito.spy(new RedisCacheService(objRedisTemplate));
        minioClientService = Mockito.spy(new MinioClientService(minioClientProp, redisCacheService));
        Mockito.doReturn(minioUrl).when(minioClientService).getFileSignedUrl(anyString());
        URL url = new URL(imageUrl);
        inputStream = url.openStream();
        Mockito.doReturn("package.json").when(minioClientService).uploadFileFromInputStream(inputStream, "application/json", "package.json");
    }

    @Test
    void getSignedUrlTest() {
        String s = minioClientService.getFileSignedUrl("abc.txt");
        assertNotNull(s);
        assertEquals(minioUrl, s);
    }

    @Test
    void uploadFileFromInputStreamTest() throws IOException {
        String s = minioClientService.uploadFileFromInputStream(inputStream, "application/json", "package.json");
        assertNotNull(s);
        assertEquals("package.json", s);
    }
}