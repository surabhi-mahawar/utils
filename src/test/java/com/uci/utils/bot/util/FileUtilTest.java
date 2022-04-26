package com.uci.utils.bot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUtilTest {

    @Test
    void isFileTypeImage() {
        String mime = "image/png";
        boolean result = FileUtil.isFileTypeImage(mime);
        assertTrue(result);
    }

    @Test
    void isFileTypeAudio() {
        String mime = "audio/mp3";
        boolean result = FileUtil.isFileTypeAudio(mime);
        assertTrue(result);
    }

    @Test
    void isFileTypeVideo() {
        String mime = "video/mp4";
        boolean result = FileUtil.isFileTypeVideo(mime);
        assertTrue(result);
    }

    @Test
    void isFileTypeDocument() {
        String mime = "application/pdf";
        boolean result = FileUtil.isFileTypeDocument(mime);
        assertTrue(result);
    }

    @Test
    void getFileTypeByMimeSubTypeString() {
        String type = "x-flv";
        String result = FileUtil.getFileTypeByMimeSubTypeString(type);
        assertNotNull(result);
    }
}