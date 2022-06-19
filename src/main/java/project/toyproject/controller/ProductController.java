package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.toyproject.FileUpload;
import project.toyproject.annotation.LoginCheck;
import project.toyproject.domain.Product;
import project.toyproject.dto.MemberDto;
import project.toyproject.service.MemberService;
import project.toyproject.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static project.toyproject.dto.ProductDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final FileUpload fileUpload;
    private final MemberService memberService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new CreateProductForm());

        return "product/createProductForm";
    }

    /**
     * TODO
     * 현재 로그인관련 로직 안짬.
     * 추후 로그인 세션 처리 후 수정예정
     * 수행 완료 후 해당 상품 디테일 페이지로 가도록
     */
    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("form") CreateProductForm form, BindingResult result,
                         @LoginCheck MemberDto.SessionMemberData loginMember,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request
                         ) throws IOException {
        if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
            return "product/createProductForm"; //다시 폼으로 이동
        }

        String realPath = request.getSession().getServletContext().getRealPath("/upload/");// 상대 경로
        String uploadFile = fileUpload.serverUploadFile(form.getThumbnail(), realPath);

        //데이터 베이스에 저장
        Long productId = productService.saveProduct(loginMember.getMemberId(), form.getTitle(), uploadFile, form.getIntro(), form.getPrice());

        redirectAttributes.addAttribute("productId", productId);

        return "redirect:/product/detail/{productId}"; //나중에 상품디테일 페이지로 넘어가게
    }

    @GetMapping("/detail/{productId}")
    public String ProductDetail(@PathVariable Long productId, Model model) {
        Product singleProduct = productService.findSingleProduct(productId);
        // 작성자 닉네임 구하기
        String nickname = singleProduct.getMember().getNickname();
        //게시글 작성 날짜 구하기
        String createDate = singleProduct.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ProductDetailData productDetailPage = new ProductDetailData(
                nickname, singleProduct.getTitle(), singleProduct.getThumbnail(), singleProduct.getIntro(), singleProduct.getPrice(), createDate);
        model.addAttribute("singleProduct", productDetailPage);
        return "product/detailPage";
    }

    //대표이미지(thumbnail) 띄우기
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource uploadImage(@PathVariable String filename, HttpServletRequest request) throws MalformedURLException {
        String realPath = request.getSession().getServletContext().getRealPath("/upload/");// 상대 경로
        return new UrlResource("file:" + realPath + filename);
    }

}
