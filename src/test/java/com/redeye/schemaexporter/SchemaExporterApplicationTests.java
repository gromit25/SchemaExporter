package com.redeye.schemaexporter;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.redeye.schemaexporter.domain.ColumnDTO;
import com.redeye.schemaexporter.domain.KeyColumnDTO;
import com.redeye.schemaexporter.domain.TableDTO;
import com.redeye.schemaexporter.target.SchemaMapper;

@SpringBootTest
class SchemaExporterApplicationTests {
	
	@Autowired
	SchemaMapper mapper;

	@Test
	void testSelectTables_1() {
		
		List<TableDTO> tables = this.mapper.selectTables(null);
		
		System.out.println("Table List");
		System.out.println("--------------------------------");
		tables.forEach(
			table -> {
				System.out.println(table.getTableName() + ":" + table.getDescription());
			}
		);
	}
	
	@Test
	void testSelectTables_2() {
		
		List<TableDTO> tables = this.mapper.selectTables("rc_t");
		
		System.out.println("Table List");
		System.out.println("--------------------------------");
		tables.forEach(
			table -> {
				System.out.println(table.getTableName() + ":" + table.getDescription());
			}
		);
	}

	
	@Test
	void testSelectColumns() {
		
		String schemaName = "";
		String tableName = "rc_req_body_spec";
		List<ColumnDTO> columns = this.mapper.selectColumns(schemaName, tableName);
		
		System.out.println("Column List in " + tableName);
		System.out.println("--------------------------------");
		columns.forEach(
			column -> {
				System.out.println(column.getColumnName() + ":" + column.getType() + ":" + column.getSize() + ":" + column.getDescription());
			}
		);
	}
	
	@Test
	void testSelectKeyColumns() {
		
		String schemaName = "";
		String tableName = "rc_req_body_spec";
		List<KeyColumnDTO> columns = this.mapper.selectKeyColumns(schemaName, tableName);
		
		System.out.println("Key Column List in " + tableName);
		System.out.println("--------------------------------");
		columns.forEach(
			column -> {
				System.out.println(column.getColumnName() + ":" + column.getKeyType() + ":" + column.getRef());
			}
		);
	}
}
