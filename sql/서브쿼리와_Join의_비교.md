기존 레거시 코드에 작성된 쿼리 중 5초 이상이 걸리는 쿼리가 존재했다.

대략 구조가 5개의 테이블을 조인(JOIN)하고, 특정 열에 대한 COUNT 연산과 GROUP BY 구문이 들어가있는 형태였다.

```mysql
SELECT A.C1,
       A.C2,
    COUNT(distinct B.C1) + COUNT(distinct C.C1) + COUNT(distinct D.C1)
FROM A
         INNER JOIN B ON A.C1 = B.C2
         INNER JOIN C ON A.C1 = C.C2
         INNER JOIN D ON A.C1 = D.C2
GROUP BY A.C1, A.C2
ORDER BY A.C2
LIMIT 100
```


테이블 구조를 단순화 하면 다음과 같고, 기존 쿼리에서 ORDER BY를 할 때 인덱스가 걸린 컬럼을 이용했지만, 집계함수로 인해 Full Table Scan이 발생하는 상황이었다.
```mysql
CREATE TABLE A
(
    C1 varchar(30) not null primary key,
    C2 datetime    null
);
create index idx_c2 on A (C2);


CREATE TABLE B
(
    C1 bigint      not null primary key,
    C2 varchar(30) not null
);
create index idx_c2 on B (C2);

CREATE TABLE C
(
    C1 bigint      not null primary key,
    C2 varchar(30) not null
);
create index idx_c2 on C (C2);

CREATE TABLE D
(
    C1 bigint      not null primary key,
    C2 varchar(30) not null
);
create index idx_c2 on D (C2);
```

Join으로 모든 테이블의 결과 Set을 합친 후 집계함수를 작성하면 인덱스를 이용하지 못하기 때문에 Join을 하지 않고 서브 쿼리 형태로 COUNT를 세는 방식으로 변경했다.

서브 쿼리 형태로 짜면 다음과 같이 짤 수 있는데, A.C2에 인덱스가 걸려있기 때문에 인덱스 스캔을 한 후 100개만 가져올 수 있게 되었다.

또한 서브 쿼리로 짜도 괜찮다고 판단한 이유는 서브쿼리 내부에서 조건으로 인덱스 스캔을 하기 때문에 큰 부하가 없다고 판단했기 때문이다. 

```mysql
SELECT A.C1,       
       A.C2,
       (SELECT COUNT(1) FROM C WHERE C.C2 = A.C1) +
       (SELECT COUNT(1) FROM D WHERE D.C2 = A.C1) +
       (SELECT COUNT(1) FROM E WHERE E.C2 = A.C2)
FROM A
ORDER BY A.C2
LIMIT 100
```

기존 방식에서는 A 테이블의 Full Table Scan으로 15만개의 데이터를 가져와 해당 쿼리를 실행하는데 5초가 소요되었는데<br>
변경된 방식에서는 인덱스 스캔으로 100개의 데이터를 가져와서 약 200ms 대의 속도를 보여줬다.