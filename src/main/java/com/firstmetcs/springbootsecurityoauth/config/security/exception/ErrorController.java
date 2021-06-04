package com.firstmetcs.springbootsecurityoauth.config.security.exception;//package com.firstmetcs.springbootsecurity.config.security.exception;
//
//import org.springframework.boot.autoconfigure.web.ErrorProperties;
//import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
//import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.OrRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//public class ErrorController extends BasicErrorController {
//
//    private static final String OAUTH_TOKEN_URL = "/oauth/token";
//
//    public ErrorController() {
//        super(new DefaultErrorAttributes(), new ErrorProperties());
//    }
//
//    @Override
//    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
//        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
//        RequestMatcher requestMatcher = new OrRequestMatcher(
//                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "GET"),
//                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "POST")
//        );
//        ErrorProperties errorProperties = getErrorProperties();
////        if (requestMatcher.matches(request)){
////            return
////        }
//        HttpStatus status = getStatus(request);
//        Map<String, Object> map = new HashMap<>();
//        map.put("message", "error");
//        return new ResponseEntity<>(body, status);
//    }
//}
