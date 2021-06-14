//package com.khumu.alimi.data;
//
///**
// * {
// *   "Type" : "Notification",
// *   "MessageId" : "ff84e18f-e979-5b44-b60c-b8ce61c6be07",
// *   "TopicArn" : "arn:aws:sns:ap-northeast-2:312979016576:khumu-messages",
// *   "Message" : "{\"id\": 96, \"url\": \"http://localhost:8000/api/articles/96\", \"board_name\": \"free\", \"board_display_name\": \"\\uc790\\uc720\\uac8c\\uc2dc\\ud310\", \"title\": \"hello, article\", \"author\": {\"username\": \"jinsu\", \"nickname\": \"\\ucc21\\uc218\", \"state\": \"unverified\"}, \"is_author\": true, \"kind\": \"named\", \"content\": \"hello, world\", \"tags\": [{\"name\": \"\\uac1c\\ubc1c\\ub108\\ubb34\\uc7ac\\ubc0c\\uc5b4\", \"followed\": false}, {\"name\": \"\\ud765\\ud574\\ub77c\\ucfe0\\ubba4\", \"followed\": true}], \"images\": [], \"comment_count\": 0, \"created_at\": \"\\uc9c0\\uae08\", \"liked\": false, \"like_article_count\": 0, \"bookmarked\": false, \"bookmark_article_count\": 0}",
// *   "Timestamp" : "2021-06-14T16:40:31.312Z",
// *   "SignatureVersion" : "1",
// *   "Signature" : "iEXALzJiEfE9j4GeW3zn45YIN4WwKQ+1Kq0TkUKiOfiM0gpwDWE1V2B89hU+UiboIvW4ibfoK566Ux5KpAodoofmsksUPKN1nLrsYWV7j/qID+gbK0eidbC75HQh3Ux/L6EpCpdEm64rC3ezy3YmmNDi5sU83EitVOPY/UBUjn1JG1A1cAEauciVUMLejT+bNE3NG7T9dC5RDibAauTsyy4qtnmiVhiDoeoShTFxCU58euXUYRgynzpDMb/M3G4fngjcxZIg9zMboU+AniYBx30iSwHOxXbImGPRFVVZZrIUA58GMpE6oCnZAaIbUzv/3ZI779EP/qV+Y0aDgn3Kqg==",
// *   "SigningCertURL" : "https://sns.ap-northeast-2.amazonaws.com/SimpleNotificationService-010a507c1833636cd94bdb98bd93083a.pem",
// *   "UnsubscribeURL" : "https://sns.ap-northeast-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:ap-northeast-2:312979016576:khumu-messages:ead8f6d1-bbf9-46e3-96a4-b1885adfd6d3",
// *   "MessageAttributes" : {
// *     "event_kind" : {"Type":"String","Value":"create"},
// *     "resource_kind" : {"Type":"String","Value":"article"}
// *   }
// * }
// */
//public class SnsMessage {
//    String type;
//    String messageId;
//    String topicArn;
//    String message;
//    MessageAttributes messageAttributes;
//
//    public static class MessageAttributes {
//        String eventKind
//    }
//
//    public static class MessageAttribute {
//        String type;
//        String value;
//    }
//
//}
