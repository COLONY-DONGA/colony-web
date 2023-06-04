package colony.webproj.controller;

import colony.webproj.dto.MemberManageDto;
import colony.webproj.dto.PostDto;
import colony.webproj.entity.type.SearchType;
import colony.webproj.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    /**
     * 회원 정보
     */
    @GetMapping("/admin/members")
    public String allMembers(@PageableDefault(size = 5) Pageable pageable,
                             @RequestParam(required = false) SearchType searchType,
                             @RequestParam(required = false) String searchValue,
                             Model model) {
        Page<MemberManageDto> MemberDtoList = adminService.findMemberInfo(pageable, searchType, searchValue);
        model.addAttribute("MemberDtoList", MemberDtoList);
        model.addAttribute("pageNum", pageable.getPageNumber());
        model.addAttribute("totalPages", MemberDtoList.getTotalPages());
        model.addAttribute("maxPage", 5);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        return "admin/memberManage";
    }

    @GetMapping("/admin/posts")
    public String allPosts(@PageableDefault(size = 5) Pageable pageable,
                           @RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchValue,
                           Model model) {
        Page<PostDto> postDtoList = adminService.findPostInfo(searchType, searchValue, pageable);
        model.addAttribute("postDtoList", postDtoList);
        model.addAttribute("pageNum", pageable.getPageNumber());
        model.addAttribute("totalPages", postDtoList.getTotalPages());
        model.addAttribute("maxPage", 5);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        return "admin/postManage";
    }

}
