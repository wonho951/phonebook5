package com.javaex.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaex.dao.PhoneDao;
import com.javaex.vo.PersonVo;

@Controller	//-->얘도 임포트 해줘야함
//@RequestMapping(value = "/pb")	//얘로 통일하면 /pb/ ~~해야 주소가됨
public class PhoneController {
	//필드
	@Autowired	//new를 하지 않겠다 너가 알아서해라 라는 의미
	private PhoneDao phoneDao;
	
	//생성자
	//메소드 g/s
	//메소드 - 일반
	
	//리스트
	@RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})	//여러개 쓰려면 {}써주기
	public String list(Model model) {		//Model은 택배박스 내놓으라는 뜻
		System.out.println("[PhoneController]");
		
		//Dao사용
		//PhoneDao phoneDao = new PhoneDao();  @repository로 dao 올리고 @autowired로 자동으로 대신해주기때문에 안해도 됨.
		 
		 //Dao의 메소드로 데이터 가져오기
		 List<PersonVo> personList = phoneDao.getPersonList();
		 
		 
		 //그림에서 4번 ModelAndView 에서 model담기 (택배박스 담기)	--> DispatcherServlet(ds)에 전달된다 --> request의 attritube영역에 넣는다.
		 model.addAttribute("personList", personList);
		
		 //ModelAndView 에서 View 공간
		return "/WEB-INF/views/list.jsp";	// DispatcherServlet 에게 test.jsp로 포워드 하라는 뜻.
		
	}
	
		
	//쓰기폼
	@RequestMapping(value = "/writeForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String writeForm() {
		System.out.println("[PhoneController.writeFrom]");
		
		return "/WEB-INF/views/writeForm.jsp";	// DispatcherServlet 에게 test.jsp로 포워드 하라는 뜻.
	}
	
	
	//쓰기
	//파라미터 다받을때 (파라미터 갯수 많아질수록 개꿀인듯)
    //ModelAttribute일때
    @RequestMapping(value="/write", method= {RequestMethod.GET,RequestMethod.POST})
    public String write(@ModelAttribute PersonVo personVo) {	//알아서 PersonVo를 new해서 파라미터까지 담아줌. 개꿀?
       System.out.println("[PhoneController.write]");
       
       //Dao사용	--> @Auto
       //PhoneDao phoneDao = new PhoneDao();     
       
       //Dao의 personInsert() 이용해서 데이터 저장
       int count = phoneDao.personInsert(personVo);	//int count는 사용 안한거임. 사용한다면 뭐 추가됐다거나 그런거 표시할때?
       
       
       //view --> 리다이렉트
       return "redirect:/list";	//리다이렉트 문법  redirect:  이 뒤에 주소써줌
    }
	
    
    
    //쓰기2(파라미터로 받을때)
    @RequestMapping(value = "/write2", method = {RequestMethod.GET, RequestMethod.POST})
    public String write2(@RequestParam("name") String name,
    					 @RequestParam("hp") String hp,
    					 @RequestParam("company") String company) {
    	System.out.println("[PhoneController.write2]");

    	
    	int count = phoneDao.personInsert2(name, hp, company);
    	
    	
    	return "redirect:/list";
    }
    
    
    //삭제
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String delete(@RequestParam("personId") int personId) {
    	System.out.println("삭제");
    	System.out.println(personId);
    	//Dao사용
    	//PhoneDao phoneDao = new PhoneDao();
    	
    	//Dao의 personDelete() 사용해서 데이터 삭제
    	int count = phoneDao.personDelete(personId);
    	
    	//view --> 리다이렉트
    	return "redirect:/list";
    	
    }
    
    
    //수정폼
    @RequestMapping(value = "/updateForm", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateForm(Model model, @RequestParam("personId") int personId) {	
    	System.out.println("수정폼");
    	System.out.println(personId);
    	
    	
    	PersonVo personVo = phoneDao.getPerson(personId);
    	
    	//model담기 (택배박스 담기)
    	model.addAttribute("personVo", personVo);
    	
    	//view 
    	return "/WEB-INF/views/updateForm.jsp";
    }
    
    
    //수정폼2
    @RequestMapping(value = "/updateForm2", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateForm2(Model model, @RequestParam("personId") int personId) {
    	System.out.println("수정폼2");
    	System.out.println(personId);
    	
    	Map<String, Object> personMap = phoneDao.getPerson2(personId);
    	System.out.println(personMap);
    	
    	model.addAttribute("pMap", personMap);
    	
    	return "/WEB-INF/views/updateForm2.jsp";
    }
    
    
    //수정
    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST} )
    public String update(@ModelAttribute PersonVo personVo) {
        System.out.println("수정");
        System.out.println(personVo);
        
        //Dao사용
        //PhoneDao phoneDao = new PhoneDao();     
        
        //Dao의 personInsert() 이용해서 데이터 저장
        //phoneDao.personUpdate(personVo);
        
        //view --> 리다이렉트
        return "redirect:/list";
    }
    
    
    

	
    
    /*************수업때문에 한것.************************************************/
    //읽기
    //pathVariable test
    @RequestMapping(value = "/board/read/{no}", method = {RequestMethod.GET, RequestMethod.POST})
    public String read(@PathVariable("no") int boardNo) {
    	System.out.println("pathVariable [read]");
    	
    	//localhost:8088/phonebook5/board/read/1	이런 형식으로 주소 쓰면됨
    	//localhost:8088/phonebook5/board/read/2
    	
    	
    	System.out.println(boardNo);
    	
    	return "";
    }
    
    
    
    
    
    
    
    
    
    
    
	
	//테스트
	@RequestMapping(value="/test")	//이름 주는거 action역할
	public String test() {
		System.out.println("test");
	
		return "/WEB-INF/views/test.jsp";	// DispatcherServlet 에게 test.jsp로 포워드 하라는 뜻. 
											// 포워드 되는 파일을 알려줌.
	}
}