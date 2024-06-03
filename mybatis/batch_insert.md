## mybatis에서 batch insert를 하는 법

mybatis에서 batch insert를 하는 방법에는 크게 2가지 방법이 있다.

1. foreach 이용
2. `Executor.BATCH`로 `SqlSession`을 열어 쿼리 실행

## foreach 이용

쿼리 자체를 다음과 같이 작성하면 된다.

```java

@Mapper
public interface ComputerMapper {
	void insertBatch(List<Computer> computers);
}

public class Computer {
	private Long id;
	private String name;
}
```

```xml

<insert id="insertBatch" parameterType="com.example.mybatisbatch.domain.Computer">
    insert into Computers (id, name)
    values
    <foreach item="computer" collection="list" separator=",">
        (
        #{computer.id},
        #{computer.name}
        )
    </foreach>
</insert>
```

별 다른 설정 없이 위와 같이 작성하면 배치 insert를 할 수 있다.

## Executor.BATCH

아래와 같이 sqlSession을 생성하거나 bean으로 주입하면 된다.

```java
public class BatchService {

	@Transactional
	void batchInsert() {
		List<Computer> computers = getManyComputers();

		try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
			ComputerMapper computerMapper = sqlSession.getMapper(ComputerMapper.class);
			for (Computer computer : computers) {
				computerMapper.insert(computer);
			}
			sqlSession.commit();
		}
	}
}
```

추가로 mysql의 경우 다음과 같이 설정을 하면 더 빠르게 batch insert가 된다.([참고](https://techblog.woowahan.com/2695/))
`rewriteBatchedStatements=true` 꼭 추가!!

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/practice?rewriteBatchedStatements=true
```

## 성능 비교

성능은 다음과 같다.
(로컬에서 테스트한 결과이다.)

|             | for      | foreach | Executor.BATCH |
|-------------|----------|---------|----------------|
| 100개 삽입     | 147 ms   | 67 ms   | 12 ms          |
| 1_000개 삽입   | 990 ms   | 113 ms  | 45 ms          |
| 10_000개 삽입  | 7760 ms  | 374 ms  | 162 ms         |
| 100_000개 삽입 | 81326 ms | 2426 ms | 1348 ms        |
> `for`는 for 문으로 insert 쿼리를 실행한 것이다.

삽입하는 요소가 커질 수록 성능 차이가 매우 큰 것을 볼 수 있다.

`for`와 다른 것들의 차이는 이해가 가는데 `foreach`와 `Executor.BATCH`는 왜 차이가 나는 것일까?

## `foreach` vs `Executor.BATCH`

`rewriteBatchedStatements=true` 해당 옵션을 사용하고 실제 실행되는 쿼리를 보면 똑같은 것을 알 수 있다.

그렇다면 왜 쿼리의 결과가 차이나는 것일까?

두 방식이 차이가 나는 이유는 jdbc 드라이버의 최적화를 받는지의 여부이다.

`Executor.BATCH` 방식은 내부적으로 jdbc의 batch operation을 사용하기 때문에 jdbc 드라이버의 최적화를 받는다.

이러한 차이로 성능 차이가 나는 것이다.

추가로, 두 방식에 대해 더 살펴본 것을 말하자면,

`foreach` 방식은 배치 insert 라기 보다는 단순히 쿼리에 요소를 추가하는 것이다.([참고](https://stackoverflow.com/questions/32649759/using-foreach-to-do-batch-insert-with-mybatis/40608353))

또한 넣는 요소가 커지면 고려해야할 점이 한번에 몇 개의 요소를 집어넣느냐인데, 한번에 너무 많은 요소를 집어넣을 경우 메모리 부족 문제가 발생할 수 있기 때문이다.(두 방법 모두)

`INSERT INTO TABLE VALEUS(?,?), (?, ?), ...` 와 같은 식으로 요소를 추가하기 위해 요소들을 메모리에 올려놓기 때문이다.

`Executor.BATCH` 방식은 적절한 배치 사이즈에 맞춰 `sqlSession.flushStatements()`을 해줘야 한다.

위의 이유로 메모리 부족 문제가 발생할 수 있기 때문이다.