package com.khumu.alimi.external.slack;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackNotifier {
    @Value("${notification.slack.enabled}")
    private boolean slackEnabled;
    @Value("${notification.slack.webhook.url}")
    private String webhookUrl;
    @Value("${notification.slack.channel}")
    private String channel;
    @Value("${notification.slack.botName}") private String botName;
    @Value("${notification.slack.icon.emoji}") private String iconEmoji;
    @Value("${notification.slack.icon.url}") private String iconUrl;

    @Autowired
    Environment env;
    @PostConstruct
    public void postConstructor(){
//        slack = Slack.getInstance();
    }
    public void sendSlack(String title, String content) {
        if (slackEnabled) {
                try { // create slack
                SlackMessage slackMessage = SlackMessage.builder().attachments(List.of(SlackMessage.Attachment.builder()
                        .title(title)
                        .text(content)
                        .footer("from " + env.getActiveProfiles()[0])
                        .build()
                )).channel(channel).build();
                String payload = new Gson().toJson(slackMessage);
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE); // send the post request
                HttpEntity<String> entity = new HttpEntity<>(payload, headers);
                restTemplate.postForEntity(webhookUrl, entity, String.class);
            } catch (Exception e) {
                log.error("Unhandled Exception occurred while send slack. [Reason] : ", e);
            }
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SlackMessage {
        @SerializedName("text")
        private String text;
        @SerializedName("channel")
        private String channel;
        @SerializedName("username")
        private String botName;
        @SerializedName("icon_emoji")
        private String iconEmoji;
        @SerializedName("icon_url")
        private String iconUrl;
        @SerializedName("attachments")
        private List<Attachment> attachments;

        @Builder
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Attachment {
            @SerializedName("title")
            private String title;
            @SerializedName("text")
            private String text;
            @SerializedName("color")
            @Builder.Default
            private String color = "#2222BB";
            @SerializedName("footer")
            private String footer;
        }
    }
}
