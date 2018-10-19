package org.zy.priceResync.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zy.priceResync.Config;

public class SqlTemplate {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SqlTemplate.class);

	private Connection conn;

	public SqlTemplate() {
		try {
			Class.forName(Config.get("app.h2.driver")).newInstance();
			this.conn = DriverManager.getConnection(Config.get("app.h2.url"), Config.get("app.h2.username"),
					Config.get("app.h2.password"));

		} catch (Exception ex) {
			log.error("fail to connect database.", ex);
			throw new RuntimeException(ex);
		}
	}

	public void query(String sql, List<Object> paramL, ResultHandler rh) {
		try {
			PreparedStatement ps = this.conn.prepareStatement(sql);
			int i = 1;
			for (Object o : paramL) {
				if (o instanceof String) {
					ps.setString(i++, (String) o);
				} else {
					throw new java.lang.UnsupportedOperationException();
				}
			}

			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> result = this.resultSetConverToMap(rs);

			rh.handle(result);

		} catch (SQLException sqle) {
			log.error("fail to get statement.", sqle);
			throw new RuntimeException(sqle);
		}
	}

	public List<Map<String, Object>> resultSetConverToMap(ResultSet rs) {
		List<Map<String, Object>> rt = new ArrayList<Map<String, Object>>();

		try {
			ResultSetMetaData mData = rs.getMetaData();
			int cnt = mData.getColumnCount();
			while (rs.next()) {
				Map<String, Object> clmn = new HashMap<String, Object>();
				for (int i = 1; i <= cnt; i++) {
					String fld = mData.getColumnName(i);
					int type = mData.getColumnType(i);
					Object value = null;
					if (Types.BIGINT == type) {
						value = rs.getLong(i);
					} else if (Types.VARCHAR == type) {
						value = rs.getString(i);
					} else if (Types.INTEGER == type) {
						value = rs.getInt(i);
					} else if (Types.TIMESTAMP == type) {
						value = rs.getTimestamp(i);
					} else if (Types.DECIMAL == type) {
						value = rs.getBigDecimal(i);
					} else {
						throw new java.lang.UnsupportedOperationException();
					}

					clmn.put(fld, value);					
				}
				rt.add(clmn);
			}
		} catch (Exception e) {
			log.error("fecth data error.", e);
			throw new RuntimeException(e);
		}

		return rt;
	}
	
	public void update(String sql, Object...paramL ){
		try {
			boolean isAuto = this.conn.getAutoCommit();
			this.conn.setAutoCommit(false);
			PreparedStatement ps = this.conn.prepareStatement(sql);
			int i = 1;
			for (Object o : paramL) {
				if (o instanceof String) {
					ps.setString(i++, (String) o);
				} else if(o instanceof Long) {
					ps.setLong(i++, (Long)o);
				} else if(o instanceof BigDecimal) {
					ps.setBigDecimal(i++, (BigDecimal)o);
				}else{
					throw new java.lang.UnsupportedOperationException();
				}
			}

			int cnt = ps.executeUpdate();
			this.conn.commit();
			this.conn.setAutoCommit(isAuto);
		} catch (SQLException sqle) {
			log.error("fail to get statement.", sqle);
			throw new RuntimeException(sqle);
		}		
	}
}
