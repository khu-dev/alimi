# alimi - 메시지 기반 알림 마이크로서비스

알리미(이하 `alimi`)는 쿠뮤의 `알림`이라는 엔티티와 `푸시` 알림 관련 다양한 기능을 담당하는 서비스입니다. 쿠뮤의 여러 마이크로서비스로부터 `메시지`를 전달 받아 **비동기적으로 작업**하며 **느슨하게 결합(Loesely coupled)**합니다.

* 

## 특징 및 동작 방식

* **본 alimi 마이크로서비스는 중앙 DB를 이용하지 않습니다.** alimi용 데이터베이스인 `khumu_notification_{{환경 이름}}` 데이터베이스를 사용합니다.

  필요한 경우 메시지 브로커에게 다른 마이크로서비스들의 이벤트를 전달받아 데이터를 replicate(복제)해 alimi DB에 반영합니다. 이때 필요한 field만 정의할 수 있기 때문에 중앙 DB와 낮은 의존성을 유지할 수 있습니다.

  또한 불필요한 참조 관계를 없앰으로써 테스트나 데이터 구성을 좀 더 쉽게 할 수 있습니다.

* **메시징 서비스를 이용해 비동기적으로 작업하며 마이크로서비스들과 느슨하게 결합합니다.** 메시징 서비스는 AWS의 `SNS` 와 `SQS`를 조합해 이용 중입니다.

  e.g. 댓글 생성에 대한 알림 작업

  1. 댓글 생성 이벤트를 SNS에게 Publish합니다.
  2. SNS는 자신의 구독자들에게 메시지를 전달합니다. 이때 alimi를 위한 SQS에도 메시지가 전달됩니다.
  3. alimi는 SQS에서 댓글 생성 이벤트 메시지를 가져와 해당 댓글 생성에 대한 알림을 받을 수신자들을 조회합니다.
  4. 조회된 수신자들에게 알림을 생성하고, 추가적으로 푸시 알림도 받는 수신자들에겐 푸시 알림도 Publish합니다.

## 개발 팁

* JPA + MySQL setting 참고 - https://victorydntmd.tistory.com/321
* Spring 배포 시 resources/ 경로의 파일을 찾기 힘든 경우
  * ClassPath를 이용해야 상대 경로를 사용하기가 편한데... Jar로 빌드한 뒤 credential들을 외부에서 추가하려다보니 로컬에선 ClassPath를 사용하고, 배포 시에는 절대 경로를 설정해 절대 경로를 이용하는 방식으로 사용 중입니다.
  * Jar 빌드 시에 존재하는 파일들은 jar로 배포, 실행 시에도 ClassPath로도 올바르게 찾을 수 있습니다.

### (Deprecated) ~~메시지 브로커로 Redis를 이용하기~~

현재는 AWS의 `SNS` + `SQS` 를 메시지 브로커로 해서 마이크로서비스들이 비동기적으로 작업하지만 기존에는 Redis의 Pub/Sub을 이용해 메시지를 전달했습니다. 하지만 사용하면서 메시지 브로커로서의 Redis에 대해 몇 가지 문제점을 느끼게 됐고 현재대로 AWS의 SNS와 SQS를 도입하게 되었습니다.

### 메시지 브로커로서의 Redis 단점

* **Consumer group 같은 개념이 존재하지 않는다.** 

  만약 특정 topic을 여러 프로세스가 구독한다면 구독 중인 모든 프로세스에게 메시지가 Publish되는 단점이 존재했습니다.

* **Subscriber가 동작하지 않는 경우 메시지는 유실된다.**

  메시지 큐 서비스들은 일반적으로 구독자가 동작하지 않으면 메시지 큐에서 보관하고 있는 반면 Redis는 단순히 바로 바로 Push하는 방식이기 때문에 해당 시점에 구독자가 올바르게 동작하지 않으면 메시지가 유실될 수 밖에 없습니다. 사실 Redis는 메시지 "큐"라고 볼 수도 없긴 합니다.

## 테스트 코드 작성 방법 및 정리

  필요한 경우 메시지 브로커에게 다른 마이크로서비스들의 이벤트를 전달받아 데이터를 replicate(복제)해 alimi DB에 반영합니다. 이때 필요한 field만 정의할 수 있기 때문에 중앙 DB와 낮은 의존성을 유지할 수 있습니다.

  또한 불필요한 참조 관계를 없앰으로써 테스트나 데이터 구성을 좀 더 쉽게 할 수 있습니다.

* **메시징 서비스를 이용해 비동기적으로 작업하며 마이크로서비스들과 느슨하게 결합합니다.** 메시징 서비스는 AWS의 `SNS` 와 `SQS`를 조합해 이용 중입니다.

  e.g. 댓글 생성에 대한 알림 작업

  1. 댓글 생성 이벤트를 SNS에게 Publish합니다.
  2. SNS는 자신의 구독자들에게 메시지를 전달합니다. 이때 alimi를 위한 SQS에도 메시지가 전달됩니다.
  3. alimi는 SQS에서 댓글 생성 이벤트 메시지를 가져와 해당 댓글 생성에 대한 알림을 받을 수신자들을 조회합니다.
  4. 조회된 수신자들에게 알림을 생성하고, 추가적으로 푸시 알림도 받는 수신자들에겐 푸시 알림도 Publish합니다.

## 개발 팁

* JPA + MySQL setting 참고 - https://victorydntmd.tistory.com/321
* Spring 배포 시 resources/ 경로의 파일을 찾기 힘든 경우
  * ClassPath를 이용해야 상대 경로를 사용하기가 편한데... Jar로 빌드한 뒤 credential들을 외부에서 추가하려다보니 로컬에선 ClassPath를 사용하고, 배포 시에는 절대 경로를 설정해 절대 경로를 이용하는 방식으로 사용 중입니다.
  * Jar 빌드 시에 존재하는 파일들은 jar로 배포, 실행 시에도 ClassPath로도 올바르게 찾을 수 있습니다.

## 테스트 코드 작성 방법 및 정리

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
