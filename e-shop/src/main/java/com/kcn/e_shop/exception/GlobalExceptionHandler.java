package com.kcn.e_shop.exception;

import com.kcn.e_shop.config.AdminConfig;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final AdminConfig adminConfig;


    @ExceptionHandler({IllegalArgumentException.class,
            IllegalStateException.class,
            EntityNotFoundException.class,
            MethodArgumentTypeMismatchException.class,
            NoHandlerFoundException.class})
    public String handleUserExceptions(Exception e,
                                       RedirectAttributes redirectAttributes,
                                       HttpServletRequest request,
                                       Principal principal) {
        logException(e, request, principal, "[User Error]");
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(AdminOperationException.class)
    public String handleAdminExceptions(AdminOperationException e,
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request,
                                        Principal principal) {
        logExceptionForAdmin(e, request, principal, "[Admin Operation Failed]");
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/admin/users";
    }

    @ExceptionHandler(ProductDeletionException.class)
    public String handleProductDeletionException(ProductDeletionException e,
                                                 RedirectAttributes redirectAttributes,
                                                 HttpServletRequest request,
                                                 Principal principal) {

        logExceptionForAdmin(e, request, principal, "[Product deletion failed]");
        redirectAttributes.addFlashAttribute("errorMessage",
                e.getMessage() + "\n Consider adjusting the stock quantity instead of deleting the product.");
        return "redirect:/product/" + e.getProductId();
    }

    @ExceptionHandler(CategoryDeletionException.class)
    public String handleCategoryDeletionException(CategoryDeletionException e,
                                                  RedirectAttributes redirectAttributes,
                                                  HttpServletRequest request,
                                                  Principal principal) {
        logExceptionForAdmin(e, request, principal, "[Category deletion failed]");
        redirectAttributes.addFlashAttribute("errorMessage",
                e.getMessage() + "\n Consider deleting individual products instead of the entire category.");
        return "redirect:/categories/" + e.getCategoryName();
    }

    @ExceptionHandler(EmbeddingOperationException.class)
    public String handleEmbeddingException(EmbeddingOperationException e,
                                           Model model,
                                           HttpServletRequest request,
                                           Principal principal) {
        logExceptionForAdmin(e, request, principal, "[Embedding Error]");
        model.addAttribute("errorMessage", "Embedding error: " + e.getMessage());
        return "product/add-product";
    }

    @ExceptionHandler({IOException.class, FileStorageException.class})
    public String handleFileExceptions(Exception e,
                                       Model model,
                                       HttpServletRequest request,
                                       Principal principal) {
        logExceptionForAdmin(e, request, principal, "[File Error]");
        model.addAttribute("errorMessage", "File error: " + e.getMessage());
        return "product/add-product";
    }

    @ExceptionHandler(FileDeleteException.class)
    public String handleFileDeleteException(FileDeleteException e,
                                            RedirectAttributes redirectAttributes,
                                            HttpServletRequest request,
                                            Principal principal) {
        logExceptionForAdmin(e, request, principal, "[File Error]");
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler({
            org.thymeleaf.exceptions.TemplateInputException.class,
            org.thymeleaf.exceptions.TemplateProcessingException.class
    })
    public String handleThymeleafErrors(Exception e,
                                        Model model,
                                        HttpServletRequest request,
                                        Principal principal) {

        logExceptionForAdmin(e, request, principal, "[Thymeleaf Rendering Error]");

        String errorMessage;

        if (principal != null && principal.getName().equals(adminConfig.getUsername())) {
            // for admin
            errorMessage = "Thymeleaf rendering error: " + e.getMessage();
        } else {
            errorMessage = "An internal rendering error occurred. Please try again later.";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "error/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleAll(Exception e,
                            Model model,
                            HttpServletRequest request,
                            Principal principal) {
        logExceptionForAdmin(e, request, principal, "[Unexpected Error]");
        model.addAttribute("errorMessage", "Unexpected error: " + e.getMessage());
        return "/error/error";
    }


    private void logException(Exception e, HttpServletRequest request, Principal principal, String prefix) {
        String username = (principal != null) ? principal.getName() : "anonymous";
        String clientIp = request.getRemoteAddr();
        String uri = request.getRequestURI();

        log.warn("{} {} | User: {} | IP: {} | Path: {}", prefix, e.getMessage(), username, clientIp, uri);
    }

    private void logExceptionForAdmin(Exception e, HttpServletRequest request, Principal principal, String prefix) {
        String username = (principal != null) ? principal.getName() : "anonymous";
        String clientIp = request.getRemoteAddr();
        String uri = request.getRequestURI();

        log.error("{} {} | User: {} | IP: {} | Path: {}", prefix, e.getMessage(), username, clientIp, uri);
    }
}


