# 1장 오브젝트와 의존관계

## 1.1 초난감 DAO
## 1.2 DAO 분리
변하는 것과 변하지 않는 것이 있다.

변경사항이 주어졌을 때 변경영향을 조절할 수 있는 코드가 좋은 코드다.

관심사 분리: 변경을 한 곳에 모아서 관리한다.

- 템플릿 메서드 패턴: 수퍼클래스에서 기본적인 로직 흐름을 만들고, 그 기능의 일부를 추상 메소드나 
오버라이딩이 가능한 protected 메서드 등으로 만든 뒤 서브클래스에서 이런 메소드를 필요에 맞게 구현해서 사용하도록 하는 방법
- 펙토리 메서드 패턴: 서브클래스에서 구체적인 오브젝트 생성 방법을 결정하게 하는 것.

UserDao에 팩토리 메서드 패턴을 적용해서 getConnection을 분리합시다.

한계점은 이미 한번 상속을 사용해버리면 다른 상속을 못하게 된다. 
그리고 getConnection을 다른 DAO에서는 재사용 불가능하다.

## 1.3 DAO의 확장
interface 도입
- 전략패턴: 필요에 따라 변경이 필요한 알고리즘을 인터페이스를 통해 통째로 외부에 분리시키고, 이를 
- 구현한 알고리즘을 필요에 따라서 바꿔가면서 사용하는 패턴.

---
# 새롭게 안 점
- DAO 개념에 대해서 알게 되었다.
- 팩토리 메서드 패턴에 대해서 확실하게 알게 되었다.
- 인터페이스라는 것이 메세지만 전달하는 협력관계 구축과 완전히 같은 개념이라고 알게됨.
- 전략패턴도 알게됨.

---
# 질문
## DAO가 뭐야?
data access object의 약자로, 데이터 엑세스 계층을 의미함.

xml을 보면 interface + 구현체로 되어있음.
-> 이것이 합쳐진 개념이 repository임.(jpa에서 repository로 합쳐짐.)

Entity는 해당 계층에서 매핑되는 객체임.

## 자바빈 규약이 뭐야?
'사용자 정보를 저장할 때는 자바빈 규약에 따른 오브젝트를 사용하는 것이 편리하다'
자바빈: 비주얼 툴(IDE로 이해하면 될 듯)에서 조작 가능한 컴포넌트를 의미한다. -> 어떤 역사와 관련된듯
자바빈을 요즘 말로 `빈`이라고 함.
- 프로퍼티가 있고 기본 생성자가 있는(그래야 프레임워크에서 대신 생성 가능) 객체임. 
