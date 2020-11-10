package es.uvigo.esei.dai.hybridserver.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import es.uvigo.esei.dai.hybridserver.NotFoundException;

public class PagesDBDAO implements PagesDAO {

	private final String db_url;
	private final String db_user;
	private final String db_password;

	public PagesDBDAO(Properties properties) {
		if(properties != null) {
			this.db_url = properties.getProperty("db.url");
			this.db_user = properties.getProperty("db.user");
			this.db_password = properties.getProperty("db.password");
			
			}else {
				this.db_url = "jdbc:mysql://localhost:3306/hstestdb";
				this.db_user = "hsdb";
				this.db_password = "hsdbpass";
				
			}
	}

	@Override
	public String getWeb(String uuid) throws NotFoundException {
		String query = "SELECT * FROM HTML WHERE uuid LIKE ?";
		try(Connection connection = DriverManager.getConnection(db_url,db_user,db_password)){
			try(PreparedStatement statement = connection.prepareStatement(query)){
				statement.setString(1, uuid);
				try(ResultSet result = statement.executeQuery()){
					if(result.next()) {
						return result.getString("content");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
			
		}
		throw new NotFoundException("No se encontró ninguna web con ese uuid");
	}

	@Override
	public boolean checkUuid(String uuid) {
		String query = "SELECT * FROM HTML WHERE uuid LIKE ?";
		try(Connection connection = DriverManager.getConnection(db_url,db_user,db_password)){
			try(PreparedStatement statement = connection.prepareStatement(query)){
				statement.setString(1, uuid);
				try(ResultSet result = statement.executeQuery()){
					if(result.next()) {
						return true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return false;
	}
	
	@Override
	public String webList() {
		StringBuilder sb = new StringBuilder();		
		String query = "SELECT uuid FROM HTML";
		try(Connection connection = DriverManager.getConnection(db_url,db_user,db_password)){
			try(PreparedStatement statement = connection.prepareStatement(query)){
				try(ResultSet result = statement.executeQuery()){
					sb.append("<html> <head><title>Hybrid Server</title></head><body>"
							+ "<h1>Hybrid Server</h1>");
					while(result.next()) {
						sb.append("<p><a href=\"html?uuid=").append(result.getString("uuid")).append("\">").append(result.getString("uuid")).append("</a></p>\n");
					}
					sb.append("</body></html>");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return sb.toString();
	}
	
	@Override
	public String createUuid() {
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();
			
		return uuid;
	}

	@Override
	public void delete(String uuid) throws NotFoundException {
		String query = "DELETE FROM HTML WHERE uuid LIKE ?";
		try(Connection connection = DriverManager.getConnection(db_url,db_user,db_password)){
			try(PreparedStatement statement = connection.prepareStatement(query)){
				statement.setString(1, uuid);
				int result = statement.executeUpdate();
				if(result!=1) throw new NotFoundException("404 Page not found",uuid);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String  putPage(String content) {
		String uuid= createUuid();
		String query = "INSERT INTO HTML VALUES (?,?)";
		try(Connection connection = DriverManager.getConnection(db_url,db_user,db_password)){
			try(PreparedStatement statement = connection.prepareStatement(query)){
				statement.setString(1, uuid);
				statement.setString(2, content);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return "<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a>";
	}

}
