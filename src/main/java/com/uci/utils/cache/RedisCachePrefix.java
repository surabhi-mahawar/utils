package com.uci.utils.cache;

public enum RedisCachePrefix {
    XMessageDao("XMessageDao"),
    Language("Language"),
    FAUserID("FAUserID"),
    MinioCDN("MinioCDN");

    private String name;

    RedisCachePrefix(String item) {
        name=item;
    }

    public String toString() {
        return name;
    }

    public static RedisCachePrefix getEnumByText(String code) {
        for (RedisCachePrefix e : RedisCachePrefix.values()) {
            if (e.name.equals(code))
                return e;
        }
        return null;
    }
}
