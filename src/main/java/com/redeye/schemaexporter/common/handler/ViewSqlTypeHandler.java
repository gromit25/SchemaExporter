package com.redeye.schemaexporter.common.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

/**
 * 
 * 
 * @author jmsohn
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ViewSqlTypeHandler extends BaseTypeHandler<String> {

	@Override
	public void setNonNullParameter(
		PreparedStatement ps,
		int i,
		String parameter,
		JdbcType jdbcType
	) throws SQLException {
		ps.setString(i, parameter);
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		
		String viewSql = rs.getString(columnName);
		
		if(viewSql != null) {
			return viewSql.replace("\n", "\n|");
		} else {
			return "N/A";
		}
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getString(columnIndex);
	}
}
