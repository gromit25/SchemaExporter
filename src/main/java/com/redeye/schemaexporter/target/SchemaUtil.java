package com.redeye.schemaexporter.target;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jutools.TypeUtil;
import com.redeye.schemaexporter.domain.ColumnDto;

/**
 * 유틸리티 클래스
 * 
 * @author jmsohn
 */
public class SchemaUtil {
	
   /**
    * table-column map 에서 table 관계를 추출하여 반환
    * 
    * @param columnMap table-column map
    * @return table 관계 문자열 목록
    */
   public static List<String> getRelationList(Map<String, List<ColumnDto>> columnMap) throws Exception {
      
      // 입력값 검증
      if(columnMap == null) {
         throw new IllegalArgumentException("column map is null.");
      }
      
      // relation set 변수
      Set<String> relationSet = new HashSet<String>();
      
      // 각 테이블의 컬럼 중 외부키를 이용하여 관계를 생성하여 추가함
      columnMap.forEach((tableName, columnList) -> {
         columnList.forEach(column -> {
            
            // 외부키가 아닌 경우 스킵
            if("T".equals(column.getIsForeignKey()) == false) {
               return;
            }
            
            // table 관계를 생성 및 추가
            StringBuilder relationStr = new StringBuilder("");
            
            relationStr
            	.append(column.getRefTableName())
            	.append(" ||--o{ ")
            	.append(column.getTableName())
            	.append(": \"\"");
            
            relationSet.add(relationStr.toString());
         });
      });
      
      return Arrays.asList(TypeUtil.toArray(relationSet, String.class));
   }
}
