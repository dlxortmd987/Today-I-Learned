## @Scheduled

특정 시간이나 일정한 시간 간격으로 코드가 실행되도록 설정하는 어노테이션.

Spring batch에서는 코드를 실행하는 기능은 없으므로 해당 어노테이션으로 배치를 실행시킨다.

## 속성

### fixedDelay

- ms 단위
- 이전 Task 종료 시점으로부터 정의된 시간만큼 지난 후 Task 실행

### fixedRate

- ms 단위
- 이전 Task의 시작 시점으로부터 정의된 시간만큼 지난 후 Task 실행
- 이전 Task의 작업 수행 시간과 상관없이 일정 주기마다 메소드 호출

### initailDelay

- ms 단위
- 초기 지연시간 설정

### cron

- Cron 표현식을 통해 작업 시간 설정
- Cron 표현식<br>
  `@Scheduled(cron = "* * * * * *")`
- 앞에서부터 초, 분, 시간, 일, 월, 요일 설정
    - `*` : 모든 조건(매시, 매일, 매주처럼 사용)을 의미
    - `?` : 설정 값 없음 (날짜와 요일에서만 사용 가능)
    - `-` : 범위를 지정할 때
    - `,` : 여러 값을 지정할 때
    - `/` : 증분값, 즉 초기값과 증가치 설정에 사용
    - `L` : 마지막 - 지정할 수 있는 범위의 마지막 값 설정 시 사용 (날짜와 요일에서만 사용 가능)
    - `W` : 가장 가까운 평일(weekday)을 설정할 때
    - `#` : N번째 주 특정 요일을 설정할 때 (-요일에서만 사용 가능)
- 범위
    - 초: 0 ~ 59
    - 분: 0 ~ 59
    - 시간: 0 ~ 23
    - 일: 1 ~ 31
    - 월: 1 ~ 12
    - 요일: 0 ~ 6 (0: 일, 1: 월, 2:화, 3:수, 4:목, 5:금, 6:토)

### zone

- time zone 설정

## 예시

```java
public class Main {
	// 매일 오후 18시에 실행
	@Scheduled(cron = "0 0 18 * * *")
	public void run() {
		System.out.println("Hello CoCo World!");
	}

	// 매달 10일,20일 14시에 실행
	@Scheduled(cron = "0 0 14 10,20 * ?")
	public void run() {
		System.out.println("Hello CoCo World!");
	}

	// 매달 마지막날 22시에 실행
	@Scheduled(cron = "0 0 22 L * ?")
	public void run() {
		System.out.println("Hello CoCo World!");
	}

	// 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
	@Scheduled(cron = "0 0 0/1 * * *")
	public void run() {
		System.out.println("Hello CoCo World!");
	}

	// 매일 9시00분-9시55분, 18시00분-18시55분 사이에 5분 간격으로 실행
	@Scheduled(cron = "0 0/5 9,18 * * *")
	public void run() {
		System.out.println("Hello CoCo World!");
	}

	// 매일 9시00분-18시55분 사이에 5분 간격으로 실행
	@Scheduled(cron = "0 0/5 9-18 * * *")
	public void run() {
		System.out.println("Hello CoCo World!");
	}

	// 매달 1일 10시30분에 실행
	@Scheduled(cron = "0 30 10 1 * *")
	public void run() {
		System.out.println("Hello CoCo World!");
	}

	// 매년 3월내 월-금 10시30분에 실행
	@Scheduled(cron = "0 30 10 ? 3 1-5")
	public void run() {
		System.out.println("Hello CoCo World!");
	}

	// 매달 마지막 토요일 10시30분에 실행
	@Scheduled(cron = "0 30 10 ? * 6L")
	public void run() {
		System.out.println("Hello CoCo World!");
	}
}

```