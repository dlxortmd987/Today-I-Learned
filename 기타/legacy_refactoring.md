## 레거시 시스템 리팩토링

### 기술 스택 선정
- SpringBoot 3.2.6
- JVM 21
- Spring Webflux
- JPA, QueryDsl
- grpc

### 테스트 코드 작성
- 인수 테스트 작성
- 단위 테스트 작성

### API 별로 이동
- 사용하는 API 확인
  - Datadog 참조

## 이동 방법

### 하나로 합치기
- 기존에 repository 들이 화면 단위로 나눠져 있었는데 이것들을 하나로 합친다.
  - backend 코드들인데 화면 단위로 repository 가 나뉘어져 있다...

### 도메인 단위로 repo 쪼개기
- 기존에 화면 단위로 나누어져 있어 도메인 단위로 다시 쪼개 각 repo 에 나누기

## 참고
- https://dealicious-inc.github.io/2021/02/15/abara.html
- https://www.youtube.com/watch?v=LSqbXorkyfQ&t=1417s