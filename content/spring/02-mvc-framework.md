---
title: "MVC Framework"
category: spring
difficulty: BASIC
tags: "MVC, Spring MVC, DispatcherServlet, 컨트롤러, 뷰 리졸버"
---

## 질문
Spring MVC의 동작 원리에 대해 설명해주세요.

## 핵심 키워드
- Model-View-Controller
- DispatcherServlet
- HandlerMapping
- ViewResolver
- @Controller / @RestController

## 답변
Spring MVC는 Model-View-Controller 패턴을 기반으로 웹 애플리케이션을 개발하기 위한 프레임워크입니다. DispatcherServlet이 모든 요청의 진입점 역할을 합니다.

### MVC 패턴

- **Model**: 데이터와 비즈니스 로직 (Service, Repository)
- **View**: 사용자에게 보여지는 화면 (JSP, Thymeleaf, JSON)
- **Controller**: 요청을 처리하고 Model과 View를 연결

### Spring MVC 동작 흐름

```
1. 클라이언트 요청 (HTTP Request)
         ↓
2. DispatcherServlet (Front Controller)
         ↓
3. HandlerMapping → 요청에 맞는 Controller 탐색
         ↓
4. HandlerAdapter → Controller 메서드 실행
         ↓
5. Controller → 비즈니스 로직 처리, ModelAndView 반환
         ↓
6. ViewResolver → 적절한 View 탐색
         ↓
7. View → 렌더링 후 응답 (HTTP Response)
```

### 주요 구성요소

**DispatcherServlet**: 모든 HTTP 요청의 진입점. Front Controller 패턴.

**HandlerMapping**: URL과 Controller 메서드를 매핑합니다.

**HandlerAdapter**: 다양한 형태의 Controller를 실행합니다.

**ViewResolver**: 뷰 이름을 실제 뷰 객체로 변환합니다.

### Controller 구현

```java
// View를 반환하는 Controller
@Controller
public class UserController {
    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list"; // View 이름 반환
    }
}

// REST API Controller (JSON 반환)
@RestController
@RequestMapping("/api/users")
public class UserApiController {
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.findAll(); // JSON 자동 변환
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserDto user = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

### @Controller vs @RestController

| 구분 | @Controller | @RestController |
|------|------------|-----------------|
| 반환 | View 이름 | 데이터(JSON/XML) |
| @ResponseBody | 필요 | 자동 포함 |
| 용도 | SSR (서버 사이드 렌더링) | REST API |

### 면접 팁
DispatcherServlet의 역할과 요청 처리 흐름을 순서대로 설명할 수 있어야 합니다. @Controller와 @RestController의 차이, @RequestBody와 @ResponseBody의 역할도 중요합니다.

## 꼬리 질문
1. DispatcherServlet의 역할은 무엇인가요?
2. @RequestBody와 @ModelAttribute의 차이는?
3. Spring MVC에서 예외 처리는 어떻게 하나요?
