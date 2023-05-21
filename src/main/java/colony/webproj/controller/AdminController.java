package colony.webproj.controller;

import colony.webproj.dto.MemberMangeDto;
import colony.webproj.entity.type.SearchType;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    /**
     * 회원 정보
     */
    @GetMapping("/admin/members")
    public String allMembers(@PageableDefault(size = 10) Pageable pageable,
                             @RequestParam(required = false) SearchType searchType,
                             @RequestParam(required = false) String searchValue,
                             Model model) {
        Page<MemberMangeDto> MemberDtoList = adminService.findMemberInfo(pageable, searchType, searchValue);
        model.addAttribute("MemberDtoList", MemberDtoList);
        model.addAttribute("pageNum", pageable.getPageNumber());
        model.addAttribute("totalPages", MemberDtoList.getTotalPages());
        model.addAttribute("maxPage", 10);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        return "/admin/memberMange";
    }

}
