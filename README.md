# alimi - 메시지 기반 알림 마이크로서비스

알리미(이하 `alimi`)는 쿠뮤의 알림 관련 기능을 담당하는 마이크로서비스입니다. 
Pub/Sub을 이용해 이벤트를 json 메시지 형태로 전달 받고, 이에 대한 Notification 데이터 생성 및
푸시 알림 생성을 수행합니다. Java Spring boot와 Redis를 이용합니다.

현재에는 Redis가 아니더라도 다양한 listener를 사용할 수 있게끔 ArticleMessageListener와 같은 것들을 Interface로 정의한 뒤 RedisArticleMessageListener로 구현해놓았다.
만약 Redis를 통한 Pub/Sub이 아니라 SQS를 통한 Polling 방식으로 변경 시 SQSArticleMessageListener만 추가적으로 구현한 뒤 Config에서 변경하도록하면 좋겠다.

## (WIP) Infrastructure

* 각각의 마이크로서비스가 Publisher로서 Redis로 이벤트에 대한 메시지를 Publish
  * e.g. comment 서버에서 대댓글 생성 이벤트에 대한 메시지를 publish
    
* Redis가 해당 message를 Subscriber인 `alimi`에게 전달

* `alimi`는 전달 받은 메시지에 담긴 event 내용을 분석하고 이에 해당하는 알림 수신자를 산출한 뒤 Notification 테이블에 데이터를 생성하고
실제로 Push 서버에 알림 요청을 보냅니다.
  
## 개발팁

* OOP의 원칙. SOLID 원칙을 준수
* TDD를 통한 튼튼하고 편리한 개발
* JPA + MySQL setting 참고 - https://victorydntmd.tistory.com/321
* Spring 배포 방법
  * ClassPath를 이용해야 상대 경로를 사용하기가 편한데... Jar로 배포하면서 제대로 classpath를 지정하는 방법을 몰라서
    지금은 Jar로 빌드 후 다시 해제 해야함..
  * 참고 - https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html
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

## 테스트 코드

* JUnit5를 바탕으로 test를 진행
* @SpringBootTest
  * Junit5에서는 @RunWith를 따로 정의할 필요 없다.
  * JUnit4에서는 @RunWith에 적절한 RunnerClass를 정의해줘야한다.
* @ExtendWith 필요한 기능을 확장적으로 추가하여 테스트를 진행할 수 있다.
  * @ExtendWith(SpringExtension.class) - Spring 앱 서비스를 전체 다 띄우는 것은 아니지만 Bean 주입과 같은
  스프링의 기능들을 사용할 수 있다.
  * @ExtendWith(MockitoExtension.class) - Spring과 유사하게 동작할 수 있도록해주는 Mock을 이용해 테스트를 진행
* @Mock - MockitoExtension을 통해 테스트를 진행할 때 해당 타입의 객체를 생성한다. Bean으로서 이용하는 것은 아니다.
* @MockBean - SpringExtension을 통해 테스트를 진행할 때 해당 타입의 객체를 생성한다. Bean으로서 이용된다.
* @Spy - @Mock은 정말 아무 동작안하는 가짜 타입을 이용하도록하는 반면, Spy는 실제 사용할 객체를 생성한다. (MockitoExtension을 사용할 때)
* @Spy - @Spy를 @Bean으로 이용할 때 (SpringExtension을 사용할 때)
* @InjectMock - @Mock이나 @MockBean, @Spy 등으로 생성한 객체를 이용해 객체를 생성한다.

## 참고
* Test관련 여러 Anootations - https://goddaehee.tistory.com/211
* Mock vs Spy - https://stackoverflow.com/questions/28295625/mockito-spy-vs-mock