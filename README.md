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
  
## 개발팁

* OOP의 원칙. SOLID 원칙을 준수
* TDD를 통한 튼튼하고 편리한 개발

### Redis 관련

#### local 환경

```bash
$ docker run -it --name redis --rm -p 6379:6379 redis
```

#### dev 환경

GCP의 K8s 클러스터에 파드/서비스 형태로 배포 예정.

redis의 경우 ingress를 통해 외부로 노출시킬 수 없기 때문에 우선은 dummy data만 들은 개발환경이기 때문에
NodePort 타입의 서비스로 제공하면 좋겠지만 Node의 IP가 변경되거나 Node가 Priviate subnet에 위치하는 경우
통신이 불가능하기 때문에 Nginx Ingress Controller을 이용해 L4 라우팅 작업을 수행하는 것이 가장 적절할 듯 하다.

#### Publish test 방법

```bash
$ redis-cli # redis-cli 접속
$ publish article '{
  "resource_kind": "comment",
  "event_kind": "create",
  "resource": {
    "id": 1,
    "article": 1,
    "author": "jinsu",
    "content": "test comment created"
  }
}'
```
