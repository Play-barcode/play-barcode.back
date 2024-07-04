package com.bandit.api.kakao.service;


import com.bandit.api.kakao.dto.KakaoFriendDto;
import com.bandit.api.kakao.dto.KakaoMessageRequest;
import com.bandit.global.service.HttpCallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.bandit.global.util.KakaoMessageTemplateUtil.createReceiverUuidsArray;
import static com.bandit.global.util.KakaoMessageTemplateUtil.inviteMessageTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService extends HttpCallService {
    private static final String GET_FRIENDS_LIST_URL = "https://kapi.kakao.com/v1/api/talk/friends";
    private static final String POST_MESSAGE_TO_FRIEND_URL = "https://kapi.kakao.com/v1/api/talk/friends/message/default/send";
    private final RestTemplate restTemplate;
    public List<KakaoFriendDto> getFriendsList(String accessToken) {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> response = httpRequest(GET_FRIENDS_LIST_URL, HttpMethod.GET, entity);
        List<KakaoFriendDto> list = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.getBody());
            JSONArray elements = (JSONArray) jsonObject.get("elements");

            for (Object element : elements) {
                KakaoFriendDto kakaoFriendDto = builderKakaoFriendDto(element);
                list.add(kakaoFriendDto);
            }

        } catch (ParseException e) {
            log.error("JSON parsing error", e);
        }

        return list;
    }
    private KakaoFriendDto builderKakaoFriendDto(Object element) {
        JSONObject obj = (JSONObject) element;
        return KakaoFriendDto.builder()
                .id(obj.get("id").toString())
                .uuid(obj.get("uuid").toString())
                .favorite((boolean) obj.get("favorite"))
                .profile_nickname(obj.get("profile_nickname").toString())
                .profile_thumbnail_image(obj.get("profile_thumbnail_image").toString())
                .build();
    }
    public void sendMessage(String accessToken,
                              KakaoMessageRequest request) {

        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", APP_TYPE_URL_ENCODED);
        header.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        JSONArray receiverUuidsArray = createReceiverUuidsArray(request.getReceiverUuids());
        JSONObject messageTemplate = inviteMessageTemplate(request.getButtonUrl());
        log.info("uuid = {}", receiverUuidsArray);
        log.info("template = {}", messageTemplate);
        parameters.add("receiver_uuids", receiverUuidsArray.toString());
        parameters.add("template_object", messageTemplate.toJSONString());

        HttpEntity<MultiValueMap<String, String>> messageRequestEntity = new HttpEntity<>(parameters, header);
        httpRequest(POST_MESSAGE_TO_FRIEND_URL, HttpMethod.POST, messageRequestEntity);
    }
}
