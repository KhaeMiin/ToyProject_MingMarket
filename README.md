# 🔥 MingMarket - 당신 근처의 밍마켓 🔥
>:bulb: 중고 거래부터 동네 정보까지, 이웃과 함께해요.

## 1. 제작 기간 & 참여 인원(역할)
- 2022년 5월 10일 ~ 진행중

## 2. 사용 기술
#### `Back-end`
1. Project:
    - Gradle Project
    - Language: Java 11
    - Spring Boot: 2.6.7
2. Dependencies
    - SpringWeb
    - Thymeleaf
    - Lombok
    - Validation
    - H2 Database
    - MySQL Driver
    - Spring Data JPA
    - Spring Security 2.4.5
#### `Front-end`
- JavaScript
- Thymeleaf
- HTML5

## 4. 구현 요구사항

## 5. 핵심 기능 & 트러블 슈팅

#### 1. JPA를 활용한 웹 애플리케이션 개발
<details>
<summary>📌기능 설명</summary>
<div markdown="1">
<br>

> 자바 진영의 <b>RM 기술 표준으로 사용되는 인터페이스의 모음인 JPA를 활용</b>하여 웹 애플리케이션을 개발하였다. <br>
> JPA 인터페이스의 구현체로 <b>Hibervate 프레임 워크</b>를 사용하였다. <br><br>
> 이렇게 JPA를 사용함으로써 INSERT/UPDATE/SELECT <b>쿼리 등을 직접 작성하지 않아도 데이터를 저장</b>할 수 있게 되었다.<br>
> SQL 중심적인 개발이 아닌 Method를 통해서 DB를 조작할 수 있어, 
> 개발자는 객체 모델을 이용해서 비즈니스 로직을 구성하는 데만 집중할 수 있었다. <br>
> 
> 특히 <b>필드 변경이나 필드를 추가하게 될 경우 JPA가 자동으로 SQL을 처리해주기 때문에 유지보수가 수월</b>하다. <br>
> 그리고 초반 개발단계에서는 H2 데이터베이스를 사용하였는데,
> 나중에 데이터베이스를 MySQL로 변경하여도 <b>쿼리를 수정하지 않아도 된다는 장점</b>이 있었다  

<br>

##### `1. 도메인 모델 분석 (연관관계 매핑 분석)`
- **회원(Member)** 과 **상품(Product)**
    - 한명의 회원은 여러 상품을 등록할 수 있다.
    - 상품(Product)이 연관관계 주인으로 다대일 관계를 갖는다.
- **회원(Member)** 과 **관심 상품(WishItem)**
    - 한명의 회원은 여러 개의 관심 상품을 가질 수 있다.
    - 관심상품(WishItem)이 연관관계 주인으로 다대일 관계를 갖는다.
- **관심 상품(WishItem)** 과 **상품(Product)**
    - 관심 상품은 단 하나의 상품에 해당된다.
    - 관심 상품(WishItem)이 연관관계 주인으로 일대일 관계를 갖는다.
- **상품(Product)** 과 **댓글(Comment)**
    - 하나의 상품에는 여러 댓글을 달 수 있다.
    - 댓글(Comment)과 연관관계 주인으로 다대일 관계를 갖는다.
- **회원(Member)** 과 **댓글(Comment)**
    - 한명의 회원은 여러 댓글을 달 수 있다.
    - 댓글(Comment)과 연관관계 주인으로 다대일 관계를 갖는다.
- **상품(Product)** 과 **카테고리(Category)**
    - 하나의 상품은 여러개의 카테고리를 가질 수 있다.
      <br><br>
##### `2. 테이블 설계`
![](https://blog.kakaocdn.net/dn/VahxL/btrHaoGhKcw/q5jKbGSMBJWhewhLUNohWK/img.png)
<br><br><br>
##### `3. 엔티티 개발`
👉 주요 사용한 어노테이션
- @Entity: 해당 클래스가 DB테이블과 1대 1 매칭
- @Id: Primary Key를 지정
- @GeneratedValue: AUTO(dafault) 데이터베이스에 의해 자동으로 생성된 값
- @Embedded: 새로운 값 타입을 직접 정의해서 사용 (재사용이 가능함), 값 타입을 사용하는 곳에 표시
- @Embeddable: 값 타입을 정의하는 곳에 표시
- @ManyToOne(fetch = LAZY), @OneToOne(fetch = LAZY): 연관 관계 맵핑
  - ![](https://blog.kakaocdn.net/dn/xW1JV/btrHfmNFlWw/SZdtqTXWq0h5RvEjcBvE80/img.png)
  - fetch = LAZY: 지연로딩 → 로딩되는 시점에 Lazy 로딩 설정이 되어있는 member 엔티티는 프록시 객체로 가져온다
  - 후에 실제 객체를 사용하는 시점에(member 사용하는 시점에) 초기화가 된다. DB에 쿼리가 나간다. (select 쿼리가 따로 2번 나감)
  - @ManyToOne, @OneToOne: 기본이 (fetch = EAGER) → 즉시로딩 이므로 LAZY로 바꿔준다.
- @JoinColumn(name = "member_id"):연관 관계를 맺을 해당 객체의 컬럼 값을 넣어주기
- @Enumerated(EnumType.STRING): 자바의 enum 타입을 매핑할 때 사용
  <br>
  <br>

**※ 엔티티에서 Setter 사용 지양**
> Setter는 호출 시 데이터가 변동됩니다.
>  <br> Setter를 열어두게 되면 프로젝트가 커지고 복잡해질수록 엔티티가 도대체 왜 어디서 변경되는지 추적하기 점점 힘들어진다.
>  <br> 그래서 엔티티의 데이터를 변경할 때는 아래 코드처럼 Setter 대신 변경 지점이 명확하도록 <b>변경을 위한 비즈니스 메서드를 따로 만들어 제공</b>하였다.
>  <br> 그리고 객체의 일관성을 유지하기 위해 객체 생성 시점에 값들을 넣어줌으로써 Setter 사용을 지양할 수 있었다.

- Member Entity

![](https://blog.kakaocdn.net/dn/rVKYu/btrHdwXCYQl/qqRCWnwt3GfE7wPjJK1km0/img.png)

- Product Entity

![](https://blog.kakaocdn.net/dn/bH7EHq/btrG9mvqVra/J03WQJAQ6G9341EnzoswS0/img.png)

- WishItem Entity

![](https://blog.kakaocdn.net/dn/bvNtgq/btrHdIpX1iS/6jBB1S1wA5bWQrrRb8zOqK/img.png)

<br>

> 아래와 같이 기본 생성자 접근자를 protected로 변경하면 new Entity() 사용을 막을 수 있어 객체의 일관성을 더 유지할 수 있다.
> <br>(protected로 설정하는 이유는 JPA 기본 스펙상 기본 생성자가 필요한데 protected로 제어하는 것까지 허용되기 때문이다.)
> <br> 롬복을 사용하여 어노테이션 설정을 통해 간단하게 설정하였다.

![](https://blog.kakaocdn.net/dn/bN2uFz/btrHevKrk2n/oS7FgdEMk3QBDGR1D947bk/img.png)

<br>
<br>
<br>

</div>
</details>

<details>
<summary>⚽트러블 슈팅</summary>
<div markdown="1">
<br>
<b>1. JPA - merge를 이용하여 값 수정시 수정하지 않는 데이터는 값이 null로 들어가짐</b>
<br><br>

> 구현 요구사항 <br>
> 유저는 상품을 자유롭게 올릴 수 있다.  
> 올린 상품을 수정할 수 있다

👇Controller

![](https://blog.kakaocdn.net/dn/bXVNYP/btrHaficUqE/Gt7w6LYKNd9lIkVRXz6Hi0/img.png)

👇ProductRepository.java

![](https://blog.kakaocdn.net/dn/sT8jW/btrHaYnjoFO/ntVeLB0X0y7xJVCbEzdTjK/img.png)



👇결과:

![](https://blog.kakaocdn.net/dn/UDMag/btrFybu624A/ImJr1Z2w3vKXhZKqz1vhj0/img.png)

##### 문제 발생
- 수정시 MEMBER_ID가 계속 null값이 채워진다.
- 게시글 수정시 게시글 작성자(member_id)는 변경될 일이 없다.
- 그래서 아래 코드 실행시 member_id = null값이다.

```
 else { // 상품이 존재할 경우 강제로 업데이트(즉, 수정)
    em.merge(product);
}
```

> 병합은 준영속 상태의 엔티티를 다시 영속 상태로 변경할 때 사용한다.  
> merge() 메서드는 준영속 상태의 엔티티를 받아 그 정보로 새로운 영속 상태의 엔티티를 반환한다.

**merge()의 동작 방식**

1.  merge()를 실행
2.  파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회
-   만약 1차 캐시에 엔티티가 없으면 데이터베이스에 엔티티를 조회하고 1차 캐시에 저장.
-   무조건 1번은 db 조회를 하므로 성능에 좋지 않을 수 있다.
3.  조회한 영속 엔티티에 product 엔티티의 값을 채워 넣음
-   이때 product 의 모든 값을 영속 엔티티에 채워 넣기 때문에 **null 값이 들어갈 수 도 있는 문제가 생긴다.**
-   이래서 **업데이트 시 merge()보단 변경 감지를 사용하자.**
4.  영속 상태의 객체를 반환

<br>

#### **✨수정된 코드✨**

**변경 감지 사용 (**dirtyChecking)****

👇Service
![](https://blog.kakaocdn.net/dn/dMaOMk/btrHdLtKbhK/w1vHzOOH40F3etaQz0cuY1/img.png)

👇Repository

```
public Product findSingleProduct(Long productId) {
    return em.find(Product.class, productId);
}
```

entityManager로 entity를 직접 꺼내, 값을 수정한다.

@Transactional으로 인하여 로직이 끝날 때 JPA에서 트랜잭션 commit 시점에 변경 감지(Dirty Checking)한 후 Flush를 한다.
<br>수정시 입력되지 않은 값은 그대로 유지된다.
<br>
<br>
<br>

</div>
</details>


#### 2. 상품등록, 회원가입 그리고 로그인시 Validation 검증
<details>
<summary>📌기능 설명</summary>
<div markdown="1">

#### `1. Valid Annotation을 이용한 Validation 체크`
> Spring에서는 사용자가 입력한 값에 대한 유효성을 체크하기 위해 Spring Validator를 사용할 수 있도록 지원하고 있다.

- build.gradle에 dependency 추가 <br>
`implementation 'org.springframework.boot:spring-boot-starter-validation'`
- **Dto**객체를 정의한 후 각 필드에 맞는 Annotation을 사용했다.
  - `@NotNull`: Null 허용하지 않음 (" ", "" 허용)
  - `@NotEmpty`: Null과 ""는 허용하지 않음 (" "는 허용) 
  - `@NotBlank`: Null과 "", " " 모두 허용하지 않음
  - `@Size(min=, max=)`: 크기가 지정된 경계를 포함한 사이에 있어야 함
  - `@Range(min=, max=)`: 숫자 값 또는 숫자 값의 문자열 표현에 적용(지정한 범위 내에 있어야 함)
- **Controller**에서는 Dto 객체 앞에 `@Valied`, 그리고 객체 뒤에는 `BindingResult result`를 명시한다.
  - `@Valid` 선언된 객체에 설정을 바탕으로 유효성 검사
  - 데이터가 유효하지 않은 속성이 있으면 그에 대한 에러 정보를 BindingResult에 담는다.
    - 아래 코드처럼 result에 에러가 담겨있는지 확인하는 로직을 만들어 에러가 있으면 폼으로 다시 이동할 수 있도록 한다. 
    ```
      if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
        return "product/createProductForm"; //다시 폼으로 이동
      }
      ```
<br>

#### `2. 회원가입시 아이디 중복 검증 & `
👇MemberService에서 따로 중복 아이디를 검증하는 메서드를 만들었다.

![](https://blog.kakaocdn.net/dn/dEOPkl/btrHfRfKHvx/ndrBqKHn87J1vQLJdmxv20/img.png)

👇그리고 화면에 보여질 오류메시지는 타임리프를 통해 아래코드와 같이 globalError()를 출력시켜주었다.
````
    <div th:if="${#fields.hasGlobalErrors()}">
        <p class="field-error" th:each="err : ${#fields.globalErrors()}"
        th:text="${err}">전체 오류 메시지</p>
    </div>
````


<br>
📝검증과 오류 메시지 공식 메뉴얼 <br>
→ https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#validation-and-
error-messages

</div>
</details>

#### 3. Spring Security 회원가입시 패스워드 암호화 적용하기
<details>
<summary>📌기능 설명</summary>
<div markdown="1">

>Spring Security<br>
>Spring 기반의 Application의 보안을 위한 Spring framework<br>
>스프링 시큐리티의 PasswordEncoder를 이용하여 패스워드를 암호화 할 것이다. <br>
>[Spring Security공식문서 바로가기](https://spring.io/projects/spring-security)<br>

##### `1. 의존성 주입`

```
dependencies {
	implementation group: 'org.springframework.boot', name: 'spring-boot-starter-security', version: '2.4.5' /* 스프링 시큐리티 */
}
```

##### `2. Config 설정`
<details>  
<summary>PasswordEncoder 코드 참조</summary>  
<div markdown="1">  

  ```
/*
 * Copyright 2011-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.crypto.password;

/**
 * Service interface for encoding passwords.
 *
 * The preferred implementation is {@code BCryptPasswordEncoder}.
 *
 * @author Keith Donald
 */
public interface PasswordEncoder {

	/**
	 * Encode the raw password. Generally, a good encoding algorithm applies a SHA-1 or
	 * greater hash combined with an 8-byte or greater randomly generated salt.
	 */
	String encode(CharSequence rawPassword);

	/**
	 * Verify the encoded password obtained from storage matches the submitted raw
	 * password after it too is encoded. Returns true if the passwords match, false if
	 * they do not. The stored password itself is never decoded.
	 * @param rawPassword the raw password to encode and match
	 * @param encodedPassword the encoded password from storage to compare with
	 * @return true if the raw password, after encoding, matches the encoded password from
	 * storage
	 */
	boolean matches(CharSequence rawPassword, String encodedPassword);

	/**
	 * Returns true if the encoded password should be encoded again for better security,
	 * else false. The default implementation always returns false.
	 * @param encodedPassword the encoded password to check
	 * @return true if the encoded password should be encoded again for better security,
	 * else false.
	 */
	default boolean upgradeEncoding(String encodedPassword) {
		return false;
	}

}
```

</div>  
</details>

PasswordEncoder는 스프링 시큐리티의 인터페이스 객체이다. <br>
PasswordEncoder는 비밀번호를 암호화하는 역할로, 구현체는 이 암호화를 어떻게 할지, 암호화 알고리즘에 해당한다.<br>
그래서 PasswordEncoder의 구현체를 대입해주고 이를 스프링 빈으로 등록하는 과정이 필요하다.<br>
기존적인 설정들을 disable하는 Config 객체는 WebSecurityConfigurerAdapter를 상속받아 configure()를 구현한다.

👇SecurityConfig
![](https://blog.kakaocdn.net/dn/Yuvb4/btrHdbAiNWx/nQgt4GDHmchEqPHPE4kvW1/img.png)
<details>  
<summary>📝참고</summary>  
<div markdown="1">

![](https://blog.kakaocdn.net/dn/bDfllg/btrFKmQyTvr/yq0ARTmbpIWYg43pvatr9K/img.png)

configure(http:HttpSecurity):void 오버라이드하였다.
</div>  
</details>

❗️여기서 **BcryptPasswordEncoder는 BCrypt라는 해시 함수를 이용하여 패스워드를 암호화하는 구현체**이다.


**Spring Security의 설정은 HttpSecurity를 오버라이드해서 설정한다.**

**`.antMatchers`**

```
.antMatchers("/css/**", "/js/**", "/*.ico", "/error", "/").permitAll()
```

- 특정 리소스에 대해서 권한을 설정한다.

- 뒤에 붙은 permitAll()은 antMatchers에서 설정한 URL의 접근을 인증없이 허용한다는 뜻이다.


**`.anyRequest`**

```
.anyRequest().authenticated()
```

- 이 옵션은 모든 리소스가 인증을 해야만 접근이 허용된다는 뜻이다.
<br>

[스프링 시큐리티 설정값 참고한 블로그 바로가기](https://kimchanjung.github.io/programming/2020/07/02/spring-security-02/)

##### `3. 회원가입/로그인 구현`

👇MemberEntity

```
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id; //시퀀스

    private String userId; //이메일(아이디)

    @Column(length = 10)
    private String nickname; //닉네임

    private String pass;
    private String username;
    private int hp;

    @Embedded
    private Address address;

    public Member(String userId, String nickname, String pass, String username, int hp, Address address) {
        this.userId = userId;
        this.nickname = nickname;
        this.pass = pass;
        this.username = username;
        this.hp = hp;
        this.address = address;
    }

    // 회원정보 수정메서드
    public void change(String nickname, String username, int hp, Address address) {
        this.nickname = nickname;
        this.username = username;
        this.hp = hp;
        this.address = address;
    }

    //비밀번호 수정메서드
    public void passwordChange(String pass) {
        this.pass = pass;
    }

    /**
     * 비밀번호를 암호화하는 메서드
     */
    public Member hashPassword(PasswordEncoder passwordEncoder) {
        this.pass = passwordEncoder.encode(this.pass);
        return this;
    }


}
```

MemberEntity에 PasswordEncoder를 사용하여 password를 인코딩하였다.

MemberRepository

```
@Repository
public class MemberRepository {

    @PersistenceContext //스프링 제공
    private EntityManager em;

    // 회원 저장
    public void save(Member member) {
        em.persist(member);
    }

    //회원 단건 조회
    public Member findOneMember(Long memberId) {
        return em.find(Member.class, memberId);
    }

    //회원 전체 조회
    public List<Member> findAllMembers() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

}
```

JPA의 편리한 CRUD

#### **1\. 회원가입**

MemberService

```
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        member.hashPassword(passwordEncoder); //스프링 시큐리티(암호화)
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 아이디 검증 메서드
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByUserId(member.getUserId());

        if (findMembers.size() > 0) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

}
```

MemberService에서 회원가입 진행시 join메서드에서

생성자를 통해 주입받은 PasswordEncoder passwordEncoder를 사용하여 비밀번호 해싱 후

Repository로 DB에 저장할 수 있도록 하였다.

MemberController

```
    private final MemberService memberService;
    private final LoginService loginService;

    @GetMapping("/join")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new CreateMemberForm());
        return "members/joinMemberForm";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute("memberForm") CreateMemberForm form, BindingResult result) { //form 안에 에러가 있으면 튕겨내지말고 result에 담음

        if (!form.getPassword().equals(form.getPasswordCheck())) {
            result.reject("passwordFail", "비밀번호가 일치하지 않습니다.");
        }

        if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
            return "members/joinMemberForm"; //다시 폼으로 이동
        }
        Address address = new Address(form.getAddress(), form.getDetailedAddress());
        Member member = new Member(form.getUserId(), form.getNickname(), form.getPassword(),
                form.getUsername(), form.getHp(), address);
        memberService.join(member);
        return "redirect:/";
    }
```

컨트롤러에서는 "/join"에 POST요청이 들어오면

기본적인 Validation 후 memberService.join()을 통해 회원가입이 진행될 수 있도록 해주었다.

#### **2\. 회원 로그인**

LoginService

```
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인
     */
    public Member login(String userId, String password) {
        Optional<Member> findMemberOptional = memberRepository.findByloginId(userId);

        //아이디 조회해서 해당 아이디 정보가 있을 경우( 없으면 null 반환받음)
        if (!findMemberOptional.isPresent()) {
            return null;
        }

        Member member = findMemberOptional.get();

        /**
         * 비밀번호 확인 (스프링 시큐리티)
         * password 암호화 이전의 비밀번호
         * member.getPass() 암호화에 사용된 클래스
         * @return true/ false
         */
        if (passwordEncoder.matches(password, member.getPass())) {
            return member;
        } else {
            return null; //비밀번호가 일치하지 않을 경우 null 반환
        }

}
```

login메소드는 회원 아이디와 비밀번호를 체크하는 메소드이다.

passwordEncoder.matches():

matches()는 내부에서 사용가자 입력한 평문 패스워드와 db에 암호화되어 저장된 패스워드가 서로 대칭되는지에 대한 알고리즘을 구현하고 있다.

먼저 아이디를 조회한 후 입력받은 값의 아이디가 있는지 확인 후 (없으면 null반환)

비밀번호가 일치하면 memberEntitiy를, 비밀번호가 일치하지 않으면 null을 반환하도록 하였다.

LonginController

```
@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    public static final String LOGIN_MEMBER = "loginMember";
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("form")LoginDto form) {
        return "/members/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("form") LoginDto form,
                        BindingResult result,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        if (result.hasErrors()) {
            return "/members/login";
        }

        Member loginMember = loginService.login(form.getUserId(), form.getPassword());


        //로그인 실패시 (null)
        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다");
            return "/members/login";
        }

        //로그인 성공처리
        Address address = loginMember.getAddress();
        MemberDto.SessionMemberData memberData = new MemberDto.SessionMemberData(
                loginMember.getId(), loginMember.getUserId(), loginMember.getNickname(), loginMember.getUsername());

        //기존 세션이 있으면 세션을 반환, 없으면 새로운 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보를 보관 (쿠키에 key: JSESSIONID , value: UUID 로 들어감)
        session.setAttribute(LOGIN_MEMBER, memberData);

        return "redirect:" + redirectURL;
    }
}
```

컨트롤러에서는 로그인 성공시 세션에 로그인 회원을 저장하고 로그인 상태 유지를 할 수 있도록 하였다.

#### **3\. 비밀번호 변경**

LoginService

```
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인
     */
    public Member login(String userId, String password) {
        Optional<Member> findMemberOptional = memberRepository.findByloginId(userId);

        //아이디 조회해서 해당 아이디 정보가 있을 경우( 없으면 null 반환받음)
        if (!findMemberOptional.isPresent()) {
            return null;
        }

        Member member = findMemberOptional.get();

        if (passwordEncoder.matches(password, member.getPass())) {
            return member;
        } else {
            return null; //비밀번호가 일치하지 않을 경우 null 반환
        }

    /**
     * 비밀번호 체크 (비밀번호 수정시 사용)
     */
    public Member passwordCheck(Long memberId, String password) {
        Member member = memberRepository.findOneMember(Long.valueOf(memberId));

        /**
         * 비밀번호 확인 (스프링 시큐리티)
         * password 암호화 이전의 비밀번호
         * member.getPass() 암호화에 사용된 클래스
         * @return passwordEncoder.matches = true/ false
         */
        if (passwordEncoder.matches(password, member.getPass())) {
            return member;
        } else {
            return null; //비밀번호가 일치하지 않을 경우 null 반환
        }
    }
}
```

passwordCheck 메서드를 통해 비밀번호 수정전 현재 비밀번호를 입력받아서 한번 더 체크하기

MemberService

```
@Servic
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        member.hashPassword(passwordEncoder); //스프링 시큐리티(암호화)
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 아이디 검증 메서드
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByUserId(member.getUserId());
/*        if (!findMembers.isEmpty()) { //isEmpty(): 문자열 길이가 0일 경우 true 반환, 여기서는 !isEmpty: 값이 있다면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }*/
        if (findMembers.size() > 0) { //이 코드가 더 최적화일 것 같다.
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAllMembers();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOneMember(Long memberId) {
        return memberRepository.findOneMember(memberId);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void editInformation(Long memberId, UpdateMemberForm form) {
        Member findMember = memberRepository.findOneMember(memberId);
        Address address = new Address(form.getAddress(), form.getDetailedAddress());
        findMember.change(form.getNickname(), form.getUsername(),form.getHp(), address);

    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    public void editPassword(Long memberId, UpdateUserPassForm form) {
        Member findMember = memberRepository.findOneMember(memberId);
        findMember.passwordChange(form.getEditYourPassword());
        findMember.hashPassword(passwordEncoder); //시큐리티 암호화
    }
}
```

JPA 변경 감지(Dirty Checking)을 활용하여

\- entity를 직접 꺼내(memberRepository.findOneMember(memberId)),

\- 변경된 비밀번호넣은 후(findMember.passwordChange(password))

\- 암호화 시킨 값으로 수정한다.(findMember.hashpassword(passwordEncoder))

```
//비밀번호 수정메서드
public void passwordChange(String pass) {
    this.pass = pass;
}

/**
 * 비밀번호를 암호화
 * @param passwordEncoder
 * @return
 */
public Member hashPassword(PasswordEncoder passwordEncoder) {
    this.pass = passwordEncoder.encode(this.pass);
    return this;
}
```

MemberController

```
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @GetMapping("/join")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new CreateMemberForm());
        return "members/joinMemberForm";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute("memberForm") CreateMemberForm form, BindingResult result) { //form 안에 에러가 있으면 튕겨내지말고 result에 담음

        if (!form.getPassword().equals(form.getPasswordCheck())) {
            result.reject("passwordFail", "비밀번호가 일치하지 않습니다.");
        }

        if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
            return "members/joinMemberForm"; //다시 폼으로 이동
        }
        Address address = new Address(form.getAddress(), form.getDetailedAddress());
        Member member = new Member(form.getUserId(), form.getNickname(), form.getPassword(),
                form.getUsername(), form.getHp(), address);
        memberService.join(member);
        return "redirect:/";
    }

    /**
     * 비밀번호 수정
     */
    @GetMapping("/{memberId}/editPassword")
    public String editPasswordForm(@PathVariable("memberId") Long memberId, Model model) {
        model.addAttribute("passwordForm", new UpdateUserPassForm());
        return "members/updatePasswordForm";
    }

    @PostMapping("/{memberId}/editPassword")
    public String editPassword(@PathVariable Long memberId,
                               @Valid @ModelAttribute("passwordForm") UpdateUserPassForm form,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        // 현재 비밀번호 일치 확인
        Member member = loginService.passwordCheck(memberId, form.getPass());
        if (member == null) {
            result.reject("passwordFail", "비밀번호가 일치하지 않습니다.");
            return "members/updatePasswordForm";
        }

        // 변경 비밀번호 (재확인 비밀번호) 일치 확인
        if (!form.getEditYourPassword().equals(form.getEditPasswordCheck())) {
            result.reject("passwordFail2", "변경할 비밀번호가 일치하지 않습니다.");
        }
        if (result.hasErrors()) {
            return "members/updatePasswordForm";
        }

        memberService.editPassword(memberId, form);

        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:/members/myPage/{memberId}";
    }
}
```


</div>
</details>

#### 4. 스프링이 제공하는 MultipartFile을 이용한 이미지 업로드
<details>
<summary>📌기능 설명</summary>
<div markdown="1">

내용

</div>
</details>


#### 5. 서블릿 HTTP 세션을 활용한 로그인 상태 유지
<details>
<summary>📌기능 설명</summary>
<div markdown="1">

내용

</div>
</details>
<details>
<summary>⚽트러블 슈팅</summary>
<div markdown="1">
<b>로그인 상태 유지시 경로 localhost:xxxx/; jsessionid=~~</b>
</div>
</details>


#### 6. 스프링 인터셉터를 이용한 로그인 체크
<details>
<summary>📌기능 설명</summary>
<div markdown="1">

내용

</div>
</details>



<br>

## 6. 기타 트러블 슈팅

<details>
<summary><b>로그인 : 없는 아이디 입력시 오류 처리(Optional 클래스 사용) - NoSuchElementException</b></summary>
<div markdown="1">
**현재 문제점**

1. 로그인시 잘못된 아이디(없는 아이디)를 입력하게 되면

![](https://blog.kakaocdn.net/dn/Aswox/btrFHEX5ZqN/BdAb7IqKdBTWXkeVKOkbIK/img.png)

2. **NoSuchElementException** 예외가 터져버린다.

![](https://blog.kakaocdn.net/dn/pGSvS/btrFTOx96lq/YUwapoxLkwOW7uLpikMdPK/img.png)

에러메시지

java.util.NoSuchElementException: No value present at java.base/java.util.Optional.get(Optional.java:148) ~\[na:na\] at project.toyproject.service.LoginService.login(LoginService.java:31) ~\[classes/:na\] at

코드보기

MemberRepository

```
/**
 *로그인시 회원 조회
*TODO
*코드 리팩토링 예정(람다함수, stream사용해보기)
 */
public Optional<Member> findByloginId(String userId) {
    List<Member> members = em.createQuery("select m from Member m", Member.class)
            .getResultList();
    for (Member m : members) {
        if (m.getUserId().equals(userId)) { //값이 있을 경우
            return Optional.of(m);
        }
    }
    return Optional.empty(); //값이 없으면 null
}
```

LoginService

```
/**
 *로그인
*/
public Member login(String userId, String password) {
    Optional<Member> findMemberOptional = memberRepository.findByloginId(userId);

    //아이디 조회해서 해당 아이디 정보가 있을 경우( 없으면 null 반환받음)
    Member member = findMemberOptional.get();
    if (member.getPass().equals(password)) { //비밀번호가 (일치) 있을 경우
        return member;
    } else {
        return null; //비밀번호가 일치하지 않을 경우 null 반환
    }
}
```

LoginController

```
@PostMapping("/login")
public String login(@Valid @ModelAttribute("form") LoginDto form,
                    BindingResult result,
                    @RequestParam(defaultValue = "/") String redirectURL,
                    HttpServletRequest request) {
    if (result.hasErrors()) {
        return "/members/login";
    }

    Member loginMember = loginService.login(form.getUserId(), form.getPassword());


    //로그인 실패시 (null)
    if (loginMember == null) {
        result.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다");
        return "/members/login";
    }

    //로그인 성공처리
    Address address = loginMember.getAddress();
    MemberDto.SessionMemberData memberData = new MemberDto.SessionMemberData(
            loginMember.getId(), loginMember.getUserId(), loginMember.getNickname(), loginMember.getUsername());

    //기존 세션이 있으면 세션을 반환, 없으면 새로운 세션을 생성
    HttpSession session = request.getSession();
    //세션에 로그인 회원 정보를 보관 (쿠키에 key: JSESSIONID , value: UUID 로 들어감)
    session.setAttribute(LOGIN_MEMBER, memberData);

    return "redirect:" + redirectURL;
}
```

MemberService 코드를 다시 보자

```
/**
 *로그인
*/
public Member login(String userId, String password) {
    Optional<Member> findMemberOptional = memberRepository.findByloginId(userId);

    //아이디 조회해서 해당 아이디 정보가 있을 경우( 없으면 null 반환받음)
    Member member = findMemberOptional.get();
    if (member.getPass().equals(password)) { //비밀번호가 (일치) 있을 경우
        return member;
    } else {
        return null; //비밀번호가 일치하지 않을 경우 null 반환
    }
}
```

우선 findMemberOptional.get()으로 Optional 객체에 저장된 값에 접근한다.

여기서 저장된 값이 있다면 if문으로 넘어갈 것이다.

하지만 **Optional 객체에 저장된 값이 null이면 NoSuchElementException 예외가 발생한다.**

해당 아이디가 없을 경우 결국 if문이 실행되기 전에 예외가 터져버리는 것이다.

따라서 get()메소드를 호출하기 전에 Optional 객체에 저장된 값이 null인지 아닌지를 먼저 확인한 후 호출해야한다.

**문제해결**

| 메소드 | 설명 |
| --- | --- |
| static <T> Optional<T> empty() | 아무런 값도 가지지 않는 비어있는 Optional 객체를 반환함. |
| T get() | Optional 객체에 저장된 값을 반환함. |
| boolean isPresent() | 저장된 값이 존재하면 true를 반환하고, 값이 존재하지 않으면 false를 반환함. |
| static <T> Optional<T> of(T value) | null이 아닌 명시된 값을 가지는 Optional 객체를 반환함. |
| static <T> Optional<T> ofNullable(T value) | 명시된 값이 null이 아니면 명시된 값을 가지는 Optional 객체를 반환하며, 명시된 값이 null이면 비어있는 Optional 객체를 반환함. |
| T orElse(T other) | 저장된 값이 존재하면 그 값을 반환하고, 값이 존재하지 않으면 인수로 전달된 값을 반환함. |
| T orElseGet(Supplier<? extends T> other) | 저장된 값이 존재하면 그 값을 반환하고, 값이 존재하지 않으면 인수로 전달된 람다 표현식의 결괏값을 반환함. |
| <X extends Throwable> T   orElseThrow(Supplier<? extends X>  exceptionSupplier) | 저장된 값이 존재하면 그 값을 반환하고, 값이 존재하지 않으면 인수로 전달된 예외를 발생시킴. |

출처 - [코딩의 시작, TCP School](http://www.tcpschool.com/java/java_stream_optional)

**get() 메소드를 호출하기 전에**

**isPresent()를 사용하여 객체에 저장된 값이 null인지 아닌지를 확인할 것이다.**

LoginService

```
public Member login(String userId, String password) {
    Optional<Member> findMemberOptional = memberRepository.findByloginId(userId);

    //아이디 조회해서 해당 아이디 정보가 있을 경우( 없으면 null 반환받음)
    if (!findMemberOptional.isPresent()) {
        return null;
    }

    Member member = findMemberOptional.get();

    if (member.getPass().equals(password)) { //비밀번호가 (일치) 있을 경우
        return member;
    } else {
        return null; //비밀번호가 일치하지 않을 경우 null 반환
    }
}
```

**boolean isPresent(): 입력받은 아이디 정보가 존재하면 true, 존재하지 않을 경우 false 반환**

if문으로 아이디 정보가 없을 경우 null을 리턴한다.

다시 로그인을 시도해본다.

![](https://blog.kakaocdn.net/dn/5X9Jo/btrFHGhphs5/JNjnGSB8w4XKgUvZqnnsPK/img.png)

생각한 방향으로 잘 작동되는 것을 볼 수 있다.

> **Optional<T> 클래스**  
>  Integer, Double 클래스처럼 **'T' 타입의 객체를 포장해주는 래퍼 클래스**  
>  **모든 타입의 참조 변수를 저장할 수 있다.**  
>  이러한 Optional 객체를 사용하면 복잡한 조건문 없이 **null 값으로 인해 발생하는 예외를 처리할 수 있다.**  
>  다양한 예제는 아래 링크 참조

[코딩의 시작, TCP School](http://www.tcpschool.com/java/java_stream_optional)

</div>
</details>

### 해결하지 못한 문제

<details>
<summary><b>로그아웃시 메인("/")페이지로 이동되지 않음("/login?logout"으로 location되는 상황)</b></summary>
<div markdown="1">
내용
</div>
</details>


<br>

## 7. 코드 리팩토링

### 1. Controller에 노출되어 있는 Entity 객체 Service계층으로 옮기기
<details>
<summary><b>코드 설명</b></summary>
<div markdown="1">


</div>
</details>

## 8. 회고 / 느낀점

### 간단한 KPT
#### Keep

#### Problem

#### Try

