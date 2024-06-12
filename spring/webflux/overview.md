## concept
기존 Servlet 기반의 Spring MVC 와 달리, Spring Webflux 는 논 블로킹으로 동작하고, Reactive Stream back pressure 를 지원하고, Netty, Undertow, Servlet containers 와 같은 서버에서 돌아간다.

모르는 단어가 많긴 한데 천천히 알아가보자.

## "Reactive"
`non-blocking`은 "reactive"하다.

그 이유는 함수(operation)가 완료된 거나 데이터가 이용 가능하다는 것에 반응하는 모델이기 때문이다.
> "reactive"는 변화에 반응하는 것을 기반으로 하는 프로그래밍 모델이다.

또한 `non-blocking` 방식에서는 back pressure를 신경써야 한다.

`blocking` 방식의 경우에는 자연스럽게 caller(호출자)가 기다리는 구조여서 back pressure 가 달성되지만, `non-blocking` 방식에서는 producer(생산자)가 목표치를 초과하지 않도록 이벤트의 비율을 조절해야 한다. (한번에 통제를 벗어나는 생산을 신경써야 한다는 의미)

Reactive Streams 는 back pressure 를 탑재한 비동기 컴포넌트 끼리의 상호작용을 다루는 spec 이다.

Reactive Streams 의 주요 목적은 구독자(subscriber)가 얼마나 빨리 또는 느리게 생산자(publisher)가 데이터를 생산하는 것을 제어할 수 있도록 하는 것이다.

쉽게 말해서 구독자가 생산자의 생산 속도를 원하는 대로 잘 제어를 할 수 있도록 하는 것이 목적이라는 의미이다.

> 만약 publisher가 느리게 생산하는 것을 하지 못할 때는 어떻게 할까?<br>
> - buffer, drop, fail 중 하나의 전략을 선택해야 한다.

## 참고
- https://docs.spring.io/spring-framework/reference/web/webflux/new-framework.html

## keywords
- Reactive Stream
- 배압(back pressure)
- Netty, Undertow