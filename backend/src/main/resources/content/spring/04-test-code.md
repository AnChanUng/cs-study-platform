---
title: "Test Code"
category: spring
difficulty: INTERMEDIATE
tags: "테스트, JUnit, Mockito, 단위 테스트, 통합 테스트"
---

## 질문
Spring에서의 테스트 코드 작성 방법과 종류에 대해 설명해주세요.

## 핵심 키워드
- 단위 테스트 (Unit Test)
- 통합 테스트 (Integration Test)
- JUnit 5
- Mockito
- @SpringBootTest / @WebMvcTest

## 답변
Spring 애플리케이션의 테스트는 크게 단위 테스트와 통합 테스트로 나뉩니다. 각각의 범위와 목적에 맞는 테스트 전략을 사용합니다.

### 단위 테스트 (Unit Test)

개별 클래스나 메서드를 독립적으로 테스트합니다. 외부 의존성은 Mock으로 대체합니다.

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 조회 - 존재하는 사용자")
    void findUser_Success() {
        // Given
        User user = new User(1L, "홍길동", "hong@email.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        UserDto result = userService.findById(1L);

        // Then
        assertThat(result.getName()).isEqualTo("홍길동");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("사용자 조회 - 존재하지 않는 사용자")
    void findUser_NotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(1L);
        });
    }
}
```

### Controller 테스트 (@WebMvcTest)

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("GET /api/users/{id} - 성공")
    void getUser() throws Exception {
        UserDto user = new UserDto(1L, "홍길동");
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("홍길동"))
            .andDo(print());
    }

    @Test
    @DisplayName("POST /api/users - 유효성 검증 실패")
    void createUser_Invalid() throws Exception {
        String body = "{\"name\": \"\", \"email\": \"invalid\"}";

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }
}
```

### Repository 테스트 (@DataJpaTest)

```java
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail() {
        User user = new User("홍길동", "hong@email.com");
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("hong@email.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("홍길동");
    }
}
```

### 통합 테스트 (@SpringBootTest)

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 사용자_생성_후_조회() throws Exception {
        // 생성
        String createBody = "{\"name\":\"홍길동\",\"email\":\"hong@email.com\"}";
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBody))
            .andExpect(status().isCreated());

        // 조회
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("홍길동"));
    }
}
```

### 테스트 어노테이션 비교

| 어노테이션 | 범위 | 용도 |
|-----------|------|------|
| @ExtendWith(MockitoExtension.class) | 단위 | Service 테스트 |
| @WebMvcTest | 슬라이스 | Controller 테스트 |
| @DataJpaTest | 슬라이스 | Repository 테스트 |
| @SpringBootTest | 전체 | 통합 테스트 |

### 면접 팁
Given-When-Then 패턴으로 테스트를 구조화하세요. Mock과 Stub의 차이, @Mock과 @MockBean의 차이를 이해하고, 각 테스트 어노테이션의 범위와 적절한 사용 시기를 설명할 수 있어야 합니다.

## 꼬리 질문
1. @Mock과 @MockBean의 차이는 무엇인가요?
2. 테스트 피라미드(Test Pyramid)란 무엇인가요?
3. 테스트 격리를 보장하는 방법은?
