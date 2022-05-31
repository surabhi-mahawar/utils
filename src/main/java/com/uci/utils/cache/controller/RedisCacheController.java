package com.uci.utils.cache.controller;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.uci.utils.cache.RedisCachePrefix;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uci.utils.cache.service.RedisCacheService;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@RestController
@Slf4j
@RequestMapping(value = "/cache/redis")
public class RedisCacheController {
	@Autowired
	private RedisCacheService service;

	/**
	 * call this to get all XMessageDao Caches
	 */
	@GetMapping(path = "/all/xMessageDAO", produces = { "application/json", "text/json" })
	public ResponseEntity getAllXMessageDAOCache() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree("{\"id\":\"api.content.cache\",\"ver\":\"3.0\",\"ts\":\"2021-06-26T22:47:05Z+05:30\",\"responseCode\":\"OK\",\"result\":{}}");
			JsonNode resultNode = mapper.createObjectNode();
			ArrayList<Pair<String, Object>> list = service.getAllCacheKeyValuesByPattern(RedisCachePrefix.XMessageDao.name());

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			if(list != null) {
				list.forEach(l -> {
					String json = "";
					try {
						json = l.getRight() != null ? ow.writeValueAsString(l.getRight()) : "";
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					((ObjectNode) resultNode).put(l.getLeft().toString(), json);
				});
			}
			((ObjectNode) jsonNode).put("result", resultNode);
			return ResponseEntity.ok(jsonNode);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}

	/**
	 * call this to get all Language Caches
	 */
	@GetMapping(path = "/all/language", produces = { "application/json", "text/json" })
	public ResponseEntity getAllLanguageCache() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree("{\"id\":\"api.content.cache\",\"ver\":\"3.0\",\"ts\":\"2021-06-26T22:47:05Z+05:30\",\"responseCode\":\"OK\",\"result\":{}}");
			JsonNode resultNode = mapper.createObjectNode();
			ArrayList<Pair<String, Object>> list = service.getAllCacheKeyValuesByPattern(RedisCachePrefix.Language.name());
			if(list != null) {
				list.forEach(l -> {
					((ObjectNode) resultNode).put(l.getLeft().toString(), (l.getRight() != null ? l.getRight().toString() : ""));
				});
			}
			((ObjectNode) jsonNode).put("result", resultNode);
			return ResponseEntity.ok(jsonNode);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}

	/**
	 * call this to get all Fusion Auth Caches
	 */
	@GetMapping(path = "/all/fa", produces = { "application/json", "text/json" })
	public ResponseEntity getAllFusionAuthCache() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree("{\"id\":\"api.content.cache\",\"ver\":\"3.0\",\"ts\":\"2021-06-26T22:47:05Z+05:30\",\"responseCode\":\"OK\",\"result\":{}}");
			JsonNode resultNode = mapper.createObjectNode();
			ArrayList<Pair<String, Object>> list = service.getAllCacheKeyValuesByPattern(RedisCachePrefix.FAUserID.name());
			if(list != null) {
				list.forEach(l -> {
					((ObjectNode) resultNode).put(l.getLeft().toString(), (l.getRight() != null ? l.getRight().toString() : ""));
				});
			}
			((ObjectNode) jsonNode).put("result", resultNode);
			return ResponseEntity.ok(jsonNode);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}

	/**
	 * call this remove cache by key
	 * @param prefix
	 * @Param key
	 */
//	@DeleteMapping(path = "/remove")
//	private void removeCacheByKey(@RequestParam String prefix, @RequestParam String key) {
//		service.deleteCache(prefix, key);
//	}
}
