package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.toyproject.service.*;
import project.toyproject.annotation.LoginCheck;
import project.toyproject.dto.ProductDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static project.toyproject.dto.MemberDto.*;
import static project.toyproject.dto.ProductDto.*;
import static project.toyproject.dto.WishItemDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final WishItemService wishItemService;
    private final FileUpload fileUpload;
    private final MemberService memberService;
    private final SearchService searchService;

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
                         @LoginCheck SessionMemberData loginMember,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request
    ) throws IOException {
        if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
            return "product/createProductForm"; //다시 폼으로 이동
        }

        String realPath = request.getSession().getServletContext().getRealPath("/upload/");// 저장 경로
        String uploadFile = fileUpload.serverUploadFile(form.getThumbnail(), realPath);

        //데이터 베이스에 저장
        Long productId = productService.saveProduct(loginMember.getMemberId(), form.getTitle(), uploadFile, form.getIntro(), form.getPrice(), form.getCategoryList());

        redirectAttributes.addAttribute("productId", productId);

        return "redirect:/product/detail/{productId}"; // 상품디테일 페이지로 넘어가게
    }

    /**
     * 상세 페이지
     */
    @GetMapping("/detail/{productId}")
    public String ProductDetail(@PathVariable Long productId, Model model, HttpServletRequest request) {
        ProductDetailData singleProduct = productService.findSingleProduct(productId);

        // 작성자 닉네임 구하기
        SelectMemberData writer = memberService.findOneMember(singleProduct.getMember().getId());

        model.addAttribute("writerId", writer.getUserId());
        model.addAttribute("singleProduct", singleProduct);

        //찜상품인지 체크
        HttpSession session = request.getSession(false);
        FindWishItem wishItem = null;
        try {
            SessionMemberData loginMember = (SessionMemberData) session.getAttribute("loginMember");
            wishItem = wishItemService.findOneWishItem(loginMember.getMemberId(), productId);
        } catch (Exception e) {
            e.getMessage();
        }
        if (wishItem != null) {
            model.addAttribute("wishItem", wishItem);
        }

        return "product/detailPage";
    }

    /**
     * 상품 수정
     */
    @GetMapping("/{productId}/edit")
    public String updateProductForm(@PathVariable("productId") Long productId, Model model) {
        ProductDetailData findProduct = productService.findSingleProduct(productId);
        UpdateProductForm form = new UpdateProductForm(productId, findProduct.getTitle(),
                findProduct.getThumbnail(), findProduct.getIntro(), findProduct.getPrice());

        model.addAttribute("form", form);
        return "product/updateProductForm";
    }

    @PostMapping("/{productId}/edit")
    public String update(@PathVariable Long productId,
                         @Valid @ModelAttribute("form") ProductDto.UpdateProductForm form, BindingResult result,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request
    ) throws IOException {

        if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
            return "product/updateProductForm"; //다시 폼으로 이동
        }

        String realPath = request.getSession().getServletContext().getRealPath("/upload/");// 상대 경로
        String uploadFile = "";

        if (form.getUploadFile().getOriginalFilename().equals("")) { // 이미지 수정 안할경우
            uploadFile = form.getUploadFileName();
        } else {
            File file = new File(realPath + form.getUploadFileName());
            file.delete(); //기존 이미지 삭제하기

            uploadFile = fileUpload.serverUploadFile(form.getUploadFile(), realPath);
        }

        //데이터 베이스에 저장
        productService.updateProduct(productId, form, uploadFile);

        redirectAttributes.addAttribute("productId", productId);

        return "redirect:/product/detail/{productId}"; // 상품디테일 페이지로 넘어가게
    }

    /**
     * 상품 삭제
     */
    @GetMapping("/{productId}/delete")
    public String removeProduct(@PathVariable("productId") Long productId, HttpServletRequest request) {
        ProductDetailData singleProduct = productService.findSingleProduct(productId);
        String realPath = request.getSession().getServletContext().getRealPath("/upload/");// 상대 경로

        File file = new File(realPath + singleProduct.getThumbnail());
        file.delete(); // 대표 이미지 파일 지우기

        productService.removeProduct(productId);
        return "redirect:/";
    }

    /**
     * 내 상점(내가 올린 상품 || 내가 찜한 상품(관심 상품)
     */
    @GetMapping("/shop/{memberId}")
    public String userProductList(@PathVariable("memberId") Long memberId, Model model) {
        List<SelectProducts> selectProducts = productService.userProductsList(memberId);
        model.addAttribute("products", selectProducts);
        return "product/myProductList";
    }

    /**
     * 찜하기
     */
    @ResponseBody
    @PostMapping("/addWishItem")
    public Long addWishItem(@RequestParam("productId") Long productId,
                              @RequestParam("memberId") Long memberId) {
        Long wishId = wishItemService.addWishItem(memberId, productId);
        return wishId;
    }

    /**
     * 찜 취소
     */
    @ResponseBody
    @PostMapping("/cancelWishItem")
    public void cancelWishItem(@RequestParam("wishId") Long wishId) {
        wishItemService.cancelWishItem(wishId);
    }


    /**
     * 상품 검색
     */
    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<SelectProducts> selectProducts = searchService.searchPosts(keyword);
        model.addAttribute("products", selectProducts);

        return "home";
    }
}
