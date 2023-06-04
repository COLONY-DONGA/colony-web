//package colony.webproj.controller;
//
//import colony.webproj.service.CommentService;
//import colony.webproj.service.PostService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(PostController.class)
//public class PostControllerTest {
//
//    @InjectMocks
//    PostController postController;
//
//    @Mock // or @MockBean if you are using Spring Boot
//    PostService postService;
//
//    @MockBean
//    private CommentService commentService;
//
//    @MockBean
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @Test
//    void givenNothing_whenRequestingPostList_thenReturnsPostList() throws Exception {
//        // Given
//        given(postService.searchPosts(any(), any(), any())).willReturn(Page.empty());
//
//        // When & Then
//        mockMvc.perform(get("/post-list"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
////                .andExpect(model().attribute())
//                .andExpect(jsonPath("$.content").isEmpty());
//
//
//        then(postService).should().searchPosts(any(), any(), any());
//    }
//
//    // ... 여기에 다른 메서드들을 테스트하는 코드를 추가하세요 ...
//}
