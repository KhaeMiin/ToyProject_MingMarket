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
import project.toyproject.domain.Member;
import project.toyproject.domain.Product;
import project.toyproject.dto.MemberDto;
import project.toyproject.dto.ProductDto;
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

    /**
     * 상품 등록
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new CreateProductForm());

        return "product/createProductForm";
    }

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

        return "redirect:/product/detail/{productId}"; // 상품디테일 페이지로 넘어가게
    }

    /**
     * 상세 페이지
     */
    @GetMapping("/detail/{productId}")
    public String ProductDetail(@PathVariable Long productId, Model model) {
        Product singleProduct = productService.findSingleProduct(productId);
        
        // 작성자 닉네임 구하기
        Member writer = memberService.findOneMember(singleProduct.getMember().getId());
        String writerNickname = writer.getNickname();
        String writerId = writer.getUserId();

        //게시글 작성 날짜 구하기
        String createDate = singleProduct.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ProductDetailData productDetailPage = new ProductDetailData(
                productId, writerNickname, singleProduct.getTitle(), singleProduct.getThumbnail(),
                singleProduct.getIntro(), singleProduct.getPrice(), createDate);

        model.addAttribute("writerId", writerId);
        model.addAttribute("singleProduct", productDetailPage);
        return "product/detailPage";
    }

    /**
     * 상품 수정
     */
    @GetMapping("/{productId}/edit")
    public String updateProductForm(@PathVariable("productId") Long productId, Model model) {
        Product singleProduct = productService.findSingleProduct(productId);

        updateProductForm form = new updateProductForm(productId, singleProduct.getTitle(),
                singleProduct.getThumbnail(), singleProduct.getIntro(), singleProduct.getPrice());

        model.addAttribute("form", form);
        return "product/updateProductForm";
    }

    @PostMapping("/{productId}/edit")
    public String update(@PathVariable Long productId,
                         @Valid @ModelAttribute("form") updateProductForm form, BindingResult result,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request
    ) throws IOException {

        if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
            return "product/updateProductForm"; //다시 폼으로 이동
        }

        String realPath = request.getSession().getServletContext().getRealPath("/upload/");// 상대 경로
        String uploadFile = fileUpload.serverUploadFile(form.getUploadFile(), realPath);

        //데이터 베이스에 저장
        productService.updateProduct(productId,form);

        redirectAttributes.addAttribute("productId", productId);

        return "redirect:/product/detail/{productId}";
    }

}
