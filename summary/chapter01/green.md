# 1장 

## 목차
# 1장

## 목차
1. [오브젝트와 의존관계](#1-오브젝트와-의존관계)
2. [데이터베이스 연결](#2-데이터베이스-연결)
3. [한심한 DAO](#3-한심한-dao)
4. [DAO의 분리](#4-dao의-분리)
    - 4.1 [관심사의 분리](#41-관심사의-분리)
5. [DAO의 확장](#5-dao의-확장)
    - 5.1 [클래스의 분리](#51-클래스의-분리)
    - 5.2 [인터페이스의 도입](#52-인터페이스의-도입)
6. [제어의 역전(IoC)](#6-제어의-역전ioc)
7. [스프링의 IoC](#7-스프링의-ioc)
8. [싱글톤 레지스트리와 오브젝트 스코프](#8-싱글톤-레지스트리와-오브젝트-스코프)
9. [의존관계 주입(DI)](#9-의존관계-주입di)
10. [XML을 이용한 설정](#10-xml을-이용한-설정)

---

## 1. 오브젝트와 의존관계

- 스프링은 객체 지향 설계과 구현에

---



## 오브젝트와 의존관계

- 스프링은 객체 지향 설계과 구현에 관해 특정 모델과 기법을 억지로 강요하지 않는다. 하지만 어떻게 효과적으로 구현하고, 사용하고, 개선할 것인지에 대해 명쾌한 기준을 마련해준다.
- 동시에 객체지향 기술과 설계, 구현에 관한 실용적인 전략과 베스트 프랙티스를 제공한다.

## 데이터베이스 연결

```bash
docker run --name mysql-container -p 3307:3306 -e MYSQL_ROOT_PASSWORD=qwer1234 -d mysql/mysql-server:8.0
```

```sql
SELECT user, host FROM mysql.user;

-- root 사용자에 대해 모든 호스트 접속 허용
UPDATE mysql.user SET host = '%' WHERE user = 'root' AND host = 'localhost';

-- 권한 변경 적용
FLUSH PRIVILEGES;
```

```bash
docker exec -it mysql-container mysql -u root -p
```

## 한심한 DAO

아래는 한심한 Dao 코드!

```sql
 public void add(User user) throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection c = DriverManager.getConnection(
        "jdbc:mysql://localhost:3307/spring", "root", "qwer1234");

    PreparedStatement ps = c.prepareStatement(
        "insert into users(id, name, password) values(?,?,?)");
    ps.setString(1, user.getId());
    ps.setString(2, user.getName());
    ps.setString(3, user.getPassword());

    ps.executeUpdate();

    ps.close();
    c.close();
  }

  public User get(String id) throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection c = DriverManager.getConnection(
        "jdbc:mysql://localhost:3307/spring", "root", "qwer1234");

    PreparedStatement ps = c.prepareStatement(
        "select * from users where id = ?");
    ps.setString(1, id);

    ResultSet rs = ps.executeQuery();
    rs.next();
    User user = new User();
    user.setId(rs.getString("id"));
    user.setName(rs.getString("name"));
    user.setPassword(rs.getString("password"));

    rs.close();
    ps.close();
    c.close();

    return user;
  }
```

### DAO의 분리

### 1.2.1 관심사의 분리

변화를 대비할 때 가장 좋은 대책은 변화의 폭을 최소화하는 것이다.

어떻게 하냐? 분리와 확장을 고려하여 설계를 한다.

관심이 같은 것끼리는 모으고, 관심이 다른 것은 분리함으로써 관심에 효과적으로 집중할 수 있게 만들어준다.

```java
//데이터베이스 연결에만 관심을 갖도록 함
private Connection getConnection() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection c = DriverManager.getConnection(
            "jdbc:mysql://localhost:3307/spring", "root", "qwer1234");
    return c;
}

```

**1.2.1 DB 커넥션 만들기의 독립**

연결을 추상화

```java

abstract Connection getConnection() throws ClassNotFoundException, SQLException;
```

연결을 추상화하면 새로운 DB 연결 방법을 적용할 때는 UserDao를 상속하여 확장하기만 하면 된다.

```java
public class NUserDao extends UserDao {
    @Override
    Connection getConnection() throws ClassNotFoundException, SQLException {
        return null;
    }
}
```

슈퍼클래스에 기본적인 **로직의 흐름**(커넥션 가져오기, SQL 실행, 반환)을 만들고, 기능의 일부를 추상 메소드나 오버라이딩 가능한 protected 메소드등으로 만든 뒤 서브 클래스에서 메소드에 맞게 구현해서 사용하도록 하는 기법을 **템플릿 메소드 패턴**이라고 한다.

그리고 UserDao의 서브클래스의 getConnection() 메소드는 어떤 Connection 클래스의 오브젝트를 어떻게 생성할 것인지 결정하는 방법으로도 볼 수 있다.

즉, 서브 클래스에서 구체적인 오브젝트 생성 방법을 결정하게 하는 것을 **팩토리 메소드 패턴**이라고 한다.

이 방법은 상속을 사용했기 때문에 아래와 같은 단점이 있다.

- UserDao가 다른 클래스를 상속하고 있었다면 상속을 쓸 수 없다
- 상속관계를 통해 두 클래스는 강결합 상태가 된다.

## 1.3 DAO의 확장

### 1.3.1 클래스의 분리

```java

public class UserDao {

private SimpleConnectionMaker simpleConnectionMaker;

public UserDao() {
    this.simpleConnectionMaker = new SimpleConnectionMaker();
}

public void add(User user) throws ClassNotFoundException, SQLException {
    Connection c = simpleConnectionMaker.makeNewConnection();

    PreparedStatement ps = c.prepareStatement(
            "insert into users(id, name, password) values(?,?,?)");
    ps.setString(1, user.getId());
    ps.setString(2, user.getName());
    ps.setString(3, user.getPassword());

    ps.executeUpdate();

    ps.close();
    c.close();
}
```

위 코드는 문제점이 있다

UserDao의 코드가 SimpleConnectionMaker라는 클래스에 종속 되어 있기 때문에 상속을 사용했을 때 처럼 UserDao 코드의 수정없이 DB 커넥션 생성 기능을 변경할 수 없다

**1.3.2 인터페이스의 도입**

```java
public interface ConnectionMaker {
    Connection makeConnection() throws ClassNotFoundException, SQLException;
}
```

### 1.3.3 관계설정 책임의 분리

UserDao를 사용하는 곳에서 어떤 커넥션을 사용할 것인지를 결정해서 UserDao에게 전달하는 방식을 사용하면 UserDao는 어떤 커넥션을 사용할 것인지에 대해 관심을 갖지 않아도 된다.

```java
public class UserDao {

  private ConnectionMaker connectionMaker;

  public UserDao(ConnectionMaker connectionMaker) {
      this.connectionMaker = connectionMaker;
  }
```

### 1.3.4 원칙과 패턴

1. 클래스나 모듈은 확장에는 열려 있어야 하고 변경에는 닫혀있어야 한다
2. 높은 응집도와 낮은 결합도
    - 높은 응집도 : 하나의 모듈, 클래스가 하나의 책임 또는 관심사에만 집중
        - 변경이 있어날 때 모듈의 많은 부분이 함께 바뀜
    - 낮은 결합도 : 책임과 관심사가 다른 오브젝트 또는 모듈과 느슨하게 연결
        - 결합도란 하나의 오브젝트가 변경이 일어날 때 관계를 맺고 있는 다른 오브젝트에게 변화를 요구하는 정도
3. 전략 패턴
    - 자신의 기능 맥락에서 필요에 따라 변경이 필요한 알고리즘을 인터페이스로 만들어 외부로 분리하고, 이른 구현한 구체적인 알고리즘 클래스를 필요에 따라 바꿔서 사용할 수 있게하는 디자인 패턴

## 1.4 제어의 역전(IoC)

### 1.4.1 오브젝트 팩토리

팩토리 : 객체의 생성 방법을 결정하고 오브젝트를 반환한다.

```java
public class DaoFactory {
    public UserDao userDao() {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        return new UserDao(connectionMaker);
    }
}
```

팩토리는 1)애플리케이션의 컴포넌트 역할을 하는 오브젝트와 2) 애플리케이션의 구조를 결정하는 오브젝트를 분리한 것이 가장 큰 장점이다

### 1.4.2 오브젝트 팩토리의 활용

```java
public class DaoFactory {
  public UserDao userDao() {
      return new UserDao(connectionMaker());
  }

  public UserDao accountDao() {
      return new UserDao(connectionMaker());
  }

  public UserDao messageDao() {
      return new UserDao(connectionMaker());
  }

  private DConnectionMaker connectionMaker() {
      return new DConnectionMaker();
  }
}
```

### 1.4.3 제어권의 이전을 통한 제어관계 역전

- 라이브러리를 사용하는 애플리케이션 코드는 애플리케이션 흐름을 직접 제어한다
- 프레임워크는 애플리케이션 코드가 프레임워크에 의해 사용된다
- 관심을 분리하고 책임을 나누고 유연하게 확장 가능한 구조를 만들기 위해 DaoFactory를 적용는 과정이 IoC를 적용하는 작업이다

## 1.5 스프링의 IoC

- 빈
    1. 스프링이 제어권을 가지고 직접 만들과 관계를 부여하는 오브젝트
    2. 스프링 컨테이너가 생성과 관계설정, 사용 등을 제어해주는 제어의 역전이 적용된 오브젝트
- 빈 팩토리
    1. 빈의 생성과 관계설정 같은 제어를 담당하는 IoC 오브젝트
    2. 좀 더 확장하면 **애플리케이션 컨텍스트**

- @Configuration
    - 스프링이 빈 팩토리를 위한 오브젝트 설정을 담당하는 클래스라고 인식시킨다

- @Bean
    - 오브젝트 생성을 담당하는 IoC용 메소드 표시

```java
@Configuration
public class DaoFactory {
  @Bean
  public UserDao userDao() {
      return new UserDao(connectionMaker());
  }

  @Bean
  private DConnectionMaker connectionMaker() {
      return new DConnectionMaker();
  }
}

```

위 코드는 DaoFactroy를 설정정보로 사용하는 애플리케이션 컨텍스트

여러가지 ApplicationContext 구현 클래스 중 `@Configuration` 이 붙은 자바 코드를 설정정보로 사용하려면 AnnotationConfigApplicationContect를 이용한다.



```java
public static void main(String[] args) throws SQLException, ClassNotFoundException {
    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    UserDao dao = context.getBean("userDao", UserDao.class);
    //userDao 라는 이름의 빈을 조회한다.

```

getBean 메소드는 기본적으로 Object 타입으로 리턴하기 때문에 리턴 타입으로 캐스팅을 해줘야하는데, 두 번째 파라미터로 리턴 타입을 전달하면 캐스팅 코드를 사용하지 않아도 된다.

### 1.5.2 애플리케이션 컨텍스트의 동작방식

1. 애플리케이션 컨텍스트는 DaoFactory 클래스를 설정정보로 등록
2. @Bean이 붙은 메소드의 이름을 가져와 빈 목록 생성
3. 애플리케이션 컨텍스트의 getBean() 메소드로 빈 이름 조회
4. 빈 생성 메소드를 호출
5. 빈 생성 후 클라이언트에게 전달

장점

- 클라이언트는 구체적인 팩토리 클래스를 알 필요가 없다
- 애플리케이션 컨텍스트는 종합 IoC 서비스를 제공한다.
- 빈을 검색하는 다양한 방법 제공한다.
    - 타입으로 조회, 특별한 어노테이션 설정 빈 조회 등

### 1.5.3 스프링 IoC 용어 정리

빈

- 스프링이 IoC 방식으로 관리하는 오브젝트
- 스프링이 직접 생성과 제어를 담당하는 오브젝트만을 빈이라고 함

빈 팩토리

- IoC를 담당하는 핵심 컨테이너
- 빈을 등록하고, 생성하고, 조회하고 돌려주고, 그 외 부가적인 빈 관리 기능 담당

어플리케이션 컨텍스트

- 빈 팩토리를 확장한 IoC 컨테이너
- 빈을 등록하고 관리하는 기본적인 기능은 빈팩토리와 동일
- 스프링이 제공하는 애플리케이션 지원 기능을 모두 포괄
- ApplicationContext는 BeanFactory를 상속

설정정보/ 설정 메타정보

- 애플리케이션 컨텍스트 또는 빈 팩토리가 IoC를 적용하기 위해 사용하는 메타 정보

컨테이너 또는 IoC 컨테이너

- IoC 방식으로 빈을 관리한다는 의미에서 애플리케이션 컨텍스트나 빈 팩토리를 컨테이너 또는 IoC 컨테이너라고 함

스프링 프레임워크

- IoC 컨테이너, 애플리케이션 컨텍스트를 ㅍ포함해서 스프링에서 제공하는 모든 기능을 통 틀어 말할 때 주로 사용

## 1.6 싱글톤 레지스트리와 오브젝트 스코프

스프링은 여러 번에 걸쳐 빈을 요청하더라도 매번 동일한 오브젝트를 돌려준다.

### 1.6.1 싱글톤 레지스트리로서의 애플리케이션 컨텍스트

- 애플리케이션 컨텍스트는 IoC 컨테이너이면서 싱글톤을 저장하고 관리하는 싱글톤 레지스트리이다.
- 스프링은 기본적으로 내부에서 생성하는 빈 오브젝트를 모두 싱글톤으로 만든다.

### 서버 애플리케이션과 싱글톤

스프링은 태생적으로 서버에서 사용, 서버 환경에서는 대개 오브젝트를 하나만 만들어서 사용하는 싱글톤 사용이 권장

- 디자인 패턴의 싱글톤 패턴은 안티패턴

### 싱글톤 패턴의 한계

자바에서 싱글톤 구현

- 클래스 밖에서 오브젝트를 생성하지 못하도록 생성자를 private
- 생성된 싱글톤 오브젝트를 저장할 수 있는 자신과 같은 타입의 스태틱 필드 정의
- staic 팩토리 메소드인 getInstance() 생성
    - 최초 호출 되는 시점만 오브젝트가 만들어지게 함
    - 생성된 오브젝트는 static 필드에 저장
- 오브젝트가 만들어진 후에는 getInstance() 메소드를 통해 저장해둔 오브젝트 반환

한계

- priavte 생성자를 갖고 있어서 상속할 수 없다.
- 테스트하기 힘들다.
- 서버 환경에서는 싱글톤이 1개인 것을 보장하지 못한다.
    - 여러 개 JVM에 분산되서 설치가 되면 독립적으로 오브젝트가 생긴다.
- 전역 상태를 만들 수 있다.
    - 객체지향 프로그래밍에서 권장하지 않는 프로그래밍 모델

### 싱글톤 레지스트리

스프링은 직접 싱글톤 형태의 오브젝트를 만들고 관리하는 싱글톤 레지스트리 기능을 제공

- 스태틱 메소드와 private 생성자를 사용해야하는 비정상적 클래스가 아니라 평범한 자바 클래스를 싱글톤으로 활용할 수 있게 한다.
- public 생성자를 가질 수 있다.
- 목 오브젝트로 대체하는 것도 가능하다.

### 1.6.2 싱글톤과 오브젝트 상태

- 싱글톤은 멀티 스레드 환경이라면 여러 스레드가 동시에 접근해서 사용 가능하기 때문에 상태 관리에 주의가 필요하다.
- 싱글톤은 기본적으로 인스턴스 필드의 값을 변경하고 유지하는 상태유지 방식으로 만들지 않는다.

인터페이스 타입은 인스턴스 변수로 사용해도 상관 없다. 읽기 전용의 정보이기 때문이다

- 단순한 읽기 전용 값이라면 static final이나 final로 선언하는 편이 낫다.

### 1.6.3 빈 스코프

스코프 : 빈이 생성되고, 존재하고, 적용되는 범위

1. 기본 스코프는 싱글톤

- 컨테이너 내에 한 개의 오브젝트만 만들어져서, 강제로 제거하지 않는 한 스프링 컨테이너가 존재하는 동안 계속 유지
1. 프로토타입 스코프
- 컨테이너에 빈을 요청할 때 마다 매번 새로운 오브젝트를 만듬
1. 요청 스코프
- 웹을 통해 새로운 HTTP 요청이 생길때마다 생성
1. 세션 스코프
- 웹의 세션과 스코프가 유사

## 1.7 의존관계 주입(DI)

### 1.7.1 제어의 역전과 의존관계 주입

IoC를 핵심을 짚어주는 의존관계 주입 이름을 사용하기 시작했다.

DI는 오브젝트 레퍼런스를 외부로부터 제공(주입) 받고 이를 통해 오브젝트와 의존관계가 동적으로 만들어지는 것이 핵심이다.

### 1.7.2 런타임 의존관계 설정

의존관계는 의존하고 있다는 건 의존 대상이 변하면 나도 변한다는 것을 뜻한다.

오브젝트가 만들어지고 나서 런타임 시에 의존관계를 맺는 대상, 즉 실제 사용대상인 오브젝트를 의존 오브젝트라고 한다.

의존관계 주입은 구체적인 의존 오브젝트와 그것을 사용할 주체(보통 클라이언트라고 부름) 오브젝트를 런타임 시에 연결해주는 작업을 뜻한다.

의존관계 주입이란 아래 세 가지 조건을 충족하는 작업을 뜻한다.

1. 클래스 모델이나 코드에는 런타임 시점의 의존관계가 드러나지 않음. 즉, 인터페이스에만 의존
2. 런타임 시점의 의존관계는 컨테이너나 팩토리 같은 제3의 존재가 결정
3. 의존관계는 사용할 오브젝트에 대한 레퍼런스를 외부에서 제공(주입)함으로써 만들어짐

DI는 자신이 사용할 오브젝트에 대한 선택과 생성 제어권을 외부로 넘긴다.( IoC 개념에 부합 )

스프링 컨테이너의 IoC는 의존관계 주입 또는 DI 라는데 초첨이 맞춰있기 때문에 IoC 컨테이너 외에도 DI 컨테이너 또는 DI 프레임워크라고도 부른다.

### 1.7.3 의존관계 검색과 주입

외부로부터 주입받지 않고 스스로 검색하는 의존관계 검색도 존재한다.

- 런타임 시 의존관계를 맺을 오브젝트를 결정하는 것과 생성하는 것은 외부 컨테이너에게 IoC로 맡기지만 가져올 때는 스스로 컨테이너에게 요청
- 의존관계 검색 예시

    ```java
    public UserDao() {
    	DaoFactory daoFactory = new DaoFactory();
    	this.connectionMaker = daoFactory.connectionMaker();
    }
    ```

    ```java
    context.getBean("connectionMaker", ConnectionMaker.class)
    ```

- 대게는 의존 관계 주입 방식이 더 깔끔함하다.  그러나 아래와 같이 의존관계 검색을 해야할 때가 있다.
    - main에서는 DI로 오브젝트를 주입받을 방법이 없기 때문에 의존관계 검색
    - 사용자 요청을 받을 때마다 서블릿에서 스프링의 컨테이너에 담긴 오브젝트를 사용하려면 한 번은 의존관계 검색 (검색은 이미 스프링 안에 구현)
    - 의존관계 검색에서는 검색하는 오브젝트는 스프링의 빈일 필요가 없다. 그러나 의존관계 주입에서는 모두 빈으로 등록되어있어야 한다. (생성,초기화 권한 필요)

### 1.7.4 의존관계 주입의 응용

의존 관계 주입의 장점

- 런타임 클래스에 대한 의존관계가 나타나지 않고 인터페이스를 통해 결합도를 낮출 수 있다.
- 다른 책임을 가진 사용 의존관계에 있는 대상이 바뀌거나 변경되어도 자신은 영향을 받지 않고, 변경을 통한 다양한 확장에는 자유롭다.

### 1.7.5 메소드를 이용한 의존관계 주입

생성자가 아닌 일반 메소드를 이용해서 의존관계를 주입할 수 있다.

- 수정자(setter)를 이용한 주입
- 일반 메소드를 이용한 주입

## 1.8 XML을 이용한 설정

XML으로도 DI 의존관계 설정 정보를 만들 수 있다

### 1.8.1

`@Configuration` - beans

`@Bean`- bean

하나의 `@Bean` 메소드를 통해 얻을 수 있는 빈 DI 정보는 3가지이다.

- 빈 이름
- 빈의 클래스
- 빈의 의존 오브젝트 - 빈의 생성자나 수정자 메소드를 통해 의존 오브젝트를 넣어준다(1개 이상일 수 있음)

```java
@Bean                        -> <bean
public ConnectionMaker            id="connectionMaker"
connectionMaker() {
	return new DConnectionMaker();	   class="springbook...DconnectionMaker"/>
}
```

```java
userDao.setConnectionMaker(connectionMaker());
```

```java
<beans xmlns="http://www.springframework.org/schema/beans">
    <bean id="connectionMaker" class="org.mobilohas.green.ch1.user.dao.DConnectionMaker"/>
    <bean id="userDao" class="org.mobilohas.green.ch1.user.dao.UserDao">
        <property name="connectionMaker" ref="connectionMaker" />
    </bean>
</beans>
```

- name 애트리뷰트 : DI에 사용할 수정자 메소드의 프로퍼티 이름
- ref 애트리뷰트 : 주입할 오브젝트를 정의한 빈의 ID
    - 보통 프로퍼티 이름과 DI 되는 빈의 이름이 같은 경우가 많다.
    - 프로퍼티 이름은 주입할 빈 오브젝트의 인터페이스를 따르는 경우가 많다.
    - 빈 이름도 인터페이스 이름을 사용하는 경우가 많다.
    - 인터페이스 이름과 다르게 정해도 상관없다.

### 1.8.2 XML을 이용하는 애플리케이션 컨텍스트

XML에서 빈의 의존관계 정보를 이용하는 IoC/DI 작업에는 GenericXmlAppliactionContext를 사용한다

애플리케이션 컨텍스트가 사용하는 XML 설정파일 이름은 관례에 따라 appliactionContext.xml이라고 만든다

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!-- XML Schema의 네임스페이스를 지정하며, 표준 값인 http://www.w3.org/2001/XMLSchema-instance를 사용 -->

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="connectionMaker" class="org.mobilohas.green.ch1.user.dao.DConnectionMaker"/>
    <bean id="userDao" class="org.mobilohas.green.ch1.user.dao.UserDao">
        <property name="connectionMaker" ref="connectionMaker" />
    </bean>

</beans>

```

```java
class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("green5");
```

### 1.8.3 DataSource 인터페이스로 변환

자바에서는 DB 커넥션을 가져오는 오브젝트 기능을 추상화한 DataSource 라는 인터페이스가 이미 존재한다

```java
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3307/spring");
        dataSource.setUsername("root");
        dataSource.setPassword("qwer1234");
        return dataSource;
    }
```

```java

public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = dataSource.getConnection();
```

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="dataSource" class="org.springframework.jdbc.datasource.SimpleDriverDataSource"/>
    <bean id="userDao" class="org.mobilohas.green.ch1.user.dao.UserDao">
        <property name="connectionMaker" ref="connectionMaker"/>
    </bean>

</beans>
```

- dataSource 메소드에서 SimpleDriverDataSource 오브젝트의 수정자로 넣어준 DB 접속정보는 나타나지 않는다.

### 1.8.4 프로퍼티 값 주입

텍스트나 단순 오브젝트 등을 수정자 메소드에 넣어주는 것을 스프링에서는 값을 주입한다라고 한다.
