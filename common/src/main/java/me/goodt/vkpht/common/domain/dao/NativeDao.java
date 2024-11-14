package me.goodt.vkpht.common.domain.dao;

import me.goodt.vkpht.common.domain.entity.DomainObject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.goodt.drive.rtcore.dto.DivisionTeamTree;
import com.goodt.drive.rtcore.dto.orgstructure.IEmployeeMetaLukView;

@Repository
public interface NativeDao extends JpaRepository<DomainObject, Long> {
    
    @Query(value = "SELECT * FROM view_employee_meta_luk", nativeQuery = true)
    List<IEmployeeMetaLukView> getAllEmployeeMetaLukView();
    
    //todo iGurkin: переделать на таблицу
    @Query(value = "select id, parent_id as parentId, lvl as level from (\r\n" + //
            "  with recursive org_tree(id, parent_id, lvl) AS (\r\n" + //
            "    select t.id, t.parent_id, 1\r\n" + //
            "      from org_division_team t where id <> parent_id\r\n" + //
            "    union all\r\n" + //
            "      select t.id, st.parent_id, st.lvl + 1\r\n" + //
            "      from org_division_team t, org_tree st\r\n" + //
            "      where t.parent_id = st.id and st.lvl < 10\r\n" + //
            "  )\r\n" + //
            "  select id, parent_id, lvl from org_tree\r\n" + //
            "  union all\r\n" + //
            "  select id, id as p_id, 0 as lvl from org_division_team\r\n" + //
            "  ) n_tree\r\n" + //
            "where n_tree.parent_id is not null\r\n" + //
            "  and n_tree.parent_id = :divisionTeamId", nativeQuery = true)
    List<DivisionTeamTree> findAllChildrenDivTeam(@Param("divisionTeamId") Long divisionTeamId);

    //todo iGurkin: переделать на таблицу
    @Query(value = "select id, parent_id as parentId, lvl as level from (\r\n" + //
        "  with recursive org_tree(id, parent_id, lvl) AS (\r\n" + //
        "    select t.id, t.parent_id, 1\r\n" + //
        "      from org_division_team t where id <> parent_id\r\n" + //
        "    union all\r\n" + //
        "      select t.id, st.parent_id, st.lvl + 1\r\n" + //
        "      from org_division_team t, org_tree st\r\n" + //
        "      where t.parent_id = st.id and st.lvl < 10\r\n" + //
        "  )\r\n" + //
        "  select id, parent_id, lvl from org_tree\r\n" + //
        "  union all\r\n" + //
        "  select id, id as p_id, 0 as lvl from org_division_team\r\n" + //
        "  ) n_tree\r\n" + //
        "where n_tree.parent_id is not null\r\n" + //
        "  and n_tree.id = :divisionTeamId", nativeQuery = true)
    List<DivisionTeamTree> findAllParentsDivTeam(@Param("divisionTeamId") Long divisionTeamId);

    @Query(value = "insert into org_division_links (child_id, parent_id, level_to_parent)\n" +
            "select id, parent_id, lvl from (\n" +
            "  with recursive org_tree(id, parent_id, lvl) AS (\n" +
            "    select t.id, t.parent_id, 1\n" +
            "      from org_division t\n" +
            "    union all\n" +
            "      select t.id, st.parent_id, st.lvl + 1\n" +
            "      from org_division t, org_tree st\n" +
            "      where t.parent_id = st.id\n" +
            "  )\n" +
            "  select id, parent_id, lvl from org_tree\n" +
            "  union all\n" +
            "  select id, id as p_id, 0 as lvl from org_division\n" +
            "  ) n_tree\n" +
            "where n_tree.parent_id is not null", nativeQuery = true)
    void rebuildDivisionTree();

    //find division_team where no head
    @Query(value = "SELECT dt.id FROM org_division_team AS dt " +
            "INNER JOIN org_division_team_role AS dtr ON dtr.division_team_id = dt.id " +
            "INNER JOIN org_role AS r ON dtr.role_id = r.id " +
            "WHERE (dt.date_to IS NULL OR CURRENT_TIMESTAMP BETWEEN dt.date_from AND dt.date_to) " +
            "AND r.system_role_id != :headSysRole " +
            "AND dt.parent_id in :divisionTeams " +
            "GROUP BY dt.id " +
            "EXCEPT " +
            "SELECT dt.id FROM org_division_team AS dt " +
            "INNER JOIN org_division_team_role AS dtr ON dtr.division_team_id = dt.id " +
            "INNER JOIN org_role AS r ON dtr.role_id = r.id " +
            "WHERE (dt.date_to IS NULL OR CURRENT_TIMESTAMP BETWEEN dt.date_from AND dt.date_to) " +
            "AND r.system_role_id = :headSysRole " +
            "AND dt.parent_id IN (:divisionTeams) " +
            "GROUP BY dt.id ", nativeQuery = true)
    List<Long> findNearestChildIdsWithoutHead(@Param("divisionTeams") List<Long> divisionTeams, @Param("headSysRole") Integer headSysRole);

    @Query(value = "select concat(table_schema, '.', TABLE_NAME) from INFORMATION_SCHEMA.VIEWS " +
        "WHERE table_schema IN :schemas", nativeQuery = true)
    List<String> getAllView(@Param("schemas") List<String> schemas);
}
