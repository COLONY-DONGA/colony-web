//package colony.webproj.service;
//
//import colony.webproj.InitDB;
//import colony.webproj.dto.PostDto;
//import colony.webproj.dto.PostFormDto;
//import colony.webproj.entity.Image;
//import colony.webproj.entity.Member;
//import colony.webproj.entity.Post;
//import colony.webproj.entity.type.SearchType;
//import colony.webproj.repository.ImageRepository;
//import colony.webproj.repository.MemberRepository;
//import colony.webproj.repository.PostRepository.PostRepository;
//import colony.webproj.service.ImageService;
//import colony.webproj.service.PostService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.*;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//public class PostServiceTest {
//
//    @Mock
//    private ImageRepository imageRepository;
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private ImageService imageService;
//
//    @InjectMocks
//    private PostService postService;
//
//    private List<Post> postList;
//    private List<Image> imageList;
//    private Pageable pageable;
//
//
//    @Autowired
//    private InitDB initDB;
//
//
//    @BeforeEach
//    public void setup() {
//        postList = new ArrayList<>();
//        imageList = new ArrayList<>();
//        pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));
////        initDB.init();
//    }
//
//    @Test
//    public void testSearchPosts_WithoutSearchKeyword_ReturnsAllPosts1() {
//        // Given
//        // Create and save test data
//        Post post1 = new Post(null,"Title 1", "Content 1",false,null,null,null);
//
//        postRepository.save(post1);
//
//
//        // When
//        Page<PostDto> result = postService.searchPosts(SearchType.TITLE, null, pageable);
//
//        // Then
//        List<PostDto> postDtoList = result.getContent();
//        Assertions.assertEquals(1, postDtoList.size());
//        // Add more assertions based on your expected behavior
//    }
//
//
//    @DisplayName("검색어 없이 게시글을 찾고 모든 게시글을 반환한다.")
//    @Test
//    public void testSearchPosts_WithoutSearchKeyword_ReturnsAllPosts() {
//        // Given
//        Page<Post> mockPage = new PageImpl<>(postList);
//        when(postRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
//
//        // When
//        Page<PostDto> result = postService.searchPosts(SearchType.TITLE, null, pageable);
//
//        // Then
//        List<PostDto> postDtoList = result.getContent();
//        System.out.println(postDtoList.get(0));
//        Assertions.assertEquals(postList.size(), result.getTotalElements());
//        // Add more assertions based on your expected behavior
//    }
//
//    @Test
//    public void testSearchPosts_WithTitleSearchType_ReturnsFilteredPosts() {
//        // Given
//        String searchKeyword = "example";
//        when(postRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(postList));
//
//        // When
//        Page<PostDto> result = postService.searchPosts(SearchType.TITLE, searchKeyword, pageable);
//
//        // Then
//        Assertions.assertEquals(postList.size(), result.getTotalElements());
//        // Add more assertions based on your expected behavior
//    }
//
//    // Add more test cases for other functions in PostService
//
//    // Example test case for savePost
////    @Test
////    public void testSavePost() throws IOException {
////        // Given
////        PostFormDto postFormDto = new PostFormDto(null,"테스트제목","테스트 내용",null,null);
////        String loginId = "example";
////        Member member = new Member(null,);
////        member.setLoginId(loginId);
////        when(memberRepository.findByLoginId(anyString())).thenReturn(java.util.Optional.of(member));
////        when(postRepository.save(any(Post.class))).thenReturn(new Post());
////        when(imageService.uploadFile(anyList())).thenReturn(imageList);
////
////        // When
////        Long savedPostId = postService.savePost(postFormDto, loginId);
////
////        // Then
////        Assertions.assertNotNull(savedPostId);
////        // Add more assertions based on your expected behavior
////    }
//}
