package com.aiear.config.session;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class JwtManager {
  private final String securityKey = "AIEAR KEY"; // TODO 민감정보는 따로 분리하는 것이 좋다
  private final Long expiredTime = 1000 * 60L * 60L * 1L; // 유효시간 1시간

  
  /**
   * Member 정보를 담은 JWT 토큰을 생성한다.
   *
   * @param member Member 정보를 담은 객체
   * @return String JWT 토큰
   */
  public String generateJwtToken(Member member) {
    Date now = new Date();
    return Jwts.builder()
        .setSubject(member.getUser_nm()) // 보통 username
        .setHeader(createHeader())
        .setClaims(createClaims(member)) // 클레임, 토큰에 포함될 정보
        .setExpiration(new Date(now.getTime() + expiredTime)) // 만료일
        .signWith(SignatureAlgorithm.HS256, securityKey)
        .compact();
  }

  private Map<String, Object> createHeader() {
    Map<String, Object> header = new HashMap<>();
    header.put("typ", "JWT");
    header.put("alg", "HS256"); // 해시 256 사용하여 암호화
    header.put("regDate", System.currentTimeMillis());
    return header;
  }

  /**
   * 클레임(Claim)을 생성한다.
   *
   * @param member 토큰을 생성하기 위한 계정 정보를 담은 객체
   * @return Map<String, Object> 클레임(Claim)
   */
  private Map<String, Object> createClaims(Member member) {
    Map<String, Object> claims = new HashMap<>();
    
    try {
		BeanInfo info = Introspector.getBeanInfo(member.getClass());
		
		for(PropertyDescriptor fb : info.getPropertyDescriptors()){
			Method reader = fb.getReadMethod();
			if(reader != null && !"class".equals(fb.getName())){
				claims.put(fb.getName(), reader.invoke(member));
			}
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    return claims;
  }
  
  /**
   * Token 에서 Claim 을 가져온다.
   *
   * @param token JWT 토큰
   * @return Claims 클레임
   */
  private Claims getClaims(String token) {
    return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();
  }

  /**
   * 토큰으로 부터 username 을 가져온다.
   *
   * @param token JWT 토큰
   * @return String Member 의 username
   */
  public String getUsernameFromToken(String token) {
    return (String) getClaims(token).get("user_nm");
  }

  /**
   * 토큰으로 부터 인가 정보를 가져온다.
   *
   * @param token JWT 토큰
   * @return Set<MemberRole> role 정보를 가지고 있는 Set
   */
  public Set<MemberRole> getMemberRoleSetFromToken(String token) {
    return (Set<MemberRole>) getClaims(token).get("user_type");
  }
  
  
  /**
   * 토큰검증
   * @param jwt
   * @return Map<String, Object>
   */
  public Map<String, Object> checkJwt(String jwt) throws UnsupportedEncodingException {
      Map<String, Object> claimMap = null;
      try {
          Claims claims = Jwts.parser()
                  .setSigningKey(securityKey.getBytes("UTF-8")) // 키 설정
                  .parseClaimsJws(jwt) // jwt의 정보를 파싱해서 시그니처 값을 검증한다.
                  .getBody();

          claimMap = claims;
          
      } catch (ExpiredJwtException e) { // 토큰이 만료되었을 경우
          System.out.println(e);
          
      } catch (Exception e) { // 나머지 에러의 경우
          System.out.println(e);
      }
      return claimMap;
  }    
  
}