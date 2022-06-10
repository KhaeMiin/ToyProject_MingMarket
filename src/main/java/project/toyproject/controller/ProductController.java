package project.toyproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.toyproject.dto.ProductDto;
import project.toyproject.service.MemberService;
import project.toyproject.service.ProductService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final MemberService memberService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new ProductDto.CreateProductForm());
        return "product/createProductForm";
    }

    /**
     * TODO
     * 현재 로그인관련 로직 안짬.
     * 추후 로그인 세션 처리 후 수정예정
     * 수행 완료 후 해당 상품 디테일 페이지로 가도록
     */
    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("form") ProductDto.CreateProductForm form, BindingResult result) {

        if (result.hasErrors()) { //만약에 result 안에 에러가 있으면
            return "product/createProductForm"; //다시 폼으로 이동
        }

/*        Member member = memberService.findOneMember(memberId);
        Product product = Product.createProduct(form.getTitle(), form.getThumbnail(), form.getIntro(), form.getPrice(), member);*/
        return "redirect:/"; //나중에 상품디테일 페이지로 넘어가게
    }
}
