package com.tuanvuong.qtsnearker.controller.administrator;

import com.tuanvuong.qtsnearker.entity.User;
import com.tuanvuong.qtsnearker.entity.Role;
import com.tuanvuong.qtsnearker.services.exceptions.UserNotFoundException;
import com.tuanvuong.qtsnearker.services.UserService;
import com.tuanvuong.qtsnearker.util.FileUploadUtil;
import com.tuanvuong.qtsnearker.util.UserExcelExporter;
import com.tuanvuong.qtsnearker.util.UserPdfExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model){
        return listByPage( "firstName", "asc", null,1, model);

    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             @PathVariable(name = "pageNum") int pageNum,
                             Model model){

        System.out.println("sort field:" +sortField);
        System.out.println("sort dir:" +sortDir);
        System.out.println("keyword:" +keyword);

        Page<User> page = userService.listByPage(pageNum,sortField,sortDir,keyword);

        List<User> listUsers = page.getContent();

        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;

        long endCount = startCount + UserService.USERS_PER_PAGE-1;

        if(endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }

        // đảo ngược giá trị
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);

        model.addAttribute("totalPages", page.getTotalPages());

        model.addAttribute("startCount", startCount);

        model.addAttribute("endCount", endCount);

        model.addAttribute("totalItem", page.getTotalElements());

        model.addAttribute("listUser", listUsers);

        model.addAttribute("sortField", sortField);

        model.addAttribute("sortDir", sortDir);

        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("keyword", keyword);

        model.addAttribute("moduleURL", "/admin/users");

        model.addAttribute("pageTitle","Danh sách khách hàng");


        return "administrator/user/user";
    }


    @GetMapping("/users/create")
    public String newUser(Model model){

        User user = new User();

        List<Role> listRoles = userService.findRoleAll();

        user.setEnabled(true);

        model.addAttribute("user", user);

        model.addAttribute("listRoles", listRoles);

        model.addAttribute("pageTitle","Thêm mới");

        return "administrator/user/user_form";
    }

    /*
    * getOriginalFilename() lấy tên gốc của tệp.
    * StringUtils.cleanPath() Làm sạch tên của tệp được tải lên.
    * */
    @PostMapping("/users/save")
    public String saveUser(User user,
                           RedirectAttributes redirectAttributes,
                           @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            user.setPhotos(fileName);

            User savedUser = userService.save(user);

            // đường dẫn thư mục ->  tạo thư mục "user-photos"
            String uploadDir = "src/main/resources/static/user-photos/" +savedUser.getId();

            // xóa ảnh cũ
            FileUploadUtil.cleanDir(uploadDir);

            // lưu tệp vào mục chỉ định
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message","The user has been saved successfully");

        return getRedirectURLtoAffectedUser(user);

    }

    private String getRedirectURLtoAffectedUser(User user) {
        // split("@")
        // tách  phần đầu của địa chỉ email
        String firstPartOfEmail = user.getEmail().split("@")[0];

        return "redirect:/admin/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    /* redirectAttributes.addFlashAttribute("key", "value")
    * khi truyền thông điệp hoặc dữ liệu từ một request tới một redirect request
    * khi thêm một flash attribute, dữ liệu đó sẽ được lưu trữ tạm thời trong session
    * và chỉ tồn tại cho một request redirect duy nhất.
    * Sau khi request redirect hoàn tất, dữ liệu flash attribute sẽ bị xóa.
    * */

     @GetMapping ("/users/edit/{id}")
     public String editUser(@PathVariable(name = "id") Integer id,
                            Model model,
                            RedirectAttributes redirectAttributes )  {
         try{
             User user =userService.findById(id);

             List<Role> listRoles = userService.findRoleAll();

             model.addAttribute("user", user);

             model.addAttribute("listRoles", listRoles);

             model.addAttribute("pageTitle","Cập nhật (ID: "+id+")");
         }
         catch (UserNotFoundException ex){

             redirectAttributes.addFlashAttribute("message",ex.getMessage());
         }

         return "administrator/user/user_form";
     }

     @GetMapping ("/users/delete/{id}")
     public String deleteUser(@PathVariable(name = "id") Integer id,
                            Model model,
                            RedirectAttributes redirectAttributes )  {
         try{
             userService.delete(id);

             redirectAttributes.addFlashAttribute("message", "The user ID:"+id+" has been deleted successfully" );
         }
         catch (UserNotFoundException ex){

             redirectAttributes.addFlashAttribute("message",ex.getMessage());
         }
         return "redirect:/admin/users";
     }

     @GetMapping("/users/{id}/enabled/{status}")
     public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                           @PathVariable("status") boolean enabled,
                                           RedirectAttributes redirectAttributes){

         userService.updateUserEnableStatus(id, enabled);

         String status = enabled ? "enabled" : "disabled";

         String message = "The user ID:" +id +" has been " + status;

         redirectAttributes.addFlashAttribute("message",message);

         return "redirect:/admin/users";
     }

     /*
     * xuất file excel
     * */
     @GetMapping("/users/export/excel")
     public void exportToExcel(HttpServletResponse response) throws IOException{

         List<User> listUsers = userService.findUserAll();

         UserExcelExporter exporter = new UserExcelExporter();

         exporter.export(listUsers, response);

     }

     /*
     * xuất file pdf
     * */
    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException{

        List<User> listUsers = userService.findUserAll();

        UserPdfExporter exporter = new UserPdfExporter();

        exporter.export(listUsers, response);

    }

}
