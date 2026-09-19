package edu.qust.userBusiness.config;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import edu.qust.common.base.Response;
import edu.qust.userBusiness.common.ClientType;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

//@Component
public class LoginSuccessHandlerImpl implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
/*        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        String code = "0";
        writer.write(JSONUtils.toJSONString(code));
        writer.flush();
        writer.close();*/

        BufferedReader reader = httpServletRequest.getReader();
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while(line != null){
            builder.append(line);
            line = reader.readLine();
        }
        reader.close();
        ClientType clientType = JSON.parseObject(builder.toString(), ClientType.class);
        if (clientType != null){
            if("vue".equals(clientType.getName())){
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                Response response = Response.SUCCESS;
                writer.write(JSONUtils.toJSONString(response));
                writer.flush();
                writer.close();
                return;
            }
        }
        httpServletResponse.sendRedirect("/");
    }
}
