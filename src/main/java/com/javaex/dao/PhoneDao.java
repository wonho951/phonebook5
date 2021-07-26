package com.javaex.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.PersonVo;


@Repository	//자동연결
public class PhoneDao {

	@Autowired
	private SqlSession sqlSession;
	

	
	//전체 리스트 가져오기
	public List<PersonVo> getPersonList(){
		System.out.println("[PhoneController.List]");
		
		//DB에 요청해서 리스트를 가져와야함
		List<PersonVo> personList = sqlSession.selectList("phonebook.selectList");
		System.out.println("dao");
		System.out.println(personList);
		
		return personList;
	}
	
	
	
	//전화번호 저장
	public int personInsert(PersonVo personVo) {
		System.out.println("[PhoneController.insert]");	//건들수 있는 영역이 아니기 때문에 제대로 들어갔나 확인.
		System.out.println(personVo);
		
		sqlSession.insert("phonebook.personInsert", personVo);	//데이터를 personVo에 넘겨준다.
		
		return 1;
	}
	
	
	//전화번호 저장2
	//Map 사용	--> vo로 만들면 편하지만 이번 한번만 쓸시에 map사용할 수 있음
	public int personInsert2(String name, String hp, String company) {
		System.out.println("[PhoneController.insert2]");
		System.out.println(name);
		System.out.println(hp);
		System.out.println(company);
		
		
		//Map을 사용해서 데이터를 묶는다.
		Map<String, Object> personMap = new HashMap<String,Object>();
		personMap.put("name", name);
		personMap.put("hp", hp);
		personMap.put("company", company);
		
		
		System.out.println(personMap);
		int count = sqlSession.insert("phonebook.personInsert2", personMap);
		System.out.println("dao결과:" + count);
		
		return 1;
	}
	
	
	//전화번호 삭제
	public int personDelete(int personId) {
		System.out.println("[PhoneController.delete]");
		System.out.println(personId);
		
		sqlSession.delete("phonebook.personDelete", personId);
		
		return 1;
	}
	
	
	//수정폼, 전화번호 가져오기
	public PersonVo getPerson(int personId) {
		System.out.println("[PhoneController.updateForm]");
		
		//들어갈때 데이터 찍어보기
		System.out.println(personId);
		
		PersonVo personVo = sqlSession.selectOne("phonebook.selectPerson", personId);
		//나올때 데이터 찍어보기
		System.out.println(personVo);
		return personVo;
	}
	
	
	
	//수정폼2, 전화번호 가져오기2
	public Map<String, Object> getPerson2(int personId) {
		System.out.println("[PhoneController.updateForm]");
		//System.out.println(personId);
		
		Map<String, Object> personMap = sqlSession.selectOne("phonebook.selectPerson2", personId);
		//System.out.println(personMap);
		//System.out.println(personMap.get("PERSON_ID"));
		
		
		return personMap;
	}
	
	
	
	
	//수정
	public int personUpdate(PersonVo personVo) {
		System.out.println("[PhoneController.update]");
		System.out.println(personVo);
		
		int count = sqlSession.update("phonebook.personUpdate", personVo);
		System.out.println(personVo);
		
		
		return count;
	}
	
	
	//수정2
	public int personUpdate2(int personId, String name, String hp, String company){
		System.out.println("여긴 dao" + personId + ","+ name + ","+hp +","+company);
		
		//Map을 사용해서 데이터를 묶는다.
		Map<String, Object> personMap = new HashMap<String,Object>();
		personMap.put("personId", personId);
		personMap.put("name", name);
		personMap.put("hp", hp);
		personMap.put("company", company);
		
		
		//int count = sqlSession.update("phonebook.personUpdate2", personMap);
		
		//System.out.println(count);
		
		return sqlSession.update("phonebook.personUpdate2", personMap);
	}
	
}
