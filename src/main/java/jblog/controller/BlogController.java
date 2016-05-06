package jblog.controller;

import jblog.config.Config;
import jblog.service.BlogService;
import jblog.service.CategoryService;
import jblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PostService postService;

    @RequestMapping("/{id}/blog-admin-basic")
    public String blogAdminBasic(
        @PathVariable("id") String id,
        Model model) {
        model.addAttribute("blogVo", blogService.getByID(id));
        return "blog/blog-admin-basic";
    }

    @RequestMapping("/{id}/blog-admin-category")
    public String blogAdminCategroy(
        @PathVariable("id") String id,
        Model model) {
        model.addAttribute("blogVo", blogService.getByID(id));
        model.addAttribute("categoryList", categoryService.getListByID(id));
        return "blog/blog-admin-category";
    }

    @RequestMapping("/{id}/blog-admin-write")
    public String blogAdminWrite(
        @PathVariable("id") String id,
        Model model) {
        model.addAttribute("blogVo", blogService.getByID(id));
        model.addAttribute("categoryList", categoryService.getListByID(id));
        return "blog/blog-admin-write";
    }

    @RequestMapping("/{id}/blog-main")
    public String blogMain(
        @PathVariable("id") String id,
        //TODO default value 해당 블로그에 맞게 수정
        @RequestParam(
            value = "category-no", required = true, defaultValue = "1")
            Long categoryNo,
        @RequestParam(
            value = "post-no", required = true, defaultValue = "1")
            Long postNo,
        Model model) {
        //        Map<String, Object> map = new HashMap<String, Object>();
        //        map.put("categoryList", categoryService.getListByID(id));
        //        map.put("postList", postService.getListByCategoyNo
        // (categoryNo));
        if ("".equals(categoryNo) || categoryNo == null) {

        }

        if ("".equals(postNo) || postNo == null) {

        }
        model.addAttribute("blogVo", blogService.getByID(id));
        model.addAttribute("categoryList", categoryService.getListByID(id));
//        System.out.println(categoryNo + " " + postNo);
        /* 해당 카테고리에서 가장 최신 글 */
        model.addAttribute("postVo",
            postService.mainPostByCategoryNo(categoryNo));
        //        model.addAttribute("map", map);
        //        System.out.println(map);
        model.addAttribute(
            "postList", postService.getListByCategoyNo(categoryNo));
        return "blog/blog-main";
    }

    @RequestMapping("{id}/changeSettings")
    public String changeSettings(
        @PathVariable("id") String id,
        @RequestParam("blog-name") String blogName,
        @RequestParam("logo-file") MultipartFile logoImg,
        Model model) {
        String img = null;
        model.addAttribute("blogVo", blogService.getByID(id));
        if (logoImg.isEmpty() == false) {
            String originFileName = logoImg.getOriginalFilename();
            String extName = originFileName.substring(
                originFileName.lastIndexOf(".") + 1,
                originFileName.length());
            String saveFileName = blogService.generateFileName(extName);
            blogService.uploadFile(
                logoImg, Config.FILE_SAVE_PATH, saveFileName);

            img = "/jblog/product-images/" + saveFileName;
        }
        blogService.changeSettings(id, blogName, img);
        return "redirect:/" + id + "/blog-main";
    }
}
