package com.aiear.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.aiear.vo.DiagnosisVO;

@Mapper
@Repository
public interface DiagnosisDAO {

//	/**
//	* @methodName    : getHospitalClinicList
//	* @author        : pcw
//	* @date        : 2022.11.20
//	* @param vo
//	* @return
//	*/
//	public List<Map<String, Object>> getHospitalClinicList(HospitalInfoVO vo);

	
	
	/**
	 * Description	: 귀 진단 결과 저장
	 * @method		: insertEarDiagnosis
	 * @author		: pcw
	 * @date		: 2023. 3. 19.
	 * @param vo
	 * @return
	 */
	public int insertEarDiagnosis(DiagnosisVO vo);
	
	
	/**
	 * Description	: 귀 진단 결과 리스트 조회
	 * @method		: getEarDiagnosisList
	 * @author		: pcw
	 * @date		: 2023. 3. 19.
	 * @param vo
	 * @return
	 */
	public List<Map<String, Object>> getEarDiagnosisList(DiagnosisVO vo);
	
	
	/**
	 * Description	: 고막 검사 결과 리스트 조회
	 * @method		: getDrumDiagnosisList
	 * @author		: pcw
	 * @date		: 2023. 3. 19.
	 * @param vo
	 * @return
	 */
	public List<Map<String, Object>> getDrumDiagnosisList(DiagnosisVO vo);
	
}
