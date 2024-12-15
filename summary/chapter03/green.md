# 3장. 템플릿

## 💡 핵심 개념
- 템플릿은 변하지 않는 코드를 재사용하고 변하는 부분만 분리하는 방법
- JDBC의 try/catch/finally 같은 반복되는 코드를 효과적으로 분리할 수 있음
- 전략 패턴을 거쳐 템플릿/콜백 패턴으로 발전하며 점진적으로 개선됨

## 학습 배경
현재 프로젝트에서 마주친 문제:
- JDBC 사용 시 Connection, PreparedStatement 등의 리소스 반환 코드 중복
- try/catch/finally 블록의 반복
- SQL 실행 전후의 반복되는 코드들

이 개념으로 해결할 수 있는 점:
- 리소스 반환을 보장하는 안전한 코드 구조
- 중복 코드 제거를 통한 유지보수성 향상
- 변경이 필요한 부분만 효과적으로 교체 가능

## 주요 개념

### 1. 초기 JDBC의 문제점
Why: 예외처리와 리소스 반환 코드의 중복
What: Connection과 PreparedStatement를 안전하게 처리하는 방법 필요
How: try/catch/finally를 사용한 기본 구조

```java
public void deleteAll() throws SQLException {
    Connection c = null;
    PreparedStatement ps = null;
    try {
        c = dataSource.getConnection();
        ps = c.prepareStatement("delete from users");
        ps.executeUpdate();
    } catch (SQLException e) {
        throw e;
    } finally {
        if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
        if (c != null) { try { c.close(); } catch (SQLException e) {} }
    }
}
```

### 2. 템플릿 메소드 패턴
Why: 변하는 부분과 변하지 않는 부분을 분리
What: 추상 메소드를 통한 변하는 부분 분리
How: 상속을 통한 기능 확장

```java
public abstract class UserDao {
    protected abstract PreparedStatement makeStatement(Connection c);
}

public class UserDaoDeleteAll extends UserDao {
    protected PreparedStatement makeStatement(Connection c) {
        return c.prepareStatement("delete from users");
    }
}
```

### 3. 전략 패턴
Why: 상속 대신 위임을 통한 유연한 확장
What: 인터페이스를 통한 전략 분리
How: Context와 Strategy의 분리

```java
public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}

public void jdbcContextWithStatementStrategy(StatementStrategy stmt) {
    try (Connection c = dataSource.getConnection();
         PreparedStatement ps = stmt.makePreparedStatement(c)) {
        ps.executeUpdate();
    }
}
```

### 4. 템플릿/콜백 패턴
Why: 전략 패턴을 더 유연하게 사용
What: 익명 클래스를 활용한 콜백 구현
How: 람다식을 활용한 간단한 구현

```java
public void add(User user) {
    jdbcContextWithStatementStrategy(c -> {
        PreparedStatement ps = c.prepareStatement(
            "insert into users(id, name, password) values(?, ?, ?)"
        );
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        return ps;
    });
}
```

### 5. JdbcTemplate 활용
Why: 스프링이 제공하는 템플릿 활용
What: 다양한 데이터 접근 기능 제공
How: 미리 만들어진 템플릿 활용

```java
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper = (rs, i) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
    };

    public void add(User user) {
        jdbcTemplate.update(
            "insert into users(id, name, password) values (?,?,?)",
            user.getId(), user.getName(), user.getPassword()
        );
    }
}
```

## 실습 리뷰
주요 구현 포인트:
- 리소스 반환 보장을 위한 try-with-resources 활용
- 콜백 인터페이스 설계 시 단일 메소드 원칙 적용
- JdbcTemplate을 통한 코드 간소화

발생한 이슈:
- 익명 클래스 내부에서 외부 변수 접근 시 final 제약
- SQL 문장을 코드 내부에 하드코딩하는 문제
- 반복되는 RowMapper 로직

해결 방법:
- 람다식을 활용한 콜백 구현 간소화
- SQL을 외부 리소스로 분리하는 방안 검토
- 공통 RowMapper를 인스턴스 변수로 분리

## 질문과 생각
❓ 템플릿/콜백 패턴과 옵저버 패턴의 차이점은?
- 실행 시점:
    - 템플릿/콜백: 템플릿 메서드 실행 중에 콜백이 한 번 호출됨
    - 옵저버: 이벤트 발생할 때마다 여러 번 호출될 수 있음
- 관계:
    - 템플릿/콜백: 1:1 관계로, 하나의 템플릿이 하나의 콜백을 실행
    - 옵저버: 1:N 관계로, 하나의 주체가 여러 옵저버에게 알림
- 목적:
    - 템플릿/콜백: 반복되는 작업 흐름 중 일부분만 변경하여 재사용
    - 옵저버: 상태 변경을 여러 객체에게 통지하여 자동으로 처리

📚 스프링의 다른 Template 클래스들 학습 필요
💡 파일 처리나 네트워크 작업에도 템플릿/콜백 패턴 적용 가능할 듯

1. 파일 처리 예시:
```java
public interface FileCallback<T> {
    T doWithFile(BufferedReader br) throws IOException;
}

public class FileTemplate {
    public <T> T fileReadTemplate(String path, FileCallback<T> callback) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return callback.doWithFile(br);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

// 사용 예시
FileTemplate template = new FileTemplate();

// 파일의 모든 라인 합계 구하기
Integer sum = template.fileReadTemplate("numbers.txt", br -> {
    int total = 0;
    String line;
    while ((line = br.readLine()) != null) {
        total += Integer.parseInt(line);
    }
    return total;
});
```

2. 네트워크 작업 예시:
```java
public interface HttpCallback<T> {
    T doWithConnection(HttpURLConnection conn) throws IOException;
}

public class HttpTemplate {
    public <T> T httpRequestTemplate(String url, HttpCallback<T> callback) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            return callback.doWithConnection(conn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}

// 사용 예시
HttpTemplate template = new HttpTemplate();

// GET 요청으로 데이터 가져오기
String response = template.httpRequestTemplate("https://api.example.com/data", conn -> {
    conn.setRequestMethod("GET");
    try (BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream()))) {
        return br.lines().collect(Collectors.joining("\n"));
    }
});
```

이러한 템플릿/콜백 패턴의 장점:
- 리소스 관리(열기/닫기)를 템플릿에서 안전하게 처리
- 실제 작업 로직만 콜백으로 분리하여 재사용성 향상
- 예외 처리를 템플릿에서 일관되게 처리 가능



