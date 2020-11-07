package es.uvigo.esei.dai.hybridserver.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

import es.uvigo.esei.dai.hybridserver.NotFoundException;

public class PagesDBDAO implements PagesDAO {

	private final String db_url;
	private final String db_user;
	private final String db_password;
	private final String db_nombre;

	public PagesDBDAO(Properties properties) {
		this.db_url = properties.getProperty("db.url");
		this.db_user = properties.getProperty("db.user");
		this.db_password = properties.getProperty("db.password");
		String[] url = db_url.split("/");
		this.db_nombre=url[url.length-1];
	}

	@Override
	public String getWeb(String uuid) throws NotFoundException {
		String query = "SELECT * FROM " + db_nombre.toUpperCase() + " WHERE uuid LIKE ?";
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
		}
		return null;
	}

	@Override
	public boolean checkUuid(String uuid) {
		String query = "SELECT * FROM " + db_nombre.toUpperCase() + " WHERE uuid LIKE ?";
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
		}
		return false;
	}
	
	@Override
	public String webList() {
		StringBuilder sb = new StringBuilder();		
		String query = "SELECT uuid FROM " + db_nombre.toUpperCase();
		try(Connection connection = DriverManager.getConnection(db_url,db_user,db_password)){
			try(PreparedStatement statement = connection.prepareStatement(query)){
				try(ResultSet result = statement.executeQuery()){
					while(result.next()) {
						sb.append("<p><a href=\"html?uuid=").append(result.getString("uuid")).append("\">").append(result.getString("uuid")).append("</a></p>\n");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String createUuid() {
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();
			
		return uuid;
	}

	@Override
	public void delete(String uuid) throws NotFoundException {
		String query = "DELETE FROM " + db_nombre.toUpperCase() + " WHERE uuid LIKE ?";
		
		
	}

}
