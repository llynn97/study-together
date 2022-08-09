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





