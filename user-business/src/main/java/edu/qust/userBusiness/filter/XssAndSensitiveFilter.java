package edu.qust.userBusiness.filter;


import com.alibaba.fastjson.JSON;
import edu.qust.common.base.Constant;
import edu.qust.common.component.security.sensitive.plugin.SensitiveFilter;
import edu.qust.userService.param.StuffParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class XssAndSensitiveFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equalsIgnoreCase("/stuff/out",httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase("POST",httpServletRequest.getMethod()))
        {
            BufferedReader reader = httpServletRequest.getReader();
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while(line != null){
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
            StuffParam stuffParam = JSON.parseObject(builder.toString(), StuffParam.class);
            String name = stuffParam.getName();
            String description = stuffParam.getDescription();

            name = HtmlUtils.htmlEscape(name);
            description = HtmlUtils.htmlEscape(description);

            name = SensitiveFilter.DEFAULT.filter(name, Constant.Separator.ASTERISK);
            description = SensitiveFilter.DEFAULT.filter(description, Constant.Separator.ASTERISK);

            stuffParam.setName(name);
            stuffParam.setDescription(description);
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write(JSON.toJSONString(stuffParam));
            writer.flush();
            writer.close();
        }
    }
}
