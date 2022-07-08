# :pushpin: MingMarket - 당신 근처의 밍마켓
>중고 거래부터 동네 정보까지, 이웃과 함께해요.

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


## 3. ERD 설계

## 4. 구현 요구사항

## 5. 핵심 기능

### 1. JPA를 활용한 웹 애플리케이션 개발
<details>
<summary><b>기능 설명</b></summary>
<div markdown="1">


</div>
</details>

### 2. 상품등록, 회원가입 혹은 로그인시 Validation 검증
<details>
<summary><b>기능 설명</b></summary>
<div markdown="1">

```
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
```
회원가입시 아이디 중복 검증 메서드

검증하고자 하는 객체(DTO) Annotation 사용함
Controller에서는 검증하고자 하는 객체(DTO) 앞에 @Valied 붙여서 검증함. 
그리고 BindingResult 객체는 검증 결과에 대한 결과 정보들을 담아서
```
        if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
            return "product/createProductForm"; //다시 폼으로 이동
        }
```
(값이 있을 경우 = 검증 결과 오류를 내는 것들) 다시 폼으로 보내버림

그 외 객체에서 검증할 수 없는 것들은 

```
//로그인 실패시 (null)
if (loginMember == null) {
result.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다");
return "/members/login";
}
```

이런식으로 
그리고 view에서는 (타임리프)
````
                <div th:if="${#fields.hasGlobalErrors()}">
                    <p class="field-error" th:each="err : ${#fields.globalErrors()}"
                       th:text="${err}">전체 오류 메시지</p>
                </div>
````
글로벌 오류로 처리

<br>
검증과 오류 메시지 공식 메뉴얼 <br>
https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#validation-and-
error-messages

</div>
</details>

### 3. Spring Security 회원가입시 패스워드 암호화 적용하기
<details>
<summary><b>기능 설명</b></summary>
<div markdown="1">
[블로그 정리](https://intelliy-min.tistory.com/49)




</div>
</details>

### 4. 스프링이 제공하는 MultipartFile을 이용한 이미지 업로드
<details>
<summary><b>기능 설명</b></summary>
<div markdown="1">

내용

</div>
</details>


### 5. 서블릿 HTTP 세션을 활용한 로그인 상태 유지
<details>
<summary><b>기능 설명</b></summary>
<div markdown="1">

내용

</div>
</details>


### 6. 스프링 인터셉터를 이용한 로그인 체크
<details>
<summary><b>기능 설명</b></summary>
<div markdown="1">

내용

</div>
</details>



<br>

## 6. 트러블 슈팅
<details>
<summary><b>로그인 상태 유지시 경로 localhost:xxxx/; jsessionid=~~</b></summary>
<div markdown="1">
내용
</div>
</details>


<details>
<summary><b>JPA - merge를 이용하여 값 수정시 null</b></summary>
<div markdown="1">
내용
</div>
</details>


<details>
<summary><b>로그인 : 없는 아이디 입력시 오류 처리(Optional 클래스 사용) - NoSuchElementException</b></summary>
<div markdown="1">
내용
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

