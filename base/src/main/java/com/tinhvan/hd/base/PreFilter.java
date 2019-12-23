package com.tinhvan.hd.base;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tinhvan.hd.base.exception.ExpectationFailedException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Order(1)
public class PreFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        corsResponse(res);
        if (req.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            res.setStatus(HttpStatus.OK.value());
            return;
        }

        String path = req.getRequestURI().substring(req.getContextPath().length());
        Log.print(path + ",Ip =" + getClientIpAddress(req));

        //Log.system("[PreFilter|RequestAPI]", "Path=" + path + ",Ip=" + getClientIpAddress(req));
        try {
            Config config = HDConfig.getInstance();

            String url = config.get("INVOKER_VALIDATE_TOKEN");

            boolean apiKeyResult = verifyAPIKey(req, config);
            boolean ignore = checkIgnores(req, config);

            if (!ignore && !apiKeyResult) {
                throw new ForbiddenException();
            }
            boolean verifyEnvironment = verifyEnvironment(req);
            if (!verifyEnvironment){
                throw new ExpectationFailedException();
            }
//            boolean requestIdResult = verifyRequestId(req);
//            if (!ignore && !requestIdResult) {
//                throw new NotAcceptableException();
//            }
            boolean authenticationResult = verifyAuthentication(req);
            if (!ignore && !authenticationResult) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new UnauthorizedException();
            }

            if (!ignore && !validateToken(url, req)) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new UnauthorizedException();
            }

//            boolean authorizationResult = verifyAuthorization(req);
//            if (!ignore && !authorizationResult) {
//                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                throw new UnauthorizedException();
//            }
            boolean requireChangePasswordResult = verifyRequireChangePassword(req, config);
            if (!ignore && !requireChangePasswordResult) {
                res.setStatus(HttpServletResponse.SC_RESET_CONTENT);
                throw new RequirePasswordException();
            }

        } catch (HDException ex) {
            responseError(req, res, ex.getCode(), ex.getMessage());
            return;
        } catch (Exception ex) {
            Log.system("[PreFilter]", ex.getMessage());
            responseError(req, res, 500, "");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean verifyAPIKey(HttpServletRequest req, Config config) {
//		String path = req.getRequestURI().substring(req.getContextPath().length());
//		String[] ignores = c.getList("IGNORE_API_KEY_CHECK");
//		if (ignores != null) {
//			boolean isIgnore = Arrays.stream(ignores).anyMatch(x -> x.equals(path));
//			if (isIgnore) {
//				return true;
//			}
//		}
        String apiKey = req.getHeader("x-api-key");
        boolean rs = false;
        if (!HDUtil.isNullOrEmpty(apiKey)) {
            String[] keys = config.getList("API_KEY");
            if (keys == null) {
                rs = true;
            } else {
                for (String key : keys) {
                    if (key.equals(apiKey)) {
                        rs = true;
                    }
                }
            }
        }
//        if (!rs) {
//            Log.system("[PreFilter][Forbiden]", getClientIpAddress(req), apiKey);
//        }
        return rs;
    }

    private boolean verifyAuthentication(HttpServletRequest req) {
        String environment = req.getHeader("x-environment");
        String authorization = req.getHeader("authorization");
        Pair<Boolean, JWTPayload> authenticationData = JWTProvider.decode(authorization);
        boolean authenticationResult = authenticationData.getLeft();
        if (!authenticationResult) {
            return false;
        }
        JWTPayload payload = authenticationData.getRight();
        if (!payload.isValid()) {
            return false;
        }
        if(!environment.equals(payload.getEnvironment())){
            return false;
        }
        req.setAttribute("JWT", payload);
        return true;

    }


    private boolean verifyEnvironment(HttpServletRequest req) {
        String environment = req.getHeader("x-environment");
        if(HDUtil.isNullOrEmpty(environment)){
            return false;
        }
        return true;

    }

    public boolean verifyAuthorization(HttpServletRequest req) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        JWTPayload jwt = (JWTPayload) req.getAttribute("JWT");
        List<Long> roles = HDAuthorize.getInstance(path);

        if (roles != null && !roles.isEmpty()) {
            if (jwt == null || !jwt.isValid()) {
                return false;
            }
            return roles.contains((long) jwt.getRole());
        }
        return true;

    }

    public boolean verifyRequireChangePassword(HttpServletRequest req, Config config) {
        JWTPayload jwt = (JWTPayload) req.getAttribute("JWT");
        if (jwt == null || !jwt.isValid()) {
            return false;
        }
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.matches("/api/v1/customer/update_new_password")) {
            return true;
        }
        if (jwt.getRole() == HDConstant.ROLE.CUSTOMER) {
            long PASSWORD_EXPIRED_TIME = Long.valueOf(config.get("PASSWORD_EXPIRED_TIME"));
            long passwordExpiredTime = jwt.getLastModifyPassword() + PASSWORD_EXPIRED_TIME;
            if (passwordExpiredTime <= HDUtil.getUnixTimeNow())
                return false;
        }
        return true;


    }

    public boolean checkIgnores(HttpServletRequest req, Config config) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        String[] ignores = config.getList("IGNORE_API_KEY_CHECK");
        if (ignores != null) {
            boolean isIgnore = Arrays.stream(ignores).anyMatch(x -> x.equals(path));
            return isIgnore;
        }
        return false;

    }

//    public boolean verifyRequestId(HttpServletRequest req) {
//        UUID xId = HDServletRequest.getXRequestId();
//        if (xId != null) {
//            HDRedis redis = HDRedis.getInstance();
//            String requestIdString = xId.toString();
//            String storePath = redis.get(requestIdString);
//            String path = req.getRequestURI().substring(req.getContextPath().length());
//            if (storePath != null && storePath.equals(path)) {
//                return false;
//            }
////            redis.set(requestIdString, path,
////                    new redis.clients.jedis.params.SetParams().ex(120));
//        }
//        return true;
//    }

    private void responseError(HttpServletRequest req, HttpServletResponse response, int code, String message) {
        try {
            //MQ.closeChannel();
            //HDRedis.dispose();
            //HDEntityManager.dispose();
            java.io.PrintWriter wr = response.getWriter();
            response.setStatus(code);
            response.setContentType("application/json");
            ResponseDTO<Object> r = new ResponseDTO<>(code);
            wr.print(r);
            wr.flush();
            wr.close();
        } catch (IOException ex) {
            Log.system("[PreFilter][responseError]", ex.getMessage());
        }
    }

    private void corsResponse(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Origin,DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,x-api-key,x-request-id,Authorization,x-environment");
        res.setHeader("Access-Control-Max-Age", "1728000");
        res.setHeader("Content-Type", "application/x-www-form-urlencoded, multipart/form-data, text/plain,application/json");
        //res.setHeader("Content-Length", "0");
        res.setStatus(HttpStatus.OK.value());
    }

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public String getClientIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    public boolean validateToken(String url, HttpServletRequest req) {
        IdPayload idPayload = new IdPayload();
        JWTPayload jwtPayload = (JWTPayload) req.getAttribute("JWT");
        if (jwtPayload == null || !jwtPayload.isValid()) {
            return false;
        }

        if (HDUtil.isNullOrEmpty(url)) {
            return false;
        }

        try {
            //String token =  JWTProvider.encode(jwtPayload);
            String path = req.getRequestURI().substring(req.getContextPath().length());
            String authorization = req.getHeader("authorization");
            String token =  authorization.substring(7);
            idPayload.setId(token);
            Log.print(path + ",Ip =" + getClientIpAddress(req) + ",Token:" + token);
            if (jwtPayload.getRole() == HDConstant.ROLE.CUSTOMER) {
                return checkTokenByRole(url, "/api/v1/customer/validate_token", idPayload);
            } else {
                return checkTokenByRole(url, "/api/v1/staff/check_token", idPayload);
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkTokenByRole(String baseUrl, String subPathRequest, IdPayload idPayload) {
        Invoker invoker = new Invoker();
        try {
            ResponseDTO<Object> dto = invoker.call(baseUrl + subPathRequest, idPayload,
                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
                    });
            if (dto == null || dto.getCode() != HttpStatus.OK.value()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
