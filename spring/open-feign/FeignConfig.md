## FeignConfig 설정 시 주의할 점

회사 코드에서 전역적으로 사용하는 FeignConfig가 있는 상황에서 특정 Client에서만 적용되는 Config를 추가해야 하는 상황이 있었다.

```java
// 기존에 전역적으로 관리되던 FeignConfig
@Configuration
public class DefaultFeignConfig {
	
	public ErrorDecoder errorDecoder() {
		return DefaultErrorDecoder();
	}
}

// 새로 추가된 Config
@Configuration // 문제의 어노테이션
public class CustomFeignConfig {

	public ErrorDecoder errorDecoder() {
		return CustomErrorDecoder();
	}
}
```

습관적으로 FeignConfig 클래스에 `@Configuration`을 붙여주었는데 다른 Client에서 Config에 등록한 빈들이 정상적으로 동작하지 않는 문제가 있었다.

FeignConfig에 `@Configuration`을 붙여주게 되면 해당 Config 클래스를 기본으로 사용한다는 의미이다. 따라서 여러 군데의 Config 파일에 `@Configuration`을 붙이게 되면 충돌로 인해서 정상적으로 동작하지 않게 된다.

따라서 특정 Client에만 Config를 커스텀하고 싶다면 @FeignClient의 속성 중 configuration에 명시하면 된다.
단, `@Configuration`은 붙이지 않은 상태로! 

```java
public class CustomFeignConfig {

	public ErrorDecoder errorDecoder() {
		return CustomErrorDecoder();
	}
}

@FeignClient(name = "custom", configuration = CustomFeignConfig.class)
public interface Client {
	
}
```