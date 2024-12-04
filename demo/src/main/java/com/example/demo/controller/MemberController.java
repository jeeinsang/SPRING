package com.example.demo.controller;
import com.example.demo.model.domain.TestDB;
import com.example.demo.model.service.BlogService;
import com.example.demo.model.service.Member_Service;
import com.example.demo.model.service.TestService; // 최상단 서비스 클래스 연동 추가

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.demo.model.domain.Board;
import com.example.demo.model.domain.Member;
import com.example.demo.model.domain.Article;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.AddMemberRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Controller

public class MemberController {
    @Autowired
    Member_Service memberService;

    // @GetMapping("/article_list") // 게시판 링크 지정
    // public String article_list(Model model) {
    // List<Article> list = blogService.findAll(); // 게시판 리스트
    // model.addAttribute("articles", list); // 모델에 추가
    // return "article_list"; // .HTML 연결
    // }

    // @GetMapping("/article_edit/{id}") // 게시판 링크 지정
    // public String article_edit(Model model, @PathVariable Long id) {
    //     Optional<Article> list = blogService.findById(id); // 선택한 게시판 글

    //     if (list.isPresent()) {
    //         model.addAttribute("article", list.get()); // 존재하면 Article 객체를 모델에 추가
    //     } else {
    //         // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
    //         return "/error_page/article_error"; // 오류 처리 페이지로 연결(이름 수정됨)
    //     }
    //     return "article_edit"; // .HTML 연결
    // }
    // @PutMapping("/api/article_edit/{id}")
    // public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    //     blogService.update(id, request);
    //     return "redirect:/article_list"; // 글 수정 이후 .html 연결
    // }
    // @DeleteMapping("/api/article_delete/{id}")
    // public String deleteArticle(@PathVariable Long id) {
    //     blogService.delete(id);
    //     return "redirect:/article_list";
    // }
    // 
    // @GetMapping("/board_list") // 새로운 게시판 링크 지정
    // public String board_list(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword) {
    //     PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
    //     Page<Board> list; // Page를 반환

    //     if (keyword.isEmpty()) {
    //         list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
    //     } else {
    //         list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
    //     }
    //     model.addAttribute("boards", list); // 모델에 추가
    //     model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
    //     model.addAttribute("currentPage", page); // 페이지 번호
    //     model.addAttribute("keyword", keyword); // 키워드
    //     return "board_list"; // .HTML 연결
    // }

    // @GetMapping("/board_view/{id}") // 게시판 링크 지정
    // public String board_view(Model model, @PathVariable Long id) {
    //     Optional<Board> list = blogService.findById(id); // 선택한 게시판 글
    
    //     if (list.isPresent()) {
    //         model.addAttribute("boards", list.get()); // 존재할 경우 실제 Article 객체를 모델에 추가
    //     } else {
    //         // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
    //         return "/error_page/article_error"; // 오류 처리 페이지로 연결
    //     }
    //     return "board_view"; // .HTML 연결
    // }
    // @GetMapping("/board_write")
    // public String board_write() {
    //     return "board_write";
    // }
    // @PostMapping("/api/boards") // 글쓰기 게시판 저장
    // public String addboards(@ModelAttribute AddArticleRequest request) {
    //     blogService.save(request);
    //     return "redirect:/board_list"; // .HTML 연결
    // }
    @GetMapping("/join_new") // 회원 가입 페이지 연결
    public String join_new() {
        return "join_new"; // .HTML 연결
    }

    @PostMapping("/api/members") // 회원 가입 저장
    public String addmembers(@ModelAttribute AddMemberRequest request) {
        memberService.saveMember(request);
        return "join_end"; // .HTML 연결
    }
    @GetMapping("/member_login") // 로그인 페이지 연결
    public String member_login() {
        return "login"; // .HTML 연결
    }
    @PostMapping("/api/login_check") // 아이디, 패스워드 로그인 체크
   public String checkMembers(@ModelAttribute AddMemberRequest request, Model model, HttpServletRequest request2, HttpServletResponse response) {
    try {
        HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
        if (session != null) {
            session.invalidate(); // 기존 세션 무효화
            Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID 초기화
            cookie.setPath("/"); // 쿠키 경로
            cookie.setMaxAge(0); // 쿠키 삭제(0으로 설정)
            response.addCookie(cookie); // 응답으로 쿠키 전달
        }
        session = request2.getSession(true); // 새로운 세션 생성
        Member member = memberService.loginCheck(request.getEmail(), request.getPassword());
        String sessionId = UUID.randomUUID().toString(); // 임의의 고유 ID로 세션 생성
        String email = request.getEmail(); // 이메일 얻기
        session.setAttribute("userId", sessionId); // 아이디 이름 설정
        session.setAttribute("email", email); // 이메일 설정
        model.addAttribute("member", member); // 로그인 성공 시 회원 정보 전달
    // @PostMapping("/api/login_check") // 로그인(아이디, 패스워드) 체크
    // public String checkMembers(@ModelAttribute AddMemberRequest request, Model model) {
    // try {
    //     Member member = memberService.loginCheck(request.getEmail(), request.getPassword()); // 패스워드 반환
    //     model.addAttribute("member", member); // 로그인 성공 시 회원 정보 전달
        return "redirect:/board_list"; // 로그인 성공 후 이동할 페이지
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
        return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
    }
    }
    @GetMapping("/api/logout") // 로그아웃 버튼 동작
    public String member_logout(Model model, HttpServletRequest request2, HttpServletResponse response) {

        try {
            HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
                session.invalidate(); // 기존 세션 무효화
                Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID is the default session cookie name
                cookie.setPath("/"); // Set the path for the cookie
                cookie.setMaxAge(0); // Set cookie expiration to 0 (removes the cookie)
                response.addCookie(cookie); // Add cookie to the response
                session = request2.getSession(true); // 새로운 세션 생성
                System.out.println("세션 userId: " + session.getAttribute("userId" )); // 초기화 후 IDE 터미널에 세션 값 출력
                return "login"; // 로그인 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
        }
}