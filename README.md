# Rule

## 로테이션

- `bell` -> `sudong` -> `green`
- 위 순서대로 매주 리더가 바뀝니다.

## 학습

- 매주 하나의 챕터씩 읽습니다.
- 학습 정리는 끝나는대로 `Pull Request` 통해 공유합니다.
    - `/summary/chapter00` 형식의 디렉토리에 `{닉네임}.md` 파일을 만들어 저장. (ex - bell.md)

## 질문

- 질문은 Issuse에 작성합니다.
- 질문은 매주 **일요일 23:59 마감**입니다.
- **리더는** 질문에 대해 **화요일 23:59까지 답변**을 달아주세요.

## 모임

- 매주 수요일 오전 9시부터 10시까지 진행합니다.
- 그 주차의 리더가 서기를 담당합니다. 모임 때 나온 얘기를 wiki에 작성합니다.
- 모임에선 이슈에 올라온 질문을 주제로 토론하거나, 얘기하고 싶은 주제로 자유 토론을 진행합니다.

<br/><br/>

# 개발 환경 세팅

- 각각의 닉네임으로 실습 모듈이 구성되어 있습니다.

## MySQL Setting

```shell
$ docker pull mysql:8.0 # 생략 가능
$ docker run -p 3307:3306 -e MYSQL_ROOT_PASSWORD=qwer1234 -d mysql:8.0
$ mysql -h 127.0.0.1 -P 3307 -u root -p
```

- 자바 코드 설정할 때 아래와 같이 설정.

```java
Class.forName("com.mysql.cj.jdbc.Driver");
Connection c = DriverManager.getConnection(
    "jdbc:mysql://localhost:3307/spring", "root", "qwer1234");
```
