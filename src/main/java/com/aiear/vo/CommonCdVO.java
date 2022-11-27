package com.aiear.vo;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonCdVO {

	@ApiModelProperty(example = "코드1")
	private String cat_cd;
	
	@ApiModelProperty(example = "코드2")
	private String cd;

	@ApiModelProperty(example = "코드3")
	private String p_cd;
	
	@ApiModelProperty(example = "코드4")
	private String cd_val;
	
	@ApiModelProperty(example = "코드5")
	private String cd_desc;
	
	@ApiModelProperty(example = "코드6")
	private Integer disp_ord;
	
	@ApiModelProperty(example = "코드7")
	private String cd_attr_1;
	
	@ApiModelProperty(example = "코드8")
	private String cd_attr_2;
	
	@ApiModelProperty(example = "코드9")
	private String cd_attr_3;

	@ApiModelProperty(example = "코드10")
	private String use_yn;
	
	public HashMap<String, Object> beanToHmap(CommonCdVO vo) {
		HashMap<String, Object> beanAsMap = new HashMap<String, Object>();
		
		try {
			BeanInfo info = Introspector.getBeanInfo(vo.getClass());
			
			for(PropertyDescriptor fb : info.getPropertyDescriptors()){
				Method reader = fb.getReadMethod();
				//TODO: class는 제외
				if(reader != null && !"class".equals(fb.getName())){
					beanAsMap.put(fb.getName(), reader.invoke(vo));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return beanAsMap;
	}
	
}
