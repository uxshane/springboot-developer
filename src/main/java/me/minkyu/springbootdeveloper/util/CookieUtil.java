package me.minkyu.springbootdeveloper.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

public class CookieUtil {

    //요청 파라미터를 바탕으로 쿠키 생성
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    //쿠키의 이름을 받아 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            return;
        }

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(name)) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    //객체를 직렬화해 쿠키의 값으로 반환
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    //객체를 역직렬화해 객체로 변화
    public static <T> T deserialize(Cookie cookie, Class<T> clazz){
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(cookie.getValue());
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                Object deserializedObject = ois.readObject();
                return clazz.cast(deserializedObject);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize cookie", e);
        }
    }

}
