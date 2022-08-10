# Response format

```json
{
  "code" : 200,
  "message" : "success to join member!",
  "data" : {
    // data...
  }
}
```

[ 기본 응답 형식 ]

<br>

## Error code and message

1. 사용자가 잘못된 요청 했을 때 : 4xx
2. 서버 에러일 때 : 5xx
3. 잘 갔을 때 : 2xx

<br>

# Study page

## 스터디 채팅창 메세지 보내기

- request

  ```http
  POST /api/v1/study/{study_id}/chat
  ```

  `study_id` 에 해당하는 스터디의 채팅창에 메세지 전송하기

  스터디 참여자들만이 요청 가능하다.

  ```json
  {
    "content" : "안녕하세요. 채팅 메세지입니다.",
    "messageType" : "전송"
  }
  ```

  - data
    1. message : 메세지 본문

  <br>

- Response

  - 전송 성공

    ```json
    {
      "code" : 200,
      "message" : "success to send message"
    }
    ```

- 전송 실패

  1. 방장 + 참여자들만 가능. 그 이외에는 권한 오류

     ```json
     {
       "code" : 403,
       "message" : "only study member can send"
     }
     ```

  2. 로그인 안된 사용자는 불가

  3. 서버오류

     ```json
     {
       "code" : 500,
       "message" : "server error"
     }
     ```

     

<br>

# 푸쉬, 알림 창

## 알림 조회

- request

  ```http
  GET /api/v1/notification
  ```

  로그인된 사용자의 알림 목록을 최신순으로 조회

- response

  알림 목록 조회 성공

  ```json
  {
    "code" : 200,
    "message" : "success to search notifications",
    "data" : [
      {
        "create_date" : 2022-07-23T01:03:42,
        "message" : "스터디 OOO에 가입되셨습니다!",
        "status" : "SUCCESS"
      },
      {
        "create_date" : 2022-07-19T11:32:50,
        "message" : "새로운 알림이 도착했습니다.",
        "status" : "SUCCESS"
      },
      {
        "create_date" : 2022-07-15T00:30:12,
        "message" : "새로운 메세지가 도착했습니다.",
        "status" : "SUCCESS"
      }
    ]
  }
  ```

  - `status` : 알림 전송 상태
    - `SUCCESS` : 성공
    - `FAIL` : 실패

- 조회 실패

  로그인 안된 사용자는 불가

  => 이거는 Spring Security에서 알아서 해줄 것

  

  서버 오류

  ```json
  {
    "code" : 500,
    "message" : "server error"
  }
  ```

  <br>

## 알림 삭제

- request

  ```http
  DELETE /api/v1/notification
  ```

  로그인된 사용자의 현재까지 받은 모든 알림 삭제되게. DB에서 삭제한다는 것

- response

  메세지

  1. 삭제 성공

     ```json
     {
       "code" : 200,
       "message" : "success to delete notifications"
     }
     ```

     

  2. 삭제 실패

     1. 이미 존재하지 않는 알림

        ```json
        {
          "code" : 404,
          "message" : "notification not founded"
        }
        ```

        

     2. 로그인 안된 사용자는 불가 => 이거는 Spring Security에서 알아서 해줄 것

  3. 서버오류

     ```json
     {
       "code" : 500,
       "message" : "server error"
     }
     ```

     <br>
     
# 회원가입
     
## 일반 회원가입
* Request

  ```http
  POST /api/v1/auth/local/new 
  ```
  <br/>

  아이디와 이메일 중복확인은 api로 회원가입 전 확인함 

  ```json
   {
      "id": "aaeun",
      "password": "1234@cda*",
      "email": "aaeun@gmail.com"
   }
  ```
  1. 사용자 닉네임: id (프론트엔드에서 15글자 제한 확인)
  2. 비밀번호: password (프론트엔드에서 8자이상, 영문, 숫자, 특수문자 조합조건 확인)
  3. 이메일: email  (프론트엔드에서 100글자 제한, 이메일 형식인지 확인)

<br/>

* Response 

  * 회원가입 성공
   
    ```json
    {
        "code": 200,
        "message": "success to join"
    }
    ```  
  <br/>
  
* 회원가입 실패
   
   1. 서버 오류

      ```json
      {
         "code": 500,
         "message": "server error"
      }
      ```
      

<br/><br/>

## 이메일 중복 확인

* request

  ```http 
  GET /api/v1/auth/local/checkemail/{email}
  ```
  
  일반 회원가입 전 이메일 중복 확인을 해야함
  
  <br/>

* response
  * 중복 확인 성공 -  중복된 이메일일 경우

    ```json
    {
       "code": 200,
       "message": "success to duplicate-check-email",
       "data": {
         "result" : false
        } 
    }
    ``` 


  * 중복 확인 성공 - 중복되지 않은 이메일일 경우 

    ```json
    {
      "code": 200,
       "message": "success to duplicate-check-email",
       "data": {
         "result" : true
        }   
    }
    ```
  
  1. 이메일 중복 여부: result (중복된 이메일이면 false , 중복된 이메일이 아니면 true)
  
  <br/>

* 이메일 중복 확인 실패
  
  1. 서버 오류

     ```json
     {
        "code": 500,
        "message": "server error"
     }
     ```

<br/><br/>

## 아이디 중복 확인

* request

  ```http 
  GET /api/v1/auth/local/checkid/{id}
  ```
  
  일반 회원가입 전 아이디 중복 확인을 해야함
  
  <br/>

* response
  * 중복 확인 성공 - 중복된 아이디일 경우

    ```json
    {
       "code": 200,
       "message": "success to duplicate-check-id",
       "data": {
         "result" : false
        } 
    }
    ``` 


  * 중복 확인 성공 - 중복되지 않은 아이디일 경우 

    ```json
    {
      "code": 200,
       "message": "success to duplicate-check-id",
       "data": {
         "result" : true
        }   
    }
    ```
  
  1. 아이디 중복 여부 : result (중복된 아이디이면 false , 중복된 아이디가 아니면 true)
  
  <br/>

* 아이디 중복 확인 실패
  
  1. 서버 오류

     ```json
     {
        "code": 500,
        "message": "server error"
     }
     ```


<br/><br/>
# 로그인
## 일반 로그인
* Request

  ```http
  POST /api/v1/auth/local 
  ```
  <br/>

   

  ```json
   {
      "id": "aaeun",
      "password": "1234@cda*",
      "method": "일반"
   }
  ```
  1. 비밀번호: password
  2. 이메일: email
  3. 로그인 방법: method(카카오/일반)

<br/>

* Response 

  * 로그인 성공
   
    ```json
    {
        "code": 200,
        "message": "success to login"
    }
    ```  
  <br/>
  
* 로그인 실패
   1. 아이디나 비밀번호가 일치하지 않을 때
      ```json
      {
         "code": 401,
         "message": "fail to login"
      }
      ```
      <br/>
   2. 서버 오류

      ```json
      {
         "code": 500,
         "message": "server error"
      }
      ```
      

<br/><br/>

## 카카오 로그인
* Request

  ```http
  GET /oauth2/authorization/kakao
  ```
  <br/>

   
    카카오 로그인 버튼을 누르면 해당 URL로 요청이 감 

<br/>

* Response 

  * 로그인 성공
   
    ```json
    {
        "code": 200,
        "message": "success to login"
    }
    ```  
    1. 사용자가 회원가입이 되어 있지 않으면 회원가입을 한 후 로그인 성공 메시지를 보냄
    2. http ://리액트주소:리액트포트/uri?token=JWT토큰 주소로 리다이렉트
  <br/>
  
* 로그인 실패
   1. 아이디나 비밀번호가 일치하지 않을 때
      ```json
      {
         "code": 401,
         "message": "fail to login"
      }
      ```
      <br/>
   2. 서버 오류

      ```json
      {
         "code": 500,
         "message": "server error"
      }
      ```




