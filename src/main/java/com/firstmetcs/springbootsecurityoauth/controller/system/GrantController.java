package com.firstmetcs.springbootsecurityoauth.controller.system;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author fancunshuo
 */
@Controller
@SessionAttributes("authorizationRequest")
public class GrantController {

    /**
     * @param model   Map<String, Object>
     * @param request HttpServletRequest
     * @return ModelAndView
     * @throws Exception Exception
     * @see WhitelabelApprovalEndpoint#getAccessConfirmation(Map, HttpServletRequest)
     */
    @RequestMapping("/custom/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("grant");
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scopes", authorizationRequest.getScope());
        return view;
    }

}