package org.palooca.olap4j;

import com.jedox.palojlib.exceptions.PaloException;
import com.jedox.palojlib.main.UserInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.jedox.palojlib.exceptions.PaloJException;
import com.jedox.palojlib.interfaces.IConnection;
import com.jedox.palojlib.interfaces.IDatabase;
import com.jedox.palojlib.main.ConnectionConfiguration;
import com.jedox.palojlib.main.ConnectionInfo;
import com.jedox.palojlib.main.SvsInfo;
import java.util.LinkedHashMap;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.OlapWrapper;
import org.olap4j.driver.xmla.XmlaOlap4jDriver;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.NamedList;


public class Olap4jConnection implements IConnection {


    @Override
    public void close(boolean bln) throws PaloException {
        close();
    }

    @Override
    public UserInfo getUserInfo(boolean bln) throws PaloException, PaloJException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeDatabase(IDatabase id) throws PaloException, PaloJException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


	class Olap4jConnectionInfo extends ConnectionInfo {
		public Olap4jConnectionInfo(String majorVersion, String minorVersion, String buildNumber, String encryptionType,
			String httpsPort) {

			super(majorVersion,minorVersion,buildNumber,buildNumber,encryptionType,httpsPort);

		}
	}
	
	private OlapConnection connection;
	private ConnectionConfiguration configuration;
	private Olap4jConnectionInfo info;
	private String connectionString;
	
	private Map<String, Olap4jDatabase> databaseLookup = new LinkedHashMap<String,Olap4jDatabase>();

	static String host = "http://192.168.169.3:7080/mdx/xmla/learn_kylin";
	static String port = "7080";
	static String username = "ADMIN";
	static String password = "KYLIN";
	static String database = "sales";

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Olap4jConnection olap4jConnection =
				new Olap4jConnection(host, port, username, password, database);

			// Вызываем метод open() для открытия соединения
			String result = olap4jConnection.open();
			System.out.println(result);

		olap4jConnection.getOlapConnection().getDatabase();
			//System.out.println("getDatabase " + );
			//System.out.println("getMetaData " + olap4jConnection.getOlapConnection().getMetaData());
	}

	public Olap4jConnection(String host, String port, String user, String password, String database) throws ClassNotFoundException, SQLException {
		
		Class.forName("org.olap4j.driver.xmla.XmlaOlap4jDriver");
		connectionString = "jdbc:xmla:Server="+host;
//		if (port!=null)
//			connectionString += ":"+port;
//		if (user!=null)
//			connectionString += ";User='" + user + "'";
//		if (password!=null)
//			connectionString += ";Password='" + password + "'";
//		if (database!=null)
//			connectionString += ";Catalog='" + database + "'";


		XmlaOlap4jDriver driver = (XmlaOlap4jDriver)DriverManager.getDriver(connectionString);

		configuration = new ConnectionConfiguration();
		configuration.setHost(host);
		configuration.setPassword(password);
		configuration.setPort(port);
		configuration.setSslPreferred(false);
		configuration.setTimeout(0);
		configuration.setUsername(user);

		String majorVersion = String.valueOf(driver.getMajorVersion());
		String minorVersion = String.valueOf(driver.getMinorVersion());
		String encryptionType = (connectionString.startsWith("https")) ? "2" : "0";

		info = new Olap4jConnectionInfo(majorVersion,minorVersion,driver.getVersion(),encryptionType,(port != null && !port.isEmpty()) ? port : "0");

	}

	@Override
	public ConnectionConfiguration getConnectionConfiguration() {
		return configuration;
	}

	@Override
	public ConnectionInfo getServerInfo() {
		return info;
	}

	@Override
	public boolean isConnected() {
		try {
			return (connection != null) && (!connection.isClosed());
		}
		catch (SQLException e) {
			return false;
		}
	}

	@Override
	public String open() throws PaloException {
		try {
			if (!isConnected()) {
				Connection jdbcConnection =
						DriverManager.getConnection(connectionString,configuration.getUsername(),configuration.getPassword());
				OlapWrapper wrapper = (OlapWrapper) jdbcConnection;
				connection = wrapper.unwrap(OlapConnection.class);
                                return "connected";

			}
                        return "connected";
		}
		catch (SQLException e) {
			throw new PaloException("Cannot open connection "+connectionString+": "+e.getMessage());
		}
	}

        @Override
	public void close() throws PaloException {
		try {
			connection.close();
			for (Olap4jDatabase wrapper : databaseLookup.values()) {
				wrapper.clearLookup();
			}
			databaseLookup.clear();
		}
		catch (SQLException e) {
			throw new PaloException(e.getMessage());
		}
	}

	@Override
	public IDatabase[] getDatabases() throws PaloJException {
                if (databaseLookup.isEmpty()) {
                    try {
                            NamedList<Catalog> list = connection.getOlapCatalogs();
                            int size = list.size();
                            IDatabase[] databases = new IDatabase[size];
                            for (int i=0; i<size;i++) {
                                    databases[i] = databaseLookup.get(list.get(i).getName());
                                    if (databases[i] == null) {
                                            Olap4jDatabase wrapper = new Olap4jDatabase(list.get(i));
                                            databases[i] = wrapper;
                                            databaseLookup.put(wrapper.getName(), wrapper);
                                    }
                            }
                    }
                    catch (OlapException e) {
                            throw new PaloJException(e.getMessage());
                    }
                }
                return databaseLookup.values().toArray(new IDatabase[0]);
	}

	@Override
	public IDatabase addDatabase(String name) throws PaloJException {
		throw new PaloJException("Creating databases not supported by OLAP4j provider.");
	}

	@Override
	public IDatabase getDatabaseByName(String name) throws PaloJException {
                if (databaseLookup.isEmpty()) {
			getDatabases();
		}
		return databaseLookup.get(name);
	}

	@Override
	public void save() throws PaloJException {
		//do nothing here, since we cannot create any objects using this wrapper anyway
	}
	
	public OlapConnection getOlapConnection() {
		return connection;
	}

	@Override
	public SvsInfo getSvsInfo() throws PaloJException, PaloJException {
		throw new PaloJException("SvsInfo not supported by OLAP4j provider.");
	}


        /*

        @Override
        public UserInfo getUserInfo(boolean bln) throws PaloException, PaloJException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
         * 
         */

}
