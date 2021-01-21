package es.uvigo.esei.dai.hybridserver.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import es.uvigo.esei.dai.entidades.Configuration;
import es.uvigo.esei.dai.hybridserver.NotFoundException;

public class XSDDBDAO implements PagesDAO {

	private final String db_url;
	private final String db_user;
	private final String db_password;

	public XSDDBDAO(Configuration config) {
		if (config != null) {
			this.db_url = config.getDbURL();
			this.db_user = config.getDbUser();
			this.db_password = config.getDbPassword();

		} else {
			this.db_url = "jdbc:mysql://localhost:3306/hstestdb";
			this.db_user = "hsdb";
			this.db_password = "hsdbpass";

		}
	}

	@Override
	public String getWeb(String uuid) {
		String query = "SELECT * FROM XSD WHERE uuid LIKE ?";
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, uuid);
				try (ResultSet result = statement.executeQuery()) {
					if (result.next()) {
						return result.getString("content");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		}
		return null;
	}

	@Override
	public boolean checkUuid(String uuid) {
		String query = "SELECT * FROM XSD WHERE uuid LIKE ?";
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, uuid);
				try (ResultSet result = statement.executeQuery()) {
					if (result.next()) {
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
	public ArrayList<String> webList() {
		String query = "SELECT uuid FROM XSD";
		ArrayList<String> uuids = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				try (ResultSet result = statement.executeQuery()) {
					while (result.next()) {
						uuids.add(result.getString("uuid"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return uuids;
	}

	@Override
	public String createUuid() {
		UUID randomUuid = UUID.randomUUID();
		String uuid = randomUuid.toString();

		return uuid;
	}

	@Override
	public void delete(String uuid) throws NotFoundException {
		String query = "DELETE FROM XSD WHERE uuid LIKE ?";
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, uuid);
				int result = statement.executeUpdate();
				if (result != 1)
					throw new NotFoundException("Not page found by the specified uuid when trying to delete");
			}
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String putPage(String[] content) {
		String uuid = createUuid();
		String query = "INSERT INTO XSD VALUES (?,?)";
		try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password)) {
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, uuid);
				statement.setString(2, content[0]);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return uuid;
	}

	@Override
	public String getContentType() {
		return "xsd";
	}

}
