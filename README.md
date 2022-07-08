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
    - Sprign Security 2.4.5
#### `Front-end`
- JavaScript
- Thymeleaf
- HTML5

</br>

## 3. ERD 설계

## 4. 핵심 기능
<details>
<summary><b>JPA를 활용한 웹 애플리케이션 개발</b></summary>
<div markdown="1">

### 1. 도메인 설계/JPA 전략

</div>
</details>

<details>
<summary><b>(상품,회원가입)등록 혹은 로그인시 Validation 검증 </b></summary>
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

<details>
<summary><b>JPA를 활용한 웹 애플리케이션 개발</b></summary>
<div markdown="1">

### 1. 도메인 설계/JPA 전략

</div>
</details>


<details>
<summary><b>JPA를 활용한 웹 애플리케이션 개발</b></summary>
<div markdown="1">

### 1. 도메인 설계/JPA 전략

</div>
</details>




</br>

## 5. 핵심 트러블 슈팅
### 5.1.
<details>
<summary><b>기존 코드</b></summary>
<div markdown="1">

</div>
</details>

<details>
<summary><b>추가된 클래스</b></summary>
<div markdown="1">

</div>
</details>

</br>

## 6. 그 외 트러블 슈팅
<details>
<summary>제목</summary>
<div markdown="1">

</div>
</details>

<details>
<summary>~~때문에 에러가 납</summary>
<div markdown="1">

</div>
</details>

## 6. 회고 / 느낀점

*블로그 회고글 중
### 간단한 KPT
#### Keep

#### Problem

#### Try

