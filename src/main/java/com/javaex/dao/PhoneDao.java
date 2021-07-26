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
		
		
		//Map를 사용해서 데이터를 묶는다.
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
	
	
	//수정폼
	public PersonVo getPerson(int personId) {
		System.out.println("[PhoneController.updateForm]");
		
		//들어갈때 데이터 찍어보기
		System.out.println(personId);
		
		PersonVo personVo = sqlSession.selectOne("phonebook.selectPerson", personId);
		//나올때 데이터 찍어보기
		System.out.println(personVo);
		return personVo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//기존방식
	/*
	@Autowired
	private DataSource dataSource;

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/* applicationContext.xml --> 세팅으로 해결해서 필요없음
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "phonedb";
	private String pw = "phonedb";
	*/
	/*
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			//Class.forName(driver);
			//이미 dataSource가 돌림
			
			
			// 2. Connection 얻어오기
			//conn = DriverManager.getConnection(url, id, pw);
			conn = dataSource.getConnection();	//이번에 연결할 데이터 소스 내놔라
			
			// System.out.println("접속성공");

		
		} 
//		  catch (ClassNotFoundException e) {
//			System.out.println("error: 드라이버 로딩 실패 - " + e);	우리가 로딩하는게 아니기때문에 필요없음
//		} 
		 catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 사람 추가
	public int personInsert(PersonVo personVo) {
		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " INSERT INTO person ";
			query += " VALUES (seq_person_id.nextval, ?, ?, ?) ";
			// System.out.println(query);

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, personVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, personVo.getHp()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, personVo.getCompany()); // ?(물음표) 중 3번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			 System.out.println("[" + count + "건 추가되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	
	//사람 리스트(검색안할때)
	public List<PersonVo> getPersonList() {
		return getPersonList("");
	}

	// 사람 리스트(검색할때)
	public List<PersonVo> getPersonList(String keword) {
		List<PersonVo> personList = new ArrayList<PersonVo>();

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " select  person_id, ";
			query += "         name, ";
			query += "         hp, ";
			query += "         company ";
			query += " from person";
			query += " order by person_id asc ";

			if (keword != "" || keword == null) {
				query += " where name like ? ";
				query += " or hp like  ? ";
				query += " or company like ? ";
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기

				pstmt.setString(1, '%' + keword + '%'); // ?(물음표) 중 1번째, 순서중요
				pstmt.setString(2, '%' + keword + '%'); // ?(물음표) 중 2번째, 순서중요
				pstmt.setString(3, '%' + keword + '%'); // ?(물음표) 중 3번째, 순서중요
			} else {
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			}

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				PersonVo personVo = new PersonVo(personId, name, hp, company);
				personList.add(personVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return personList;

	}


	// 사람 수정
	public int personUpdate(PersonVo personVo) {
		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " update person ";
			query += " set name = ? , ";
			query += "     hp = ? , ";
			query += "     company = ? ";
			query += " where person_id = ? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, personVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, personVo.getHp()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, personVo.getCompany()); // ?(물음표) 중 3번째, 순서중요
			pstmt.setInt(4, personVo.getPersonId()); // ?(물음표) 중 4번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			// System.out.println(count + "건 수정되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

	// 사람 삭제
	public int personDelete(int personId) {
		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " delete from person ";
			query += " where person_id = ? ";
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setInt(1, personId);// ?(물음표) 중 1번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			System.out.println(count + "건 삭제되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

	/*
	// 한 사람만 가져오기 
	public PersonVo getPerson(int personId) {
		
		PersonVo personVo = null;	//변수만 잡음
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " select  person_id, ";
			query += " 		   name, ";
			query += "         hp, ";
			query += "		   company ";
			query += " from person ";
			query += " where person_id = ? ";
			// System.out.println(query);

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setInt(1, personId); // ?(물음표) 중 1번째, 순서중요

			rs = pstmt.executeQuery(); // 쿼리문 실행

			
			
			// 4.결과처리
			while(rs.next()) {
				int pid = rs.getInt("person_Id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				
				personVo = new PersonVo(pid, name, hp, company);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return personVo;
	}
	*/
	
	
	
	
	
	
	
	
	
	/*
	public PersonVo getPerson(int personId) {
	      int count=0;
	      getConnection();
	      PersonVo personVo = null;
	      try {
	         String query = "";
	         query += " select  person_id, ";
	         query += "         name, ";
	         query += "         hp, ";
	         query += "         company ";
	         query += " from  person ";
	         query += " where  person_id = ? ";
	         
	         pstmt = conn.prepareStatement(query);
	         pstmt.setInt(1, personId);
	      
	         rs = pstmt.executeQuery();
	         
	         
	         //결과처리
	         while(rs.next()) {
	            int pid = rs.getInt("person_id");
	            String name = rs.getString("name");
	            String hp = rs.getString("hp");
	            String company = rs.getString("company");
	            
	            personVo = new PersonVo(pid, name, hp, company);
	         }
	         
	      } catch (SQLException e) {
	         System.out.println("error:" + e);
	      }
	      
	      return personVo;
	   }
	   */
	
	
	/*
	// 사람 1명 가져오기
	public PersonVo getPerson(int personId) {
		PersonVo personVo = null;
		
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select  person_id, ";
			query += "         name, ";
			query += "         hp, ";
			query += "         company ";
			query += " from person";
			query += " where person_id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, personId);
			
			rs = pstmt.executeQuery();
		
			// 4.결과처리
			while(rs.next()) {
				int pid = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");
				
				personVo = new PersonVo(pid, name, hp, company);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 4.결과처리

		close();

		return personVo;
	}*/

	
}
