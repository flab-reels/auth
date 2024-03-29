package com.flabreels.auth.oauth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String name;
    private String email;
    private String picture;

    static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes){
        switch (provider){
            case "google":
                return ofGoogle(attributeKey, attributes);
            case "naver":
                return ofNaver("id",attributes);
            case "facebook":
                return ofFaceBook(attributeKey,attributes);
            default:
                throw new RuntimeException();
        }
    }
    private static OAuth2Attribute ofFaceBook(String attributeKey, Map<String, Object> attributes){
        return OAuth2Attribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("imageUrl"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }


    private static OAuth2Attribute ofGoogle(String attributeKey, Map<String, Object> attributes){
        return OAuth2Attribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }
    private static OAuth2Attribute ofNaver(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .attributeKey(attributeKey)
                .build();
    }

    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("email", email);
        map.put("picture", picture);

        return map;
    }

}
