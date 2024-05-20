기존 레거시 코드에 작성된 쿼리 중 5초 이상이 걸리는 쿼리가 존재했다.

대략 구조가 5개의 테이블을 조인(JOIN)하고, 특정 열에 대한 COUNT 연산과 GROUP BY 구문이 들어가있는 형태였다.
쿼리를 설명하면, A의 PK와 1대 다로 연관된 C, D, E 테이블이 있고 PK와 연관된 C, D, E 테이블의 row 수를 세는 조회 쿼리이다.

```mysql
SELECT A.C1
    # ...중략 
    COUNT(C.C1) + COUNT(D.C1) + COUNT(E.C1)
FROM A
INNER JOIN B
INNER JOIN C ON A.C1 = C.C2 
INNER JOIN D ON A.C1 = D.C2
INNER JOIN E ON A.C1 = E.C2
GROUP BY A.C1, A.C2, A.C2, B.C2,  A.C2
LIMIT 100
```

운영 환경에서 대략 row 수를 나열하면 다음과 같았다.

| 테이블 | row 수 |
|-----|-------|
| A   | 10만개  |
| B   | 1000개 |
| C   | 9만개   |
| D   | 9만개   |
| E   | 9만개   |

따라서 조인 조건을 고려한 조인 연산의 결과물이 대략 8억개 정도 나오는 상황이었다.

JOIN 연산 결과 자체가 많이 나오기도 하고, GROUP BY와 COUNT 연산까지 수행하기 떄문에 5초가 나오는 것으로 예상했다.

해당 쿼리는 굳이 JOIN으로 계산하는 것보다 서브쿼리로 작성하면 더 효율적이지 않을까 생각했다.

그 이유는, A 테이블의 PK와 연관된 C, D, E의 row 수가 10개 미만인 현재 상황에서, JOIN 으로 조회하는 것 대비 서브쿼리는 전체 row 수를 줄일 수 있게 때문이다.

실행계획

해당 쿼리를 결국 다음과 같이 바꾸고 쿼리 실행 속도를 200ms 대로 낮출 수 있었다...!

```mysql
SELECT A.C1
    # ...중략 
    (SELECT COUNT(1) FROM C WHERE C.C2 = A.C1) + 
       (SELECT COUNT(1) FROM D WHERE D.C2 = A.C1) + 
       (SELECT COUNT(1) FROM E WHERE E.C2 = A.C2)
FROM A
INNER JOIN B
LIMIT 100
```