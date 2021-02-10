# alimi - 메시지 기반 알림 마이크로서비스

알리미(이하 `alimi`)는 쿠뮤의 알림 관련 기능을 담당하는 마이크로서비스입니다. 
Pub/Sub을 이용해 이벤트를 json 메시지 형태로 전달 받고, 이에 대한 Notification 데이터 생성 및
푸시 알림 생성을 수행합니다. Java Spring boot와 Redis를 이용합니다.

## (WIP) Infrastructure

* 각각의 마이크로서비스가 Publisher로서 Redis로 이벤트에 대한 메시지를 Publish
  * e.g. comment 서버에서 대댓글 생성 이벤트에 대한 메시지를 publish
    
* Redis가 해당 message를 Subscriber인 `alimi`에게 전달

* `alimi`는 전달 받은 메시지에 담긴 event 내용을 분석하고 이에 해당하는 알림 수신자를 산출한 뒤 Notification 테이블에 데이터를 생성하고
실제로 Push 서버에 알림 요청을 보냅니다.
  
## 개발 방식

* OOP의 원칙. SOLID 원칙을 준수
* TDD를 통한 튼튼하고 편리한 개발


