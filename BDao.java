package com.javalec.spring_prj_board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.javalec.spring_prj_board.dto.BDto;
import com.javalec.spring_prj_board.util.Constant;

public class BDao {

	DataSource dataSource = null;
	JdbcTemplate template = null;
	
	public BDao() {
		Context context = null;
		
		try {
			context = new InitialContext();
			dataSource = (DataSource)context.lookup("java:comp/env/jdbc/mysql");
			
			template = Constant.template;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<BDto> list() {
		String query = "select bid, bName, bTitle, bContent, bDate, bHit, bGroup, "
				+ "bIndent, bStep from mvc_board order by bGroup, "
				+ "bStep desc ";
		System.out.println("list query = " + query);
		
		ArrayList<BDto> dtos = (ArrayList<BDto>) template.query(query, new BeanPropertyRowMapper<BDto>(BDto.class));
		
		return dtos;
				
	}
	
	
/*	public ArrayList<BDto> list() {
		
		ArrayList<BDto> dtos = new ArrayList<BDto>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		String query = "select bid, bName, bTitle, bContent, bDate, bHit, bGroup, "
				+ "bIndent, bStep from mvc_board order by bGroup, "
				+ "bStep desc ";
		System.out.println("list query = " + query);
		
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()){
				int bId = resultSet.getInt("bId");
				String bName = resultSet.getString("bName");
				String bTitle = resultSet.getString("bTitle");
				String bContent = resultSet.getString("bContent");
				Timestamp bDate = resultSet.getTimestamp("bDate");
				int bHit = resultSet.getInt("bHit");
				int bGroup = resultSet.getInt("bGroup");
				int bStep = resultSet.getInt("bStep");
				int bIndent = resultSet.getInt("bIndent");
				
				BDto dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
				dtos.add(dto);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet != null) resultSet.close();
				if(connection != null) connection.close();
				if(preparedStatement != null) preparedStatement.close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		
		return dtos;
			
	}
*/
	
	
	public void write(final String bName, final String bTitle, final String bContent) {

		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				String query ="INSERT INTO MVC_BOARD (bName, bTitle, bContent, bHit, bGroup, bStep, bIndent ) "
						+ " SELECT ? , ?, ? , 0, max(bId)+1, 0, 0 FROM MVC_BOARD";
				PreparedStatement psmt = arg0.prepareStatement(query);
				psmt.setString(1, bName);
				psmt.setString(2, bTitle);
				psmt.setString(3, bContent);
				
				return psmt;
			}
		});
		
		
		String query ="INSERT INTO MVC_BOARD (bName, bTitle, bContent, bHit, bGroup, bStep, bIndent ) "
				+ " SELECT ? , ?, ? , 0, max(bId)+1, 0, 0 FROM MVC_BOARD";
		
		
		
/*		Connection connection = null;
		PreparedStatement psmt = null;
		
		try {
			connection = dataSource.getConnection();
			
			String query = "INSERT INTO MVC_BOARD (bName, bTitle, bContent, bHit, bGroup, "
					+ " bStep, bIndent ) VALUES ( ?, ?, ?, 0, 0 , 0, 0) ";
			String query ="INSERT INTO MVC_BOARD (bName, bTitle, bContent, bHit, bGroup, bStep, bIndent ) "
					+ " SELECT ? , ?, ? , 0, max(bId)+1, 0, 0 FROM MVC_BOARD";
		
			psmt = connection.prepareStatement(query);
			
			psmt.setString(1, bName);
			psmt.setString(2, bTitle);
			psmt.setString(3, bContent);
//			psmt.setTimestamp(4, date());
			
			System.out.println("Insert Query : " + query );
			
			int rn = psmt.executeUpdate();
					
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(connection != null) connection.close();
				if(psmt != null) psmt.close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
*/	}

	public BDto contentView(String bId) {
		upHit(bId);
		
		String query = "select bid, bName, bTitle, bContent, bDate, bHit, bGroup, bIndent, bStep from mvc_board WHERE bId = " + bId;
		System.out.println(Integer.parseInt(bId));
		
		BDto dto = template.queryForObject(query, new BeanPropertyRowMapper<BDto>(BDto.class));
		
/*		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet resultSet = null;
		BDto dto = new BDto();
		

		try {
			connection = dataSource.getConnection();
			String query = "select bid, bName, bTitle, bContent, bDate, bHit, bGroup, bIndent, bStep from mvc_board WHERE bId = ? ";
			psmt = connection.prepareStatement(query);
			System.out.println(Integer.parseInt(bId));
			psmt.setInt(1,  Integer.parseInt(bId));
			
			System.out.println("Select query : " + query);
			
			resultSet = psmt.executeQuery();
			
			if(resultSet.next()) {
				dto.setbId(resultSet.getInt("bId"));
				dto.setbName(resultSet.getString("bName"));
				dto.setbTitle(resultSet.getString("bTitle"));
				dto.setbContent(resultSet.getString("bContent"));
				dto.setbDate(resultSet.getTimestamp("bDate"));
				dto.setbGroup(resultSet.getInt("bGroup"));
				dto.setbHit(resultSet.getInt("bHit"));
				dto.setbIndent(resultSet.getInt("bIndent"));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(connection != null)	connection.close();
				if(psmt != null) psmt.close();
				if(resultSet != null )	resultSet.close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}*/
		
		return dto;
	}

	private void upHit(final String bId) {

		String query = "UPDATE MVC_BOARD SET bHit = bHit + 1 WHERE bId = ?";
		
		template.update(query, new PreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, Integer.parseInt(bId));
			}
		});
		
/*		Connection connection = null;
		PreparedStatement psmt = null;
		
		try {
			connection = dataSource.getConnection();
			
			String query = "UPDATE MVC_BOARD SET bHit = bHit + 1 WHERE bId = ?";
			
			psmt = connection.prepareStatement(query);
			
			psmt.setInt(1, Integer.parseInt(bId));
//			psmt.setString(1, bId);
			
			System.out.println("HIT Query : " + query );
			
			int rn = psmt.executeUpdate();
					
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(connection != null) connection.close();
				if(psmt != null) psmt.close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		*/
	}

	public void modify(final String bId, final String bName, final String bTitle, final String bContent) {

		String query = "UPDATE MVC_BOARD SET bTitle = ?, bName = ?, bContent = ? WHERE bId = ? ";
		
		template.update(query,new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement arg0) throws SQLException {
				// TODO Auto-generated method stub
				arg0.setString(1, bTitle);
				arg0.setString(2, bName);
				arg0.setString(3, bContent);
				arg0.setInt(4, Integer.parseInt(bId));
			}
		});
		
		/*		Connection connection = null;
		PreparedStatement psmt = null;
		
		try {
			connection = dataSource.getConnection();
			String query = "UPDATE MVC_BOARD SET bTitle = ?, bName = ?, bContent = ? WHERE bId = ? ";
			psmt = connection.prepareStatement(query);
			System.out.println("modify query : " + query);
			psmt.setString(1, bTitle);
			psmt.setString(2,  bName);
			psmt.setString(3, bContent);
			psmt.setInt(4, Integer.parseInt(bId));
			
			int rn = psmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) connection.close();
				if(psmt != null) psmt.close();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}*/
		
	}

	public void delete(final String bId) {

		String query = "Delete from MVC_BOARD WHERE bId = ? ";

		template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement arg0) throws SQLException {
				arg0.setString(1, bId);
				
			}
		});
		
/*		Connection connection = null;
		PreparedStatement psmt = null;
		
		try {
			connection = dataSource.getConnection();
			String query = "Delete from MVC_BOARD WHERE bId = ? ";
			psmt = connection.prepareStatement(query);
			psmt.setInt(1, Integer.parseInt(bId));
			int rn = psmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(connection != null) connection.close();
				if(psmt != null) psmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		*/
	}
		


}
